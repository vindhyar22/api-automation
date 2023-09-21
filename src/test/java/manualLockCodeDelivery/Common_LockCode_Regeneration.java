package manualLockCodeDelivery;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.sessionId;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Common_LockCode_Regeneration {

	String dtauthloginkey;
	String dtauthloginkey2;
	String GKRCreatedResponse;
	String keyDetails;
	String code;
	String codedelievered;
	String smartthings_code1;
	String smartthings_code2;
	String Key_ID;
	String CLCCreatedResponse;
	String masterCodeId;
	String NoOfDevices;
	String ZoneName;
	String AllZones;
	String ZoneId;
	String ZoneType;
	String deviceCount;
	String deviceId;
	String deliveredDevices;
	String codeRegenerated;

	@BeforeClass
	public void setUp() {
		// Base URI
		RestAssured.baseURI = "https://api-qa.dthreaddev.com";
	}

	@Test(priority = 0)
	public void testAuth() {
		String requestBody = "{ \"email\": \"ios28@yopmail.com\", \"password\": \"Pass@12345\" }";

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
	public void Generate_Common_Lock_codes() throws Throwable {

		Thread.sleep(84000);
		System.out.println("waited for 84 sec for generate clc");

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", "e787c685-c805-4f35-bd30-316428dc5f32")
				.header("Authorization", "Bearer " + sessionId).when().get("/api/zones/{propertyId}").then()
				.statusCode(200).log().ifValidationFails().extract().response();

		// Get the list of Zone IDs from the response
		List<String> ZoneIds = response.jsonPath().getList("data.id");

		// Create a list to store common area zone IDs
		List<String> commonAreaZoneIds = new ArrayList<>();

		for (int i = 0; i < ZoneIds.size(); i++) {
			String zoneType = response.jsonPath().getString("data[" + i + "].ZoneType.name");
			int deviceCount = response.jsonPath().getInt("data[" + i + "].devices");

			if (deviceCount != 0 && "Common area".equals(zoneType)) {
				String zoneId = response.jsonPath().getString("data[" + i + "].id");
				commonAreaZoneIds.add(zoneId);
			}
		}

		System.out.println("commonAreaZoneIds---------->" + commonAreaZoneIds);
		String zonesListString = "\"" + String.join("\", \"", commonAreaZoneIds) + "\"";
		System.out.println("zonesListString----->" + zonesListString);

		// Request Body
		String requestBody = "{\n" + "  \"propertyId\": \"e787c685-c805-4f35-bd30-316428dc5f32\",\n"
				+ "  \"type\": \"COMMON_LOCK_CODE\",\n" + "  \"collectionId\": \"\",\n" + "  \"name\": \"\",\n"
				+ "  \"zonesList\": [" + zonesListString + "]\n" + "}";

		// Send POST request and get the response
		Response response2 = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).body(requestBody).when().post("/api/mastercode").then()
				.statusCode(201).log().ifValidationFails().extract().response();

		CLCCreatedResponse = response2.getBody().asPrettyString();

		code = response2.jsonPath().getString("data.code");
		masterCodeId = response2.jsonPath().getString("data.id");

		System.out.println("code-------->" + code);
		System.out.println("masterCodeId-------->" + masterCodeId);

		Assert.assertFalse(CLCCreatedResponse.isEmpty(), "CLCCreatedResponse is empty or null");

	}

	@Test(priority = 3, enabled = true)
	public void RegenerateClc() throws Throwable {
		Thread.sleep(7000);
		System.out.println("waited for 7 sec for generate clc");

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

		codeRegenerated = response2.jsonPath().getString("data.code");
		System.out.println("codeRegenerated  -----" + codeRegenerated);

	}

	@Test(priority = 4, enabled = true)
	public void CLCDetails() throws InterruptedException {
		Thread.sleep(180000);
		Thread.sleep(180000);

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.queryParam("type", "COMMON_LOCK_CODE").pathParam("propertyId", "e787c685-c805-4f35-bd30-316428dc5f32")
				.header("Authorization", "Bearer " + sessionId).when().get("/api/mastercode/{propertyId}").then()
				.statusCode(200).log().ifValidationFails().extract().response();

		List<String> MastercodeIds = response.jsonPath().getList("data.masterCodesList.id");
		for (int i = 0; i < MastercodeIds.size(); i++) {
			String masterCodeId2 = response.jsonPath().getString("data.masterCodesList[" + i + "].id");

			if (masterCodeId.equals(masterCodeId2)) {

				NoOfDevices = response.jsonPath().getString("data.masterCodesList[" + i + "].totalDevices");
				deliveredDevices = response.jsonPath().getString("data.masterCodesList[" + i + "].successfulDevices");

				System.out.println(" NoOfDevices  -----" + NoOfDevices);
				System.out.println(" deliveredDevices  -----" + deliveredDevices);
			}
		}
	}

	@Test(priority = 5, enabled = true)
	public void getCodeDeliveryStatus() throws Throwable {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("masterCodeId", masterCodeId)
				.header("dt-property-id", "e787c685-c805-4f35-bd30-316428dc5f32")
				.header("Authorization", "Bearer " + sessionId).when()
				.get("/api/mastercode/{masterCodeId}/device-details").then().statusCode(200).log().ifValidationFails()
				.extract().response();

		codedelievered = response.jsonPath().getString("data[0].isDelivered");
		System.out.println("codedelievered-->" + codedelievered);
		Assert.assertEquals(codedelievered, "true");

		List<String> deviceIds = response.jsonPath().getList("data.id");
		for (int i = 0; i < deviceIds.size(); i++) {

			deviceId = response.jsonPath().getString("data[" + i + "].id");
			System.out.println(" deviceId  -----" + deviceId);

			Response response2 = RestAssured.given().accept(ContentType.JSON).contentType(ContentType.JSON)
					.header("Authorization", "Bearer 21c0977b-0667-4ebc-ae6f-f04435c384e1")
					.header("Cache-Control", "no-cache").pathParam("deviceId", deviceId).when()
					.get("https://api.smartthings.com/v1/devices/{deviceId}/components/main/capabilities/lockCodes/status")
					.then().statusCode(200).log().ifValidationFails().extract().response();

			smartthings_code1 = "code_" + codeRegenerated;
			System.out.println(smartthings_code1);
			smartthings_code2 = response2.jsonPath().getString("lockCodes.value");
			Assert.assertTrue(smartthings_code2.contains(smartthings_code1));

		}
	}

	@Test(priority = 6, enabled = true)
	public void DeleteMastercode() throws Throwable {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", "e787c685-c805-4f35-bd30-316428dc5f32").queryParam("type", "COMMON_LOCK_CODE")
				.header("Authorization", "Bearer " + sessionId).when().get("/api/mastercode/{propertyId}").then()
				.statusCode(200).log().ifValidationFails().extract().response();

		List<String> MastercodeIds = response.jsonPath().getList("data.masterCodesList.id");
		for (int i = 0; i < MastercodeIds.size(); i++) {
			String masterCodeId2 = response.jsonPath().getString("data.masterCodesList[" + i + "].id");

			if (masterCodeId.equals(masterCodeId2)) {

				masterCodeId = response.jsonPath().getString("data.masterCodesList[0].id");
				System.out.println("masterCodeId------>" + masterCodeId);

				String requestBody = "{ \"masterCodeId\": \"" + masterCodeId + "\" }";

				Response response2 = given().accept(ContentType.JSON).contentType(ContentType.JSON)
						.header("Authorization", "Bearer " + sessionId)
						.header("dt-property-id", "e787c685-c805-4f35-bd30-316428dc5f32").body(requestBody).when()
						.delete("/api/mastercode").then().statusCode(200).log().ifValidationFails().extract()
						.response();

				String DeleteKeysResponse = response2.getBody().asPrettyString();

				System.out.println("DeleteKeysResponse----->" + DeleteKeysResponse);

			}
		}

	}
}
