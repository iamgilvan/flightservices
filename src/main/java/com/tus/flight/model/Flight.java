package com.tus.flight.model;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "flight")
public class Flight {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "flight_number")
	private String flightNumber;
	@Column(name = "operating_airlines")
	private String operatingAirlines;
	@Column(name = "departure_city")
	private String departureCity;
	@Column(name = "arrival_city")
	private String arrivalCity;
	@Column(name = "date_of_departure")
	private LocalDate dateOfDeparture;
	@Column(name = "estimated_departure_time")
	private Time estimatedDepartureTime;

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(
			name = "RESERVATION",
			joinColumns = @JoinColumn(name = "FLIGHT_ID"),
			inverseJoinColumns = @JoinColumn(name = "PASSENGER_ID")
	)
	private List<Passenger> passengers = new ArrayList<>();

	public Flight() {
	}

	public Flight(String flightNumber, String operationAirlines, String departureCity, String arrivalCity, String dateOfDeparture, String estimatedDepartureTime) {
		this.flightNumber = flightNumber;
		this.operatingAirlines = operationAirlines;
		this.departureCity = departureCity;
		this.arrivalCity = arrivalCity;
		if(dateOfDeparture != null) {
			this.dateOfDeparture = LocalDate.parse(dateOfDeparture);
		}
		if(estimatedDepartureTime != null) {
			this.estimatedDepartureTime = Time.valueOf(estimatedDepartureTime);
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getArrivalCity() {
		return arrivalCity;
	}

	public void setArrivalCity(String arrivalCity) {
		this.arrivalCity = arrivalCity;
	}

	public LocalDate getDateOfDeparture() {
		return dateOfDeparture;
	}

	public void setDateOfDeparture(LocalDate dateOfDeparture) {
		this.dateOfDeparture = dateOfDeparture;
	}

	public Time getEstimatedDepartureTime() {
		return estimatedDepartureTime;
	}

	public void setEstimatedDepartureTime(Time estimatedDepartureTime) {
		this.estimatedDepartureTime = estimatedDepartureTime;
	}

	public String getDepartureCity() {
		return departureCity;
	}

	public void setDepartureCity(String departureCity) {
		this.departureCity = departureCity;
	}

	public List<Passenger> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<Passenger> passengers) {
		this.passengers = passengers;
	}
}
