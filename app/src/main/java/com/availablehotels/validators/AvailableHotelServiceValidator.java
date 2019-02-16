package com.availablehotels.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.availablehotels.app.dto.SearchHotelRequestDTO;
import com.availablehotels.app.services.commons.AVHotelsLogger;

/**
 * 
 * @author Mohammad Izwayyed
 *
 */
public class AvailableHotelServiceValidator {

	public static final Logger avHotelLogger = AVHotelsLogger.getLogger();

	public String validate(SearchHotelRequestDTO searchRequestDTO) {
		
		avHotelLogger.info("Starting to Validate Request");
		
		if (searchRequestDTO.getFromDate() == null || "".equals(searchRequestDTO.getFromDate())) {
			avHotelLogger.error("FromDate is Empty");
			return "FromDate is Empty";
		}
		
		if (!isValidDateFormat(searchRequestDTO.getFromDate())) {
			avHotelLogger.error("Invalid FromDate Format");
			return "Invalid FromDate Format";
		}
		
		if (searchRequestDTO.getToDate() == null || "".equals(searchRequestDTO.getToDate())) {
			avHotelLogger.error("toDate is Empty");
			return "toDate is Empty";
		}
		
		if (!isValidDateFormat(searchRequestDTO.getToDate())) {
			avHotelLogger.error("Invalid toDate Format");
			return "Invalid toDate Format";
		}
		
		if (searchRequestDTO.getCityCode() == null || "".equals(searchRequestDTO.getCityCode())) {
			avHotelLogger.error("City IATA Code is Empty");
			return "City IATA Code is Empty";
		}

		if (!ResourceBundle.getBundle("ListOfCities").containsKey(searchRequestDTO.getCityCode())) {
			avHotelLogger.error("Invalid City IATA Code");
			return "Invalid City IATA Code";
		}
		
		return null;
	}
	
	private synchronized boolean isValidDateFormat(String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }

}
