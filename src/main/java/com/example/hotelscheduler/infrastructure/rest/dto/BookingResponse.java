package com.example.hotelscheduler.infrastructure.rest.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class BookingResponse {
	public Long id;
	public String userFullName;
	public String userEmail;
	public String roomNumber;
	public LocalDate checkInDate;
	public LocalDate checkOutDate;
	public String notes;
	public OffsetDateTime createdAt;
}