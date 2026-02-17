package com.tus.flight.controller;

import com.tus.flight.dto.FlightDTO;
import com.tus.flight.dto.PassengerDTO;
import com.tus.flight.service.FlightService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@WebMvcTest(FlightController.class)
class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightService flightService;

    @Test
    void getAllFlights_ShouldReturnFlightsAndOkStatus() throws Exception {
        // Arrange
        FlightDTO flightDTO = new FlightDTO();
        flightDTO.setFlightNumber("AA123");
        flightDTO.setDepartureCity("New York");
        flightDTO.setArrivalCity("London");

        List<FlightDTO> flightsList = Collections.singletonList(flightDTO);
        Page<FlightDTO> flightsPage = new PageImpl<>(flightsList, PageRequest.of(0, 3), 1);

        when(flightService.getAllFlights(any(Pageable.class))).thenReturn(flightsPage);

        // Act & Assert
        mockMvc.perform(get("/flights")
                        .param("page", "0")
                        .param("size", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].flightNumber").value("AA123"))
                .andExpect(jsonPath("$.content[0].departureCity").value("New York"))
                .andExpect(jsonPath("$.content[0].arrivalCity").value("London"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void addPassengerToFlight_ShouldReturnCreatedStatus() throws Exception {
        // Arrange
        Long flightId = 1L;

        String passengerJson = "{\"firstName\":\"Gilvan\", \"lastName\":\"Almeida\", \"email\":\"gilvan@example.com\"}";

        PassengerDTO responseDto = new PassengerDTO();
        responseDto.setFirstName("Gilvan");

        when(flightService.addPassengerToFlight(eq(flightId), any(PassengerDTO.class)))
                .thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/{id}/passengers", flightId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(passengerJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Gilvan"));
    }
}