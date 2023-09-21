package deviceThreadApi;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.sessionId;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class properties {

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
	String userId = "b65f5568-806c-46d0-9515-d8e1777b74b2";

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
	public void getAllProperties() {

		// Send POST request and get the response
		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.headers("Authorization", "Bearer " + sessionId).when().get("/api/properties/").then().statusCode(200)
				.extract().response();

		String getAllProperties = response.getBody().asPrettyString();
		System.out.println("getAllProperties---->" + getAllProperties);

		Assert.assertFalse(getAllProperties.isEmpty(), "getAllProperties is empty or null");
		// working
	}

	@Test(priority = 2, enabled = false)
	public void getPropertyById() {

		// Send POST request and get the response
		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", propertyId).headers("Authorization", "Bearer " + sessionId).when()
				.get("/api/properties/{propertyId}").then().statusCode(200).extract().response();

		String getPropertyById = response.getBody().asPrettyString();

		System.out.println(" getPropertyByIdResponse  -----" + getPropertyById);

		Assert.assertFalse(getPropertyById.isEmpty(), "getPropertyById is empty or null");

		// working

	}

	@Test(priority = 2, enabled = false)
	public void getAllPropertyUsers() throws Throwable {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", propertyId).header("Authorization", "Bearer " + sessionId).when()
				.get("/api/properties/{propertyId}/users").then().statusCode(200).extract().response();

		String getAllPropertyUsers = response.getBody().asPrettyString();

		System.out.println("getAllPropertyUsers is  -------------" + getAllPropertyUsers);

		Assert.assertFalse(getAllPropertyUsers.isEmpty(), "getAllPropertyUsers is empty or null");
		// working

	}

	@Test(priority = 2, enabled = false)
	public void getPropertyType() {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).when().get("/api/properties/type").then()
				.statusCode(200).extract().response();

		String getPropertyType = response.getBody().asPrettyString();
		System.out.println("getPropertyType----->" + getPropertyType);

		// Excluded

	}

	@Test(priority = 2, enabled = false)
	public void issueCountsForAllProperties() {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).when().get("/api/properties/issues").then()
				.statusCode(200).extract().response();

		String issueCountsForAllProperties = response.getBody().asPrettyString();
		System.out.println("issueCountsForAllProperties----->" + issueCountsForAllProperties);

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
	public void deleteProperty() {
		String propertyId2 = "d5c66d47-4f07-49cd-bf0b-e99b5d6a7e76";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", propertyId2).header("Authorization", "Bearer " + sessionId).when()
				.delete("/api/properties/{propertyId}").then().statusCode(200).extract().response();

		String deleteProperty = response.getBody().asPrettyString();
		System.out.println("deleteProperty----->" + deleteProperty);

		// working

	}

	@Test(priority = 2, enabled = false)
	public void addProperty() {
		String requestBody = "{\n" + "  \"officialName\": \"testProperty2\",\n" + "  \"sameAsOfficialName\": true,\n"
				+ "  \"name\": \"testProperty\",\n" + "  \"apartment\": \"test\",\n" + "  \"street\": \"test\",\n"
				+ "  \"city\": \"test\",\n" + "  \"state\": \"test\",\n" + "  \"country\": \"IN\",\n"
				+ "  \"zipCode\": \"string\",\n" + "  \"timezone\": \"Asia/Kolkata\",\n"
				+ "  \"organizationId\": \"aa5dc61b-61ab-4c1e-ad1e-9e856a948797\"\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).body(requestBody).when().post("/api/properties/").then()
				.statusCode(201).extract().response();

		String addProperty = response.getBody().asPrettyString();
		System.out.println("addProperty----->" + addProperty);

		// working

	}

	@Test(priority = 2, enabled = false)
	public void updateProperty() {

		String requestBody = "{\n" + "  \"sourceZoneId\": \"a36e4cc6-c106-4d01-b555-5e91d0b0192a\",\n"
				+ "  \"targetZoneId\": \"d3aeca39-6493-44cf-bab3-e465c66a1a34\"\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", propertyId).header("Authorization", "Bearer " + sessionId).body(requestBody)
				.when().put("/api/properties/{propertyId}").then().statusCode(200).extract().response();

		String updateProperty = response.getBody().asPrettyString();
		System.out.println("updateProperty----->" + updateProperty);

		// progress

	}

	@Test(priority = 2, enabled = false)
	public void getLockCodeTime() {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", propertyId).header("Authorization", "Bearer " + sessionId).when()
				.get("/api/properties/{propertyId}/set-lockcode-pref-value").then().statusCode(200).extract()
				.response();

		String getLockCodeTime = response.getBody().asPrettyString();
		System.out.println("getLockCodeTimeresponse----->" + getLockCodeTime);

		// working

	}

	@Test(priority = 2, enabled = true)
	public void getUserRoleAtProperty() {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", propertyId).pathParam("userId", userId)
				.header("Authorization", "Bearer " + sessionId).when()
				.get("/api/properties/{propertyId}/userRole/{userId}").then().statusCode(200).extract().response();

		String getUserRoleAtProperty = response.getBody().asPrettyString();
		System.out.println("getUserRoleAtProperty----->" + getUserRoleAtProperty);

		// working

	}

	@Test(priority = 2, enabled = true)
	public void updateLockCodeTimeValue() {

		String requestBody = "{\n" + "  \"value\": 0\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", propertyId).header("Authorization", "Bearer " + sessionId).body(requestBody)
				.when().put("/api/properties/{propertyId}/set-lockcode-pref").then().statusCode(200).extract()
				.response();

		String updateLockCodeTimeValue = response.getBody().asPrettyString();
		System.out.println("updateLockCodeTimeValue----->" + updateLockCodeTimeValue);

		// progress

	}

	@Test(priority = 2, enabled = true)
	public void addOrganizationUserToAProperty() {

		String requestBody = "{\n" + "  \"organizationId\": \"string\",\n" + "  \"type\": \"string\"\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", propertyId).header("Authorization", "Bearer " + sessionId).body(requestBody)
				.when().post("/api/properties/{propertyId}/organization-role").then().statusCode(200).extract()
				.response();

		String addOrganizationUserToAProperty = response.getBody().asPrettyString();
		System.out.println("addOrganizationUserToAProperty----->" + addOrganizationUserToAProperty);

		// progress

	}

}