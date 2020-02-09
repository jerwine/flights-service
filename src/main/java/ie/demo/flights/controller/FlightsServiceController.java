package ie.demo.flights.controller;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ie.demo.flights.api.json.AvailibilityJson;
import ie.demo.flights.mapper.AvailibilityMapper;
import ie.demo.flights.service.FlightsService; 

@RestController
@RequestMapping("/flights")
public class FlightsServiceController {

	final FlightsService flightsService;
	final AvailibilityMapper availibilityMapper;

	public FlightsServiceController( FlightsService flightsService, AvailibilityMapper availibilityMapper ) {
		this.flightsService = flightsService;
		this.availibilityMapper = availibilityMapper;
	}

	/**
	 * Request Flights Availabilities
	 * @param originAirport
	 * @param destinationAirport
	 * @param departuerDate
	 * @param returnDate
	 * @param numOfPassengers
	 * @return
	 */
	@GetMapping( path="/{originAirport}/{destinationAirport}/{departuerDate}/{returnDate}/{numOfPassengers}", produces = "application/json" )
	@ResponseStatus( HttpStatus.OK )
	public AvailibilityJson getAvailabilities(
		@PathVariable( required = true ) String originAirport,
		@PathVariable( required = true ) String destinationAirport,
		@PathVariable( required = true ) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date departuerDate,
		@PathVariable( required = true ) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date returnDate,
		@PathVariable( required = true ) String numOfPassengers) {
		return availibilityMapper.toAvailibilityJson(
				flightsService.getAvailabilities( originAirport, destinationAirport, departuerDate, returnDate, numOfPassengers ) );
	}
}