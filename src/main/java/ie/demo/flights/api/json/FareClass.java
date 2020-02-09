package ie.demo.flights.api.json;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class FareClass {
	Ticket ticket;
	BookingFee bookingFee;
	Tax tax;
}