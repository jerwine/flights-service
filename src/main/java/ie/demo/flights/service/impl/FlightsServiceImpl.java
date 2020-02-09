package ie.demo.flights.service.impl;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ie.demo.flights.domain.Availability;
import ie.demo.flights.service.FlightsService;

@Service
public class FlightsServiceImpl implements FlightsService {

	final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

	static final String URL = "http://private-anon-06627bb57c-mockairline.apiary-mock.com/flights";

	final RestTemplate restTemplate;

	public FlightsServiceImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	/*
	 * @see com.travelport.digital.task.service.TaskService#getAvailabilities(String, String, Date, Date, String)
	 */
	@Override
	public Availability getAvailabilities(String originAirport, String destinationAirport, Date departuerDate,
			Date returnDate, String numOfPassengers) {

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<Availability> entity = new HttpEntity<Availability>(headers);

		return restTemplate.exchange(
				URL + "/" + originAirport + "/" + destinationAirport + "/" + simpleDateFormat.format(departuerDate)
						+ "/" + simpleDateFormat.format(returnDate) + "/" + numOfPassengers,
				HttpMethod.GET, entity, Availability.class).getBody();
	}
}
