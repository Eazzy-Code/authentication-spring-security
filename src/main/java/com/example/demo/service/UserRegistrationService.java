package com.example.demo.service;

import com.example.demo.dto.CreateUserRequest;
import com.example.demo.dto.NotificationRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserRegistrationService {

	private final UserRepository userRepository;
	private final NotificationService notificationService;
	private final PasswordEncoder passwordEncoder;

	public UserRegistrationService(UserRepository userRepository,
								   NotificationService notificationService,
								   PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.notificationService = notificationService;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public void createNewUser(CreateUserRequest createUserRequest) {
		User user = new User();

		String encodedPassword = passwordEncoder.encode(createUserRequest.getPassword());
		user.setPassword(encodedPassword);
		user.setUsername(createUserRequest.getUsername());
		user.setId(UUID.randomUUID().toString());

		userRepository.save(user);

		this.notificationService.send(
				new NotificationRequest("Seja bem vindo", user.getUsername()));
	}

}
