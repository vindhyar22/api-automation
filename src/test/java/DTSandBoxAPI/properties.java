package DTSandBoxAPI;

import static io.restassured.RestAssured.given;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import com.testng.retry.ConfigData;
import com.testng.retry.dataReader;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class properties {

	@Test(priority = 0, enabled = false)
	public void properties_GET_retrieveTheListOfProperties() throws InterruptedException, Throwable, IOException {

		RestAssured.baseURI = "https://api-sandbox.devicethread.com";

		dataReader data = new dataReader();
		JSONObject testdata = data.readData();
		String sessionid2 = (String) testdata.get("sessionid2");
		String token = "Bearer " + sessionid2;
		System.out.println("token" + token);

		System.out.println("Endpoint-------->" + ConfigData.properties_GET_retrieveTheListOfProperties());

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", token).when().get(ConfigData.properties_GET_retrieveTheListOfProperties())
				.then().statusCode(200).log().all().extract().response();

		String responsebody = response.getBody().asPrettyString();

		System.out.println(" responsebody  -----" + responsebody);
	}

	@Test(priority = 0, enabled = true)
	public void properties_GET_retrievePropertyByPropertyId() throws InterruptedException, Throwable, IOException {

		RestAssured.baseURI = "https://api-sandbox.devicethread.com";

		dataReader data = new dataReader();
		JSONObject testdata = data.readData();
		String sessionid2 = (String) testdata.get("sessionid2");
		String SandBoxpropertyId = (String) testdata.get("SandBoxpropertyId");
		System.out.println("SandBoxpropertyId---->" + SandBoxpropertyId);

		String Endpoint = ConfigData.properties_GET_retrievePropertyByPropertyId();
		// + SandBoxpropertyId;
		System.out.println("Endpoint--------->" + Endpoint);

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", SandBoxpropertyId).header("Authorization", "Bearer " + sessionid2).when()
				.get(Endpoint).then().statusCode(200).log().all().extract().response();
		String responsebody = response.getBody().asPrettyString();

		System.out.println(" responsebody  -----" + responsebody);
	}

	@Test(priority = 0, enabled = false)
	public void retrieveTheConfigurationSettingsOfAnOrganization() throws InterruptedException, Throwable, IOException {

		RestAssured.baseURI = "https://api-sandbox.devicethread.com";

		dataReader data = new dataReader();
		JSONObject testdata = data.readData();
		String sessionid2 = (String) testdata.get("sessionid2");
		String SandBoxpropertyId = (String) testdata.get("SandBoxpropertyId");
		System.out.println("SandBoxpropertyId---->" + SandBoxpropertyId);

		String Endpoint = ConfigData.properties_GET_retrieveTheConfigurationSettingsOfAnProperties();
		// + SandBoxpropertyId;

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", SandBoxpropertyId).header("Authorization", "Bearer " + sessionid2).when()
				.get(Endpoint).then().statusCode(200).log().all().extract().response();
		String responsebody = response.getBody().asPrettyString();

		System.out.println(" responsebody  -----" + responsebody);
	}

	@Test(priority = 0, enabled = false)
	public void updateTheConfigurationSettingsOfAnOrganization() throws InterruptedException, Throwable, IOException {
		RestAssured.baseURI = "https://api-sandbox.devicethread.com";

		dataReader data = new dataReader();
		JSONObject testdata = data.readData();
		String sessionid2 = (String) testdata.get("sessionid2");
		String SandBoxpropertyId = (String) testdata.get("SandBoxpropertyId");
		System.out.println("SandBoxpropertyId---->" + SandBoxpropertyId);
		String Endpoint = ConfigData.properties_PUT_updateTheConfigurationSettingsOfAnProperties();

		System.out.println("settingsEndpoint--->" + Endpoint);

		String requestBody = "{\"reservation\": {\"checkInCheckOutTime\": {\"checkInTime\": \"02:30\",\"checkOutTime\": \"22:30\"}},\"lock\": {\"batteryAlert\": 30,\"code\": {\"length\": 4,\"type\": \"PHONE_NUMBER\"},\"codeDelivery\": {\"setLockCodeBeforeCheckIn\": 1440,\"codeVerificationInterval\": 1440},\"lockMonitoringInterval\": 120}}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", SandBoxpropertyId).header("Authorization", "Bearer " + sessionid2)
				.body(requestBody).when().put(Endpoint).then().log().all().statusCode(200).extract().response();
		String responsebody = response.getBody().asPrettyString();

		System.out.println(" responsebody  -----" + responsebody);
	}

}
