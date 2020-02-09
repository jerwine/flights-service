package ie.demo.flights.api.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class On {
	private Date date;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mma")
	private Date time;
}