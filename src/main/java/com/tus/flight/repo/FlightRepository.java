package com.tus.flight.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tus.flight.model.Flight;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByDateOfDeparture(java.time.LocalDate date);
}
