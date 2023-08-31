package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


import static io.restassured.RestAssured.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class Manual_SKR_Regenerate {
	
	
	String dtauthloginkey;
	String dtauthloginkey2;
	String SKRCreatedResponse;
	String keyDetails;
	String code;
	String codedelievered;
	String smartthings_code1;
	String smartthings_code2;
	String Key_ID;
	String userId;
	String code_regenerate;

	
	@BeforeClass
	public void setUp() {
		// Base URI
		RestAssured.baseURI = "https://api-qa.dthreaddev.com";
	}

	@Test(priority = 0)
	public void testAuth() {
		String requestBody = "{ \"email\": \"ios28@yopmail.com\", \"password\": \"Pass@123\" }";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON).body(requestBody).when()
				.post("/api/sessions/auth").then().statusCode(200) // Validate the status code is 200 (OK)
				.extract().response();

		dtauthloginkey = response.jsonPath().getString("data.userData.id");
		dtauthloginkey2 = response.jsonPath().getString("data.userData.organizationId");

		Assert.assertFalse(dtauthloginkey.isEmpty(), "ID is empty or null");
		Assert.assertFalse(dtauthloginkey2.isEmpty(), "Organization ID is empty or null");
		
	}

	@Test(priority = 1)
	public void testVerify2FA() {

		// Request Body
		String requestBody = "{ \"code\": \"QWERTY\" }";

		// Send POST request and get the response
		Response response = given().accept(ContentType.JSON).header("dt-auth-login-key", dtauthloginkey)
				.header("dt-auth-login-key-2", dtauthloginkey2).contentType(ContentType.JSON).body(requestBody).when()
				.post("/api/sessions/verify-2fa").then().statusCode(200).extract().response();

		sessionId = response.jsonPath().getString("data.sessionToken");
		System.out.println(" Sessionid  -----" + sessionId);

		Assert.assertFalse(sessionId.isEmpty(), "sessionId is empty or null");
	
	}
	
	@Test(priority = 2)
	public void getuserId() {
		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", "e787c685-c805-4f35-bd30-316428dc5f32")
				.header("Authorization","Bearer "+sessionId)
				.when().get("/api/properties/{propertyId}/users")
				.then().statusCode(200).extract().response();
	userId = response.jsonPath().getString("data.activeUsers[0].userId");
	System.out.println("userId--------->"+userId);
	}
	
	@Test(priority = 3, enabled = true)
	public void createSKR() {
		LocalDate today = LocalDate.now();
		LocalTime currentTimeUTC = LocalTime.now(ZoneOffset.UTC);
		LocalTime nextHourTimeUTC = currentTimeUTC.plus(1, ChronoUnit.HOURS);
		//String startdate = today + " " + currentTimeUTC;
		String enddate = today + " " + nextHourTimeUTC;
		int leftLimit = 97; // letter 'a'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = 10;
	    Random random = new Random();
	    
	    
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSZ");

        // Get the current time in UTC
        ZonedDateTime currentTime = ZonedDateTime.now();

        // Format the current time using the defined formatter
        String startdate = currentTime.format(formatter);

        // Print the formatted current time
        System.out.println("Formatted current time: " + startdate);
    
	    
	    String generatedString = random.ints(leftLimit, rightLimit + 1)
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();

		
		String requestBody = "{\n" +
                "  \"propertyId\": \"e787c685-c805-4f35-bd30-316428dc5f32\",\n" +
                "  \"type\": \"STAFF\",\n" +
                "  \"firstName\": \"Swagger\",\n" +
                "  \"lastName\": \"Staff\",\n" +
                "  \"email\": \"string@gmail.com\",\n" +
                "  \"phone\": \"\",\n" +
                "  \"userId\": \""+userId+"\",\r\n" +
                "  \"collectionIds\": [\n" +
                "    \"5bff1c30-8cef-42a2-9e86-c74e6c459afd\"\n" +
                "  ],\n" +
                "  \"requestKeyName\": \"\",\n" +
                "  \"startDate\": \""+startdate+"\",\r\n" +
                "  \"endDate\": \"\",\n" +
                "  \"neverExpires\": true,\n" +
                "  \"phoneNumberCountryCode\": \"\"\n" +
                "}";
		
		Response response = given().accept(ContentType.JSON)
				.contentType(ContentType.JSON).header("Authorization", "Bearer " + sessionId).body(requestBody).when()
				.post("/api/keyrequest/create").then().statusCode(201).extract().response();

		SKRCreatedResponse = response.getBody().asPrettyString();
		//System.out.println("PropertiesFromCB is  -------------" + GKRCreatedResponse);
		Key_ID= response.jsonPath().getString("data.keyId");
		System.out.println("Key_ID------->"+Key_ID);

		Assert.assertFalse(SKRCreatedResponse.isEmpty(), "SKRCreatedResponse is empty or null");
		
	}

	
	@Test(priority = 4, enabled = true)
	public void getKeyId() {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("type", "STAFF")
				.pathParam("propertyId", "e787c685-c805-4f35-bd30-316428dc5f32")
				.header("Authorization", "Bearer " + sessionId).when().get("/api/keyrequest/{propertyId}/{type}/summary")
				.then().statusCode(200).extract().response();

		
		Key_ID = response.jsonPath().getString("data.keys[0].id");
		System.out.println("Key_ID is  -------------" + Key_ID);

		Assert.assertFalse(Key_ID.isEmpty(), "Key_ID is empty or null");
		
	}
	
	@Test(priority = 5, enabled = true)
	public void getKeyDetails() {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("keyId", Key_ID)
				.header("dt-property-id", "e787c685-c805-4f35-bd30-316428dc5f32")
				.header("Authorization", "Bearer " + sessionId).when().get("/api/keyrequest/STAFF/{keyId}/detail")
				.then().statusCode(200).extract().response();

		keyDetails = response.getBody().asPrettyString();

		code = response.jsonPath().getString("data.code");
		System.out.println("code is  -------------" + code);

		Assert.assertFalse(keyDetails.isEmpty(), "keyDetails is empty or null");
		
	}
	
	@Test(priority = 6,enabled=true)
	public void keyRegenerate() throws InterruptedException {
	


		Response response = given().accept(ContentType.JSON)
				.pathParam("keyId", Key_ID)
				.header("dt-property-id", "e787c685-c805-4f35-bd30-316428dc5f32")
				.header("Authorization", "Bearer " + sessionId).when().put("/api/keyrequest/{keyId}/update")
				.then().statusCode(200).extract().response();

		code_regenerate = response.jsonPath().getString("data");
		System.out.println("code_regenerate---->"+code_regenerate);
		
	}

	@Test(priority = 7,enabled=true)
	public void getCodeDeliveryStatus() throws InterruptedException {
	//3min delay	
	Thread.sleep(180000);
	Thread.sleep(180000);

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("keyId", Key_ID)
				.header("dt-property-id", "e787c685-c805-4f35-bd30-316428dc5f32")
				.header("Authorization", "Bearer " + sessionId).when().get("/api/keyrequest/{keyId}/device-detail")
				.then().statusCode(200).extract().response();

		codedelievered = response.jsonPath().getString("data[0].isDelivered");

		Assert.assertEquals(codedelievered, "true");
		
	}

	@Test(priority =8,enabled=true)
	public void smartthingsverification() {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer 7216f5e4-1204-43ba-8688-ca3aff6524ae")
				.header("Cache-Control", "no-cache").when()
				.get("https://api.smartthings.com/v1/devices/057e2be1-96e1-417a-9ac1-2891c9262877/components/main/capabilities/lockCodes/status")
				.then().statusCode(200).extract().response();

		smartthings_code1 = "code_" + code_regenerate;
		System.out.println(smartthings_code1);
		smartthings_code2 = response.jsonPath().getString("lockCodes.value");
		Assert.assertTrue(smartthings_code2.contains(smartthings_code1));
		
	}
	
}