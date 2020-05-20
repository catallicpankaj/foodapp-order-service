package com.foodapp.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
@EnableMongoRepositories(basePackages = "com.foodapp.order.repo")
public class OrderServiceConfig {

	/**
	 * Bean for ObjectMapper class, to be used for JSON Processing.
	 * 
	 * @return object for ObjectMapper with all the pre set properties.
	 */
	@Bean("jacksonObjectMapper")
	public ObjectMapper jacksonObjectMapper() {
		ObjectMapper jacksonObjectMapper = new ObjectMapper();
		jacksonObjectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
		jacksonObjectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
		jacksonObjectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
		return jacksonObjectMapper;
	}
	
}
