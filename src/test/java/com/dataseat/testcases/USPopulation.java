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
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;

@RunWith(Parameterized.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class USPopulation {
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
		return new String[] { "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020" };

	}

	public USPopulation(String arg) {
		this.year = arg;
	}

	@Before
	@Epic("Data seat")
	@Feature("FEATURE - US Population Testing")
	@Story("US - Population")
	@Step("Triggering end point")
	@Description("Setup")
	public void setup() {
		httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
	}
	
	@Test
	@Severity(SeverityLevel.CRITICAL)
	@Epic("Data seat")
	@Feature("FEATURE - US Population Testing")
	@Story("US - Population")
	@Step("Triggering end point")
	@Description("Initialize")
	public void A_initilization() {

		try {
			// = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

			assertTrue("Year should be between 2012 and 2020",
					(2012 <= Integer.parseInt(year)) && (Integer.parseInt(year) <= 2020));

			builder = new URIBuilder("https://datausa.io/api/data");
			builder.addParameter("drilldowns", "Nation");
			builder.addParameter("measures", "Population");
			builder.addParameter("year", year);
			request = HttpRequest.newBuilder().GET().uri(URI.create(builder.toString())).build();
			response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			jsonObjectData = getJSONObjectfromData();

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
	@Epic("Data seat")
	@Feature("FEATURE - US Population Testing")
	@Story("US - Population")
	@Step("Verify URI")
	@Description("To check URI")
	public void B_Check_URI() {
		assertEquals(builder.toString(), response.uri().toString());
	}

	@Test
	@Epic("Data seat")
	@Feature("FEATURE - US Population Testing")
	@Story("US - Population")
	@Step("Verify Status Code")
	@Description("checkStatusCode")
	public void C_Check_Status_Code() {
		assertEquals(response.statusCode(), 200);
	}

	@Test
	@Epic("Data seat")
	@Feature("FEATURE - US Population Testing")
	@Story("US - Population")
	@Step("Verify Population datatype")
	@Description("To check population should be an integer")
	public void D_check_population_should_be_an_integer() {
		if (jsonObjectData != null) {
			try {
				Integer.parseInt(String.valueOf(jsonObjectData.get("Population")));

				assertTrue(true);
			} catch (Exception e) {
				assertTrue("Population is not present/contains character", false);
			}
		} else {
			assertTrue("Data not present for year:"+year, false);
		}
	}

	@Test
	@Epic("Data seat")
	@Feature("FEATURE - US Population Testing")
	@Story("US - Population")
	@Step("Verify Year")
	@Description("To Check year is same as query parameter")
	public void E_Check_IDYear_should_be_same_as_year_in_the_query() {
		if (jsonObjectData != null) {
			assertEquals(jsonObjectData.get("Year"), year, "Year not present/not same as send");
			assertEquals(String.valueOf(jsonObjectData.get("ID Year")), year, "ID Year not present/not same as send");
		} else {
			assertTrue("Data not present for year:"+year, false);
		}
	}

	@Test
	@Epic("Data seat")
	@Feature("FEATURE - US Population Testing")
	@Story("US - Population")
	@Step("Verify JSON")
	@Description("To validate data")
	public void F_Check_data_Schema() {
		if (jsonObjectData != null) {
			assertEquals(id_nation, jsonObjectData.getString("ID Nation"), "ID Nation Mismatch");
			assertEquals(nation, jsonObjectData.getString("Nation"), "Nation Mismatch");
			assertEquals(slug_nation, jsonObjectData.getString("Slug Nation"), "Slug Nation Mismatch");
		} else {
			assertTrue("Data not present for year:"+year, false);
		}
	}

}
