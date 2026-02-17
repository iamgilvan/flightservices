package com.tus.flight.mapper;

import com.tus.flight.dto.FlightDTO;
import com.tus.flight.dto.PassengerDTO;
import com.tus.flight.model.Flight;
import com.tus.flight.model.Passenger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FlightMapperTest {
    private final FlightMapper flightMapper = new FlightMapper();

    @Test
    void toPassengerDTO_ShouldMapFieldsCorrectly() {
        // Arrange
        Passenger passenger = new Passenger("Gilvan", "Almeida", "gilvan@example.com");

        // Act
        PassengerDTO dto = flightMapper.toPassengerDTO(passenger);

        // Assert
        assertNotNull(dto);
        assertEquals("Gilvan", dto.getFirstName());
        assertEquals("Almeida", dto.getLastName());
        assertEquals("gilvan@example.com", dto.getEmail());
    }

    @Test
    void toFlightDTO_ShouldMapFieldsCorrectly() {
        // Arrange
        Flight flight = new Flight();
        flight.setFlightNumber("AA123");
        flight.setDepartureCity("New York");
        flight.setArrivalCity("London");

        // Act
        FlightDTO dto = flightMapper.toFlightDTO(flight);

        // Assert
        assertNotNull(dto);
        assertEquals("AA123", dto.getFlightNumber());
        assertEquals("New York", dto.getDepartureCity());
        assertEquals("London", dto.getArrivalCity());
    }
}
