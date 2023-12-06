package Refactoring_APIs;

import static io.restassured.RestAssured.given;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.testng.retry.rConfigData;

import deviceThreadApi.Base;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class properties extends Base {

	String sessionId = "";

	@Test(priority = 0, enabled = false)
	public void Properties_POST_createproperty() throws Throwable, IOException {

		// Request Body
		String requestBody = "{\n" + "    \"name\": \"Taj hotel\",\n" + "    \"officialName\": \"Taj\",\n"
				+ "    \"sameAsOfficialName\": true,\n" + "    \"apartment\": \"nirmal\",\n"
				+ "    \"city\": \"delhi\",\n" + "    \"country\": \"india\",\n" + "    \"state\": \"up\",\n"
				+ "    \"street\": \"khandwala\",\n" + "    \"zipCode\": \"201301\"\n" + "}";

		// Send POST request and get the response
		Response response = given().accept(ContentType.JSON).header("Authorization", "Bearer " + sessionId)
				.contentType(ContentType.JSON).body(requestBody).when()
				.post(rConfigData.Properties_POST_createproperty()).then().statusCode(200).extract().response();

		String createPropertyResponse = response.getBody().asPrettyString();

		System.out.println(" createPropertyResponse  -----" + createPropertyResponse);

		Assert.assertFalse(createPropertyResponse.isEmpty(), "createPropertyResponse is empty or null");

	}

	@Test(priority = 0, enabled = false)
	public void Properties_GET_getproperty() throws Throwable {

		String propertyId = "";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", propertyId).header("Authorization", "Bearer " + sessionId).when()
				.get(rConfigData.Properties_GET_getproperty()).then().statusCode(200).extract().response();

		String getPropertyResponse = response.getBody().asPrettyString();

		System.out.println("getPropertyResponse is  -------------" + getPropertyResponse);

		Assert.assertFalse(getPropertyResponse.isEmpty(), "getPropertyResponse is empty or null");

	}

	@Test(priority = 0, enabled = false)
	public void Properties_GET_getAllProperty() throws Throwable, IOException {

		String propertyId = "";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("propertyId", propertyId).header("Authorization", "Bearer " + sessionId).when()
				.get(rConfigData.Properties_GET_getAllProperty()).then().statusCode(200).extract().response();

		String getAllPropertyResponse = response.getBody().asPrettyString();
		System.out.println("getUserResponse----->" + getAllPropertyResponse);

	}

	@Test(priority = 0, enabled = false)
	public void Properties_PATCH_updateProperty() throws Throwable, IOException {

		String userId = "";

		String requestBody = "{\n" + "    \"emailId\": \"uday29@gmail.com\",\n" + "    \"firstName\": \"uday\",\n"
				+ "    \"lastName\": \"s\",\n" + "    \"phoneNumber\": \"9987665432\",\n"
				+ "    \"password\": \"123456\",\n" + "    \"phoneNumberCountryCode\": \"IN\"\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON).pathParam("userId", userId)
				.header("Authorization", "Bearer " + sessionId).body(requestBody).when()
				.patch(rConfigData.Properties_PATCH_updateProperty()).then().statusCode(200).extract().response();

		String updatePropertyResponse = response.getBody().asPrettyString();
		System.out.println("updatePropertyResponse----->" + updatePropertyResponse);

	}

	@Test(priority = 0, enabled = false)
	public void Properties_DELETE_deleteProperty() throws Throwable, IOException {

		String userId = "";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON).pathParam("userId", userId)
				.header("Authorization", "Bearer " + sessionId).when()
				.delete(rConfigData.Properties_DELETE_deleteProperty()).then().statusCode(200).extract().response();

		String deletepropertyResponse = response.getBody().asPrettyString();
		System.out.println("deletepropertyResponse----->" + deletepropertyResponse);

	}

	@Test(priority = 0, enabled = false)
	public void Properties_GET_getUSstatelist() throws Throwable, IOException {

		String code = "X8Q80J";

		String requestBody = "{\n" + "    \"code\": \"" + code + "\"\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).body(requestBody).when()
				.post(rConfigData.Properties_GET_getUSstatelist()).then().statusCode(200).extract().response();

		String getUSstatelistResponse = response.getBody().asPrettyString();
		System.out.println("getUSstatelistResponse----->" + getUSstatelistResponse);

	}

}