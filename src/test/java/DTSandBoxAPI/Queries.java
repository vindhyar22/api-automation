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

public class Queries {

	@Test(priority = 0, enabled = true)
	public void Queries_POST_queryDevicesBasedOnSetOFSearchCriteria()
			throws InterruptedException, Throwable, IOException {

		RestAssured.baseURI = "https://api-sandbox.devicethread.com";

		dataReader data = new dataReader();
		JSONObject testdata = data.readData();
		String sessionid2 = (String) testdata.get("sessionid2");
		String token = "Bearer " + sessionid2;
		System.out.println("token" + token);
		String SandBoxpropertyId = (String) testdata.get("SandBoxpropertyId");
		String sandboxCollectionId = "744b7484-96d6-4f3e-b414-ce6cae37941c";
		String reservationId = "ab9cb45f-f9a9-4669-a6de-af6c0feb35f2";

		String requestBody1 = "{\n" + "  \"propertyId\": \"" + SandBoxpropertyId + "\",\n" + "  \"collectionId\": \""
				+ sandboxCollectionId + "\",\n" + "  \"reservationId\": \"" + reservationId + "\",\n"
				+ "  \"deviceType\": \"Lock\",\n" + "  \"properties\": [\n" + "    {\n"
				+ "      \"property\": \"batteryLevel\",\n" + "      \"value\": [\n" + "        \"50\"\n" + "      ],\n"
				+ "      \"op\": \"eq\"\n" + "    }\n" + "}\n";

		String requestBody = "{\n" + "  \"propertyId\": \"785f9501-372e-49aa-b8f0-5b1ae423f563\",\n"
				+ "  \"deviceType\": \"Lock\",\n" + "  \"properties\": [\n" + "    {\n"
				+ "      \"property\": \"batteryLevel\",\n" + "      \"value\": [\"50\"],\n" + "      \"op\": \"lt\"\n"
				+ "    }\n" + "  ]\n" + "}";

		System.out.println("Endpoint-------->" + ConfigData.Queries_POST_queryDevicesBasedOnSetOFSearchCriteria());

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", token).body(requestBody).when()
				.post(ConfigData.Queries_POST_queryDevicesBasedOnSetOFSearchCriteria()).then().statusCode(200).log()
				.all().extract().response();

		String responsebody = response.getBody().asPrettyString();

		System.out.println(" responsebody  -----" + responsebody);
	}

}
