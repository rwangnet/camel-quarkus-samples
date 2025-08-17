package com.example.hotelscheduler.infrastructure.camel.processor;

import com.example.hotelscheduler.application.exception.ResourceNotFoundException;
import com.example.hotelscheduler.application.usecase.ScheduleBookingService;
import com.example.hotelscheduler.domain.model.Booking;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetBookingByIdProcessor implements Processor {
	private final ScheduleBookingService scheduleBookingService;

	public GetBookingByIdProcessor(ScheduleBookingService scheduleBookingService) {
		this.scheduleBookingService = scheduleBookingService;
	}

	@Override
	public void process(Exchange exchange) {
		String idStr = exchange.getMessage().getHeader("id", String.class);
		Long id = idStr != null ? Long.valueOf(idStr) : null;
		if (id == null) {
			throw new IllegalArgumentException("Missing id path variable");
		}
		Optional<Booking> opt = scheduleBookingService.getBooking(id);
		if (opt.isEmpty()) {
			throw new ResourceNotFoundException("Booking with id %d not found".formatted(id));
		}
		exchange.getMessage().setBody(opt.get());
	}
}