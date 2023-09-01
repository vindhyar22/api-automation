package tests;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.sessionId;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Manual_GKR_Generate {

	String dtauthloginkey;
	String dtauthloginkey2;
	String GKRCreatedResponse;
	String keyDetails;
	String code;
	String codedelievered;
	String smartthings_code1;
	String smartthings_code2;
	String Key_ID;

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

	@Test(priority = 2, enabled = true)
	public void createGKR() {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 10;
		Random random = new Random();

		// Define the custom formatter for the desired format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS+0000");

		// Get the current date and time
		LocalDateTime currentDateTime = LocalDateTime.now(ZoneOffset.UTC);

		// Calculate the end date and time as current date and time plus 1 hour
		LocalDateTime endDateTime = currentDateTime.plusDays(1);

		// Format the start and end dates using the defined formatter
		String startDate = currentDateTime.format(formatter);
		String endDate = endDateTime.format(formatter);

		String generatedString = random.ints(leftLimit, rightLimit + 1).limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

		String firstName = generatedString;
		String requestBody = "{\r\n" + "  \"propertyId\": \"e787c685-c805-4f35-bd30-316428dc5f32\",\r\n"
				+ "  \"type\": \"GUEST\",\r\n" + "  \"firstName\": \"" + firstName + "\",\r\n"
				+ "  \"lastName\": \"random user\",\r\n" + "  \"email\": \"\",\r\n" + "  \"phone\": \"\",\r\n"
				+ "  \"userId\": \"\",\r\n" + "  \"collectionIds\": [\r\n"
				+ "    \"5bff1c30-8cef-42a2-9e86-c74e6c459afd\"\r\n" + "  ],\r\n" + "  \"requestKeyName\": \"\",\r\n"
				+ "  \"startDate\": \"" + startDate + "\",\r\n" + "  \"endDate\": \"" + endDate + "\",\r\n"
				+ "  \"neverExpires\": false,\r\n" + "  \"phoneNumberCountryCode\": \"\"\r\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).body(requestBody).when().post("/api/keyrequest/create")
				.then().statusCode(201).extract().response();

		GKRCreatedResponse = response.getBody().asPrettyString();
		// System.out.println("PropertiesFromCB is -------------" + GKRCreatedResponse);
		Key_ID = response.jsonPath().getString("data.id");
		System.out.println("Key_ID---->" + Key_ID);

		Assert.assertFalse(GKRCreatedResponse.isEmpty(), "GKRCreatedResponse is empty or null");

	}

	@Test(priority = 3, enabled = true)
	public void getKeyDetails() {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON).pathParam("keyId", Key_ID)
				.header("dt-property-id", "e787c685-c805-4f35-bd30-316428dc5f32")
				.header("Authorization", "Bearer " + sessionId).when().get("/api/keyrequest/GUEST/{keyId}/detail")
				.then().statusCode(200).extract().response();

		keyDetails = response.getBody().asPrettyString();

		code = response.jsonPath().getString("data.gkrCodes[0].code");
		System.out.println("code is  -------------" + code);

		Assert.assertFalse(keyDetails.isEmpty(), "keyDetails is empty or null");

	}

	@Test(priority = 4, enabled = true)
	public void getCodeDeliveryStatus() throws InterruptedException {
		// 3min delay
		Thread.sleep(180000);

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON).pathParam("keyId", Key_ID)
				.header("dt-property-id", "e787c685-c805-4f35-bd30-316428dc5f32")
				.header("Authorization", "Bearer " + sessionId).when().get("/api/keyrequest/{keyId}/device-detail")
				.then().statusCode(200).extract().response();

		codedelievered = response.jsonPath().getString("data[0].isDelivered");

		Assert.assertEquals(codedelievered, "true");

	}

	@Test(priority = 5, enabled = true)
	public void smartthingsverification() {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer 21c0977b-0667-4ebc-ae6f-f04435c384e1")
				.header("Cache-Control", "no-cache").when()
				.get("https://api.smartthings.com/v1/devices/ca85a332-9c24-4e06-a2ec-7de4a97bf35d/components/main/capabilities/lockCodes/status")
				.then().statusCode(200).extract().response();

		smartthings_code1 = "code_" + code;
		System.out.println(smartthings_code1);
		smartthings_code2 = response.jsonPath().getString("lockCodes.value");
		Assert.assertTrue(smartthings_code2.contains(smartthings_code1));

	}

}