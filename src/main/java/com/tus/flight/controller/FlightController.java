package com.tus.flight.controller;

import java.time.LocalDate;
import java.util.List;

import com.tus.flight.dto.FlightDTO;
import com.tus.flight.dto.PassengerDTO;
import com.tus.flight.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FlightController {

	private final FlightService flightService;

	public FlightController(FlightService flightService) {
		this.flightService = flightService;
	}

	@GetMapping("/flights")
	public ResponseEntity<Page<FlightDTO>> getAll(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return ResponseEntity.ok(flightService.getAllFlights(PageRequest.of(page, size)));
	}

	@PostMapping
	public ResponseEntity<FlightDTO> create(@RequestBody FlightDTO dto) {
		return new ResponseEntity<>(flightService.createFlight(dto), HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		flightService.deleteFlight(id);
		return ResponseEntity.noContent().build();
	}

	//demo comment
	@PutMapping("/{id}")
	public ResponseEntity<FlightDTO> update(@PathVariable Long id, @RequestBody FlightDTO dto) {
		return ResponseEntity.ok(flightService.updateFlight(id, dto));
	}

	@GetMapping("/{id}/passengers")
	public ResponseEntity<List<PassengerDTO>> listPassengers(@PathVariable Long id) {
		return ResponseEntity.ok(flightService.getPassengersByFlight(id));
	}

	@PostMapping("/{id}/passengers")
	public ResponseEntity<PassengerDTO> createPassenger(@PathVariable Long id, @RequestBody PassengerDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(flightService.addPassengerToFlight(id, dto));
	}

	@GetMapping("/search")
	public ResponseEntity<List<FlightDTO>> searchByDate(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		return ResponseEntity.ok(flightService.findByDate(date));
	}
}
