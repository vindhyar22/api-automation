package testing;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.sessionId;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Guest_master_code_Regeneration {

	String dtauthloginkey;
	String dtauthloginkey2;
	String GKRCreatedResponse;
	String keyDetails;
	String code;
	String codedelievered;
	String smartthings_code1;
	String smartthings_code2;
	String Key_ID;
	String FSCCreatedResponse;
	String masterCodeId;
	String code2;
	String firstCodeDelievered;
	String SecondCodeDelievered;
	String GMCCreatedResponse;
	String countOfDevices;
	int codeDeliveredDevices;
	String deviceId;
	String codeRegeneratedConfirmation;

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
	public void Generate_Guest_Master_codes() throws InterruptedException {

		// Request Body
		String requestBody = "{\n" + "  \"propertyId\": \"e787c685-c805-4f35-bd30-316428dc5f32\",\n"
				+ "  \"type\": \"GUEST_MASTER_CODE\",\n" + "  \"collectionId\": \"\",\n" + "  \"name\": \"\",\n"
				+ "  \"zonesList\": [\n" + "    \"\"\n" + "  ]\n" + "}";

		// Send POST request and get the response
		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).body(requestBody).when().post("/api/mastercode").then()
				.statusCode(202).extract().response();

		GMCCreatedResponse = response.getBody().asPrettyString();
		System.out.println("GMCCreatedResponse  -----" + GMCCreatedResponse);

		Assert.assertFalse(GMCCreatedResponse.isEmpty(), "GMCCreatedResponse is empty or null");

	}

	@Test(priority = 3, enabled = true)
	public void getGMCDetails() throws Throwable {

		Thread.sleep(120000);
		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", "e787c685-c805-4f35-bd30-316428dc5f32").queryParam("type", "GUEST_MASTER_CODE")
				.header("Authorization", "Bearer " + sessionId).when().get("/api/mastercode/{propertyId}").then()
				.statusCode(200).extract().response();

		masterCodeId = response.jsonPath().getString("data.id");
		System.out.println("masterCodeId  -----" + masterCodeId);

		countOfDevices = response.jsonPath().getString("totalDevices");
		// codeDeliveredDevices = response.jsonPath().getInt("successfulDevices");

	}

	@Test(priority = 4, enabled = true)
	public void RegenerateGMC() {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("masterCodeId", masterCodeId)
				.header("dt-property-id", "e787c685-c805-4f35-bd30-316428dc5f32")
				.header("Authorization", "Bearer " + sessionId).when()
				.get("/api/mastercode/{masterCodeId}/device-details").then().statusCode(200).extract().response();

		List<String> deviceIds = response.jsonPath().getList("data.id");
		for (int i = 0; i < deviceIds.size(); i++) {

			deviceId = response.jsonPath().getString("data[" + i + "].id");
			System.out.println(" deviceId  -----" + deviceId);
			code = response.jsonPath().getString("data[" + i + "].code");
			System.out.println(" code1  -----" + code);

		}
		String deviceIdsString = String.join("\",\"", deviceIds);
		String requestBody = "{\n" + "  \"collectionId\": \"\",\n" + "  \"deviceIds\": [\n" + "    \"" + deviceIdsString
				+ "\"\n" + "  ]\n" + "}";

		Response response2 = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("masterCodeId", masterCodeId)
				.header("dt-property-id", "e787c685-c805-4f35-bd30-316428dc5f32")
				.header("Authorization", "Bearer " + sessionId).body(requestBody).when()
				.put("/api/mastercode/{masterCodeId}/regenerate").then().statusCode(200).extract().response();

		codeRegeneratedConfirmation = response2.getBody().asPrettyString();
		System.out.println("codeRegeneratedConfirmation  -----" + codeRegeneratedConfirmation);

	}

	@Test(priority = 5, enabled = true)
	public void getCodeDeliveryStatus() throws Throwable {
		// 1min delay
		Thread.sleep(120000);
		Thread.sleep(120000);

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("masterCodeId", masterCodeId)
				.header("dt-property-id", "e787c685-c805-4f35-bd30-316428dc5f32")
				.header("Authorization", "Bearer " + sessionId).when()
				.get("/api/mastercode/{masterCodeId}/device-details").then().statusCode(200).extract().response();

		List<String> deviceIds = response.jsonPath().getList("data.id");
		for (int i = 0; i < deviceIds.size(); i++) {

			deviceId = response.jsonPath().getString("data[" + i + "].id");
			System.out.println(" deviceId  -----" + deviceId);
			code = response.jsonPath().getString("data[" + i + "].code");
			System.out.println(" code2  -----" + code);

			codedelievered = response.jsonPath().getString("data[" + i + "].isDelivered");
			System.out.println("codedelievered-->" + codedelievered);

			Response response2 = RestAssured.given().accept(ContentType.JSON).contentType(ContentType.JSON)
					.header("Authorization", "Bearer 21c0977b-0667-4ebc-ae6f-f04435c384e1")
					.header("Cache-Control", "no-cache").pathParam("deviceId", deviceId).when()
					.get("https://api.smartthings.com/v1/devices/{deviceId}/components/main/capabilities/lockCodes/status")
					.then().statusCode(200).extract().response();

			smartthings_code1 = "code_" + code;
			System.out.println(smartthings_code1);
			smartthings_code2 = response2.jsonPath().getString("lockCodes.value");
			Assert.assertTrue(smartthings_code2.contains(smartthings_code1));
			// Working
		}

	}

}
