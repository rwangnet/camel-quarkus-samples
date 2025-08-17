package com.example.hotelscheduler.infrastructure.persistence.jpa.adapter;

import com.example.hotelscheduler.domain.model.Booking;
import com.example.hotelscheduler.domain.port.BookingRepositoryPort;
import com.example.hotelscheduler.infrastructure.persistence.jpa.entity.BookingEntity;
import com.example.hotelscheduler.infrastructure.persistence.jpa.springdata.SpringDataBookingRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookingRepositoryAdapter implements BookingRepositoryPort {
	private final SpringDataBookingRepository jpaRepository;

	public BookingRepositoryAdapter(SpringDataBookingRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	@Override
	public Booking save(Booking booking) {
		BookingEntity entity = toEntity(booking);
		BookingEntity saved = jpaRepository.save(entity);
		return toDomain(saved);
	}

	@Override
	public Optional<Booking> findById(Long id) {
		return jpaRepository.findById(id).map(this::toDomain);
	}

	@Override
	public List<Booking> findAll() {
		return jpaRepository.findAll().stream().map(this::toDomain).toList();
	}

	private BookingEntity toEntity(Booking booking) {
		BookingEntity entity = new BookingEntity();
		entity.setId(booking.getId());
		entity.setUserFullName(booking.getUserFullName());
		entity.setUserEmail(booking.getUserEmail());
		entity.setRoomNumber(booking.getRoomNumber());
		entity.setCheckInDate(booking.getCheckInDate());
		entity.setCheckOutDate(booking.getCheckOutDate());
		entity.setNotes(booking.getNotes());
		entity.setCreatedAt(booking.getCreatedAt());
		return entity;
	}

	private Booking toDomain(BookingEntity entity) {
		Booking booking = new Booking();
		booking.setId(entity.getId());
		booking.setUserFullName(entity.getUserFullName());
		booking.setUserEmail(entity.getUserEmail());
		booking.setRoomNumber(entity.getRoomNumber());
		booking.setCheckInDate(entity.getCheckInDate());
		booking.setCheckOutDate(entity.getCheckOutDate());
		booking.setNotes(entity.getNotes());
		booking.setCreatedAt(entity.getCreatedAt());
		return booking;
	}
}