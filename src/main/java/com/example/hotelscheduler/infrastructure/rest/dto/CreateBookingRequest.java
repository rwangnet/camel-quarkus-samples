package com.example.hotelscheduler.infrastructure.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class CreateBookingRequest {
	@NotBlank
	private String userFullName;
	@NotBlank
	@Email
	private String userEmail;
	@NotBlank
	private String roomNumber;
	@NotNull
	@Future
	private LocalDate checkInDate;
	@NotNull
	@Future
	private LocalDate checkOutDate;
	private String notes;

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
}