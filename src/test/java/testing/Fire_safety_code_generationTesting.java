package testing;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.sessionId;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Fire_safety_code_generationTesting {

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
	String NoOfDevices;

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
	public void Generate_Fire_Safety_codes() throws InterruptedException {

		// Request Body
		String requestBody = "{\n" + "  \"propertyId\": \"e787c685-c805-4f35-bd30-316428dc5f32\",\n"
				+ "  \"type\": \"FIRE_SAFETY_CODE\",\n" + "  \"collectionId\": \"\",\n" + "  \"name\": \"\",\n"
				+ "  \"zonesList\": [\n" + "    \"\"\n" + "  ]\n" + "}";

		// Send POST request and get the response
		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).body(requestBody).when().post("/api/mastercode").then()
				.statusCode(201).extract().response();

		FSCCreatedResponse = response.getBody().asPrettyString();
		// code = response.jsonPath().getString("data.code");
		masterCodeId = response.jsonPath().getString("data.id");

		Assert.assertFalse(FSCCreatedResponse.isEmpty(), "FSCCreatedResponse is empty or null");

	}

	@Test(priority = 3, enabled = true)
	public void GMCDetails() throws InterruptedException {
		// 3min delay
		Thread.sleep(60000);
		// Thread.sleep(180000);

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.queryParam("type", "FIRE_SAFETY_CODE").pathParam("propertyId", "e787c685-c805-4f35-bd30-316428dc5f32")
				.header("Authorization", "Bearer " + sessionId).when().get("/api/mastercode/{propertyId}").then()
				.statusCode(200).extract().response();

		code = response.jsonPath().getString("data.code");
		System.out.println(" code  -----" + code);
		NoOfDevices = response.jsonPath().getString("data.totalDevices");
		System.out.println(" NoOfDevices  -----" + NoOfDevices);

	}

	@Test(priority = 4, enabled = true)
	public void getCodeDeliveryStatus() throws Throwable {
		// 3min delay
		Thread.sleep(60000);
		// Thread.sleep(180000);

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("masterCodeId", masterCodeId)
				// .pathParam("masterCodeId", "64dc3a51-f06c-46ce-acdc-80d481b4ea81")
				.header("dt-property-id", "e787c685-c805-4f35-bd30-316428dc5f32")
				.header("Authorization", "Bearer " + sessionId).when()
				.get("/api/mastercode/{masterCodeId}/device-details").then().statusCode(200).extract().response();

		codedelievered = response.jsonPath().getString("data[0].isDelivered");
		System.out.println("codedelievered-->" + codedelievered);
		Assert.assertEquals(codedelievered, "true");

	}

	@Test(priority = 5, enabled = true)
	public void smartthingsverification() throws Throwable {
		Thread.sleep(30000);

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer 7216f5e4-1204-43ba-8688-ca3aff6524ae")
				.header("Cache-Control", "no-cache").when()
				.get("https://api.smartthings.com/v1/devices/057e2be1-96e1-417a-9ac1-2891c9262877/components/main/capabilities/lockCodes/status")
				.then().statusCode(200).extract().response();
		// smartthings_code1 = "code_5014";
		smartthings_code1 = "code_" + code;
		System.out.println(smartthings_code1);
		smartthings_code2 = response.jsonPath().getString("lockCodes.value");
		Assert.assertTrue(smartthings_code2.contains(smartthings_code1));

	}

}
