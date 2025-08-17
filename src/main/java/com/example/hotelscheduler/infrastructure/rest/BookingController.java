package com.example.hotelscheduler.infrastructure.rest;

import com.example.hotelscheduler.application.usecase.ScheduleBookingService;
import com.example.hotelscheduler.domain.model.Booking;
import com.example.hotelscheduler.infrastructure.rest.dto.BookingResponse;
import com.example.hotelscheduler.infrastructure.rest.dto.CreateBookingRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
	private final ScheduleBookingService scheduleBookingService;

	public BookingController(ScheduleBookingService scheduleBookingService) {
		this.scheduleBookingService = scheduleBookingService;
	}

	@PostMapping
	public ResponseEntity<BookingResponse> create(@RequestBody @Valid CreateBookingRequest request) {
		Booking saved = scheduleBookingService.createBooking(new ScheduleBookingService.CreateBookingCommand(
			request.getUserFullName(),
			request.getUserEmail(),
			request.getRoomNumber(),
			request.getCheckInDate(),
			request.getCheckOutDate(),
			request.getNotes()
		));
		BookingResponse response = toResponse(saved);
		return ResponseEntity.created(URI.create("/api/bookings/" + saved.getId())).body(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<BookingResponse> get(@PathVariable Long id) {
		return scheduleBookingService.getBooking(id)
			.map(booking -> ResponseEntity.ok(toResponse(booking)))
			.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@GetMapping
	public ResponseEntity<List<BookingResponse>> list() {
		List<BookingResponse> responses = scheduleBookingService.listBookings().stream().map(this::toResponse).toList();
		return ResponseEntity.ok(responses);
	}

	private BookingResponse toResponse(Booking booking) {
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