package com.example.hotelscheduler.infrastructure.persistence.jpa.springdata;

import com.example.hotelscheduler.infrastructure.persistence.jpa.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataBookingRepository extends JpaRepository<BookingEntity, Long> {
}