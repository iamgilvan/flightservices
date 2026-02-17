package com.tus.flight.service;

import com.tus.flight.dto.FlightDTO;
import com.tus.flight.dto.PassengerDTO;
import com.tus.flight.exception.ResourceNotFoundException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    void getAllFlights_ShouldReturnPageOfFlightDTOs() {
        Pageable pageable = PageRequest.of(0, 10);
        Flight flight = new Flight();
        Page<Flight> flightPage = new PageImpl<>(Collections.singletonList(flight));
        FlightDTO flightDTO = new FlightDTO();

        when(flightRepository.findAll(pageable)).thenReturn(flightPage);
        when(flightMapper.toFlightDTO(flight)).thenReturn(flightDTO);

        Page<FlightDTO> result = flightService.getAllFlights(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(flightRepository).findAll(pageable);
        verify(flightMapper).toFlightDTO(flight);
    }

    @Test
    void createFlight_ShouldReturnCreatedFlightDTO() {
        FlightDTO inputDTO = new FlightDTO();
        inputDTO.setFlightNumber("FL123");
        Flight savedFlight = new Flight();
        FlightDTO outputDTO = new FlightDTO();
        outputDTO.setFlightNumber("FL123");

        when(flightRepository.save(any(Flight.class))).thenReturn(savedFlight);
        when(flightMapper.toFlightDTO(savedFlight)).thenReturn(outputDTO);

        FlightDTO result = flightService.createFlight(inputDTO);

        assertNotNull(result);
        assertEquals("FL123", result.getFlightNumber());
        verify(flightRepository).save(any(Flight.class));
    }

    @Test
    void deleteFlight_ShouldCallRepositoryDelete_WhenFlightExists() {
        Long id = 1L;
        when(flightRepository.existsById(id)).thenReturn(true);

        flightService.deleteFlight(id);

        verify(flightRepository).deleteById(id);
    }

    @Test
    void deleteFlight_ShouldThrowResourceNotFoundException_WhenFlightDoesNotExist() {
        Long id = 1L;
        when(flightRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> flightService.deleteFlight(id));
    }

    @Test
    void findByDate_ShouldReturnListOfFlightDTOs() {
        LocalDate date = LocalDate.now();
        Flight flight = new Flight();
        FlightDTO flightDTO = new FlightDTO();

        when(flightRepository.findByDateOfDeparture(date)).thenReturn(Collections.singletonList(flight));
        when(flightMapper.toFlightDTO(flight)).thenReturn(flightDTO);

        List<FlightDTO> result = flightService.findByDate(date);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(flightRepository).findByDateOfDeparture(date);
    }

    @Test
    void getPassengersByFlight_ShouldReturnListOfPassengerDTOs_WhenFlightExists() {
        Long flightId = 1L;
        Flight flight = new Flight();
        Passenger passenger = new Passenger();
        flight.getPassengers().add(passenger);
        PassengerDTO passengerDTO = new PassengerDTO();

        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        when(flightMapper.toPassengerDTO(passenger)).thenReturn(passengerDTO);

        List<PassengerDTO> result = flightService.getPassengersByFlight(flightId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(flightRepository).findById(flightId);
    }

    @Test
    void getPassengersByFlight_ShouldThrowEntityNotFoundException_WhenFlightDoesNotExist() {
        Long flightId = 1L;
        when(flightRepository.findById(flightId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> flightService.getPassengersByFlight(flightId));
    }

    @Test
    void addPassengerToFlight_ShouldReturnPassengerDto_WhenSuccessful() {
        // Arrange
        Long flightId = 1L;
        PassengerDTO inputDto = new PassengerDTO();
        inputDto.setFirstName("Gilvan");

        Flight flight = new Flight();
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
        assertTrue(flight.getPassengers().contains(savedPassenger));
        verify(flightRepository).findById(flightId);
        verify(passengerRepository).save(any(Passenger.class));
    }

    @Test
    void addPassengerToFlight_ShouldThrowEntityNotFoundException_WhenFlightIdDoesNotExist() {
        // Arrange
        Long flightId = 999L;
        PassengerDTO inputDto = new PassengerDTO();

        when(flightRepository.findById(flightId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            flightService.addPassengerToFlight(flightId, inputDto);
        });

        verify(flightRepository).findById(flightId);
    }

    @Test
    void updateFlight_ShouldReturnUpdatedFlightDTO_WhenFlightExists() {
        Long id = 1L;
        FlightDTO inputDTO = new FlightDTO();
        inputDTO.setFlightNumber("NewNumber");
        inputDTO.setDepartureCity("NewCity");
        inputDTO.setArrivalCity("ArrivalCity");
        inputDTO.setOperatingAirlines("Airline");
        inputDTO.setEstimatedDepartureTime("10:00:00");
        inputDTO.setDateOfDeparture("2023-12-01");

        Flight flight = new Flight();
        Flight savedFlight = new Flight();
        FlightDTO outputDTO = new FlightDTO();
        outputDTO.setFlightNumber("NewNumber");

        when(flightRepository.findById(id)).thenReturn(Optional.of(flight));
        when(flightRepository.save(any(Flight.class))).thenReturn(savedFlight);
        when(flightMapper.toFlightDTO(savedFlight)).thenReturn(outputDTO);

        FlightDTO result = flightService.updateFlight(id, inputDTO);

        assertNotNull(result);
        assertEquals("NewNumber", result.getFlightNumber());
        verify(flightRepository).findById(id);
        verify(flightRepository).save(flight);
        assertEquals("NewNumber", flight.getFlightNumber());
        assertEquals("NewCity", flight.getDepartureCity());
    }

    @Test
    void updateFlight_ShouldThrowResourceNotFoundException_WhenFlightDoesNotExist() {
        Long id = 1L;
        FlightDTO inputDTO = new FlightDTO();
        when(flightRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> flightService.updateFlight(id, inputDTO));
    }
}
