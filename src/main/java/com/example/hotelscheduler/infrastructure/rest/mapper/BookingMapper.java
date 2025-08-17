package com.example.hotelscheduler.infrastructure.rest.mapper;

import com.example.hotelscheduler.domain.model.Booking;
import com.example.hotelscheduler.infrastructure.rest.dto.BookingResponse;

public final class BookingMapper {
	private BookingMapper() {}

	public static BookingResponse toResponse(Booking booking) {
		BookingResponse r = new BookingResponse();
		r.id = booking.getId();
		r.userFullName = booking.getUserFullName();
		r.userEmail = booking.getUserEmail();
		r.roomNumber = booking.getRoomNumber();
		r.checkInDate = booking.getCheckInDate();
		r.checkOutDate = booking.getCheckOutDate();
		r.notes = booking.getNotes();
		r.createdAt = booking.getCreatedAt();
		return r;
	}
}