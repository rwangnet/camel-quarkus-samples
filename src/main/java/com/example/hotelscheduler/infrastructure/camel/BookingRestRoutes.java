package com.example.hotelscheduler.infrastructure.camel;

import com.example.hotelscheduler.domain.model.Booking;
import com.example.hotelscheduler.infrastructure.camel.processor.CreateBookingProcessor;
import com.example.hotelscheduler.infrastructure.camel.processor.GetBookingByIdProcessor;
import com.example.hotelscheduler.infrastructure.camel.processor.ListBookingsProcessor;
import com.example.hotelscheduler.infrastructure.rest.dto.BookingResponse;
import com.example.hotelscheduler.infrastructure.rest.dto.CreateBookingRequest;
import com.example.hotelscheduler.infrastructure.rest.mapper.BookingMapper;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

@Component
public class BookingRestRoutes extends RouteBuilder {
	private final CreateBookingProcessor createBookingProcessor;
	private final GetBookingByIdProcessor getBookingByIdProcessor;
	private final ListBookingsProcessor listBookingsProcessor;

	public BookingRestRoutes(CreateBookingProcessor createBookingProcessor,
			GetBookingByIdProcessor getBookingByIdProcessor,
			ListBookingsProcessor listBookingsProcessor) {
		this.createBookingProcessor = createBookingProcessor;
		this.getBookingByIdProcessor = getBookingByIdProcessor;
		this.listBookingsProcessor = listBookingsProcessor;
	}

	@Override
	public void configure() throws Exception {
		restConfiguration()
			.component("servlet")
			.bindingMode(RestBindingMode.json)
			.dataFormatProperty("prettyPrint", "true");

		rest("/bookings")
			.post()
				.type(CreateBookingRequest.class)
				.outType(BookingResponse.class)
				.to("direct:createBooking")
			.get("/{id}")
				.outType(BookingResponse.class)
				.to("direct:getBookingById")
			.get()
				.produces("application/json")
				.to("direct:listBookings");

		from("direct:createBooking")
			.routeId("create-booking")
			.process(createBookingProcessor)
			.process(exchange -> {
				Booking booking = exchange.getMessage().getBody(Booking.class);
				BookingResponse r = BookingMapper.toResponse(booking);
				exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 201);
				exchange.getMessage().setHeader("Location", URI.create("/api/bookings/" + r.id));
				exchange.getMessage().setBody(r);
			})
			.to("seda:booking-created");

		from("direct:getBookingById")
			.routeId("get-booking-by-id")
			.process(getBookingByIdProcessor)
			.process(exchange -> {
				if (exchange.getMessage().getHeader(Exchange.HTTP_RESPONSE_CODE) != null) {
					return;
				}
				Booking booking = exchange.getMessage().getBody(Booking.class);
				exchange.getMessage().setBody(BookingMapper.toResponse(booking));
			});

		from("direct:listBookings")
			.routeId("list-bookings")
			.process(listBookingsProcessor)
			.process(exchange -> {
				List<Booking> list = exchange.getMessage().getBody(List.class);
				List<BookingResponse> out = list.stream().map(BookingMapper::toResponse).toList();
				exchange.getMessage().setBody(out);
			});
	}
}