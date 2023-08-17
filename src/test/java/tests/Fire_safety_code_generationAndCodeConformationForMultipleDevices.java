package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;

import java.util.List;

public class Fire_safety_code_generationAndCodeConformationForMultipleDevices{
	
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
	
	String deviceId;
	int DeviceCount;
	

	
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
	
	
	@Test(priority = 2,enabled=true)
	public void Generate_Fire_Safety_codes() throws InterruptedException {
		
	

		// Request Body
				String requestBody = "{\n" +
		                "  \"propertyId\": \"9bb338ed-701f-4a28-bf81-a3776bdc541c\",\n" +
		                "  \"type\": \"FIRE_SAFETY_CODE\",\n" +
		                "  \"collectionId\": \"\",\n" +
		                "  \"name\": \"\",\n" +
		                "  \"zonesList\": [\n" +
		                "    \"\"\n" +
		                "  ]\n" +
		                "}";

				
				// Send POST request and get the response
				Response response = given().accept(ContentType.JSON)
						.contentType(ContentType.JSON).header("Authorization", "Bearer " + sessionId).body(requestBody).when()
						.post("/api/mastercode").then().statusCode(201).extract().response();

		FSCCreatedResponse = response.getBody().asPrettyString();
		masterCodeId = response.jsonPath().getString("data.id");

		Assert.assertFalse(FSCCreatedResponse.isEmpty(), "FSCCreatedResponse is empty or null");
	

	}
	

	@Test(priority = 3,enabled=true)
	public void FSCDetails() throws InterruptedException {
	//1min delay	
	Thread.sleep(60000);
	

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.queryParam("type", "FIRE_SAFETY_CODE")
				.pathParam("propertyId", "9bb338ed-701f-4a28-bf81-a3776bdc541c")
				.header("Authorization", "Bearer " + sessionId).when().get("/api/mastercode/{propertyId}")
				.then().statusCode(200).extract().response();

		code = response.jsonPath().getString("data.code");
		System.out.println(" code  -----" + code);
		NoOfDevices = response.jsonPath().getString("data.totalDevices");
		System.out.println(" NoOfDevices  -----" + NoOfDevices);
		
	}
	


	@Test(priority = 4,enabled=true)
	public void getCodeDeliveryStatus() throws Throwable   {
	//1min delay	
	Thread.sleep(60000);
	

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("masterCodeId", masterCodeId)
				.header("dt-property-id", "9bb338ed-701f-4a28-bf81-a3776bdc541c")
				.header("Authorization", "Bearer " + sessionId).when().get("/api/mastercode/{masterCodeId}/device-details")
				.then().statusCode(200).extract().response();

		codedelievered = response.jsonPath().getString("data[0].isDelivered");
		System.out.println("codedelievered-->"+codedelievered);
		Assert.assertEquals(codedelievered, "true");
		
		List<String> deviceIds = response.jsonPath().getList("data.id");
		for (int i = 0; i < deviceIds.size(); i++) {
		  
			 deviceId 	=response.jsonPath().getString("data["+i+"].id");
			 System.out.println(" deviceId  -----" + deviceId);
			 

			 Response response2 = RestAssured.given()
		                .accept(ContentType.JSON)
		                .contentType(ContentType.JSON)
		                .header("Authorization", "Bearer 59af3838-4d59-4a86-9974-2c51119c86c0")
		                .header("Cache-Control", "no-cache")
		                .pathParam("deviceId", deviceId)  
		                .when()
		                .get("https://api.smartthings.com/v1/devices/{deviceId}/components/main/capabilities/lockCodes/status")
		                .then()
		                .statusCode(200)
		                .extract()
		                .response();

		        smartthings_code1 = "code_" + code;
				smartthings_code1 = "code_" + code;
				System.out.println(smartthings_code1);
				smartthings_code2 = response2.jsonPath().getString("lockCodes.value");
				Assert.assertTrue(smartthings_code2.contains(smartthings_code1));
		
		}

	}
}
	
	

	
