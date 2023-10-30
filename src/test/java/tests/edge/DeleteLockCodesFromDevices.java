package tests.edge;

import java.io.FileReader;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class DeleteLockCodesFromDevices {

	String dtauthloginkey;
	String dtauthloginkey2;
	String GKRCreatedResponse;
	String keyDetails;
	String code;
	String codedelievered;
	String smartthings_code1;
	String smartthings_code2;
	String Key_ID;
	String deviceId;
	String StTokens;

	@Test(enabled = true)
	public void CodeDeletionAndVerification() throws Throwable {

		// Read the JSON file
		FileReader reader = new FileReader("C:\\Users\\amerk\\git\\lock-api-automation\\JsonFiles\\deviceIds.json");
		JSONObject jsonObject = new JSONObject(new JSONTokener(reader));

		// Get the array of device IDs
		JSONArray StTokensArray = jsonObject.getJSONArray("StTokens");

		// Process each device ID
		for (int i = 0; i < StTokensArray.length(); i++) {
			StTokens = StTokensArray.getString(i);
			// System.out.println("Processing StTokensArray : " + StTokens);
			String Token = "Bearer " + "" + StTokens + "";
			System.out.println("Token------------->" + Token);

			Response response3 = (Response) RestAssured.given().accept(ContentType.JSON).contentType(ContentType.JSON)
					.header("Authorization", Token).header("Cache-Control", "no-cache").when()
					.get("https://api.smartthings.com/v1/devices").then().statusCode(200).extract().response();

			List<String> deviceIds = response3.jsonPath().getList("items.findAll { it.type != 'HUB' }.deviceId");

			for (String deviceId : deviceIds) {
				System.out.println("Processing Device ID: " + deviceId);

				// Get the name and label of device
				Response response7 = RestAssured.given().accept(ContentType.JSON).contentType(ContentType.JSON)
						.header("Authorization", Token).header("Cache-Control", "no-cache")
						.pathParam("deviceId", deviceId).when().get("https://api.smartthings.com/v1/devices/{deviceId}")
						.then().statusCode(200).extract().response();

				String devicename = response7.jsonPath().getString("name");
				String deviceLabel = response7.jsonPath().getString("label");

				// Get the State of devices
				Response response6 = RestAssured.given().accept(ContentType.JSON).contentType(ContentType.JSON)
						.header("Authorization", Token).header("Cache-Control", "no-cache")
						.pathParam("deviceId", deviceId).when()
						.get("https://api.smartthings.com/devices/{deviceId}/health").then().statusCode(200).extract()
						.response();

				String DeviceStatus = response6.jsonPath().getString("state");

				if (DeviceStatus.equalsIgnoreCase("ONLINE")) {

					// Thread.sleep(2000);
					Response response = RestAssured.given().accept(ContentType.JSON).contentType(ContentType.JSON)
							.header("Authorization", Token).header("Cache-Control", "no-cache")
							.pathParam("deviceId", deviceId).when()
							.get("https://api.smartthings.com/v1/devices/{deviceId}/components/main/capabilities/lockCodes/status")
							.then().statusCode(200).extract().response();

					String DeviceCodes = response.jsonPath().getString("lockCodes.value");
					System.out.println("DeviceCodes----->" + DeviceCodes);

					JSONObject jsonObject2 = new JSONObject(DeviceCodes);
					StringBuilder indices = new StringBuilder();

					for (String key : jsonObject2.keySet()) {
						if (key.matches("\\d+")) {
							indices.append(key).append(", ");
						}
					}

					if (indices.length() > 0) {
						indices.delete(indices.length() - 2, indices.length()); // Remove the trailing comma and space
					}
					String indexValues = indices.toString();

					System.out.println("Index Positions: " + indexValues);

					String[] indexArray = indexValues.split(", ");
					int count = indexArray.length;
					System.out.println("count---->" + count);

					for (String index : indexArray) {
						// System.out.println("Current index: " + index);

						int Actualindex = Integer.parseInt(index);

						if (Actualindex != 249 && Actualindex != 251) {

							System.out.println("Current index: " + Actualindex);
							Thread.sleep(7000);
							String requestBody = "[\n" + "    {\n" + "        \"component\": \"main\",\n"
									+ "        \"capability\": \"lockCodes\",\n"
									+ "        \"command\": \"deleteCode\",\n" + "        \"arguments\": ["
									+ Actualindex + "]\n" + "    }\n" + "]";

							System.out.println("Request Body: " + requestBody);

							Response response2 = RestAssured.given().accept(ContentType.JSON)
									.contentType(ContentType.JSON)
									.header("Authorization", "Bearer 21c0977b-0667-4ebc-ae6f-f04435c384e1")
									.header("Cache-Control", "no-cache").pathParam("deviceId", deviceId)
									.body(requestBody).when()
									.post("https://api.smartthings.com/v1/devices/{deviceId}/commands").then()
									.statusCode(200).extract().response();

							String DeletedResponse = response2.getBody().asPrettyString();
							System.out.println("DeletedResponse-----> " + DeletedResponse);
						}
					}

					Response response5 = RestAssured.given().accept(ContentType.JSON).contentType(ContentType.JSON)
							.header("Authorization", Token).header("Cache-Control", "no-cache")
							.pathParam("deviceId", deviceId).when()
							.get("https://api.smartthings.com/v1/devices/{deviceId}/components/main/capabilities/lockCodes/status")
							.then().statusCode(200).extract().response();

					String deviceCodes = response5.jsonPath().getString("lockCodes.value");
					System.out.println("DeviceCodes of the last device after deletion: " + deviceCodes);

					JSONObject jsonObject3 = new JSONObject(deviceCodes);
					StringBuilder indices2 = new StringBuilder();

					for (String key : jsonObject3.keySet()) {
						if (key.matches("\\d+")) {
							indices2.append(key).append(", ");
						}
					}

					if (indices2.length() > 0) {
						indices2.delete(indices2.length() - 2, indices2.length()); // Remove the trailing comma and
																					// space
					}
					String indexValues2 = indices2.toString();
					Thread.sleep(2000);

					System.out.println("Index Positions: " + indexValues2);

					String[] indexArray2 = indexValues2.split(", ");
					int count2 = indexArray2.length;
					System.out.println("Remaining Lock codes in this device After deletion perform---->" + count2);

				} else {
					System.out.println(devicename + " " + deviceLabel + " -->" + deviceId + "---------->This device is "
							+ DeviceStatus);
				}
			}
		}
	}

}
