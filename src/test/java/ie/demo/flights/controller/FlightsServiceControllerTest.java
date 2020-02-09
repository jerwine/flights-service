package ie.demo.flights.controller;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;

import ie.demo.flights.api.json.AvailibilityJson;
import ie.demo.flights.domain.ObjectFactory;
import ie.demo.flights.mapper.AvailibilityMapper;
import ie.demo.flights.service.FlightsService;

public class FlightsServiceControllerTest {

	ObjectFactory objectFactory = new ObjectFactory();

	WebTestClient webTestClient;
	FlightsServiceController flightsServiceController;
	FlightsService flightsService;
	AvailibilityMapper availibilityMapper;

	@BeforeEach
	public void setUp() throws Exception {
		flightsService = Mockito.mock( FlightsService.class );
		flightsServiceController = new FlightsServiceController( flightsService, availibilityMapper );
		webTestClient = WebTestClient.bindToController( flightsServiceController ).build();
	}

	@Test
	public void testGetAvailabilities() {
		BDDMockito.given( flightsService.getAvailabilities( "BUD", "DUB", 
				new Date(), new Date(), "1") )
		.willReturn( objectFactory.createAvailability() );

		webTestClient.get()
			.uri( "/flights" )
			.exchange()
			.expectBody( AvailibilityJson.class );
	}
}
