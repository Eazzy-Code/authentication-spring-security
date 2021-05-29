package com.example.demo.controller;

import com.example.demo.dto.CreateUserRequest;
import com.example.demo.service.UserRegistrationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

	private final UserRegistrationService userRegistrationService;

	public UserController(UserRegistrationService userRegistrationService) {
		this.userRegistrationService = userRegistrationService;
	}

	@PostMapping
	public void createUser(@RequestBody CreateUserRequest createUserRequest) {
		userRegistrationService.createNewUser(createUserRequest);
	}
}
