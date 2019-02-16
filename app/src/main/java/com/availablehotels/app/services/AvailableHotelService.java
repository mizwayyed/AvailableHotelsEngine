package com.availablehotels.app.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

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

import com.availablehotels.app.dto.SearchHotelRequestDTO;
import com.availablehotels.app.dto.SearchHotelResponseDTO;
import com.availablehotels.app.services.commons.AVHotelsLogger;
import com.availablehotels.app.services.commons.ServicesUtils;
import com.availablehotels.providers.SearchProvider;
import com.availablehotels.providers.SearchProviderFactory;
import com.availablehotels.validators.AvailableHotelServiceValidator;

@Path("AvailableHotels")
public class AvailableHotelService {
	
	public static final Logger avHotelLogger = AVHotelsLogger.getLogger();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findHotels(@QueryParam("fromDate") String fromDate,
    		@QueryParam("toDate") String toDate,
    		@QueryParam("city") String city,
    		@QueryParam("numberOfAdults") Integer numberOfAdults) {
    	
    	avHotelLogger.info("Request Recieved");
    	avHotelLogger.info("Filling DTO");
    	
    	SearchHotelRequestDTO searchDTO = new SearchHotelRequestDTO();
    	searchDTO.setFromDate(fromDate);
    	searchDTO.setToDate(toDate);
    	searchDTO.setCityCode(city);
    	searchDTO.setNumberOfAdults(numberOfAdults);
    	
    	String errorCode = new AvailableHotelServiceValidator().validate(searchDTO);
    	
    	if (errorCode != null) {
    		 return Response.status(Status.OK).entity(new ServicesUtils().generateErrorJSON(errorCode)).build();
    	}
    	
    	/* Iterate the list of available Providers.
    	/ Only the This list in the Bundle will be Used in the search. 
    	 * 
    	 * This can become handy incase a provider is added/removed
    	 * 
    	 */
        ResourceBundle bundle = ResourceBundle.getBundle("ProvidersList");
        
        // get the keys
        Enumeration<String> enumeration = bundle.getKeys();
        
        List<SearchHotelResponseDTO> providersResponsesList = new ArrayList<SearchHotelResponseDTO>();
        
        while (enumeration.hasMoreElements()) {
        	SearchProvider hotelProvider = null;
        	try {
        		hotelProvider = SearchProviderFactory.createSearchProvider(bundle.getString(enumeration.nextElement()));
        		providersResponsesList.addAll(hotelProvider.findHotels(searchDTO));
        		
			} catch (Exception e) {
				avHotelLogger.error("Abnormal Exception", e);
	    		 return Response.status(Status.BAD_GATEWAY).entity(new ServicesUtils().generateErrorJSON("Abnormal Error. Try again later")).build();
			}
        }
        
        avHotelLogger.trace("Number Of Parsed Hotels = " + providersResponsesList.size());

        // Return Response
        return prepareResponse(providersResponsesList);	
        
    }
    
    private Response prepareResponse(List<SearchHotelResponseDTO> providersResponsesList) {
    	
    	try {
    		// Sort on fare ascedning.
    		Collections.sort(providersResponsesList, new Comparator<SearchHotelResponseDTO>() {
    			@Override
    			public int compare(SearchHotelResponseDTO c1, SearchHotelResponseDTO c2) {
    				return Double.compare(new Double(c1.getFare()), new Double(c2.getFare()));
    			}
    		});

    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	
    	JsonArrayBuilder hotelsArrayBuilder = Json.createArrayBuilder();

    	for (SearchHotelResponseDTO searchHotelResponseDTO : providersResponsesList) {
    		
        	JsonArrayBuilder amenitiesArraybuilder = Json.createArrayBuilder();
        	
        	for (String amenity : searchHotelResponseDTO.getRoomAmenities()) {
        		amenitiesArraybuilder.add(amenity);
    		}
        	
        	JsonArray amenitiesArray = amenitiesArraybuilder.build();

        	JsonObject hotelObject = Json.createObjectBuilder().add("provider", searchHotelResponseDTO.getProvider())
       	 			.add("hotelName", searchHotelResponseDTO.getHotelName())
       	 			.add("fare", searchHotelResponseDTO.getFare())
       	 			.add("amenities", amenitiesArray)
       	 			.build();
        	
        	hotelsArrayBuilder.add(hotelObject);
		}
    	
     	JsonObject hotels = Json.createObjectBuilder().add("hotels", hotelsArrayBuilder.build())
   	 			.build();
    	
    	avHotelLogger.trace("Returning : " + hotels.toString());
    	
        return Response.status(Status.OK).entity(hotels.toString()).build();	
    	
    }
}
