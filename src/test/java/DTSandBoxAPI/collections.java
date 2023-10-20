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

public class collections {

	@Test(priority = 0, enabled = false)
	public void collections_GET_retrieveTheCollectionsInAProperty()
			throws InterruptedException, Throwable, IOException {

		RestAssured.baseURI = "https://api-sandbox.devicethread.com";

		dataReader data = new dataReader();
		JSONObject testdata = data.readData();
		String sessionid2 = (String) testdata.get("sessionid2");
		String token = "Bearer " + sessionid2;
		System.out.println("token" + token);

		System.out.println("Endpoint-------->" + ConfigData.collections_GET_retrieveTheCollectionsInAProperty());

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", token).when()
				.get(ConfigData.collections_GET_retrieveTheCollectionsInAProperty()).then().statusCode(200).log().all()
				.extract().response();

		String responsebody = response.getBody().asPrettyString();

		System.out.println(" responsebody  -----" + responsebody);
	}

	@Test(priority = 0, enabled = true)
	public void collections_post_createNewCollectionsInProperty() throws InterruptedException, Throwable, IOException {

		RestAssured.baseURI = "https://api-sandbox.devicethread.com";

		dataReader data = new dataReader();
		JSONObject testdata = data.readData();
		String sessionid2 = (String) testdata.get("sessionid2");

		String Endpoint = ConfigData.collections_post_createNewCollectionsInProperty();

		System.out.println("Endpoint--------->" + Endpoint);

		String requestBody = "{\"collectionNames\": [\"Col1l\"]}";
		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionid2).body(requestBody).when().post(Endpoint).then()
				.statusCode(201).log().all().extract().response();
		String responsebody = response.getBody().asPrettyString();

		System.out.println(" responsebody  -----" + responsebody);
	}

}
