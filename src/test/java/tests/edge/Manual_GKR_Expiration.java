package tests.edge;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.sessionId;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Manual_GKR_Expiration {

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

	@Test(priority = 2, enabled = true, invocationCount = 4)
	public void ExpireGKR() {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", "e787c685-c805-4f35-bd30-316428dc5f32").pathParam("type", "GUEST")
				.header("Authorization", "Bearer " + sessionId).when()
				.get("/api/keyrequest/{propertyId}/{type}/summary").then().statusCode(200).extract().response();

		keyDetails = response.getBody().asPrettyString();
		Assert.assertFalse(keyDetails.isEmpty(), "keyDetails is empty or null");

		List<String> Key_IDs = response.jsonPath().getList("data.keys.id");
		for (int i = 0; i < Key_IDs.size(); i++) {

			Key_ID = response.jsonPath().getString("data.keys[" + i + "].id");
			System.out.println(" Key_ID  -----" + Key_ID);
			System.out.println(" count  -----" + i);

			Response response2 = given().accept(ContentType.JSON).contentType(ContentType.JSON)
					.pathParam("keyId", Key_ID).header("dt-property-id", "e787c685-c805-4f35-bd30-316428dc5f32")
					.header("Authorization", "Bearer " + sessionId).when().put("/api/keyrequest/{keyId}/expire").then()
					.statusCode(200).extract().response();

			String ExpirationResponse = response2.getBody().asPrettyString();
			System.out.println("ExpirationResponse is  -------------" + ExpirationResponse);

		}

	}

}