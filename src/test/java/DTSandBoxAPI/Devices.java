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

public class Devices {

	@Test(priority = 0, enabled = false)
	public void Devices_GET_retrieveListOfDevicesInAProperty() throws InterruptedException, Throwable, IOException {

		RestAssured.baseURI = "https://api-sandbox.devicethread.com";

		dataReader data = new dataReader();
		JSONObject testdata = data.readData();
		String sessionid2 = (String) testdata.get("sessionid2");
		String token = "Bearer " + sessionid2;
		System.out.println("token" + token);

		System.out.println("Endpoint-------->" + ConfigData.Devices_GET_retrieveListOfDevicesInAProperty());

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", token).when().get(ConfigData.Devices_GET_retrieveListOfDevicesInAProperty())
				.then().statusCode(200).log().all().extract().response();

		String responsebody = response.getBody().asPrettyString();

		System.out.println(" responsebody  -----" + responsebody);
	}

	@Test(priority = 0, enabled = true)
	public void Devices_GET_retrieveTheDetailsofDeviceInAProperty()
			throws InterruptedException, Throwable, IOException {

		RestAssured.baseURI = "https://api-sandbox.devicethread.com";

		dataReader data = new dataReader();
		JSONObject testdata = data.readData();
		String sessionid2 = (String) testdata.get("sessionid2");
		String deviceId = "e6201c73-d178-495c-8ffa-c4cd5e55dd18";

		String Endpoint = ConfigData.Devices_GET_retrieveTheDetailsofDeviceInAProperty();

		System.out.println("Endpoint--------->" + Endpoint);

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("deviceId", deviceId).header("Authorization", "Bearer " + sessionid2).when().get(Endpoint)
				.then().statusCode(200).log().all().extract().response();
		String responsebody = response.getBody().asPrettyString();

		System.out.println(" responsebody  -----" + responsebody);
	}

}
