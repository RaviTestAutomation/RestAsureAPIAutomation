/*
 * *Author : RaviKumar Mogulluru
 */

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

public class DeleteBookingAPITest  {

	RequestSpecification httpRequest;
	Response response;
	ReadJson readJson;
	String token = " ";
	private static final Logger log = LogManager.getLogger(DeleteBookingAPITest.class);
	
	
	@BeforeTest
	public void generateToken() throws IOException, ParseException
	{
		readJson = new ReadJson();
		httpRequest = RestAssured.given();
		httpRequest.header("content-type", "application/json");
		httpRequest.body(readJson.readJsonData("Authentication"));
		Response response = httpRequest.post("https://restful-booker.herokuapp.com/auth");
		log.info("Respnse :"+response.body().asString());
		JSONParser parser = new JSONParser();
		JSONObject jsonObj = (JSONObject) parser.parse(response.body().asString());
		token = jsonObj.get("token").toString();
		log.info("Access Token :"+token);
		log.info("Authentication API response code:"+response.getStatusCode());
		log.info("Authentication API response message:"+response.getStatusLine());
	}
	@Test
	public void deleteBookingTest(ITestContext context) throws ParseException {
		log.info("######################Performing Delete Booking######################");
		JSONObject data = new JSONObject();
		httpRequest = RestAssured.given();
		httpRequest.header("content-type", "application/json");
		System.out.println("Access token value is:"+token);
		httpRequest.header("Cookie","token="+token);
		int id = (Integer) context.getAttribute("booking-id");
		httpRequest.pathParam("id", id);
		response = httpRequest.delete(RestAPITestConstants.baseUrl+RestAPITestConstants.bookingUrl+"/{id}");
		log.info("Response :" + response.body().asString());
		log.info("UpdateBooking responsecode :"+response.getStatusCode());
		log.info("UpdateBooking response message :"+response.getStatusLine());
		//validating get booking after delete booking id
		response = httpRequest.get(RestAPITestConstants.baseUrl+RestAPITestConstants.bookingUrl+"/{id}");
		log.info("Response :" + response.body().asString());
		log.info("After delete Get booking response code :"+response.getStatusCode());
		log.info("After delete Get booking response message :"+response.getStatusLine());
		Assert.assertEquals(404, response.getStatusCode(),"Get Booking failing with status code:");
		Assert.assertEquals(true, response.getStatusLine().contains("Not Found"),"Get Booking failing with status message:");
	}

}
