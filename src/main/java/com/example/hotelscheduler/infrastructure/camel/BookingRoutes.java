package com.example.hotelscheduler.infrastructure.camel;

import com.example.hotelscheduler.domain.model.Booking;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class BookingRoutes extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("seda:booking-created")
			.routeId("booking-created-log")
			.log("New booking created: ${body}");

		from("direct:health")
			.routeId("health-route")
			.setBody(constant("OK"));
	}
}