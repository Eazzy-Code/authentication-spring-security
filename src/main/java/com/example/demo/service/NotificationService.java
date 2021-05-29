package com.example.demo.service;

import com.example.demo.dto.NotificationDto;
import com.example.demo.dto.NotificationRequest;
import com.example.demo.entity.Notification;
import com.example.demo.entity.User;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificationService {

	private final NotificationRepository notificationRepository;
	private final UserRepository userRepository;

	public NotificationService(NotificationRepository notificationRepository,
							   UserRepository userRepository) {
		this.notificationRepository = notificationRepository;
		this.userRepository = userRepository;
	}

	public void send(NotificationRequest notificationRequest) {
		User user = this.userRepository.findByUsername(notificationRequest.getUsername())
				.orElseThrow();

		Notification notification = new Notification();
		notification.setId(UUID.randomUUID().toString());
		notification.setMessage(notificationRequest.getMessage());
		notification.setSentAt(LocalDateTime.now());
		notification.setSentTo(user);

		this.notificationRepository.save(notification);
	}

	public List<NotificationDto> getNotifications() {
		return notificationRepository.findAll()
				.stream().map(this::toNotificationDto)
				.collect(Collectors.toList());
	}

	private NotificationDto toNotificationDto(Notification notification) {
		return new NotificationDto(
				notification.getId(),
				notification.getMessage(),
				notification.getSentAt(),
				notification.getSentTo().getUsername());
	}
}
