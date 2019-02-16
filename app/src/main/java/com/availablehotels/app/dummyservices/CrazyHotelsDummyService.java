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

@Path("CrazyHotelsDummyService")
public class CrazyHotelsDummyService {
	
	public static final Logger logger = AVHotelsLogger.getLogger();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findHotels(@QueryParam("city") String city,
    		@QueryParam("from") String fromDate,
    		@QueryParam("to") String toDate,
    		@QueryParam("adultsCount") Integer numberOfAdults) {
    	
    	logger.trace(" Request Recieved in CrazyHotelsDummyService");
    	
    	JsonArrayBuilder builder = Json.createArrayBuilder();
    	
    	String[] amenities =  new String[] {"Wi-Fi, Shower, Pool View"};
    	for (String string : amenities) {
    		builder.add(string);
		}
    	
    	JsonArray amenitiesArray = builder.build();

    	JsonObject dummyValue = Json.createObjectBuilder().add("hotel", "Mariott")
   	 			.add("hotelRate", "***")
   	 			.add("hotelFare", "100.00")
   	 			.add("discount", "30.00")
   	 			.add("roomAmenities", amenitiesArray)
   	 			.build();
    	
    	JsonObject dummyValue2 = Json.createObjectBuilder().add("hotel", "Kempenski")
   	 			.add("hotelRate", "*****")
   	 			.add("hotelFare", "780.00")
   	 			.add("roomAmenities", amenitiesArray)
   	 			.build();
    	
    	JsonArrayBuilder hotelBuilder = Json.createArrayBuilder();
    	hotelBuilder.add(dummyValue);
    	hotelBuilder.add(dummyValue2);
    	
    	JsonObject hotels = Json.createObjectBuilder().add("hotels", hotelBuilder.build())
   	 			.build();
    	
    	logger.trace("Returning : " + hotels.toString());
        return Response.status(Status.OK).entity(hotels.toString()).build();	
        
    }
}
