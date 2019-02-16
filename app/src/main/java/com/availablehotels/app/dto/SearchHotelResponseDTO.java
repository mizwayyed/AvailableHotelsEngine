package com.availablehotels.app.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchHotelResponseDTO implements Comparable {

	private String provider;
	private String hotelName;
	private String rate;
	private String fare;
	private List<String> roomAmenities = new ArrayList<String>(); 
	private Map additionalData = new HashMap();
	
	/**
	 * @return the additionalData
	 */
	public Map getAdditionalData() {
		return additionalData;
	}

	/**
	 * @return the hotelName
	 */
	public String getHotelName() {
		return hotelName;
	}

	/**
	 * @param hotelName the hotelName to set
	 */
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	/**
	 * @return the rate
	 */
	public String getRate() {
		return rate;
	}

	/**
	 * @param rate the rate to set
	 */
	public void setRate(String rate) {
		this.rate = rate;
	}

	/**
	 * @return the fare
	 */
	public String getFare() {
		return fare;
	}

	/**
	 * @param fare the fare to set
	 */
	public void setFare(String fare) {
		this.fare = fare;
	}

	/**
	 * @return the roomAmenities
	 */
	public List<String> getRoomAmenities() {
		return roomAmenities;
	}

	/**
	 * @return the provider
	 */
	public String getProvider() {
		return provider;
	}

	/**
	 * @param provider the provider to set
	 */
	public void setProvider(String provider) {
		this.provider = provider;
	}

	@Override
	public int compareTo(Object other) {
		Double thisDouble = new Double(this.fare);
		Double otherDouble = new Double(this.fare);

		int comparefareThis = thisDouble.intValue();
		int compareFareOther = otherDouble.intValue();
		
		return comparefareThis-compareFareOther;
	}
}
