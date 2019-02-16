/**
 * 
 */
package com.availablehotels.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Mohammad Izwayyed
 * 
 * Unit testing Methods
 *
 */
public class AvailableHotelServiceTest  {

	private HttpServer server;
	private WebTarget target;

	@Before
	public void setUp() throws Exception {
		// start the server
		server = Main.startServer();
		// create the client
		Client c = ClientBuilder.newClient();

		target = c.target(Main.BASE_URI);
	}

	@After
	public void tearDown() throws Exception {
		server.stop();
	}

	/**
	 * Test the response from the API.
	 */
	@Test
	public void testConnection() {
		String responseMsg = target.path("AvailableHotels").request().get(String.class);
		assertNotNull(responseMsg); 
	}
	
	/**
	 * Test sending all valid params
	 */
	@Test
	public void testNOMissingParams_Ok() {
		Response output = target.path("AvailableHotels")
				.queryParam("fromDate", "2019-08-08")
				.queryParam("toDate", "2019-08-10")
				.queryParam("numberOfAdults", "1")
				.queryParam("city", "AMM")
				.request()
				.get();
		
		assertEquals("Should return status 200", 200, output.getStatus());
		assertNotNull("Should return response", output.getEntity());
	}
	
	/**
	 * Test missing param From Date
	 */
	@Test
	public void testMissingParams_FromDate() {
		Response output = target.path("AvailableHotels")
				.queryParam("toDate", "2019-08-10")
				.queryParam("numberOfAdults", "1")
				.queryParam("city", "AMM")
				.request()
				.get();
		
		assertEquals("Should return status 200", 200, output.getStatus());
		assertEquals("Should return {\"error\":\"FromDate is Empty\"} ", "{\"error\":\"FromDate is Empty\"}", output.readEntity(String.class));
	}
	
	
	/**
	 * Test invalid From Date
	 */
	@Test
	public void testInvalidParams_FromDate() {
		Response output = target.path("AvailableHotels")
				.queryParam("fromDate", "2019/0sad-08")
				.queryParam("toDate", "2019-08-10")
				.queryParam("numberOfAdults", "1")
				.queryParam("city", "AMM")
				.request()
				.get();
		
		assertEquals("Should return status 200", 200, output.getStatus());
		assertEquals("Should return {\"error\":\"Invalid FromDate Format\"} ", "{\"error\":\"Invalid FromDate Format\"}", output.readEntity(String.class));
	}
	
	/**
	 * Test missing param To Date
	 */
	@Test
	public void testMissingParams_ToDate() {
		Response output = target.path("AvailableHotels")
				.queryParam("fromDate", "2019-08-10")
				.queryParam("numberOfAdults", "1")
				.queryParam("city", "AMM")
				.request()
				.get();
		
		assertEquals("Should return status 200", 200, output.getStatus());
		assertEquals("Should return {\"error\":\"toDate is Empty\"} ", "{\"error\":\"toDate is Empty\"}", output.readEntity(String.class));
	}
	
	
	/**
	 * Test invalid To Date
	 */
	@Test
	public void testInvalidParams_ToDate() {
		Response output = target.path("AvailableHotels")
				.queryParam("fromDate", "2019-08-10")
				.queryParam("toDate", "2019sadasd08A10")
				.queryParam("numberOfAdults", "1")
				.queryParam("city", "AMM")
				.request()
				.get();
		
		assertEquals("Should return status 200", 200, output.getStatus());
		assertEquals("Should return {\"error\":\"Invalid toDate Format\"} ", "{\"error\":\"Invalid toDate Format\"}", output.readEntity(String.class));
	}
	
	/**
	 * Test missing City
	 */
	@Test
	public void testMissingParams_city() {
		Response output = target.path("AvailableHotels")
				.queryParam("fromDate", "2019-08-10")
				.queryParam("toDate", "2019-08-10")
				.queryParam("numberOfAdults", "1")
				.request()
				.get();
		
		assertEquals("Should return status 200", 200, output.getStatus());
		assertEquals("Should return {\"error\":\"City IATA Code is Empty\"} ", "{\"error\":\"City IATA Code is Empty\"}", output.readEntity(String.class));
	}
	
	
	/**
	 * Test Invalid City IATA Code
	 */
	@Test
	public void testInvalidParams_city() {
		Response output = target.path("AvailableHotels")
				.queryParam("fromDate", "2019-08-10")
				.queryParam("toDate", "2019-08-10")
				.queryParam("numberOfAdults", "1")
				.queryParam("city", "AMMDAS")
				.request()
				.get();
		
		assertEquals("Should return status 200", 200, output.getStatus());
		assertEquals("Should return {\"error\":\"Invalid City IATA Code\"} ", "{\"error\":\"Invalid City IATA Code\"}", output.readEntity(String.class));
	}

}
