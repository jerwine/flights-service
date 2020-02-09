package ie.demo.flights.api.json;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Flight {
	String operator;
	String flightNumber;
	String departsFrom;
	String arrivesAt;
	DepartsOn departsOn;
	ArrivesOn arrivesOn;
	String flightTime;

	public static class DepartsOn extends On {}
	public static class ArrivesOn extends On {}

	List<FarePrices> farePrices = new ArrayList<>();
}