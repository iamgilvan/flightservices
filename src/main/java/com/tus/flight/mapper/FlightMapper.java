package com.tus.flight.mapper;

import com.tus.flight.dto.FlightDTO;
import com.tus.flight.dto.PassengerDTO;
import com.tus.flight.model.Flight;
import com.tus.flight.model.Passenger;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FlightMapper {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public PassengerDTO toPassengerDTO(Passenger passenger) {
        if (passenger == null) return null;

        PassengerDTO dto = new PassengerDTO();
        dto.setFirstName(passenger.getFirstName());
        dto.setLastName(passenger.getLastName());
        dto.setEmail(passenger.getEmail());
        if (passenger.getFlight() != null) {
            dto.setFlightId(passenger.getFlight().getId());
        }
        return dto;
    }

    public FlightDTO toFlightDTO(Flight flight) {
        if (flight == null) return null;

        FlightDTO dto = new FlightDTO();
        dto.setId(flight.getId());
        dto.setFlightNumber(flight.getFlightNumber());
        dto.setDepartureCity(flight.getDepartureCity());
        dto.setArrivalCity(flight.getArrivalCity());
        dto.setOperatingAirlines(flight.getOperatingAirlines());
        if (flight.getEstimatedDepartureTime() != null) {
            dto.setEstimatedDepartureTime(flight.getEstimatedDepartureTime().toString());
        }
        if (flight.getDateOfDeparture() != null) {
            dto.setDateOfDeparture(flight.getDateOfDeparture().format(dateFormatter));
        }

        if (flight.getPassengers() != null) {
            List<PassengerDTO> passengerDTOs = flight.getPassengers().stream()
                    .map(this::toPassengerDTO)
                    .collect(Collectors.toList());
            dto.setPassengers(passengerDTOs);
        }
        return dto;
    }
}
