package tests.edge;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.sessionId;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class DeleteLockCodesFromSmarthings {

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
	public void getDeviceId() throws InterruptedException {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", "e787c685-c805-4f35-bd30-316428dc5f32")
				.header("Authorization", "Bearer " + sessionId).when().get("/api/devices/property/{propertyId}").then()
				.statusCode(200).extract().response();

		deviceId = response.jsonPath().getString("data[1].id");
		System.out.println(" deviceId  -----" + deviceId);

	}

	@Test(priority = 3, enabled = true)
	public void smartthingsverification() throws Throwable {
		Thread.sleep(200);
		Response response = RestAssured.given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer 21c0977b-0667-4ebc-ae6f-f04435c384e1")
				.header("Cache-Control", "no-cache").pathParam("deviceId", deviceId).when()
				.get("https://api.smartthings.com/v1/devices/{deviceId}/components/main/capabilities/lockCodes/status")
				.then().statusCode(200).extract().response();

		String DeviceCodes = response.jsonPath().getString("lockCodes.value");
		System.out.println("DeviceCodes----->" + DeviceCodes);

		JSONObject jsonObject = new JSONObject(DeviceCodes);
		StringBuilder indices = new StringBuilder();

		for (String key : jsonObject.keySet()) {
			if (key.matches("\\d+")) {
				indices.append(key).append(", ");
			}
		}

		if (indices.length() > 0) {
			indices.delete(indices.length() - 2, indices.length()); // Remove the trailing comma and space
		}
		String indexValues = indices.toString();

		System.out.println("Index Positions: " + indexValues);

		String[] indexArray = indexValues.split(", ");

		for (String index : indexArray) {
			// System.out.println("Current index: " + index);

			int Actualindex = Integer.parseInt(index);

			if (Actualindex != 249 && Actualindex != 251) {

				System.out.println("Current index: " + Actualindex);
				Thread.sleep(7000);
				String requestBody = "[\n" + "    {\n" + "        \"component\": \"main\",\n"
						+ "        \"capability\": \"lockCodes\",\n" + "        \"command\": \"deleteCode\",\n"
						+ "        \"arguments\": [" + Actualindex + "]\n" + "    }\n" + "]";

				Response response2 = RestAssured.given().accept(ContentType.JSON).contentType(ContentType.JSON)
						.header("Authorization", "Bearer 21c0977b-0667-4ebc-ae6f-f04435c384e1")
						.header("Cache-Control", "no-cache").pathParam("deviceId", deviceId).body(requestBody).when()
						.post("https://api.smartthings.com/v1/devices/{deviceId}/commands").then().statusCode(200)
						.extract().response();

				String DeletedResponse = response2.getBody().asPrettyString();
				System.out.println("DeletedResponse-----> " + DeletedResponse);
			}
		}

	}
}
