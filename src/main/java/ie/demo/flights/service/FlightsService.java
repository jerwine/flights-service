package ie.demo.flights.service;

import java.util.Date;

import ie.demo.flights.domain.Availability;

public interface FlightsService {

	/**
	 * Return Flights for the parameters
	 * @param originAirport
	 * @param destinationAirport
	 * @param departuerDate
	 * @param returnDate
	 * @param numOfPassengers
	 * @return
	 */
	Availability getAvailabilities( String originAirport, String destinationAirport, 
			Date departuerDate, Date returnDate, String numOfPassengers );
}
