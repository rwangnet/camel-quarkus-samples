package com.example.hotelscheduler.infrastructure.camel.processor;

import com.example.hotelscheduler.application.usecase.ScheduleBookingService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class ListBookingsProcessor implements Processor {
	private final ScheduleBookingService scheduleBookingService;

	public ListBookingsProcessor(ScheduleBookingService scheduleBookingService) {
		this.scheduleBookingService = scheduleBookingService;
	}

	@Override
	public void process(Exchange exchange) {
		exchange.getMessage().setBody(scheduleBookingService.listBookings());
	}
}