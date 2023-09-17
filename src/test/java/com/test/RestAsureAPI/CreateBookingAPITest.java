/*
 * *Author : RaviKumar Mogulluru
 */

package com.test.RestAsureAPI;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.test.RestAPITestBase.RestAPITestConstants;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utilities.ReadJson;

public class CreateBookingAPITest  {

	RequestSpecification httpRequest;
	Response response;
	ReadJson readJson;
	int bookingId;
	private static final Logger log = LogManager.getLogger(CreateBookingAPITest.class);
	
	@Test
	public void ceateBookingTest(ITestContext context) throws ParseException, IOException {
		log.info("######################Performing Create Booking######################");
		JSONObject payload = new JSONObject();
		readJson = new ReadJson();
		httpRequest = RestAssured.given();
		httpRequest.header("content-type", "application/json");
		httpRequest.body(readJson.readJsonData("CreateBooking"));
		response = httpRequest.post(RestAPITestConstants.baseUrl+RestAPITestConstants.bookingUrl);
		log.info("Response :" + response.body().asString());
		log.info("Create Booking API responsecode :"+response.getStatusCode());
		log.info("Create Booking API  response message :"+response.getStatusLine());
		JSONParser parser = new JSONParser();
		JSONObject jsonObj = (JSONObject) parser.parse(response.body().asString());
		bookingId = Integer.parseInt(jsonObj.get("bookingid").toString());
		log.info("Bookingid:"+bookingId);
		context.setAttribute("booking-id", bookingId);
		Assert.assertEquals(200, response.getStatusCode(),"Create Booking failing with status code:");
		Assert.assertEquals(true, response.getStatusLine().contains("OK"),"Create Booking failing with status message:");
	}
	
}
