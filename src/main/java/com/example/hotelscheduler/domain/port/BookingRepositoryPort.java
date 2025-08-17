package com.example.hotelscheduler.domain.port;

import com.example.hotelscheduler.domain.model.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepositoryPort {
	Booking save(Booking booking);
	Optional<Booking> findById(Long id);
	List<Booking> findAll();
}