package ie.demo.flights.api.json;

public enum FareType {

	YIF( "YIF", "economy" ), CIF( "CIF", "business" ), FIF( "FIF", "first" );

	private String id;
	private String desc;

	private FareType( String id, String desc ) {
		this.id = id;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public String getId() {
		return id;
	}

	public static FareType fareType( String id ) {
		switch ( id ) {
			case "YIF": return FareType.YIF;
			case "CIF": return FareType.CIF;
			case "FIF": return FareType.FIF;
			default: return null;
		}
	}

	public static FareClass fareClass( FareType fareType ) {
		FareClass fareClass = null;
		if( FareType.YIF.equals( fareType ) ) fareClass = new Economy();
		if( FareType.CIF.equals( fareType ) ) fareClass = new Business();
		if( FareType.FIF.equals( fareType ) ) fareClass = new First();
		return fareClass;
	}
}