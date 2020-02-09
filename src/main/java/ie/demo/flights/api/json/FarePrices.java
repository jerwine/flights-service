package ie.demo.flights.api.json;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FarePrices {
	First first;
	Business business;
	Economy economy;
}