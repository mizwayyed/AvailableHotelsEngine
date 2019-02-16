package com.availablehotels.app.services.commons;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * 
 * @author Mohammad Izwayyed
 *
 */
public class ServicesUtils {

    /**
     * 
     * @param errorCode
     * @return
     */
    public JsonObject generateErrorJSON(String errorCode) {
    	 JsonObject value = Json.createObjectBuilder().add("error", errorCode).build();
    	 return value;
    }
    
}
