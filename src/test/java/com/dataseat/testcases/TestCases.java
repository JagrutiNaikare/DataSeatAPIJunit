package com.dataseat.testcases;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class TestCases {
	public static URIBuilder builder;
	public static HttpClient httpClient;
	public static HttpRequest request;
	public static HttpResponse<String> response;
	public static String id_nation = "01000US";
	public static String nation = "United States";
	public static String slug_nation = "united-states";

	public static JSONObject jsonObjectData;
	private String year;

	@Parameterized.Parameters
	public static String[] data() {
		return new String[] { "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020" };

	}

	public TestCases(String arg) {
		this.year = arg;
	}

	@Test
	public void classA_Test1() {

		try {

			System.out.println("*****************************************");
			httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

			assertTrue("Year should be between 2012 and 2020",
					(2012 <= Integer.parseInt(year)) && (Integer.parseInt(year) <= 2020));

			builder = new URIBuilder("https://datausa.io/api/data");
			builder.addParameter("drilldowns", "Nation");
			builder.addParameter("measures", "Population");
			builder.addParameter("year", year);

			System.out.println(builder.toString());
			request = HttpRequest.newBuilder().GET().uri(URI.create(builder.toString())).build();

			response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			jsonObjectData = getJSONObjectfromData();
			assertTrue(response.body() != null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("classA_Test1: " + year);
	}

	private static JSONObject getJSONObjectfromData() {
		JSONObject jsonObject = new JSONObject(response.body().toString());

		JSONArray arr = jsonObject.getJSONArray("data");

		if (!arr.isEmpty()) {
			JSONObject jsonObjectData = arr.getJSONObject(0);
			return jsonObjectData;
		}
		return null;
	}

	@Test
	public void checkURI() {
		assertEquals(builder.toString(), response.uri().toString());
	}

	@Test
	public void checkStatusCode() {
		assertEquals(response.statusCode(), 200);
	}

	@Test
	public void checkPopulationIsInteger() {
		try {
			Integer.parseInt(String.valueOf(jsonObjectData.get("Population")));

			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	@Test
	public void checkYearisSameasParam() {
		assertEquals(jsonObjectData.get("Year"), year);
		assertEquals(String.valueOf(jsonObjectData.get("ID Year")), year);

	}

	@Test
	public void validateData() {
		assertEquals(id_nation, jsonObjectData.getString("ID Nation"));
		assertEquals(nation, jsonObjectData.getString("Nation"));
		assertEquals(slug_nation, jsonObjectData.getString("Slug Nation"));
	}

}
