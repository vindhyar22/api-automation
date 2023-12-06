package Refactoring_APIs;

import static io.restassured.RestAssured.given;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.testng.retry.rConfigData;

import deviceThreadApi.Base;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class zones extends Base {

	String sessionId = "";

	@Test(priority = 0, enabled = false)
	public void Zones_POST_AddZone() throws Throwable, IOException {

		// Request Body
		String requestBody = "{\n" + "    \"name\": \"Zone4\",\n"
				+ "    \"zoneTypeId\": \"57385997-bc75-412a-a7f7-34298c7d40ae\",\n"
				+ "    \"propertyId\": \"e4297426-023a-4064-bd4c-69172442552e\",\n"
				+ "    \"parentId\": \"442169c6-05a4-4448-8b50-cb5036c9123e\"\n" + "}";

		// Send POST request and get the response
		Response response = given().accept(ContentType.JSON).header("Authorization", "Bearer " + sessionId)
				.contentType(ContentType.JSON).body(requestBody).when().post(rConfigData.Zones_POST_AddZone()).then()
				.statusCode(200).extract().response();

		String addZoneResponse = response.getBody().asPrettyString();

		System.out.println(" addZoneResponse  -----" + addZoneResponse);

		Assert.assertFalse(addZoneResponse.isEmpty(), "addZoneResponse is empty or null");

	}

	@Test(priority = 0, enabled = false)
	public void Zones_POST_addZoneType() throws Throwable {

		// String propertyId = "";

		String requestBody = "{\n" + "    \"name\": \"Gallery\"\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).body(requestBody).when()
				.post(rConfigData.Zones_POST_addZoneType()).then().statusCode(200).extract().response();

		String addZoneTypeResponse = response.getBody().asPrettyString();

		System.out.println("addZoneTypeResponse is  -------------" + addZoneTypeResponse);

		Assert.assertFalse(addZoneTypeResponse.isEmpty(), "addZoneTypeResponse is empty or null");

	}

	@Test(priority = 0, enabled = false)
	public void Zones_PUT_moveZone() throws Throwable, IOException {

		// String propertyId = "";

		String requestBody = "{\n" + "    \"sourceZoneId\": \"442169c6-05a4-4448-8b50-cb5036c9123e\",\n"
				+ "    \"targetZoneId\": \"da9f81c9-17a5-4f6d-b7c2-df493e25e12c\"\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).body(requestBody).when()
				.put(rConfigData.Zones_PUT_moveZone()).then().statusCode(200).extract().response();

		String moveZoneResponse = response.getBody().asPrettyString();
		System.out.println("moveZoneResponse----->" + moveZoneResponse);

	}

	@Test(priority = 0, enabled = false)
	public void Zones_GET_getZoneById() throws Throwable, IOException {

		String zoneId = "";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON).pathParam("zoneId", zoneId)
				.header("Authorization", "Bearer " + sessionId).when().get(rConfigData.Zones_GET_getZoneById()).then()
				.statusCode(200).extract().response();

		String Response = response.getBody().asPrettyString();
		System.out.println("Response----->" + Response);

	}

	@Test(priority = 0, enabled = false)
	public void Zones_GET_getZoneTypes() throws Throwable, IOException {

		// String userId = "";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).when().get(rConfigData.Zones_GET_getZoneTypes()).then()
				.statusCode(200).extract().response();

		String getzonetypesResponse = response.getBody().asPrettyString();
		System.out.println("getzonetypesResponse----->" + getzonetypesResponse);

	}

	@Test(priority = 0, enabled = false)
	public void Zones_GET_getAllZones() throws Throwable, IOException {

		// String code = "X8Q80J";

		// String requestBody = "{\n" + " \"code\": \"" + code + "\"\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).when().get(rConfigData.Zones_GET_getAllZones()).then()
				.statusCode(200).extract().response();

		String getAllZonesResponse = response.getBody().asPrettyString();
		System.out.println("getAllZonesResponse----->" + getAllZonesResponse);

	}

	@Test(priority = 0, enabled = false)
	public void Zones_GET_getZoneSummary() throws Throwable, IOException {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).when().get(rConfigData.Zones_GET_getZoneSummary())
				.then().statusCode(200).extract().response();

		String getzoneSummaryResponse = response.getBody().asPrettyString();
		System.out.println("getzoneSummaryResponse----->" + getzoneSummaryResponse);

	}

	@Test(priority = 0, enabled = false)
	public void Zones_GET_getRecentZones() throws Throwable, IOException {

		String code = "X8Q80J";

		String requestBody = "{\n" + "    \"code\": \"" + code + "\"\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).body(requestBody).when()
				.get(rConfigData.Zones_GET_getRecentZones()).then().statusCode(200).extract().response();

		String getRecentZonesResponse = response.getBody().asPrettyString();
		System.out.println("getRecentZonesResponse----->" + getRecentZonesResponse);

	}

	@Test(priority = 0, enabled = false)
	public void Zones_DeleteZone() throws Throwable, IOException {

		String zoneId = "";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON).pathParam(zoneId, zoneId)
				.header("Authorization", "Bearer " + sessionId).when().delete(rConfigData.Zones_DeleteZone()).then()
				.statusCode(200).extract().response();

		String DeletezoneResponse = response.getBody().asPrettyString();
		System.out.println("DeletezoneResponse----->" + DeletezoneResponse);

	}

	@Test(priority = 0, enabled = false)
	public void Zones_PUT_putZone() throws Throwable, IOException {

		// String code = "X8Q80J";
		String zoneId = "";

		String requestBody = "{\n" + "    \"propertyId\": \"3f3ea641-d73d-459e-8d77-15641c0268b8\",\n"
				+ "    \"name\": \"Zone_1\"\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON).pathParam(zoneId, zoneId)

				.header("Authorization", "Bearer " + sessionId).body(requestBody).when()
				.put(rConfigData.Zones_PUT_putZone()).then().statusCode(200).extract().response();

		String putZoneResponse = response.getBody().asPrettyString();
		System.out.println("putZoneResponse----->" + putZoneResponse);

	}

}