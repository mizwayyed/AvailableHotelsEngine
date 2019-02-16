package com.availablehotels.providers;

import java.lang.reflect.Constructor;

public class SearchProviderFactory {

	@SuppressWarnings("all")
	public static SearchProvider createSearchProvider (String providerPath) throws Exception {
		
		SearchProvider provider = null;
        try {
            
            // Define and instantiate the Class
            Class clazz = Class.forName(providerPath);

            // Construct the class
            Constructor adapterConstrc = clazz.getConstructor();

            // get new Instance
            provider = (SearchProvider) clazz.newInstance();
            return provider;
            
        } catch (Exception e) {
            throw e;
        }
	}
}
