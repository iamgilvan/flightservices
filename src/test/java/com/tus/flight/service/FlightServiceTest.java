package com.tus.flight.service;

import com.tus.flight.dto.FlightDTO;
import com.tus.flight.dto.PassengerDTO;
import com.tus.flight.mapper.FlightMapper;
import com.tus.flight.model.Flight;
import com.tus.flight.model.Passenger;
import com.tus.flight.repo.FlightRepository;
import com.tus.flight.repo.PassengerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FlightServiceTest {
    @Mock
    private FlightRepository flightRepository;
    @Mock
    private PassengerRepository passengerRepository;
    @Mock
    private FlightMapper flightMapper;

    private FlightService flightService;

    @BeforeEach
    void setUp() {
        flightService = new FlightService(flightRepository, passengerRepository, flightMapper);
    }
    @Test
    void addPassengerToFlight_ShouldReturnPassengerDto_WhenSuccessful() {
        // Arrange (Configuração)
        Long flightId = 1L;
        PassengerDTO inputDto = new PassengerDTO();
        inputDto.setFirstName("Gilvan");

        Flight flight = new Flight();
        Passenger passenger = new Passenger("Gilvan", "Almeida", "gilvan@example.com");
        Passenger savedPassenger = new Passenger("Gilvan", "Almeida", "gilvan@example.com");
        PassengerDTO expectedResponse = new PassengerDTO();
        expectedResponse.setFirstName("Gilvan");

        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        when(passengerRepository.save(any(Passenger.class))).thenReturn(savedPassenger);
        when(flightMapper.toPassengerDTO(savedPassenger)).thenReturn(expectedResponse);

        // Act
        PassengerDTO result = flightService.addPassengerToFlight(flightId, inputDto);

        // Assert
        assertNotNull(result);
        assertEquals("Gilvan", result.getFirstName());
        verify(flightRepository).findById(flightId);
    }
}
