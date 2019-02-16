package com.availablehotels.providers;

import java.util.List;

import com.availablehotels.app.dto.SearchHotelRequestDTO;
import com.availablehotels.app.dto.SearchHotelResponseDTO;

/**
 * 
 * @author Mohammad Izwayyed
 *
 */
public interface SearchProvider {

	public List<SearchHotelResponseDTO> findHotels(SearchHotelRequestDTO searchHotelRequestDTO);
	
}
