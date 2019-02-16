package com.availablehotels.providers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import org.apache.log4j.Logger;

import com.availablehotels.app.dto.SearchHotelRequestDTO;
import com.availablehotels.app.dto.SearchHotelResponseDTO;
import com.availablehotels.app.services.commons.AVHotelsLogger;

public class CrazyHotelsProvider implements SearchProvider {

	public static final Logger logger = AVHotelsLogger.getLogger();
    private ResourceBundle serverConfigBundle;
    
	@Override
	public List<SearchHotelResponseDTO> findHotels(SearchHotelRequestDTO searchHotelRequestDTO) {
		
		// Make needed changed specifec for Crazy Hotel API
		try {
			morphRequest(searchHotelRequestDTO);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		serverConfigBundle = ResourceBundle.getBundle("ServerConfig");

		try {
		String baseURI = serverConfigBundle.getString("CRAZY_HOTEL_BASE_URI");
		String serviceName = "CrazyHotelsDummyService";
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
		
		logger.info("Response Recieved from CrazyHotelsDummyService: " +  output.toString());
		conn.disconnect();
		
		return parseResponse(output.toString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private String generateURL (String baseURI, String serviceName, SearchHotelRequestDTO dto) {
		return new StringBuffer(baseURI).append(serviceName)
				.append("?city=").append(dto.getCityCode())
				.append("&from=")
				.append(dto.getFromDate())
				.append("&to=")
				.append(dto.getToDate())
				.append("&adultsCount=").
				append(dto.getNumberOfAdults()).
				toString();
	}
	
	private List<SearchHotelResponseDTO> parseResponse (String response) {
		
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
    		hotelResponseDTO.setFare(hotel.getString("hotelFare"));
    		hotelResponseDTO.setHotelName(hotel.getString("hotel"));
    		hotelResponseDTO.setRate(hotel.getString("hotelRate"));
    		hotelResponseDTO.setProvider("CrazyHotels");
    		
    		// Fare should be Per night and not for the whole stay.
    		if (hotel.containsKey("discount")) {
    			logger.trace("Applying Discount");
    			Double roomFareOld = new Double (hotel.getString("hotelFare"));
    			Double discount = new Double (hotel.getString("discount"));
        		hotelResponseDTO.setFare(String.valueOf(roomFareOld-discount));

    		}
    		
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
	private synchronized void morphRequest (SearchHotelRequestDTO requestDTO) throws ParseException {
		
		Date fromDateOld = new SimpleDateFormat("yyyy-MM-dd").parse(requestDTO.getFromDate());
		Date toDateOld = new SimpleDateFormat("yyyy-MM-dd").parse(requestDTO.getToDate());
		
		// Morphed Date Format
		SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		
		requestDTO.setFromDate(newFormat.format(fromDateOld));
		requestDTO.setToDate(newFormat.format(toDateOld));
		
		// Calculate the number of days.
		int numberOfDays = (int) ChronoUnit.DAYS.between(fromDateOld.toInstant(),toDateOld.toInstant());
		requestDTO.getAdditionalData().put("numberOfDays", numberOfDays);

	}
}
