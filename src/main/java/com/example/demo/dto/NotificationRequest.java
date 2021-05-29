package com.example.demo.dto;

public class NotificationRequest {

	private final String message;
	private final String username;

	public NotificationRequest(String message, String username) {
		this.message = message;
		this.username = username;
	}

	public String getMessage() {
		return message;
	}

	public String getUsername() {
		return username;
	}
}
