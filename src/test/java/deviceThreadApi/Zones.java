package deviceThreadApi;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.sessionId;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Zones {

	String dtauthloginkey;
	String dtauthloginkey2;
	String GKRCreatedResponse;
	String keyDetails;
//Email Sent code needs to be passed here 
	String code = "1234";

	String codedelievered;
	String smartthings_code1;
	String smartthings_code2;
	String Key_ID;
	String refreshToken;
	String organizationId;
	String roleId;
	String EmailId;
	String registerUserResponse;
	String userDetails;
	String email2 = "ios28@yopmail.com";
	String newPassword = "Pass@12345";
	String firstName = "Amer";
	String lastName = "Khan";
	String phoneNumber = "7019156513";
	String imageURL;
	String zoneId = "d3aeca39-6493-44cf-bab3-e465c66a1a34";
	String propertyId = "e787c685-c805-4f35-bd30-316428dc5f32";
	String getAllDevicesFromZone;
	String getAllDevicesforpropertyResponse;
	String getAllDeviceshubsandzonesforproperty;
	String deviceName = "M3-L1-23";
	String deviceId = "ca85a332-9c24-4e06-a2ec-7de4a97bf35d";
	String zone2 = "2fb64727-d559-4c9f-a4f8-0e216a341071";
	String getZoneInProperty;
	String getZoneCountInProperty;
	String zoneTypeId = "a8f00a92-4910-4bb3-ba77-8c6f6a37b0ef";
	String zoneName = "testerzone3";

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
		refreshToken = response.jsonPath().getString("data.refreshToken");
		System.out.println(" Sessionid  -----" + sessionId);
		// System.out.println(" Sessionid -----" + refreshToken);

		Assert.assertFalse(sessionId.isEmpty(), "sessionId is empty or null");

	}

	@Test(priority = 2, enabled = false)
	public void getZoneInProperty() {

		// Send POST request and get the response
		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", propertyId).headers("Authorization", "Bearer " + sessionId).when()
				.get("/api/zones/{propertyId}").then().statusCode(200).extract().response();

		getZoneInProperty = response.getBody().asPrettyString();
		System.out.println("getZoneInProperty---->" + getZoneInProperty);

		Assert.assertFalse(getZoneInProperty.isEmpty(), "getZoneInProperty is empty or null");
		// working
	}

	@Test(priority = 2, enabled = false)
	public void getZoneCountInProperty() {

		// Send POST request and get the response
		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", propertyId).headers("Authorization", "Bearer " + sessionId).when()
				.get("/api/zones/{propertyId}/summary").then().statusCode(200).extract().response();

		getZoneCountInProperty = response.getBody().asPrettyString();

		System.out.println(" getZoneCountInPropertyResponse  -----" + getZoneCountInProperty);

		Assert.assertFalse(getZoneCountInProperty.isEmpty(), "getZoneCountInProperty is empty or null");

		// working

	}

	@Test(priority = 2, enabled = false)
	public void getRecentZonesInProperty() throws Throwable {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", propertyId).header("Authorization", "Bearer " + sessionId).when()
				.get("/api/zones/{propertyId}/recent").then().statusCode(200).extract().response();

		String getRecentZonesInProperty = response.getBody().asPrettyString();

		System.out.println("getRecentZonesInProperty is  -------------" + getRecentZonesInProperty);

		Assert.assertFalse(getRecentZonesInProperty.isEmpty(), "getRecentZonesInProperty is empty or null");
		// working

	}

	@Test(priority = 2, enabled = false)
	public void updatezone() {

		String requestBody = "{\n" + "  \"name\": \"" + zoneName + "\",\n" + "  \"typeId\": \"" + zoneTypeId + "\"\n"
				+ "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON).pathParam("zoneId", zoneId)
				.header("Authorization", "Bearer " + sessionId).header("dt-property-id", propertyId).body(requestBody)
				.when().put("/api/zones/{zoneId}").then().statusCode(200).extract().response();

		String updatezone = response.getBody().asPrettyString();
		System.out.println("updatezone----->" + updatezone);
		// working
	}

	@Test(priority = 2, enabled = false)
	public void getChildZones() {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON).pathParam("zoneId", zone2)
				.header("Authorization", "Bearer " + sessionId).header("dt-property-id", propertyId).when()
				.get("/api/zones/{zoneId}/sub-zones").then().statusCode(200).extract().response();

		String getChildZones = response.getBody().asPrettyString();
		System.out.println("getChildZones----->" + getChildZones);

		// working

	}

	@Test(priority = 2, enabled = false)
	public void getZoneTypesForZone() {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).when().get("/api/zones/types").then().statusCode(200)
				.extract().response();

		String getZoneTypesForZone = response.getBody().asPrettyString();
		System.out.println("getZoneTypesForZone----->" + getZoneTypesForZone);

		// working

	}

	@Test(priority = 2, enabled = false)
	public void getZoneDetails() {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON).pathParam("zoneId", zoneId)
				.header("Authorization", "Bearer " + sessionId).header("dt-property-id", propertyId).when()
				.get("/api/zones/{zoneId}/details").then().statusCode(200).extract().response();

		String getZoneDetails = response.getBody().asPrettyString();
		System.out.println("getZoneDetails----->" + getZoneDetails);

		// working

	}

	@Test(priority = 2, enabled = false)
	public void deleteZone() {
		String deletezone = "a36e4cc6-c106-4d01-b555-5e91d0b0192a";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("zoneId", deletezone).header("Authorization", "Bearer " + sessionId)
				.header("dt-property-id", propertyId).when().delete("/api/zones/{zoneId}").then().statusCode(200)
				.extract().response();

		String deleteZone = response.getBody().asPrettyString();
		System.out.println("deleteZone----->" + deleteZone);

		// working

	}

	@Test(priority = 2, enabled = false)
	public void getPropertyZonesbyZoneTypes() {
		String zoneType = "Common area";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", propertyId).queryParam("zoneType", zoneType)
				.header("Authorization", "Bearer " + sessionId).when().get("/api/zones/{propertyId}/getZonesByType")
				.then().statusCode(200).extract().response();

		String getPropertyZonesbyZoneTypes = response.getBody().asPrettyString();
		System.out.println("getPropertyZonesbyZoneTypes----->" + getPropertyZonesbyZoneTypes);

		// working

	}

	@Test(priority = 2, enabled = false)
	public void moveZoneToAnotherZone() {

		String requestBody = "{\n" + "  \"sourceZoneId\": \"a36e4cc6-c106-4d01-b555-5e91d0b0192a\",\n"
				+ "  \"targetZoneId\": \"d3aeca39-6493-44cf-bab3-e465c66a1a34\"\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).header("dt-property-id", propertyId).body(requestBody)
				.when().put("/api/zones/moveZone").then().statusCode(200).extract().response();

		String moveZoneToAnotherZone = response.getBody().asPrettyString();
		System.out.println("moveZoneToAnotherZone----->" + moveZoneToAnotherZone);

		// working

	}

	@Test(priority = 2, enabled = false)
	public void addZone() {

		String requestBody = "{\n" + "  \"name\": \"" + zoneName + "\",\n" + "  \"zoneTypeId\": \"" + zoneTypeId
				+ "\",\n" + "  \"propertyId\": \"" + propertyId + "\",\n" + "  \"parentId\": \"\"\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).body(requestBody).when().post("/api/zones").then()
				.statusCode(201).extract().response();

		String addZone = response.getBody().asPrettyString();
		System.out.println("addZoneresponse----->" + addZone);

		// working

	}

	@Test(priority = 2, enabled = true)
	public void getSubZonesForZoneWithDeviceStatus() {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON).pathParam("zoneId", zoneId)
				.header("Authorization", "Bearer " + sessionId).header("dt-property-id", propertyId).when()
				.get("/api/zones/{zoneId}/sub-zones/device-status").then().statusCode(200).extract().response();

		String getSubZonesForZoneWithDeviceStatus = response.getBody().asPrettyString();
		System.out.println("getSubZonesForZoneWithDeviceStatus----->" + getSubZonesForZoneWithDeviceStatus);

		// working

	}

}