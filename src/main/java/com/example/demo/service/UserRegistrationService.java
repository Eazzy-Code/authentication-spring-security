package com.example.demo.service;

import com.example.demo.dto.CreateUserRequest;
import com.example.demo.dto.NotificationRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserRegistrationService {

	private final UserRepository userRepository;
	private final NotificationService notificationService;

	public UserRegistrationService(UserRepository userRepository,
								   NotificationService notificationService) {
		this.userRepository = userRepository;
		this.notificationService = notificationService;
	}

	@Transactional
	public void createNewUser(CreateUserRequest createUserRequest) {
		User user = new User();

		user.setUsername(createUserRequest.getUsername());
		user.setPassword(createUserRequest.getPassword());
		user.setId(UUID.randomUUID().toString());

		userRepository.save(user);

		this.notificationService.send(
				new NotificationRequest("Seja bem vindo", user.getUsername()));
	}

}
