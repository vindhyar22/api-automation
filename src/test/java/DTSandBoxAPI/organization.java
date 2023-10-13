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

public class organization {

	@Test(priority = 0, enabled = false)
	public void RetrieveTheListOfOrg() throws InterruptedException, Throwable, IOException {

		RestAssured.baseURI = "https://api-sandbox.devicethread.com";

		dataReader data = new dataReader();
		JSONObject testdata = data.readData();
		String sessionid2 = (String) testdata.get("sessionid2");
		String token = "Bearer " + sessionid2;
		System.out.println("token" + token);

		System.out.println("Endpoint-------->" + ConfigData.Organization_GET_retrieveTheListOfOrg());

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", token).when().get(ConfigData.Organization_GET_retrieveTheListOfOrg()).then()
				.statusCode(200).log().all().extract().response();

		String responsebody = response.getBody().asPrettyString();

		System.out.println(" responsebody  -----" + responsebody);
	}

	@Test(priority = 0, enabled = false)
	public void retrieveOrganizationById() throws InterruptedException, Throwable, IOException {

		RestAssured.baseURI = "https://api-sandbox.devicethread.com";

		dataReader data = new dataReader();
		JSONObject testdata = data.readData();
		String sessionid2 = (String) testdata.get("sessionid2");
		String orgId = (String) testdata.get("orgId");

		String Endpoint = ConfigData.Organization_GET_retrieveOrganizationById() + orgId;

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				// .pathParam("orgId", orgId)
				.header("Authorization", "Bearer " + sessionid2).when().get(Endpoint).then().statusCode(200).log().all()
				.extract().response();
		String responsebody = response.getBody().asPrettyString();

		System.out.println(" responsebody  -----" + responsebody);
	}

	@Test(priority = 0, enabled = false)
	public void retrieveTheConfigurationSettingsOfAnOrganization() throws InterruptedException, Throwable, IOException {

		RestAssured.baseURI = "https://api-sandbox.devicethread.com";

		dataReader data = new dataReader();
		JSONObject testdata = data.readData();
		String sessionid2 = (String) testdata.get("sessionid2");
		String orgId = (String) testdata.get("orgId");

		String Endpoint = ConfigData.Organization_GET_retrieveTheConfigurationSettingsOfAnOrganization() + orgId;

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON).pathParam("orgId", orgId)
				.header("Authorization", "Bearer " + sessionid2).when().get(Endpoint).then().statusCode(200).log().all()
				.extract().response();
		String responsebody = response.getBody().asPrettyString();

		System.out.println(" responsebody  -----" + responsebody);
	}

	@Test(priority = 0, enabled = true)
	public void updateTheConfigurationSettingsOfAnOrganization() throws InterruptedException, Throwable, IOException {
		RestAssured.baseURI = "https://api-sandbox.devicethread.com";

		dataReader data = new dataReader();
		JSONObject testdata = data.readData();
		String sessionid2 = (String) testdata.get("sessionid2");
		String orgId = (String) testdata.get("orgId");

		String Endpoint = ConfigData.Organization_PUT_updateTheConfigurationSettingsOfAnOrganization();

		System.out.println("settingsEndpoint--->" + Endpoint);

		// String Endpoint =
		// ConfigData.Organization_PUT_updateTheConfigurationSettingsOfAnOrganization()
		// + orgId;

		String requestBody = "{\n" + "  \"reservation\": {\n" + "    \"checkInCheckOutTime\": {\n"
				+ "      \"checkInTime\": \"02:30\",\n" + "      \"checkOutTime\": \"22:30\",\n"
				+ "      \"canOverride\": true\n" + "    }\n" + "  },\n" + "  \"lock\": {\n"
				+ "    \"batteryAlert\": {\n" + "      \"value\": 20,\n" + "      \"canOverride\": true\n" + "    },\n"
				+ "    \"code\": {\n" + "      \"length\": {\n" + "        \"value\": 6,\n"
				+ "        \"canOverride\": true\n" + "      },\n" + "      \"type\": {\n"
				+ "        \"value\": \"AUTOMATIC\",\n" + "        \"canOverride\": true\n" + "      }\n" + "    }\n"
				+ "  },\n" + "  \"codeDelivery\": {\n" + "    \"setLockCodeBeforeCheckIn\": {\n"
				+ "      \"value\": 30,\n" + "      \"canOverride\": true\n" + "    },\n"
				+ "    \"codeVerificationInterval\": {\n" + "      \"value\": 60,\n" + "      \"canOverride\": true\n"
				+ "    }\n" + "  },\n" + "  \"lockMonitoringInterval\": {\n" + "    \"value\": 15,\n"
				+ "    \"canOverride\": true\n" + "  }\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON).pathParam("orgId", orgId)
				.header("Authorization", "Bearer " + sessionid2).body(requestBody).when().put(Endpoint).then().log()
				.all().statusCode(200).extract().response();
		String responsebody = response.getBody().asPrettyString();

		System.out.println(" responsebody  -----" + responsebody);
	}

}
