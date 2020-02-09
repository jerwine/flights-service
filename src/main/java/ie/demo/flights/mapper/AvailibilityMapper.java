package ie.demo.flights.mapper;

import static ie.demo.flights.api.json.FareType.fareType;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.mapstruct.Mapper;

import ie.demo.flights.api.json.AvailibilityJson;
import ie.demo.flights.api.json.BookingFee;
import ie.demo.flights.api.json.Business;
import ie.demo.flights.api.json.Economy;
import ie.demo.flights.api.json.FareClass;
import ie.demo.flights.api.json.FarePrices;
import ie.demo.flights.api.json.FareType;
import ie.demo.flights.api.json.First;
import ie.demo.flights.api.json.Flight;
import ie.demo.flights.api.json.Flight.ArrivesOn;
import ie.demo.flights.api.json.MoneyBase;
import ie.demo.flights.api.json.Tax;
import ie.demo.flights.api.json.Ticket;
import ie.demo.flights.domain.Availability;

@Mapper
public class AvailibilityMapper {

	final SimpleDateFormat sdf = new SimpleDateFormat( "HH:mm" );
	final DecimalFormat df = new DecimalFormat( "00" );

	/**
	 * Transform the incoming Availability model
	 * @param source
	 * @return
	 */
	public AvailibilityJson toAvailibilityJson( Availability source ) {

		AvailibilityJson destination = new AvailibilityJson();

		for( Availability.Flight f : source.getFlight() ) {

			Flight flight = new Flight();

			flight.setOperator( f.getCarrierCode() );
			flight.setFlightNumber( f.getFlightDesignator() );
			flight.setDepartsFrom( f.getOriginAirport() );
			flight.setArrivesAt( f.getDestinationAirport() );

			Flight.DepartsOn departsOn = new Flight.DepartsOn();
			departsOn.setDate( f.getDepartureDate().getTime() );
			departsOn.setTime( f.getDepartureDate().getTime() );
			flight.setDepartsOn( departsOn );

			ArrivesOn arrivesOn = new Flight.ArrivesOn();
			arrivesOn.setDate( f.getArrivalDate().getTime() );
			arrivesOn.setTime( f.getArrivalDate().getTime() );
			flight.setArrivesOn( arrivesOn );

			flight.setFlightTime( durationFormatted( f.getDepartureDate().getTime(), f.getArrivalDate().getTime() ) );
			flight.getFarePrices().add( populateFarePrices( f ) );
			destination.getFlight().add( flight );
		}
		return destination;
	}

	/**
	 * Populate all the Fare Prices
	 * @param f
	 * @return
	 */
	FarePrices populateFarePrices( Availability.Flight f ) {
		FarePrices farePrices = new FarePrices();
		for( Availability.Flight.Fares.Fare fare : f.getFares().getFare() ) {
			/* Evaluate the fare type */
			FareType fareType = fareType( fare.getClazz() );
			/* Populate fare types */
			populateFareTypes( fare, farePrices, fareType );
		}
		return farePrices;
	}

	/**
	 * Populate Fare Types
	 * @param fare
	 * @param farePrices
	 * @param fareType
	 */
	void populateFareTypes( Availability.Flight.Fares.Fare fare, FarePrices farePrices, FareType fareType ) {

		/* Economy */
		if( FareType.YIF.equals( fareType ) ) {
			farePrices.setEconomy( new Economy() );
			populatePrices( farePrices.getEconomy(), fare );

		/* Business */
		} else if( FareType.CIF.equals( fareType ) ) {
			farePrices.setBusiness( new Business() );
			populatePrices( farePrices.getBusiness(), fare );

		/* First Class */
		} else if( FareType.FIF.equals( fareType ) ) {

			farePrices.setFirst( new First() );
			populatePrices( farePrices.getFirst(), fare );
		}
	}

	/**
	 * Populate Prices
	 * @param fareClass
	 * @param fare
	 * @return
	 */
	void populatePrices( FareClass fareClass, Availability.Flight.Fares.Fare fare ) {

		fareClass.setTicket( new Ticket() );
		populateCurrencyAndAmount( fareClass.getTicket(), fare.getBasePrice() );

		fareClass.setBookingFee( new BookingFee() );
		populateCurrencyAndAmount( fareClass.getBookingFee(), fare.getFees() );

		fareClass.setTax( new Tax() );
		populateCurrencyAndAmount( fareClass.getTax(), fare.getTax() );
	}

	/**
	 * The value ( for example "EUR 136.00" ) is split into the MoneyBase
	 * @param moneyBase
	 * @param value
	 */
	void populateCurrencyAndAmount( MoneyBase moneyBase, String value ) {
		StringTokenizer st = new StringTokenizer( value, " " );
		moneyBase.setCurrency( st.nextToken() );
		moneyBase.setAmount( st.nextToken() );
	}

	/**
	 * Return the difference between the startDate and endDate formatted
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	String durationFormatted( Date startDate, Date endDate ) {
		DateTime startDateTime = new DateTime( startDate.getTime() );
		DateTime endDateTime = new DateTime( endDate.getTime() );
		Period period = new Period( startDateTime, endDateTime );
		return df.format( period.getHours() ) + ":" + df.format( period.getMinutes() ) ;
	}
}
