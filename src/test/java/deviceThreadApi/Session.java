package deviceThreadApi;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.sessionId;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Session {

	String dtauthloginkey;
	String dtauthloginkey2;
	String GKRCreatedResponse;
	String keyDetails;
	String code;
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
		System.out.println(" Sessionid  -----" + refreshToken);

		Assert.assertFalse(sessionId.isEmpty(), "sessionId is empty or null");

	}

	@Test(priority = 2)
	public void refreshToken() {

		// Request Body
		String requestBody = "{ \"refreshToken\": " + refreshToken + " }";

		// Send patch request and get the response
		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON).body(requestBody).when()
				.patch("/api/sessions/refreshtoken").then().statusCode(200).extract().response();

		sessionId = response.jsonPath().getString("data.sessionToken");
		refreshToken = response.jsonPath().getString("data.refreshToken");
		System.out.println(" Sessionid  -----" + sessionId);
		System.out.println(" Sessionid  -----" + refreshToken);

		Assert.assertFalse(sessionId.isEmpty(), "sessionId is empty or null");

	}

	@Test(priority = 3)
	public void verifyInvitation() {

		// Request Body
		String requestBody = "{ \"code\": \"123456\" }";

		// Send POST request and get the response
		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON).body(requestBody).when()
				.post("/api/users/verify-invitation").then().statusCode(200).extract().response();

		organizationId = response.jsonPath().getString("data.organizationId");
		roleId = response.jsonPath().getString("data.role.id");
		EmailId = response.jsonPath().getString("data.emailId");
		System.out.println(" organizationId  -----" + organizationId);
		System.out.println(" roleId  -----" + roleId);
		System.out.println(" EmailId  -----" + EmailId);

		Assert.assertFalse(organizationId.isEmpty(), "organizationId is empty or null");

	}

	@Test(priority = 4)
	public void registerUser() {

		// Request Body
		String requestBody = "{\n" + "  \"emailId\": " + EmailId + ",\n" + "  \"password\": \"UGFzc0AxMjM=\",\n"
				+ "  \"firstName\": \"Automation\",\n" + "  \"lastName\": \"user\",\n"
				+ "  \"phoneNumber\": \"+917019156513\",\n" + "  \"phoneNumberCountryCode\": \"IN\",\n"
				+ "  \"roleId\": \"34f42606-7049-4bcd-bece-ed042b5c2cd5\",\n" + "  \"organizationId\": "
				+ organizationId + ",\n" + "  \"jobTitle\": \"Tester\"\n" + "}";

		// Send POST request and get the response
		Response response = given().accept(ContentType.JSON).header("dt-auth-org-key", "123456")
				.header("dt-auth-org-key-2", EmailId).header("dt-auth-org-key-3", organizationId)
				.contentType(ContentType.JSON).body(requestBody).when().post("/api/users/").then().statusCode(200)
				.extract().response();

		registerUserResponse = response.getBody().asPrettyString();

		System.out.println(" registerUserResponse  -----" + registerUserResponse);

		Assert.assertFalse(registerUserResponse.isEmpty(), "registerUserResponse is empty or null");

	}

	@Test(priority = 4, enabled = true)
	public void getUserDetails() throws Throwable {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("userId", dtauthloginkey).header("Authorization", "Bearer " + sessionId).when()
				.get("/api/users/{userId}/details").then().statusCode(200).extract().response();

		userDetails = response.getBody().asPrettyString();

		System.out.println("userDetails is  -------------" + userDetails);

		Assert.assertFalse(userDetails.isEmpty(), "userDetails is empty or null");

	}

}