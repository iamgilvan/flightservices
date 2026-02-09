package com.tus.flight.service;

import com.tus.flight.dto.FlightDTO;
import com.tus.flight.dto.PassengerDTO;
import com.tus.flight.exception.ResourceNotFoundException;
import com.tus.flight.mapper.FlightMapper;
import com.tus.flight.model.Flight;
import com.tus.flight.model.Passenger;
import com.tus.flight.repo.FlightRepository;
import com.tus.flight.repo.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightService {
    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private FlightMapper flightMapper;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Page<FlightDTO> getAllFlights(Pageable pageable) {
        return flightRepository.findAll(pageable).map(flightMapper::toFlightDTO);
    }

    public FlightDTO createFlight(FlightDTO dto) {
        Flight entity = new Flight(dto.getFlightNumber(), dto.getOperatingAirlines(), dto.getDepartureCity(), dto.getArrivalCity(), dto.getDateOfDeparture(), dto.getEstimatedDepartureTime() );
        return flightMapper.toFlightDTO(flightRepository.save(entity));
    }

    public void deleteFlight(Long id) {
        if(!flightRepository.existsById(id)) throw new ResourceNotFoundException("Flight not found with id: " + id);
        flightRepository.deleteById(id);
    }

    public List<FlightDTO> findByDate(LocalDate date) {
        return flightRepository.findByDateOfDeparture(date).stream()
                .map(flightMapper::toFlightDTO)
                .collect(Collectors.toList());
    }

    public List<PassengerDTO> getPassengersByFlight(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new EntityNotFoundException("Flight not found"));

        return flight.getPassengers().stream()
                .map(flightMapper::toPassengerDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PassengerDTO addPassengerToFlight(Long flightId, PassengerDTO dto) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new EntityNotFoundException("Flight not found"));

        Passenger passenger = new Passenger(dto.getFirstName(), dto.getLastName() , dto.getEmail());

        Passenger savedPassenger = passengerRepository.save(passenger);

        flight.getPassengers().add(savedPassenger);

        Passenger saved = passengerRepository.save(passenger);
        return flightMapper.toPassengerDTO(saved);
    }


    public FlightDTO updateFlight(Long id, FlightDTO dto) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));

        flight.setFlightNumber(dto.getFlightNumber());
        flight.setDepartureCity(dto.getDepartureCity());
        flight.setArrivalCity(dto.getArrivalCity());
        flight.setOperatingAirlines(dto.getOperatingAirlines());

        if(dto.getEstimatedDepartureTime() != null) {
            flight.setEstimatedDepartureTime(Time.valueOf(dto.getEstimatedDepartureTime()));
        }
        if(dto.getDateOfDeparture() != null) {
            flight.setDateOfDeparture(LocalDate.parse(dto.getDateOfDeparture()));
        }

        return flightMapper.toFlightDTO(flightRepository.save(flight));
    }
}
