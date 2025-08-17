package com.example.hotelscheduler.infrastructure.camel.processor;

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
			exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
			exchange.getMessage().setBody("Missing id path variable");
			return;
		}
		Optional<Booking> opt = scheduleBookingService.getBooking(id);
		if (opt.isEmpty()) {
			exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 404);
			exchange.getMessage().setBody(null);
			return;
		}
		exchange.getMessage().setBody(opt.get());
	}
}