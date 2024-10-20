package com.mayflowertech.chilla.config;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@Configuration
public class JacksonFilterConfig {
	private static final Logger logger = LoggerFactory.getLogger(JacksonFilterConfig.class);
    private SimpleFilterProvider filterProvider;
    private final Set<String> addedFilters = new HashSet<>(); 
    
    private ObjectMapper mapper;
    
    public JacksonFilterConfig() {
        filterProvider = new SimpleFilterProvider()
                .addFilter("ApiResultFilter", SimpleBeanPropertyFilter.serializeAll());
        addedFilters.add("ApiResultFilter");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setFilterProvider(filterProvider);
    }

    @Bean
    public ObjectMapper objectMapper() {
        // Create a new ObjectMapper instance
    	if(this.mapper == null) {
            this.mapper = new ObjectMapper();
            // Set the filter provider on the ObjectMapper
            mapper.setFilterProvider(filterProvider);
            return mapper;    		
    	}else {
    		return this.mapper;
    	}
    }

    // Method to retrieve the global filter provider
    public SimpleFilterProvider getGlobalFilterProvider() {
        return filterProvider;
    }
    
    public void clearFilters() {
    	filterProvider = new SimpleFilterProvider()
                .addFilter("ApiResultFilter", SimpleBeanPropertyFilter.serializeAll());
    	addedFilters.clear();
    	addedFilters.add("ApiResultFilter");
    }
    
    public void applyFilters(String filterId, String... fields) {
    	 if (addedFilters.contains(filterId)) {
             logger.info("Warning: Filter with ID '" + filterId + "' already exists.");
         } else {
             filterProvider.addFilter(filterId, SimpleBeanPropertyFilter.filterOutAllExcept(fields));
             addedFilters.add(filterId);
             logger.info("Added filter '" + filterId + "' with fields: " + String.join(", ", fields));
         }
        // Add new filter for the specified filter ID
        filterProvider.addFilter(filterId, SimpleBeanPropertyFilter.filterOutAllExcept(fields));
        // Set the filter provider back on the ObjectMapper
        objectMapper().setFilterProvider(filterProvider);
    }
    
    public void printExistingFilters() {
        if (addedFilters.isEmpty()) {
            logger.info("No filters have been added.");
        } else {
            logger.info("Existing filters: " + String.join(", ", addedFilters));
        }
    }

}
