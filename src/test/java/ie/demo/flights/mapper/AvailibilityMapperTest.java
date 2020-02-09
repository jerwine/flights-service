package ie.demo.flights.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.jupiter.api.Test;

import ie.demo.flights.api.json.AvailibilityJson;
import ie.demo.flights.api.json.FareClass;
import ie.demo.flights.api.json.FarePrices;
import ie.demo.flights.api.json.FareType;
import ie.demo.flights.api.json.First;
import ie.demo.flights.api.json.MoneyBase;
import ie.demo.flights.domain.Availability;
import ie.demo.flights.domain.Availability.Flight;
import ie.demo.flights.domain.Availability.Flight.Fares;
import ie.demo.flights.domain.Availability.Flight.Fares.Fare;

public class AvailibilityMapperTest {

	final static SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
	final static SimpleDateFormat dateTimeFormat = new SimpleDateFormat( "yyyy-MM-dd kk:mm" );
	final static SimpleDateFormat timeFormat = new SimpleDateFormat( "kk:mm" );

	AvailibilityMapper availibilityMapper = new AvailibilityMapper();

	@Test
	public void testToAvailibilityJson_I() throws ParseException {
		Availability source = new Availability();
		assertNotNull( availibilityMapper.toAvailibilityJson( source ) );
	}

	@Test
	public void testToAvailibilityJson_II() throws ParseException {
		Availability source = new Availability();
		Flight flight = new Flight();
		flight.setCarrierCode( "EI" );
		flight.setFlightDesignator( "EI554" );
		flight.setOriginAirport( "DUB" );

		flight.setDepartureDate( Calendar.getInstance() );
		flight.getDepartureDate().setTime( dateTimeFormat.parse( "2014-01-02 10:48" ) );

		flight.setArrivalDate( Calendar.getInstance() );
		flight.getArrivalDate().setTime( dateTimeFormat.parse( "2014-01-02 13:04" ) );

		Fares fares = new Fares();
		Fare fif = new Fare();
		fif.setClazz( FareType.YIF.getId() );
		fif.setBasePrice( "EUR 272.00" );
		fif.setFees( "EUR 17.00" );
		fif.setTax( "EUR 13.60" );
		fares.getFare().add( fif );
		flight.setFares( fares );
		source.getFlight().add( flight );

		AvailibilityJson result = availibilityMapper.toAvailibilityJson( source );

		assertEquals( result.getFlight().get( 0 ).getOperator(), "EI" );
		assertEquals( result.getFlight().get( 0 ).getFlightNumber(), "EI554" );
		assertEquals( result.getFlight().get( 0 ).getDepartsFrom(), "DUB" );
		assertEquals( result.getFlight().get( 0 ).getDepartsOn().getDate(), dateTimeFormat.parse( "2014-01-02 10:48" ) );
		assertEquals( result.getFlight().get( 0 ).getDepartsOn().getTime(), dateTimeFormat.parse( "2014-01-02 10:48" ) );
		assertEquals( result.getFlight().get( 0 ).getArrivesOn().getDate(), dateTimeFormat.parse( "2014-01-02 13:04" ) );
		assertEquals( result.getFlight().get( 0 ).getArrivesOn().getTime(), dateTimeFormat.parse( "2014-01-02 13:04" ) );

		assertEquals( result.getFlight().get( 0 ).getFarePrices().get( 0 ).getEconomy().getTicket().getCurrency(), "EUR" );
		assertEquals( result.getFlight().get( 0 ).getFarePrices().get( 0 ).getEconomy().getTicket().getAmount(), "272.00" );
		assertEquals( result.getFlight().get( 0 ).getFarePrices().get( 0 ).getEconomy().getBookingFee().getCurrency(), "EUR" );
		assertEquals( result.getFlight().get( 0 ).getFarePrices().get( 0 ).getEconomy().getBookingFee().getAmount(), "17.00" );
		assertEquals( result.getFlight().get( 0 ).getFarePrices().get( 0 ).getEconomy().getTax().getCurrency(), "EUR" );
		assertEquals( result.getFlight().get( 0 ).getFarePrices().get( 0 ).getEconomy().getTax().getAmount(), "13.60" );
	}

