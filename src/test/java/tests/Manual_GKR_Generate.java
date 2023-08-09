package tests;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import report.ExtentTestNGListener;

import static io.restassured.RestAssured.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class Manual_GKR_Generate extends ExtentTestNGListener {
	private ExtentTest test;
	
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
		LocalDate today = LocalDate.now();
		LocalTime currentTimeUTC = LocalTime.now(ZoneOffset.UTC);
		LocalTime nextHourTimeUTC = currentTimeUTC.plus(1, ChronoUnit.HOURS);
		String startdate = today + " " + currentTimeUTC;
		String enddate = today + " " + nextHourTimeUTC;
		String firstName ="TEST"+Math.round(Math.random()*1000);
		String requestBody = "{\r\n" + 
				"  \"propertyId\": \"e7d7dae7-e4e3-41e5-881b-9582dd0a00a0\",\r\n" + 
				"  \"type\": \"GUEST\",\r\n" + 
				"  \"firstName\": \""+firstName+"\",\r\n" + 
				"  \"lastName\": \"User\",\r\n" + 
				"  \"email\": \"\",\r\n" + 
				"  \"phone\": \"\",\r\n" + 
				"  \"userId\": \"\",\r\n" + 
				"  \"collectionIds\": [\r\n" + 
				"    \"57d2b110-5e17-4329-88ed-3325c95a6e5a\"\r\n" + 
				"  ],\r\n" + 
				"  \"requestKeyName\": \"\",\r\n" + 
				"  \"startDate\": \""+startdate+"\",\r\n" + 
				"  \"endDate\": \""+enddate+"\",\r\n" + 
				"  \"neverExpires\": false,\r\n" + 
				"  \"phoneNumberCountryCode\": \"\"\r\n" + 
				"}";
		

		Response response = given().accept(ContentType.JSON)

				.contentType(ContentType.JSON).header("Authorization", "Bearer " + sessionId).body(requestBody).when()
				.post("/api/keyrequest/create").then().statusCode(201).extract().response();

		GKRCreatedResponse = response.getBody().asPrettyString();
		//System.out.println("PropertiesFromCB is  -------------" + GKRCreatedResponse);
		Key_ID= response.jsonPath().getString("data.id");

		Assert.assertFalse(GKRCreatedResponse.isEmpty(), "GKRCreatedResponse is empty or null");
		

	}

	@Test(priority = 3, enabled = true)
	public void getKeyDetails() {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("keyId", Key_ID)
				.header("dt-property-id", "e7d7dae7-e4e3-41e5-881b-9582dd0a00a0")
				.header("Authorization", "Bearer " + sessionId).when().get("/api/keyrequest/GUEST/{keyId}/detail")
				.then().statusCode(200).extract().response();

		keyDetails = response.getBody().asPrettyString();

		code = response.jsonPath().getString("data.gkrCodes[0].code");
		System.out.println("keyDetails is  -------------" + code);

		Assert.assertFalse(keyDetails.isEmpty(), "keyDetails is empty or null");
		

	}

	@Test(priority = 4,enabled=false)
	public void getCodeDeliveryStatus() throws InterruptedException {
	//3min delay	
	Thread.sleep(180000);

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("keyId", Key_ID)
				.header("dt-property-id", "e7d7dae7-e4e3-41e5-881b-9582dd0a00a0")
				.header("Authorization", "Bearer " + sessionId).when().get("/api/keyrequest/{keyId}/device-detail")
				.then().statusCode(200).extract().response();

		codedelievered = response.jsonPath().getString("data[0].isDelivered");

		Assert.assertEquals(codedelievered, "true");
	

	}

	@Test(priority =5,enabled=false)
	public void smartthingsverification() {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer 1a37152a-591f-41b7-a9fa-5d8126b648e5")
				.header("Cache-Control", "no-cache").when()
				.get("https://api.smartthings.com/v1/devices/54a9b5cd-bb73-4601-a48a-884c74d66ac4/components/main/capabilities/lockCodes/status")
				.then().statusCode(200).extract().response();

		smartthings_code1 = "code_" + code;
		System.out.println(smartthings_code1);
		smartthings_code2 = response.jsonPath().getString("lockCodes.value");
		Assert.assertTrue(smartthings_code2.contains(smartthings_code1));

	}
	
	
}
