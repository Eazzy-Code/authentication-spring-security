package com.example.demo.dto;

import java.time.LocalDateTime;

public class NotificationDto {

	private String id;
	private String message;
	private LocalDateTime sentAt;
	private String sentTo;

	public NotificationDto(String id, String message, LocalDateTime sentAt, String sentTo) {
		this.id = id;
		this.message = message;
		this.sentAt = sentAt;
		this.sentTo = sentTo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getSentAt() {
		return sentAt;
	}

	public void setSentAt(LocalDateTime sentAt) {
		this.sentAt = sentAt;
	}

	public String getSentTo() {
		return sentTo;
	}

	public void setSentTo(String sentTo) {
		this.sentTo = sentTo;
	}
}
