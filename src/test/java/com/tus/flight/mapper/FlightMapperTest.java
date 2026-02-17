package com.tus.flight.mapper;

import com.tus.flight.dto.PassengerDTO;
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
}
