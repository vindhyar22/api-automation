package tests;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.sessionId;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class pms_Gkr_Code_Regeneration {

	String dtauthloginkey;
	String dtauthloginkey2;
	String GKRCreatedResponse;
	String keyDetails;
	String code;
	String codedelievered;
	String smartthings_code1;
	String smartthings_code2;
	String Key_ID;
	String deviceId;
	String masterCodeId;
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
		Response response = given().accept(ContentType.JSON).header("dt-auth-login-key", dtauthloginkey)
				.header("dt-auth-login-key-2", dtauthloginkey2).contentType(ContentType.JSON).body(requestBody).when()
				.post("/api/sessions/verify-2fa").then().statusCode(200).extract().response();

		sessionId = response.jsonPath().getString("data.sessionToken");
		System.out.println(" Sessionid  -----" + sessionId);
		Assert.assertFalse(sessionId.isEmpty(), "sessionId is empty or null");
	}

	@Test(priority = 2, enabled = true)
	public void getKeyAndVerifyCodeDelivery() throws Throwable {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", "e787c685-c805-4f35-bd30-316428dc5f32").pathParam("type", "GUEST")
				.header("Authorization", "Bearer " + sessionId).when()
				.get("/api/keyrequest/{propertyId}/{type}/summary").then().statusCode(200).extract().response();

		List<String> Key_IDs = response.jsonPath().getList("data.keys.id");
		for (int i = 0; i < Key_IDs.size(); i++) {

			String isPmsActive = response.jsonPath().getString("data.keys[" + i + "].pms.type");

			if (isPmsActive.equalsIgnoreCase("CLOUDBEDS")) {
				System.out.println("PMS KeyRequest (" + i + ")");

				Key_ID = response.jsonPath().getString("data.keys[" + i + "].id");

				code = response.jsonPath().getString("data.keys[" + i + "].code");
				System.out.println("code1 is  -------------" + code);
				System.out.println("keyID is  -------------" + Key_ID);
				// System.out.println("PMS KeyRequest (" + i + ")");

				Response response2 = given().accept(ContentType.JSON).pathParam("keyId", Key_ID)
						.header("dt-property-id", "e787c685-c805-4f35-bd30-316428dc5f32")
						.header("Authorization", "Bearer " + sessionId).when().put("/api/keyrequest/{keyId}/update")
						.then().statusCode(200).extract().response();

				code_regenerate = response2.jsonPath().getString("data");
				System.out.println("code_regenerate---->" + code_regenerate);

				Thread.sleep(120000);

				// get all device details for request key of guest
				Response response3 = given().accept(ContentType.JSON).contentType(ContentType.JSON)
						.pathParam("keyId", Key_ID).header("dt-property-id", "e787c685-c805-4f35-bd30-316428dc5f32")
						.header("Authorization", "Bearer " + sessionId).when()
						.get("/api/keyrequest/{keyId}/device-detail").then().statusCode(200).extract().response();

				List<String> deviceIds = response3.jsonPath().getList("data.id");
				for (int j = 0; j < deviceIds.size(); j++) {
					deviceId = response3.jsonPath().getString("data[" + j + "].id");
					System.out.println(" deviceId  -----" + deviceId);
					codedelievered = response3.jsonPath().getString("data[" + j + "].isDelivered");

					if (codedelievered == "true") {
						Assert.assertEquals(codedelievered, "true");
						System.out.println("In device codedelievered-->" + codedelievered);

					} else {
						System.out.println("code is not delivered still. extend wait time to max of 5mins or 10mins");
					}

					Response response4 = RestAssured.given().accept(ContentType.JSON).contentType(ContentType.JSON)
							.header("Authorization", "Bearer 21c0977b-0667-4ebc-ae6f-f04435c384e1")
							.header("Cache-Control", "no-cache").pathParam("deviceId", deviceId).when()
							.get("https://api.smartthings.com/v1/devices/{deviceId}/components/main/capabilities/lockCodes/status")
							.then().statusCode(200).extract().response();

					smartthings_code1 = "code_" + code_regenerate;
					System.out.println("smartthings_code------>" + smartthings_code1);
					smartthings_code2 = response4.jsonPath().getString("lockCodes.value");
					if (codedelievered == "true") {
						Assert.assertTrue(smartthings_code2.contains(smartthings_code1));
						System.out.println("In Smarthings codedelievered-->" + codedelievered);

					} else {
						System.out.println("code is not delivered still. extend wait time to max of 5mins or 10mins");
					}

				}
			}

		}
	}

}
