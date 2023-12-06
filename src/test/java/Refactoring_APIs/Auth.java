package Refactoring_APIs;

import static io.restassured.RestAssured.given;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.testng.retry.rConfigData;

import deviceThreadApi.Base;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Auth extends Base {

	String sessionId = "";

	@Test(priority = 0, enabled = false)
	public void Auth_POST_Verify2FA() throws Throwable, IOException {
		String EmailId = "mukti@gmail.com";

		// Request Body
		String requestBody = "{\n" + "    \"otp\": \"ft3x\",\n" + "    \"emailId\": \"" + EmailId + "\"\n" + "}";

		// Send POST request and get the response
		Response response = given().accept(ContentType.JSON).header("Authorization", "Bearer " + sessionId)
				.contentType(ContentType.JSON).body(requestBody).when().post(rConfigData.Auth_POST_Verify2FA()).then()
				.statusCode(200).extract().response();

		String verify2faResponse = response.getBody().asPrettyString();

		System.out.println(" verify2faResponse  -----" + verify2faResponse);

		Assert.assertFalse(verify2faResponse.isEmpty(), "createUserResponse is empty or null");

	}

	@Test(priority = 0, enabled = false)
	public void Auth_POST_send2faMail() throws Throwable {
		String EmailId = "mukti@gmail.com";
		String password = "123456";

		String requestBody = "{\n" + "    \"emailId\": \"" + EmailId + "\",\n" + "    \"password\": \"" + password
				+ "\"\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).body(requestBody).when()
				.post(rConfigData.Auth_POST_send2faMail()).then().statusCode(200).extract().response();

		String send2faResponse = response.getBody().asPrettyString();

		System.out.println("send2faResponse is  -------------" + send2faResponse);

		Assert.assertFalse(send2faResponse.isEmpty(), "allUserDetails is empty or null");

	}

	@Test(priority = 0, enabled = false)
	public void Auth_POST_forgetPassword() throws Throwable, IOException {
		String EmailId = "mukti@gmail.com";

		String requestBody = "{\n" + "    \"emailId\": \"" + EmailId + "\"\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON).pathParam("userId", userId)
				.header("Authorization", "Bearer " + sessionId).body(requestBody).when()
				.post(rConfigData.Auth_POST_forgetPassword()).then().statusCode(200).extract().response();

		String forgetPasswordResponse = response.getBody().asPrettyString();
		System.out.println("forgetPasswordResponse----->" + forgetPasswordResponse);

	}

	@Test(priority = 0, enabled = false)
	public void Auth_POST_verifyforgetPasswordCode() throws Throwable, IOException {
		String EmailId = "mukti@gmail.com";
		String requestBody = "{\n" + "    \"otp\": \"ft3x\",\n" + "    \"emailId\": \"" + EmailId + "\"\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON).pathParam("userId", userId)
				.header("Authorization", "Bearer " + sessionId).body(requestBody).when()
				.post(rConfigData.Auth_POST_verifyforgetPasswordCode()).then().statusCode(200).extract().response();

		String VerifyforgetPasswordResponse = response.getBody().asPrettyString();
		System.out.println("updateUserDetailsResponse----->" + VerifyforgetPasswordResponse);

	}

	@Test(priority = 0, enabled = false)
	public void Auth_POST_ResetPassword() throws Throwable, IOException {

		String EmailId = "mukti@gmail.com";
		String password = "123456";

		String requestBody = "{\n" + "    \"emailId\": \"" + EmailId + "\",\n" + "    \"password\": \"" + password
				+ "\"\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON).pathParam("userId", userId)
				.header("Authorization", "Bearer " + sessionId).body(requestBody).when()
				.post(rConfigData.Auth_POST_ResetPassword()).then().statusCode(200).extract().response();

		String resetpasswordRespponse = response.getBody().asPrettyString();
		System.out.println("resetpasswordRespponse----->" + resetpasswordRespponse);

	}

	@Test(priority = 0, enabled = false)
	public void Auth_POST_Login() throws Throwable, IOException {

		String EmailId = "mukti@gmail.com";
		String password = "123456";

		String requestBody = "{\n" + "    \"emailId\": \"" + EmailId + "\",\n" + "    \"password\": \"" + password
				+ "\"\n" + "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).body(requestBody).when()
				.post(rConfigData.Auth_POST_Login()).then().statusCode(200).extract().response();

		String loginResponse = response.getBody().asPrettyString();
		System.out.println("loginResponse----->" + loginResponse);

	}

	@Test(priority = 0, enabled = false)
	public void Auth_PATCH_refreshToken() throws Throwable, IOException {

		String requestBody = "{\n"
				+ "    \"refreshToken\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjA0YjM0MGM0LWVjZjktNDdlNC1iZTk3LTljN2YyZmQzNmZhZiIsImVtYWlsSWQiOiJ1KioqKjZAZ21haWwuY29tIiwiZmlyc3ROYW1lIjoidWRheSIsImxhc3ROYW1lIjoicyIsImlhdCI6MTY5ODgxNDAwNSwiZXhwIjoxNjk5NDE4ODA1fQ.cZzHY63ET54OM34jQNxGOqpznEGmfQyWJkv5M41kHxc\"\n"
				+ "}";

		Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
				.header("Authorization", "Bearer " + sessionId).body(requestBody).when()
				.post(rConfigData.Auth_PATCH_refreshToken()).then().statusCode(200).extract().response();

		String refreshTokenResponse = response.getBody().asPrettyString();
		System.out.println("verifyinviteUserResponse----->" + refreshTokenResponse);

	}

}