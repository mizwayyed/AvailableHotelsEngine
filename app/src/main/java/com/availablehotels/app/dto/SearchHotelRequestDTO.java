package com.availablehotels.app.dto;

import java.util.HashMap;
import java.util.Map;

public class SearchHotelRequestDTO {

	private String fromDate;
	private String toDate;
	private String cityCode;
	private Integer numberOfAdults;
	private Map additionalData = new HashMap();
	
	/**
	 * @return the fromDate
	 */
	public String getFromDate() {
		return fromDate;
	}
	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	/**
	 * @return the toDate
	 */
	public String getToDate() {
		return toDate;
	}
	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	/**
	 * @return the cityCode
	 */
	public String getCityCode() {
		return cityCode;
	}
	/**
	 * @param cityCode the cityCode to set
	 */
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	/**
	 * @return the numberOfAdults
	 */
	public Integer getNumberOfAdults() {
		return numberOfAdults;
	}
	/**
	 * @param numberOfAdults the numberOfAdults to set
	 */
	public void setNumberOfAdults(Integer numberOfAdults) {
		this.numberOfAdults = numberOfAdults;
	}
	/**
	 * @return the additionalData
	 */
	public Map getAdditionalData() {
		return additionalData;
	}
}
