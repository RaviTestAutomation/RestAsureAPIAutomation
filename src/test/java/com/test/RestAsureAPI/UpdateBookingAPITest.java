package com.test.RestAsureAPI;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.test.RestAPITestBase.RestAPITestConstants;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utilities.ReadJson;

public class UpdateBookingAPITest {

	RequestSpecification httpRequest;
	Response response;
	ReadJson readJson;
	int id;
	String token = " ";
	private static final Logger log = LogManager.getLogger(CreateBookingAPITest.class);

	@BeforeTest
	public void generateToken() throws IOException, ParseException {
		readJson = new ReadJson();
		httpRequest = RestAssured.given();
		httpRequest.header("content-type", "application/json");
		httpRequest.body(readJson.readJsonData("Authentication"));
		Response response = httpRequest.post("https://restful-booker.herokuapp.com/auth");
		log.info("Respnse :" + response.body().asString());
		JSONParser parser = new JSONParser();
		JSONObject jsonObj = (JSONObject) parser.parse(response.body().asString());
		token = jsonObj.get("token").toString();
		log.info("Access Token :" + token);
		// context.setAttribute("booking-id", bookingId);
		log.info("Authentication API response code:" + response.getStatusCode());
		log.info("Authentication API response message:" + response.getStatusLine());
	}

	@Test
	public void updateBookingTest(ITestContext context) throws ParseException, IOException {
		log.info("######################Performing Update Booking######################");
		httpRequest = RestAssured.given();
		log.info("Access token value is:" + token);
		httpRequest.header("content-type", "application/json");
		httpRequest.header("Cookie", "token=" + token);
		httpRequest.body(readJson.readJsonData("UpdateBooking"));
		int id = (Integer) context.getAttribute("booking-id");
		httpRequest.pathParam("id", id);
		response = httpRequest.put(RestAPITestConstants.baseUrl + RestAPITestConstants.bookingUrl + "/{id}");
		log.info("Response :" + response.body().asString());
		log.info("UpdateBooking responsecode :" + response.getStatusCode());
		log.info("UpdateBooking response message :" + response.getStatusLine());
		JSONParser parser = new JSONParser();
		JSONObject responseJsonObj = (JSONObject) parser.parse(response.body().asString());
		log.info("Booking id updated value are:");
		log.info("Booking id updated firstname:" + responseJsonObj.get("firstname"));
		log.info("Booking id updated lastname:" + responseJsonObj.get("lastname"));
		JSONObject inputJsonObject = readJson.readJsonData("CreateBooking");
		log.info("Booking id Original value are:");
		log.info("Booking id Original firstname:" + inputJsonObject.get("firstname"));
		log.info("Booking id Original lastname:" + inputJsonObject.get("lastname"));
		Assert.assertNotEquals(inputJsonObject.get("firstname"), responseJsonObj.get("firstname"),
				"Update booking fail with the wrong firstname " + responseJsonObj.get("firstname"));
		Assert.assertEquals(inputJsonObject.get("lastname"), responseJsonObj.get("lastname"),
				"Update booking fail with the wrong lastname " + responseJsonObj.get("lastname"));
	}

}
