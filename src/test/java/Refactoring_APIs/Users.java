package Refactoring_APIs;

import static io.restassured.RestAssured.given;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.testng.retry.rConfigData;

import deviceThreadApi.Base;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Users extends Base {

	String sessionId = "";

	@Test(priority = 0, enabled = false)
	public void Users_POST_createUser() throws Throwable, IOException {

		// Request Body
		String requestBody = "{\n" + "    \"emailId\": \"uday30@gmail.com\",\n" + "    \"firstName\": \"uday\",\n"
				+ "    \"lastName\": \"s\",\n" + "    \"phoneNumber\": \"9987665432\",\n"
				+ "    \"password\": \"123456\",\n" + "    \"phoneNumberCountryCode\": \"IN\",\n"
				+ "    \"organizationId\": \"b6a689df-7640-4cd6-95dc-9afc99589011\",\n"
				+ "    \"roleId\": \"8d95535c-8768-45b5-8753-f2aa7f42f033\",\n" + "    \"jobTitle\": \"manager\"\n"
				+ "}";

		// Send POST request and get the response
		Response response = given().accept(ContentType.JSON).header("Authorization", "Bearer " + sessionId)
				.contentType(ContentType.JSON).body(requestBody).when().post(rConfigData.Users_POST_createUser()).then()
				.statusCode(200).extract().response();

		String createUserResponse = response.getBody().asPrettyString();

		System.out.println(" registerUserResponse  -----" + createUserResponse);

		Assert.assertFalse(createUserResponse.isEmpty(), "createUserResponse is empty or null");

	}

	@Test(priority = 0, enabled = false)
	public void Users_Get_getAllUsers() throws Throwable {

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.pathParam("userId", dtauthloginkey).header("Authorization", "Bearer " + sessionId).when()
				.get(rConfigData.Users_Get_getAllUsers()).then().statusCode(200).extract().response();

		String allUserDetails = response.getBody().asPrettyString();

		System.out.println("allUserDetails is  -------------" + allUserDetails);

		Assert.assertFalse(allUserDetails.isEmpty(), "allUserDetails is empty or null");

	}

	@Test(priority = 0, enabled = false)
	public void Users_Get_getUser() throws Throwable, IOException {

		String userId = "";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON).pathParam("userId", userId)
				.header("Authorization", "Bearer " + sessionId).when().get(rConfigData.Users_Get_getUser()).then()
				.statusCode(200).extract().response();

		String getUserResponse = response.getBody().asPrettyString();
		System.out.println("getUserResponse----->" + getUserResponse);

	}

	@Test(priority = 0, enabled = false)
	public void Users_Patch_UpdateUserDetails() throws Throwable, IOException {

		String userId = "";

		String requestBody = "{\n" + "    \"emailId\": \"uday29@gmail.com\",\n" + "    \"firstName\": \"uday\",\n"
				+ "    \"lastName\": \"s\",\n" + "    \"phoneNumber\": \"9987665432\",\n"
				+ "    \"password\": \"123456\",\n" + "    \"phoneNumberCountryCode\": \"IN\"\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON).pathParam("userId", userId)
				.header("Authorization", "Bearer " + sessionId).body(requestBody).when()
				.patch(rConfigData.Users_Patch_UpdateUserDetails()).then().statusCode(200).extract().response();

		String updateUserDetailsResponse = response.getBody().asPrettyString();
		System.out.println("updateUserDetailsResponse----->" + updateUserDetailsResponse);

	}

	@Test(priority = 0, enabled = false)
	public void Users_Delete_deleteUser() throws Throwable, IOException {

		String userId = "";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON).pathParam("userId", userId)
				.header("Authorization", "Bearer " + sessionId).when().delete(rConfigData.Users_Delete_deleteUser())
				.then().statusCode(200).extract().response();

		String deleteUserResponse = response.getBody().asPrettyString();
		System.out.println("deleteUserResponse----->" + deleteUserResponse);

	}

	@Test(priority = 0, enabled = false)
	public void Users_post_verifyOrganizationInvite() throws Throwable, IOException {

		String code = "X8Q80J";

		String requestBody = "{\n" + "    \"code\": \"" + code + "\"\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).body(requestBody).when()
				.post(rConfigData.Users_post_verifyOrganizationInvite()).then().statusCode(200).extract().response();

		String verifyinviteUserResponse = response.getBody().asPrettyString();
		System.out.println("verifyinviteUserResponse----->" + verifyinviteUserResponse);

	}

}