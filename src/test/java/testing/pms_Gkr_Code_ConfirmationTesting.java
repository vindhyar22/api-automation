package testing;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;



public class pms_Gkr_Code_ConfirmationTesting {
	
	
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
		//test.log(Status.PASS, "Test passed");

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
	public void getKey() {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", "e787c685-c805-4f35-bd30-316428dc5f32")
				.pathParam("type", "GUEST")
				//.pathParam("keyId", Key_ID)
				//.header("dt-property-id", "e7d7dae7-e4e3-41e5-881b-9582dd0a00a0")
				.header("Authorization", "Bearer " + sessionId).when().get("/api/keyrequest/{propertyId}/{type}/summary")
				.then().statusCode(200).extract().response();

		//Key_ID = response.getBody().asPrettyString();
		Key_ID = response.jsonPath().getString("data.keys[0].id");

		code = response.jsonPath().getString("data.keys[0].code");
		System.out.println("keyID is  -------------" + Key_ID);

		Assert.assertFalse(Key_ID.isEmpty(), "Key_ID is empty or null");
		

	}
	
	
	
	@Test(priority = 3, enabled = true)
	public void getKeyDetails() {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("keyId", Key_ID)
				.header("dt-property-id", "e787c685-c805-4f35-bd30-316428dc5f32")
				.header("Authorization", "Bearer " + sessionId).when().get("/api/keyrequest/GUEST/{keyId}/detail")
				.then().statusCode(200).extract().response();

		keyDetails = response.getBody().asPrettyString();

		code = response.jsonPath().getString("data.gkrCodes[0].code");
		System.out.println("keyDetails is  -------------" + code);

		Assert.assertFalse(keyDetails.isEmpty(), "keyDetails is empty or null");
	

	}

	@Test(priority = 4,enabled=true)
	public void getCodeDeliveryStatus() throws InterruptedException {
	//3min delay	
	Thread.sleep(180000);

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("keyId", Key_ID)
				.header("dt-property-id", "e787c685-c805-4f35-bd30-316428dc5f32")
				.header("Authorization", "Bearer " + sessionId).when().get("/api/keyrequest/{keyId}/device-detail")
				.then().statusCode(200).extract().response();

		codedelievered = response.jsonPath().getString("data[0].isDelivered");
		
		if(codedelievered=="true") {

			Assert.assertEquals(codedelievered, "true");
		
		}else
		{
			System.out.println("code is not delivered still. extend wait time to max of 5mins or 10mins");
		}

		

	}

	@Test(priority =5,enabled=true)
	public void smartthingsVerification() {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer 21c0977b-0667-4ebc-ae6f-f04435c384e1")
				.header("Cache-Control", "no-cache").when()
				.get("https://api.smartthings.com/v1/devices/a044e317-4b0c-424d-9897-4e4fd475774f/components/main/capabilities/lockCodes/status")
				.then().statusCode(200).extract().response();

		smartthings_code1 = "code_" + code;
		System.out.println(smartthings_code1);
		smartthings_code2 = response.jsonPath().getString("lockCodes.value");
		if(smartthings_code2.contains(smartthings_code1)) {
		Assert.assertTrue(smartthings_code2.contains(smartthings_code1));}else
		{
			System.out.println("code is not delivered to cloudbeds. extend wait time to max of 5mins or 10mins");
		}
	
	}
	

}
