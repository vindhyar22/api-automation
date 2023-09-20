package deviceThreadApi;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.sessionId;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Devices {

	String dtauthloginkey;
	String dtauthloginkey2;
	String GKRCreatedResponse;
	String keyDetails;
//Email Sent code needs to be passed
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
	String protocolId = "2b868443-e0b8-45d6-9262-5ebdff0d8a39";

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
	public void getAllDevicesFromZone() {

		// Send POST request and get the response
		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON).pathParam("zoneId", zoneId)
				.headers("Authorization", "Bearer " + sessionId).header("dt-property-id", propertyId).when()
				.get("/api/devices/zone/{zoneId}").then().statusCode(200).extract().response();

		getAllDevicesFromZone = response.getBody().asPrettyString();
		System.out.println("getAllDevicesFromZone---->" + getAllDevicesFromZone);

		Assert.assertFalse(getAllDevicesFromZone.isEmpty(), "getAllDevicesFromZone is empty or null");
//working
	}

	@Test(priority = 2, enabled = false)
	public void getAllDevicesforproperty() {

		// Send POST request and get the response
		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", propertyId).headers("Authorization", "Bearer " + sessionId).when()
				.get("/api/devices/property/{propertyId}").then().statusCode(200).extract().response();

		getAllDevicesforpropertyResponse = response.getBody().asPrettyString();

		System.out.println(" getAllDevicesforpropertyResponse  -----" + getAllDevicesforpropertyResponse);

		Assert.assertFalse(getAllDevicesforpropertyResponse.isEmpty(),
				"getAllDevicesforpropertyResponse is empty or null");

		// working

	}

	@Test(priority = 2, enabled = false)
	public void getAllDeviceshubsandzonesforproperty() throws Throwable {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", propertyId).header("Authorization", "Bearer " + sessionId).when()
				.get("/api/devices/property/{propertyId}/withHubsAndZones").then().statusCode(200).extract().response();

		getAllDeviceshubsandzonesforproperty = response.getBody().asPrettyString();

		System.out.println("getAllDeviceshubsandzonesforproperty is  -------------" + userDetails);

		Assert.assertFalse(getAllDeviceshubsandzonesforproperty.isEmpty(),
				"getAllDeviceshubsandzonesforproperty is empty or null");
		// working

	}

	@Test(priority = 2, enabled = false)
	public void RenameDevice() {

		String requestBody = "{\"name\":\"" + deviceName + "\"}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("deviceId", deviceId).header("Authorization", "Bearer " + sessionId)
				.header("dt-property-id", propertyId).body(requestBody).when().put("/api/devices/{deviceId}").then()
				.statusCode(200).extract().response();

		String RenameDevice = response.getBody().asPrettyString();
		System.out.println("RenameDevice----->" + RenameDevice);
//working
	}

	@Test(priority = 2, enabled = false)
	public void getissuecountsforproperty() {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", propertyId).header("Authorization", "Bearer " + sessionId).when()
				.get("/api/devices/property/{propertyId}/issue/count").then().statusCode(200).extract().response();

		String getissuecountsforproperty = response.getBody().asPrettyString();
		System.out.println("getissuecountsforproperty----->" + getissuecountsforproperty);

		// working

	}

	@Test(priority = 2, enabled = false)
	public void getissueforproperty() {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", propertyId).header("Authorization", "Bearer " + sessionId).when()
				.get("/api/devices/property/{propertyId}/issues").then().statusCode(200).extract().response();

		String getissueforproperty = response.getBody().asPrettyString();
		System.out.println("getissueforproperty----->" + getissueforproperty);

		// working

	}

	@Test(priority = 2, enabled = false)
	public void getDeviceBatteryStatus() {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("deviceId", deviceId).header("Authorization", "Bearer " + sessionId)
				.header("dt-property-id", propertyId).when().get("/api/devices/{deviceId}/battery-status").then()
				.statusCode(200).extract().response();

		String getDeviceBatteryStatus = response.getBody().asPrettyString();
		System.out.println("getDeviceBatteryStatus----->" + getDeviceBatteryStatus);

		// working

	}

	@Test(priority = 2, enabled = false)
	public void DeleteDevicesforzone() {

		String requestBody = "{\"deviceIds\": [\"" + deviceId + "\"], \"propertyId\": \"" + propertyId + "\"}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).body(requestBody).when().delete("/api/devices").then()
				.statusCode(200).extract().response();

		String DeleteDevicesforzone = response.getBody().asPrettyString();
		System.out.println("DeleteDevicesforzone----->" + DeleteDevicesforzone);

		// working

	}

	@Test(priority = 2, enabled = false)
	public void Deviceaction() {

		String requestBody = "{\"deviceId\": \"" + deviceId + "\", \"action\": \"\"}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).header("dt-property-id", propertyId).body(requestBody)
				.when().post("/api/devices/action").then().statusCode(200).extract().response();

		String Deviceactionresponse = response.getBody().asPrettyString();
		System.out.println("DeleteDevicesforzone----->" + Deviceactionresponse);

		// working

	}

	@Test(priority = 2, enabled = false)
	public void getdeviceactivity() {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("deviceId", deviceId).header("Authorization", "Bearer " + sessionId)
				.header("dt-property-id", propertyId).when().get("/api/devices/{deviceId}/activity").then()
				.statusCode(200).extract().response();

		String deviceactivityresponse = response.getBody().asPrettyString();
		System.out.println("deviceactivityresponse----->" + deviceactivityresponse);

		// working

	}

	@Test(priority = 2, enabled = false)
	public void getLockStatusfromSmartThings() {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("deviceId", deviceId).header("Authorization", "Bearer " + sessionId)
				.header("dt-property-id", propertyId).when().get("/api/devices/{deviceId}/lock-status").then()
				.statusCode(200).extract().response();

		String getLockStatusfromSmartThingsresponse = response.getBody().asPrettyString();
		System.out.println("getLockStatusfromSmartThingsresponse----->" + getLockStatusfromSmartThingsresponse);

		// working

	}

	@Test(priority = 2, enabled = false)
	public void Refreshlockstatus() {

		String requestBody = "{\"deviceId\": \"" + deviceId + "\"}";
		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).header("dt-property-id", propertyId).body(requestBody)
				.when().post("/api/devices/refresh-lock").then().statusCode(200).extract().response();

		String Refreshlockstatusresponse = response.getBody().asPrettyString();
		System.out.println("Refreshlockstatusresponse----->" + Refreshlockstatusresponse);

		// working

	}

	@Test(priority = 2, enabled = true)
	public void addDevicesToZone() {

		String requestBody = "{\n" + "  \"zoneId\": \"d3aeca39-6493-44cf-bab3-e465c66a1a34\",\n" + "  \"devices\": [\n"
				+ "    {\n" + "      \"deviceId\": \"1f14d0f6-229c-487e-9be7-d92cdd9fb28a\",\n"
				+ "      \"name\": \"base-lock\",\n" + "      \"label\": \"Kwikset Door Lock\",\n"
				+ "      \"manufacturerName\": \"SmartThingsCommunity\",\n" + "      \"location\": {\n"
				+ "        \"id\": \"12361ea1-892f-4dd2-b483-38e51895f1c4\",\n" + "        \"name\": \"S7\"\n"
				+ "      },\n" + "      \"room\": {\n" + "        \"id\": \"6bee6f84-51c5-408a-9110-874452bbebce\",\n"
				+ "        \"name\": \"Living room\"\n" + "      },\n" + "      \"hubDetails\": {\n"
				+ "        \"id\": \"661aafb7-d391-448f-9e57-c145180522a1\",\n" + "        \"name\": \"M3-H1-11\",\n"
				+ "        \"label\": \"SmartThings v3 Hub\",\n" + "        \"manufacturerName\": \"SmartThings\",\n"
				+ "        \"status\": \"OFFLINE\"\n" + "      },\n" + "      \"deviceNetworkType\": \"ZIGBEE\",\n"
				+ "      \"deviceTypeName\": \"ZIGBEE LOCK\",\n" + "      \"isLock\": true,\n"
				+ "      \"isCompatible\": true,\n" + "      \"deviceState\": \"OFFLINE\",\n"
				+ "      \"status\": \"unknown\",\n" + "      \"batteryStatus\": \"50%\",\n"
				+ "      \"lastUpdatedTime\": \"2023-09-19T01:15:54.119Z\",\n"
				+ "      \"account\": \"st-s7@dthreaddev.com\"\n" + "    }\n" + "  ],\n" + "  \"propertyId\": \""
				+ propertyId + "\",\n" + "  \"protocolId\": \"" + protocolId + "\"\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).body(requestBody).when().post("/api/devices").then()
				.statusCode(200).extract().response();

		String addDevicesToZone = response.getBody().asPrettyString();
		System.out.println("addDevicesToZone----->" + addDevicesToZone);

	}

	@Test(priority = 2, enabled = false)
	public void moveDeviceToAnotherZone() {

		// String zone2 = "2fb64727-d559-4c9f-a4f8-0e216a341071";

		String requestBody = "{\n" + "  \"moveToZoneId\": \"" + zoneId + "\",\n" + "  \"devices\": [\n" + "    {\n"
				+ "      \"name\": \"M3-L2-24\",\n" + "      \"data\": {\n" + "        \"properties\": {\n"
				+ "          \"brand\": \"SmartThingsCommunity\",\n"
				+ "          \"deviceManufacturerCode\": \"SmartThingsCommunity\"\n" + "        },\n"
				+ "        \"protocol\": {\n" + "          \"type\": \"cloud\",\n"
				+ "          \"vendor\": \"SmartThingsCommunity\",\n" + "          \"networkType\": \"ZWAVE\",\n"
				+ "          \"location\": {\n" + "            \"id\": \"12361ea1-892f-4dd2-b483-38e51895f1c4\",\n"
				+ "            \"name\": \"S7\"\n" + "          },\n" + "          \"room\": {\n"
				+ "            \"name\": \"Living room\"\n" + "          },\n" + "          \"deviceTypeId\": \"\",\n"
				+ "          \"deviceTypeName\": \"ZWAVE LOCK\",\n"
				+ "          \"account\": \"st-s7@dthreaddev.com\"\n" + "        },\n"
				+ "        \"hubId\": \"661aafb7-d391-448f-9e57-c145180522a1\",\n"
				+ "        \"smartThingsName\": \"Lock\",\n" + "        \"state\": \"ONLINE\",\n"
				+ "        \"status\": \"unlocked\",\n" + "        \"batteryPercentage\": \"62%\",\n"
				+ "        \"lastUpdatedTime\": \"2023-09-19T04:47:00Z\"\n" + "      },\n"
				+ "      \"networkId\": 842,\n" + "      \"deviceTypeId\": 22,\n" + "      \"blocked\": false,\n"
				+ "      \"id\": \"a044e317-4b0c-424d-9897-4e4fd475774f\",\n" + "      \"hubDetails\": {\n"
				+ "        \"id\": \"661aafb7-d391-448f-9e57-c145180522a1\",\n" + "        \"name\": \"M3-H1-11\",\n"
				+ "        \"label\": \"SmartThings v3 Hub\",\n" + "        \"manufacturerName\": \"SmartThings\",\n"
				+ "        \"status\": \"ONLINE\"\n" + "      },\n" + "      \"zone\": {\n"
				+ "        \"name\": \"Parent zone 2\",\n"
				+ "        \"id\": \"c8159633-63ed-4cf0-b9ee-344acd740466\"\n" + "      }\n" + "    }\n" + "  ]\n"
				+ "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).header("dt-property-id", propertyId).body(requestBody)
				.when().put("/api/devices").then().statusCode(200).extract().response();

		String moveDeviceToAnotherZoneResponse = response.getBody().asPrettyString();
		System.out.println("moveDeviceToAnotherZoneResponse----->" + moveDeviceToAnotherZoneResponse);

	}

	@Test(priority = 2, enabled = false)
	public void getIssueForZone() {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", propertyId).pathParam("zoneId", zone2)
				.header("Authorization", "Bearer " + sessionId).when()
				.get("/api/devices/{propertyId}/{zoneId}/issue/count").then().statusCode(200).extract().response();

		String getIssueForZone = response.getBody().asPrettyString();
		System.out.println("getIssueForZone----->" + getIssueForZone);

		// working

	}

}