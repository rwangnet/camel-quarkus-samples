package com.example.hotelscheduler.infrastructure.camel.processor;

import com.example.hotelscheduler.application.usecase.ScheduleBookingService;
import com.example.hotelscheduler.domain.model.Booking;
import com.example.hotelscheduler.infrastructure.rest.dto.CreateBookingRequest;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Objects;

@Component
public class CreateBookingProcessor implements Processor {
	private final ScheduleBookingService scheduleBookingService;

	public CreateBookingProcessor(ScheduleBookingService scheduleBookingService) {
		this.scheduleBookingService = scheduleBookingService;
	}

	@Override
	public void process(Exchange exchange) {
		CreateBookingRequest req = exchange.getMessage().getBody(CreateBookingRequest.class);
		Objects.requireNonNull(req, "Request body must not be null");
		if (req.getCheckInDate() == null || req.getCheckOutDate() == null) {
			throw new IllegalArgumentException("Both checkInDate and checkOutDate are required");
		}
		if (!req.getCheckOutDate().isAfter(req.getCheckInDate())) {
			throw new IllegalArgumentException("checkOutDate must be after checkInDate");
		}

		Booking saved = scheduleBookingService.createBooking(new ScheduleBookingService.CreateBookingCommand(
			req.getUserFullName(),
			req.getUserEmail(),
			req.getRoomNumber(),
			req.getCheckInDate(),
			req.getCheckOutDate(),
			req.getNotes()
		));
		exchange.getMessage().setBody(saved);
	}
}