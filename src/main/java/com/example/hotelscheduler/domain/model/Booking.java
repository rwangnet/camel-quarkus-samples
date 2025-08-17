package com.example.hotelscheduler.domain.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class Booking {
	private Long id;
	private String userFullName;
	private String userEmail;
	private String roomNumber;
	private LocalDate checkInDate;
	private LocalDate checkOutDate;
	private String notes;
	private OffsetDateTime createdAt;

	public Booking() {}

	public Booking(Long id, String userFullName, String userEmail, String roomNumber, LocalDate checkInDate, LocalDate checkOutDate, String notes, OffsetDateTime createdAt) {
		this.id = id;
		this.userFullName = userFullName;
		this.userEmail = userEmail;
		this.roomNumber = roomNumber;
		this.checkInDate = checkInDate;
		this.checkOutDate = checkOutDate;
		this.notes = notes;
		this.createdAt = createdAt;
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getUserFullName() { return userFullName; }
	public void setUserFullName(String userFullName) { this.userFullName = userFullName; }

	public String getUserEmail() { return userEmail; }
	public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

	public String getRoomNumber() { return roomNumber; }
	public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

	public LocalDate getCheckInDate() { return checkInDate; }
	public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }

	public LocalDate getCheckOutDate() { return checkOutDate; }
	public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }

	public String getNotes() { return notes; }
	public void setNotes(String notes) { this.notes = notes; }

	public OffsetDateTime getCreatedAt() { return createdAt; }
	public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}