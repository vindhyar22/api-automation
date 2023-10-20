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

public class Reservations {

	@Test(priority = 0, enabled = false)
	public void Reservations_post_createNewReservationInProperty() throws InterruptedException, Throwable, IOException {

		RestAssured.baseURI = "https://api-sandbox.devicethread.com";

		dataReader data = new dataReader();
		JSONObject testdata = data.readData();
		String sessionid2 = (String) testdata.get("sessionid2");
		String SandBoxpropertyId = (String) testdata.get("SandBoxpropertyId");

		String token = "Bearer " + sessionid2;
		System.out.println("token" + token);

		String sandboxCollectionId = "744b7484-96d6-4f3e-b414-ce6cae37941c";

		// Note :- Here time should not overlap with another key
		String startDate = "2023-10-21T12:00:00Z";
		String endDate = "2023-10-22T12:00:00Z";
		String reservationRefId = "2";

		System.out.println("Endpoint-------->" + ConfigData.Reservations_post_createNewReservationInProperty());

		String requestBody = "{\n" + "  \"reservationRefId\": \"" + reservationRefId + "\",\n" + "  \"propertyId\": \""
				+ SandBoxpropertyId + "\",\n" + "  \"startDate\": \"" + startDate + "\",\n" + "  \"endDate\": \""
				+ endDate + "\",\n" + "  \"status\": \"confirmed\",\n" + "  \"collections\": [\n" + "    {\n"
				+ "      \"id\": \"" + sandboxCollectionId + "\",\n" + "      \"guests\": [\n" + "        {\n"
				+ "          \"firstName\": \"riya\",\n" + "          \"lastName\": \"roy\",\n"
				+ "          \"email\": \"john.doe@aos.com\",\n" + "          \"phoneNumberCountryCode\": \"IN\",\n"
				+ "          \"phoneNumber\": \"+91-9876543210\"\n" + "        }\n" + "      ]\n" + "    }\n" + "  ],\n"
				+ "  \"guestFirstName\": \"John\",\n" + "  \"guestLastName\": \"Doe\",\n"
				+ "  \"guestEmail\": \"john.doe@aos.com\"\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", token).body(requestBody).when()
				.post(ConfigData.Reservations_post_createNewReservationInProperty()).then().statusCode(201).log().all()
				.extract().response();

		String responsebody = response.getBody().asPrettyString();

		System.out.println(" responsebody  -----" + responsebody);
	}

	@Test(priority = 0, enabled = false)
	public void Reservations_get_retrieveAReservationByReservationReferenceId()
			throws InterruptedException, Throwable, IOException {

		RestAssured.baseURI = "https://api-sandbox.devicethread.com";

		dataReader data = new dataReader();
		JSONObject testdata = data.readData();
		String sessionid2 = (String) testdata.get("sessionid2");
		String SandBoxpropertyId = (String) testdata.get("SandBoxpropertyId");
		System.out.println("SandBoxpropertyId---->" + SandBoxpropertyId);
		String reservationRefId = "2";

		String Endpoint = ConfigData.Reservations_get_retrieveAReservationByReservationReferenceId();

		System.out.println("Endpoint--------->" + Endpoint);

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("reservationRefId", reservationRefId).header("Authorization", "Bearer " + sessionid2).when()
				.get(Endpoint).then().statusCode(200).log().all().extract().response();
		String responsebody = response.getBody().asPrettyString();

		String reservationId = response.jsonPath().getString("data.id");
		System.out.println("reservationId------->" + reservationId);

		System.out.println(" responsebody  -----" + responsebody);
	}

	@Test(priority = 0, enabled = false)
	public void Reservations_put_updateAnExistingReservationInProperty()
			throws InterruptedException, Throwable, IOException {

		RestAssured.baseURI = "https://api-sandbox.devicethread.com";

		dataReader data = new dataReader();
		JSONObject testdata = data.readData();
		String sessionid2 = (String) testdata.get("sessionid2");
		String sandboxCollectionId = "744b7484-96d6-4f3e-b414-ce6cae37941c";
		String startDate = "2023-10-23T12:00:00Z";
		String endDate = "2023-10-24T12:00:00Z";

		String reservationRefId = "2";
		String requestBody = "{\n" + "  \"startDate\": \"" + startDate + "\",\n" + "  \"endDate\": \"" + endDate
				+ "\",\n" + "  \"status\": \"confirmed\",\n" + "  \"guestFirstName\": \"riya\",\n"
				+ "  \"guestLastName\": \"royupdated\",\n" + "  \"guestEmail\": \"john.doe@aos.com\",\n"
				+ "  \"collections\": [\n" + "    {\n" + "      \"id\": \"" + sandboxCollectionId + "\",\n"
				+ "      \"op\": \"ADD\",\n" + "      \"guest\": [\n" + "        {\n"
				+ "          \"firstName\": \"John\",\n" + "          \"lastName\": \"Doe\",\n"
				+ "          \"email\": \"john.doe@aos.com\",\n" + "          \"phoneNumberCountryCode\": \"IN\",\n"
				+ "          \"phoneNumber\": \"+91-9876543210\"\n" + "        }\n" + "      ]\n" + "    }\n" + "  ]\n"
				+ "}";

		String Endpoint = ConfigData.Reservations_put_updateAnExistingReservationInProperty();
		// + SandBoxpropertyId;

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("reservationRefId", reservationRefId).header("Authorization", "Bearer " + sessionid2)
				.body(requestBody).when().put(Endpoint).then().statusCode(200).log().all().extract().response();
		String responsebody = response.getBody().asPrettyString();

		System.out.println(" responsebody  -----" + responsebody);
	}

	@Test(priority = 0, enabled = true)
	public void Reservations_delete_deleteAnExistingReservationInProperty()
			throws InterruptedException, Throwable, IOException {
		RestAssured.baseURI = "https://api-sandbox.devicethread.com";

		dataReader data = new dataReader();
		JSONObject testdata = data.readData();
		String sessionid2 = (String) testdata.get("sessionid2");
		String Endpoint = ConfigData.Reservations_delete_deleteAnExistingReservationInProperty();

		String reservationRefId = "2";

		System.out.println("settingsEndpoint--->" + Endpoint);

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("reservationRefId", reservationRefId).header("Authorization", "Bearer " + sessionid2).when()
				.delete(Endpoint).then().log().all().statusCode(200).extract().response();
		String responsebody = response.getBody().asPrettyString();

		System.out.println(" responsebody  -----" + responsebody);
	}

}
