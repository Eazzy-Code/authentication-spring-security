package com.example.demo.controller;

import com.example.demo.dto.TokenRequest;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@RestController
public class TokenController {

	@Value("${jwt.private.key}")
	RSAPrivateKey key;

	private final AuthenticationManager authenticationManager;
	private final AuthenticationTrustResolver authenticationTrustResolver;

	public TokenController(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
		this.authenticationTrustResolver = new AuthenticationTrustResolverImpl();
	}

	@PostMapping("/token")
	public ResponseEntity<String> token(@RequestBody @Valid TokenRequest tokenRequest) {
		Authentication authentication = authenticate(tokenRequest);

		if (authentication == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		Instant now = Instant.now();
		long expiry = 36000L;

		String scope = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(" "));
		JWTClaimsSet claims = new JWTClaimsSet.Builder()
				.issuer("self")
				.issueTime(new Date(now.toEpochMilli()))
				.expirationTime(new Date(now.plusSeconds(expiry).toEpochMilli()))
				.subject(authentication.getName())
				.claim("scope", scope)
				.build();

		JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256).build();
		SignedJWT jwt = new SignedJWT(header, claims);
		return ResponseEntity.ok(sign(jwt).serialize());
	}

	private Authentication authenticate(TokenRequest tokenRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(tokenRequest.getUsername(), tokenRequest.getPassword()));

		if (authentication == null || this.authenticationTrustResolver.isAnonymous(authentication)) {
			return null;
		}

		return authentication;
	}

	SignedJWT sign(SignedJWT jwt) {
		try {
			jwt.sign(new RSASSASigner(this.key));
			return jwt;
		} catch (Exception ex) {
			throw new IllegalArgumentException(ex);
		}
	}

}
