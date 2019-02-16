package com.availablehotels.app.services.commons;

import org.apache.log4j.Logger;

/**
 * The <code>AVHotelsLogger</code> class is a <code>Logger</code>
 * wrapper.
 * 
 * <p>
 * This class generates an appropriate instance of 
 * <code>Logger</code> depending on configuration in
 * <code>log4j.properties</code> which should be used
 * in all Modules.
 * 
 * @author Mohammad Izwayyed
 * @version 1.0, Feb 16, 2019 
 * @since 1.0
 */

public class AVHotelsLogger {

	private final static Logger logger = Logger.getLogger(AVHotelsLogger.class);
	
	// Prevent direct instantiation 
	private AVHotelsLogger() {
		
	}
	
	/**
	 * Gets an instance of type <code>Logger</code>.
	 * 
	 * @return An instance of <code>Logger</code>.
	 */
	public static Logger getLogger(){
		return logger;
	}

}
