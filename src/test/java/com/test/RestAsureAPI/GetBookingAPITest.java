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
import org.testng.annotations.Test;

import com.github.javafaker.Faker;
import com.test.RestAPITestBase.RestAPITestConstants;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utilities.ReadJson;

public class GetBookingAPITest {
	
	RequestSpecification httpRequest;
	Response response;
	int id;
	ReadJson readJson;
	private static final Logger log = LogManager.getLogger(GetBookingAPITest.class);
	
	
	@Test
	public void  getBookingTest(ITestContext context) throws ParseException, IOException {
		log.info("######################Performing Get Booking######################");
		readJson = new ReadJson();
		httpRequest = RestAssured.given();
		int id = (Integer) context.getAttribute("booking-id");
		log.info("Booking id:"+id);
		httpRequest.pathParams("id",id);
		response = httpRequest.get(RestAPITestConstants.baseUrl+RestAPITestConstants.bookingUrl+"/{id}");
		log.info("Response :" + response.body().asString());
		log.info("Get booking responsecode :"+response.getStatusCode());
		log.info("Get booking response message :"+response.getStatusLine());
		Assert.assertEquals(200, response.getStatusCode(),"Get Booking failing with status code:");
		Assert.assertEquals(true, response.getStatusLine().contains("OK"),"Get Booking failing with status message:");
		JSONParser parser = new JSONParser();
		JSONObject responseJsonObj = (JSONObject) parser.parse(response.body().asString());
		log.info("Booking id retrived value are:");
		log.info("Booking id retrived firstname:"+responseJsonObj.get("firstname"));
		log.info("Booking id retrived lastname:"+responseJsonObj.get("lastname"));
		JSONObject inputJsonObject = readJson.readJsonData("CreateBooking");
		Assert.assertEquals(inputJsonObject.get("firstname"), responseJsonObj.get("firstname"),"Get booking fail with the wrong firstname "+responseJsonObj.get("firstname"));
		Assert.assertEquals(inputJsonObject.get("lastname"), responseJsonObj.get("lastname"),"Get booking fail with the wrong lastname "+responseJsonObj.get("lastname"));
	}
}
