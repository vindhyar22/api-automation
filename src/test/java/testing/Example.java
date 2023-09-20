package testing;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import org.json.JSONObject;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;

import io.restassured.mapper.ObjectMapper;

public class Example {
	@Test
	public void check() {
//
//		LocalDate today = LocalDate.now();
//
//		LocalTime currentTimeUTC = LocalTime.now(ZoneOffset.UTC);
//
//		LocalTime nextHourTimeUTC = currentTimeUTC.plus(1, ChronoUnit.HOURS);
//
//		String startdate = today + " " + currentTimeUTC;
//		String enddate = today + " " + nextHourTimeUTC;
//		System.out.println(startdate);
//		System.out.println(enddate);
//		System.out.println("TEST"+Math.round(Math.random()*1000));
		int leftLimit = 97; // letter 'a'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = 10;
	    Random random = new Random();

	    String generatedString = random.ints(leftLimit, rightLimit + 1)
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();

	    System.out.println(generatedString);
	}
	
}