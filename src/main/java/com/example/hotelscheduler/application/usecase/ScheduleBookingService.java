package com.example.hotelscheduler.application.usecase;

import com.example.hotelscheduler.domain.model.Booking;
import com.example.hotelscheduler.domain.port.BookingRepositoryPort;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleBookingService {
	private final BookingRepositoryPort bookingRepositoryPort;
	private final ProducerTemplate producerTemplate;

	public ScheduleBookingService(BookingRepositoryPort bookingRepositoryPort, ProducerTemplate producerTemplate) {
		this.bookingRepositoryPort = bookingRepositoryPort;
		this.producerTemplate = producerTemplate;
	}

	public Booking createBooking(CreateBookingCommand command) {
		Booking booking = new Booking(
			null,
			command.userFullName(),
			command.userEmail(),
			command.roomNumber(),
			command.checkInDate(),
			command.checkOutDate(),
			command.notes(),
			OffsetDateTime.now()
		);
		Booking saved = bookingRepositoryPort.save(booking);
		producerTemplate.sendBody("seda:booking-created", saved);
		return saved;
	}

	public Optional<Booking> getBooking(Long id) {
		return bookingRepositoryPort.findById(id);
	}

	public List<Booking> listBookings() {
		return bookingRepositoryPort.findAll();
	}

	public record CreateBookingCommand(String userFullName, String userEmail, String roomNumber,
			java.time.LocalDate checkInDate, java.time.LocalDate checkOutDate, String notes) {}
}