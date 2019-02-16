package com.availablehotels.app.dummyservices;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.availablehotels.app.services.commons.AVHotelsLogger;

@Path("BestHotelsDummyService")
public class BestHotelsDummyService {
	
	public static final Logger logger = AVHotelsLogger.getLogger();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findHotels(@QueryParam("city") String city,
    		@QueryParam("fromDate") String fromDate,
    		@QueryParam("toDate") String toDate,
    		@QueryParam("numberOfAdults") Integer numberOfAdults) {
    	
    	logger.trace(" Request Recieved in BestHotelsDummyService");
    	
    	JsonArrayBuilder builder = Json.createArrayBuilder();
    	
    	String[] amenities =  new String[] {"LED TV, Safe, In-room iron, Wi-Fi"};
    	for (String string : amenities) {
    		builder.add(string);
		}
    	
    	JsonArray amenitiesArray = builder.build();

    	JsonObject dummyValue = Json.createObjectBuilder().add("hotel", "Fairmont")
   	 			.add("hotelRate", "4")
   	 			.add("hotelFare", "505.50")
   	 			.add("roomAmenities", amenitiesArray)
   	 			.build();
    	
    	JsonObject dummyValue2 = Json.createObjectBuilder().add("hotel", "Sheraton")
   	 			.add("hotelRate", "5")
   	 			.add("hotelFare", "650.22")
   	 			.add("roomAmenities", amenitiesArray)
   	 			.build();
    	
    	JsonObject dummyValue3 = Json.createObjectBuilder().add("hotel", "Sheraton")
   	 			.add("hotelRate", "5")
   	 			.add("hotelFare", "50.20")
   	 			.add("roomAmenities", amenitiesArray)
   	 			.build();
    	
    	JsonArrayBuilder hotelBuilder = Json.createArrayBuilder();
    	hotelBuilder.add(dummyValue);
    	hotelBuilder.add(dummyValue2);
    	hotelBuilder.add(dummyValue3);
    	
    	JsonObject hotels = Json.createObjectBuilder().add("hotels", hotelBuilder.build())
   	 			.build();
    	
    	logger.trace("Returning : " + hotels.toString());
        return Response.status(Status.OK).entity(hotels.toString()).build();	
        
    }
}
