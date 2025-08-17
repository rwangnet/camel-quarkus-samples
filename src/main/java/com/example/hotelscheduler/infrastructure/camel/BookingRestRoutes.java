package com.example.hotelscheduler.infrastructure.camel;

import com.example.hotelscheduler.infrastructure.persistence.jpa.entity.BookingEntity;
import com.example.hotelscheduler.infrastructure.rest.dto.BookingResponse;
import com.example.hotelscheduler.infrastructure.rest.dto.CreateBookingRequest;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Component
public class BookingRestRoutes extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		restConfiguration()
			.component("servlet")
			.contextPath("/api")
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
			.process(exchange -> {
				CreateBookingRequest req = exchange.getMessage().getBody(CreateBookingRequest.class);
				Objects.requireNonNull(req, "Request body must not be null");
				if (req.getCheckInDate() == null || req.getCheckOutDate() == null) {
					throw new IllegalArgumentException("Both checkInDate and checkOutDate are required");
				}
				if (!req.getCheckOutDate().isAfter(req.getCheckInDate())) {
					throw new IllegalArgumentException("checkOutDate must be after checkInDate");
				}
				BookingEntity entity = new BookingEntity();
				entity.setUserFullName(req.getUserFullName());
				entity.setUserEmail(req.getUserEmail());
				entity.setRoomNumber(req.getRoomNumber());
				entity.setCheckInDate(req.getCheckInDate());
				entity.setCheckOutDate(req.getCheckOutDate());
				entity.setNotes(req.getNotes());
				entity.setCreatedAt(OffsetDateTime.now());
				exchange.getMessage().setBody(entity);
			})
			.to("jpa:com.example.hotelscheduler.infrastructure.persistence.jpa.entity.BookingEntity")
			.process(exchange -> {
				BookingEntity e = exchange.getMessage().getBody(BookingEntity.class);
				BookingResponse r = new BookingResponse();
				r.id = e.getId();
				r.userFullName = e.getUserFullName();
				r.userEmail = e.getUserEmail();
				r.roomNumber = e.getRoomNumber();
				r.checkInDate = e.getCheckInDate();
				r.checkOutDate = e.getCheckOutDate();
				r.notes = e.getNotes();
				r.createdAt = e.getCreatedAt();
				exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 201);
				exchange.getMessage().setHeader("Location", URI.create("/api/bookings/" + r.id));
				exchange.getMessage().setBody(r);
			})
			.to("seda:booking-created");

		from("direct:getBookingById")
			.routeId("get-booking-by-id")
			.to("jpa:com.example.hotelscheduler.infrastructure.persistence.jpa.entity.BookingEntity?query=select b from BookingEntity b where b.id = :?id&singleResult=true")
			.process(exchange -> {
				BookingEntity e = exchange.getMessage().getBody(BookingEntity.class);
				if (e == null) {
					exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 404);
					exchange.getMessage().setBody(null);
					return;
				}
				BookingResponse r = new BookingResponse();
				r.id = e.getId();
				r.userFullName = e.getUserFullName();
				r.userEmail = e.getUserEmail();
				r.roomNumber = e.getRoomNumber();
				r.checkInDate = e.getCheckInDate();
				r.checkOutDate = e.getCheckOutDate();
				r.notes = e.getNotes();
				r.createdAt = e.getCreatedAt();
				exchange.getMessage().setBody(r);
			});

		from("direct:listBookings")
			.routeId("list-bookings")
			.to("jpa:com.example.hotelscheduler.infrastructure.persistence.jpa.entity.BookingEntity?query=select b from BookingEntity b")
			.process(exchange -> {
				List<BookingEntity> list = exchange.getMessage().getBody(List.class);
				List<BookingResponse> out = list.stream().map(e -> {
					BookingResponse r = new BookingResponse();
					r.id = e.getId();
					r.userFullName = e.getUserFullName();
					r.userEmail = e.getUserEmail();
					r.roomNumber = e.getRoomNumber();
					r.checkInDate = e.getCheckInDate();
					r.checkOutDate = e.getCheckOutDate();
					r.notes = e.getNotes();
					r.createdAt = e.getCreatedAt();
					return r;
				}).toList();
				exchange.getMessage().setBody(out);
			});
	}
}