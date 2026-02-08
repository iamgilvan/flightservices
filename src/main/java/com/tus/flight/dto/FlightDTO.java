package com.tus.flight.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class FlightDTO {
    private Long id;
    private String flightNumber;
    private String operatingAirlines;
    private String departureCity;
    private String arrivalCity;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String dateOfDeparture;
    private String estimatedDepartureTime;

    private List<PassangerDTO> passengers = new ArrayList<>();

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getOperatingAirlines() {
        return operatingAirlines;
    }

    public void setOperatingAirlines(String operatingAirlines) {
        this.operatingAirlines = operatingAirlines;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public String getDateOfDeparture() {
        return dateOfDeparture;
    }

    public void setDateOfDeparture(String dateOfDeparture) {
        this.dateOfDeparture = dateOfDeparture;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<PassangerDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassangerDTO> passengers) {
        this.passengers = passengers;
    }

    public String getEstimatedDepartureTime() {
        return estimatedDepartureTime;
    }

    public void setEstimatedDepartureTime(String estimatedDepartureTime) {
        this.estimatedDepartureTime = estimatedDepartureTime;
    }
}
