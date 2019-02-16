package com.availablehotels.providers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import org.apache.log4j.Logger;

import com.availablehotels.app.dto.SearchHotelRequestDTO;
import com.availablehotels.app.dto.SearchHotelResponseDTO;
import com.availablehotels.app.services.commons.AVHotelsLogger;

public class BestHotelProvider implements SearchProvider {

	public static final Logger logger = AVHotelsLogger.getLogger();
    private ResourceBundle serverConfigBundle;
    
	@Override
	public List<SearchHotelResponseDTO> findHotels(SearchHotelRequestDTO searchHotelRequestDTO) {
		
        serverConfigBundle = ResourceBundle.getBundle("ServerConfig");
        
        // Get the Number of Days. 
        morphRequest(searchHotelRequestDTO);
        
		try {
		String baseURI = serverConfigBundle.getString("BEST_HOTEL_BASE_URI");
		String serviceName = "BestHotelsDummyService";
		URL url = new URL(generateURL(baseURI, serviceName, searchHotelRequestDTO));
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
			(conn.getInputStream())));

		StringBuffer output = new StringBuffer();
		String line;
		System.out.println("Output from Server .... \n");
		while ((line = br.readLine()) != null) {
			System.out.println(line);
			output.append(line);
		}
		
		logger.info("Response Recieved from BestHotelsDummyService: " +  output.toString());
		conn.disconnect();
		
		int numberOfDays =  (int) searchHotelRequestDTO.getAdditionalData().get("numberOfDays");
		
		return parseResponse(output.toString(), numberOfDays);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private String generateURL (String baseURI, String serviceName, SearchHotelRequestDTO dto) {
		return new StringBuffer(baseURI).append(serviceName).append("?city=").append(dto.getCityCode()).append("&fromDate=").append(dto.getFromDate()).append("&toDate=").append(dto.getToDate())
		.append("&numberOfAdults=").append(dto.getNumberOfAdults()).toString();
	}
	
	private List<SearchHotelResponseDTO> parseResponse (String response, int numberOfDays) {
		
		List<SearchHotelResponseDTO> listOfHotels = new ArrayList<SearchHotelResponseDTO>();
		
		// Transformed the returned String into a JsonObject
		JsonReader jsonReader = Json.createReader(new StringReader(response));
		JsonObject jsonObject = jsonReader.readObject();
		jsonReader.close();
		
		JsonArray hotelsArray = jsonObject.getJsonArray("hotels");
		
		logger.debug("Nummber of Retrived Hotels: " + hotelsArray.size());

		for (JsonValue hotelObject : hotelsArray) {
            final JsonObject hotel = (JsonObject) hotelObject;
    		
    		SearchHotelResponseDTO hotelResponseDTO  = new SearchHotelResponseDTO();
    		hotelResponseDTO.setHotelName(hotel.getString("hotel"));
    		hotelResponseDTO.setRate(hotel.getString("hotelRate"));
    		hotelResponseDTO.setProvider("BestHotels");
    		
    		Double totalPrice =  new Double (hotel.getString("hotelFare"));
    		Double nightPrice = totalPrice / numberOfDays;
    		hotelResponseDTO.setFare(String.valueOf(nightPrice));

    		JsonArray roomAmenitiesArray = hotel.getJsonArray("roomAmenities");
    		
    		for (JsonValue roomAmenity : roomAmenitiesArray) {
    			hotelResponseDTO.getRoomAmenities().add(roomAmenity.toString());
    		}
    		
            listOfHotels.add(hotelResponseDTO);
		}

		return listOfHotels;
	}
	
	/**
	 *  make changes required to CrazyHotel API
	 * @return 
	 * @throws ParseException 
	 */
	private synchronized void morphRequest (SearchHotelRequestDTO requestDTO) {
		
		try {
		Date fromDateOld = new SimpleDateFormat("yyyy-MM-dd").parse(requestDTO.getFromDate());
		Date toDateOld = new SimpleDateFormat("yyyy-MM-dd").parse(requestDTO.getToDate());
		
		long diff = toDateOld.getTime() - fromDateOld.getTime();
		logger.trace("Number of Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
		int numOfDays = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		requestDTO.getAdditionalData().put("numberOfDays", numOfDays);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
