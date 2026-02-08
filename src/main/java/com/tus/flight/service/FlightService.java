package com.tus.flight.service;

import com.tus.flight.dto.FlightDTO;
import com.tus.flight.dto.PassangerDTO;
import com.tus.flight.exception.ResourceNotFoundException;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightService {
    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Page<FlightDTO> getAllFlights(Pageable pageable) {
        return flightRepository.findAll(pageable).map(this::convertToFlightDTO);
    }

    public FlightDTO createFlight(FlightDTO dto) {
        Flight entity = new Flight(dto.getFlightNumber(), dto.getOperatingAirlines(), dto.getDepartureCity(), dto.getArrivalCity(), dto.getDateOfDeparture(), dto.getEstimatedDepartureTime() );
        return convertToFlightDTO(flightRepository.save(entity));
    }

    public void deleteFlight(Long id) {
        if(!flightRepository.existsById(id)) throw new ResourceNotFoundException("Flight not found with id: " + id);
        flightRepository.deleteById(id);
    }

    public List<FlightDTO> findByDate(LocalDate date) {
        return flightRepository.findByDateOfDeparture(date).stream()
                .map(this::convertToFlightDTO)
                .collect(Collectors.toList());
    }

    public List<PassangerDTO> getPassengersByFlight(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new EntityNotFoundException("Flight not found"));

        return flight.getPassengers().stream()
                .map(this::convertToReservationDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PassangerDTO addPassengerToFlight(Long flightId, PassangerDTO dto) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new EntityNotFoundException("Flight not found"));

        Passenger passenger = new Passenger();
        passenger.setFirstName(dto.getFirstName());
        passenger.setLastName(dto.getLastName());
        passenger.setEmail(dto.getEmail());
        passenger.setFlight(flight);

        Passenger saved = passengerRepository.save(passenger);
        return convertToReservationDTO(saved);
    }


    public FlightDTO updateFlight(Long id, FlightDTO dto) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));

        flight.setFlightNumber(dto.getFlightNumber());
        flight.setDepartureCity(dto.getDepartureCity());
        flight.setArrivalCity(dto.getArrivalCity());
        if(dto.getDateOfDeparture() != null) {
            flight.setDateOfDeparture(LocalDate.parse(dto.getDateOfDeparture()));
        }

        return convertToFlightDTO(flightRepository.save(flight));
    }


    private FlightDTO convertToFlightDTO(Flight flight) {
        FlightDTO dto = new FlightDTO();
        dto.setId(flight.getId());
        dto.setFlightNumber(flight.getFlightNumber());
        dto.setDepartureCity(flight.getDepartureCity());
        dto.setArrivalCity(flight.getArrivalCity());
        dto.setOperatingAirlines(flight.getOperatingAirlines());
        dto.setEstimatedDepartureTime(flight.getEstimatedDepartureTime().toString());
        if (flight.getDateOfDeparture() != null) {
            dto.setDateOfDeparture(flight.getDateOfDeparture().format(formatter));
        }
        if (flight.getPassengers() != null) {
            List<PassangerDTO> passengerDTOs = new ArrayList<>();
            for (Passenger p : flight.getPassengers()) {
                passengerDTOs.add(convertToReservationDTO(p));
            }
            dto.setPassengers(passengerDTOs);
        }
        return dto;
    }

    private PassangerDTO convertToReservationDTO(Passenger p) {
        PassangerDTO dto = new PassangerDTO();
        dto.setFirstName(p.getFirstName());
        dto.setLastName(p.getLastName());
        dto.setEmail(p.getEmail());
        dto.setFlightId(p.getFlight().getId());
        return dto;
    }

}
