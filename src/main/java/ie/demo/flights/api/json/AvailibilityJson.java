package ie.demo.flights.api.json;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Getter;

@Getter
@JsonRootName(value = "availability")
public class AvailibilityJson {
	List<Flight> flight = new ArrayList<>();
}