package Refactoring_APIs;

import static io.restassured.RestAssured.given;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.testng.retry.rConfigData;

import deviceThreadApi.Base;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class organization extends Base {

	String sessionId = "";

	@Test(priority = 0, enabled = false)
	public void organization_POST_addOrganization() throws Throwable, IOException {

		// Request Body
		String requestBody = "{\n" + "    \"code\": \"NdTIRv\",\n" + "    \"name\": \"Dev Server Org\",\n"
				+ "    \"phoneNumber\": \"+919754577305\",\n" + "    \"phoneNumberCountryCode\": \"IN\",\n"
				+ "    \"country\": \"IN\",\n" + "    \"state\": \"Andhra Pradesh\",\n"
				+ "    \"city\": \"Hyderabad\",\n" + "    \"address1\": \"address1\",\n"
				+ "    \"address2\": \"address2\",\n" + "    \"zipCode\": \"754209\",\n"
				+ "    \"timezone\": \"Asia/Kolkata\",\n" + "    \"superAdminData\": {\n"
				+ "        \"jobTitle\": \"Developer\",\n" + "        \"firstName\": \"Akriti\",\n"
				+ "        \"lastName\": \"Saraogi\",\n" + "        \"password\": \"Pass12345\",\n"
				+ "        \"emailId\": \"devdt3@yopmail.com\",\n" + "        \"phoneNumber\": \"+91-8923789127\",\n"
				+ "        \"phoneNumberCountryCode\": \"IN\"\n" + "    }\n" + "}";

		// Send POST request and get the response
		Response response = given().accept(ContentType.JSON).header("Authorization", "Bearer " + sessionId)
				.contentType(ContentType.JSON).body(requestBody).when()
				.post(rConfigData.organization_POST_addOrganization()).then().statusCode(200).extract().response();

		String addOrganizationResponse = response.getBody().asPrettyString();

		System.out.println(" addOrganizationResponse  -----" + addOrganizationResponse);

		Assert.assertFalse(addOrganizationResponse.isEmpty(), "addOrganizationResponse is empty or null");

	}

	@Test(priority = 0, enabled = false)
	public void organization_PUT_updateOrganization() throws Throwable {

		String organizationId = "";

		String requestBody = "{\n" + "  \"name\": \"My Organization One\",\n"
				+ "  \"phoneNumber\": \"+919876543210\",\n" + "  \"phoneNumberCountryCode\": \"IN\",\n"
				+ "  \"country\": \"IN\",\n" + "  \"state\": \"OD\",\n" + "  \"city\": \"Bhubaneswar\",\n"
				+ "  \"address1\": \"Bhubaneswar\",\n" + "  \"address2\": \"Bhubaneswar\",\n"
				+ "  \"zipCode\":\"754209\",\n" + "  \"timezone\":\"Asia/Kolkata\"\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam(organizationId, organizationId).header("Authorization", "Bearer " + sessionId)
				.body(requestBody).when().put(rConfigData.organization_PUT_updateOrganization()).then().statusCode(200)
				.extract().response();

		String updateOrganizationResponse = response.getBody().asPrettyString();

		System.out.println("updateOrganizationResponse is  -------------" + updateOrganizationResponse);

		Assert.assertFalse(updateOrganizationResponse.isEmpty(), "updateOrganizationResponse is empty or null");

	}

	@Test(priority = 0, enabled = false)
	public void organization_GET_getAllOrganization() throws Throwable, IOException {

		// String propertyId = "";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).when()
				.get(rConfigData.organization_GET_getAllOrganization()).then().statusCode(200).extract().response();

		String getAllOrganizationResponse = response.getBody().asPrettyString();
		System.out.println("getAllOrganizationResponse----->" + getAllOrganizationResponse);

	}

	@Test(priority = 0, enabled = false)
	public void organization_Delete_deleteOrganization() throws Throwable, IOException {

		String OrganizationId = "";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON).pathParam("zoneId", zoneId)
				.header("Authorization", "Bearer " + sessionId).when()
				.get(rConfigData.organization_Delete_deleteOrganization()).then().statusCode(200).extract().response();

		String deleteOrganizationResponse = response.getBody().asPrettyString();
		System.out.println("deleteOrganizationResponse----->" + deleteOrganizationResponse);

	}

	@Test(priority = 0, enabled = false)
	public void organization_get_getOrganizationById() throws Throwable, IOException {

		String organizationId = "";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam(organizationId, organizationId).header("Authorization", "Bearer " + sessionId).when()
				.get(rConfigData.organization_get_getOrganizationById()).then().statusCode(200).extract().response();

		String OrganizationDetailsResponse = response.getBody().asPrettyString();
		System.out.println("OrganizationDetailsResponse----->" + OrganizationDetailsResponse);

	}

	@Test(priority = 0, enabled = false)
	public void organization_Post_organizationInvite() throws Throwable, IOException {

		String requestBody = "{\n" + "    \"firstName\": \"Akriti\",\n" + "    \"lastName\": \"Saraogi\",\n"
				+ "    \"emailId\": \"devdt4@yopmail.com\"\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).body(requestBody).when()
				.post(rConfigData.organization_Post_organizationInvite()).then().statusCode(200).extract().response();

		String organizationInviteResponse = response.getBody().asPrettyString();
		System.out.println("organizationInviteResponse----->" + organizationInviteResponse);

	}

	@Test(priority = 0, enabled = false)
	public void organization_PUT_updateBillableContact() throws Throwable, IOException {

		String requestBody = "{\n" + "    \"isBillable\": true,\n"
				+ "    \"billableContactId\": \"eb9ee2cc-354a-4368-9b9a-9e69b995ca0e\"\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).body(requestBody).when()
				.put(rConfigData.organization_PUT_updateBillableContact()).then().statusCode(200).extract().response();

		String updateBillablecontactResponse = response.getBody().asPrettyString();
		System.out.println("updateBillablecontactResponse----->" + updateBillablecontactResponse);

	}

	@Test(priority = 0, enabled = false)
	public void organization_DELETE_deleteOrSuspendOrgUser() throws Throwable, IOException {

		String requestBody = "{\n" + "    \"userToRemove\": [\"eb9ee2cc-354a-4368-9b9a-9e69b995ca0e\"],\n"
				+ "    \"organizationId\": \"d1751a99-f42b-4ad8-a6dd-760cd39d601e\",\n" + "    \"isDeleted\": false,\n"
				+ "    \"isSuspended\": true\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).body(requestBody).when()
				.delete(rConfigData.organization_DELETE_deleteOrSuspendOrgUser()).then().statusCode(200).extract()
				.response();

		String deleteOrSuspendOrguserResponse = response.getBody().asPrettyString();
		System.out.println("deleteOrSuspendOrguserResponse----->" + deleteOrSuspendOrguserResponse);

	}

}