	@Test
	public void testPopulateFarePrices() {
		Availability.Flight f = new Flight();
		Fares fares = new Fares();
		Fare fif = new Fare();
		fif.setClazz( FareType.YIF.getId() );
		fif.setBasePrice( "EUR 272.00" );
		fif.setFees( "EUR 17.00" );
		fif.setTax( "EUR 13.60" );
		fares.getFare().add( fif );
		f.setFares( fares );
		FarePrices farePrices = availibilityMapper.populateFarePrices( f );
		assertEquals( farePrices.getEconomy().getTicket().getCurrency(), "EUR" );
		assertEquals( farePrices.getEconomy().getTicket().getAmount(), "272.00" );
		assertEquals( farePrices.getEconomy().getBookingFee().getCurrency(), "EUR" );
		assertEquals( farePrices.getEconomy().getBookingFee().getAmount(), "17.00" );
		assertEquals( farePrices.getEconomy().getTax().getCurrency(), "EUR" );
		assertEquals( farePrices.getEconomy().getTax().getAmount(), "13.60" );
	}

	@Test
	public void testPopulateFareTypes() {
		Availability.Flight.Fares.Fare fare = new Availability.Flight.Fares.Fare();
		fare.setBasePrice( "EUR 272.00" );
		fare.setFees( "EUR 17.00" );
		fare.setTax( "EUR 13.60" );
		FarePrices farePrices = new FarePrices();
		FareType fareType = FareType.YIF;
		availibilityMapper.populateFareTypes( fare, farePrices, fareType );
		assertEquals( farePrices.getEconomy().getTicket().getCurrency(), "EUR" );
		assertEquals( farePrices.getEconomy().getTicket().getAmount(), "272.00" );
		assertEquals( farePrices.getEconomy().getBookingFee().getCurrency(), "EUR" );
		assertEquals( farePrices.getEconomy().getBookingFee().getAmount(), "17.00" );
		assertEquals( farePrices.getEconomy().getTax().getCurrency(), "EUR" );
		assertEquals( farePrices.getEconomy().getTax().getAmount(), "13.60" );
	}

	@Test
	public void testPopulatePrices() {
		FareClass fareClass = new First();
		Availability.Flight.Fares.Fare fare = new Availability.Flight.Fares.Fare();
		fare.setBasePrice( "EUR 272.00" );
		fare.setFees( "EUR 17.00" );
		fare.setTax( "EUR 13.60" );
		availibilityMapper.populatePrices( fareClass, fare );
		assertEquals( fareClass.getTicket().getCurrency(), "EUR" );
		assertEquals( fareClass.getTicket().getAmount(), "272.00" );
		assertEquals( fareClass.getBookingFee().getCurrency(), "EUR" );
		assertEquals( fareClass.getBookingFee().getAmount(), "17.00" );
		assertEquals( fareClass.getTax().getCurrency(), "EUR" );
		assertEquals( fareClass.getTax().getAmount(), "13.60" );
	}

	@Test
	public void testPopulateCurrencyAndAmount() {
		MoneyBase moneyBase = new MoneyBase();
		availibilityMapper.populateCurrencyAndAmount( moneyBase, "EUR 12.00" );
		assertEquals( "EUR", moneyBase.getCurrency() );
		assertEquals( "12.00", moneyBase.getAmount() );
	}

	@Test
	public void durationFormatted_I() throws ParseException {
		String result = availibilityMapper.durationFormatted(
				dateTimeFormat.parse( "2014-01-02 10:48" ),
				dateTimeFormat.parse( "2014-01-02 13:04" ) );
		assertEquals( "02:16", result );
	}

	@Test
	public void durationFormatted_II() throws ParseException {
		String result = availibilityMapper.durationFormatted(
				dateTimeFormat.parse( "2014-01-02 00:00" ),
				dateTimeFormat.parse( "2014-01-02 00:00" ) );
		assertEquals( "00:00", result );
	}
}
