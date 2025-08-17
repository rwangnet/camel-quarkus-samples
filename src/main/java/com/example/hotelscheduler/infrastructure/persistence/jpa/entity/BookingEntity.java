package com.example.hotelscheduler.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "bookings")
public class BookingEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String userFullName;

	@Column(nullable = false)
	private String userEmail;

	@Column(nullable = false)
	private String roomNumber;

	@Column(nullable = false)
	private LocalDate checkInDate;

	@Column(nullable = false)
	private LocalDate checkOutDate;

	@Column(length = 1000)
	private String notes;

	@Column(nullable = false)
	private OffsetDateTime createdAt;

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