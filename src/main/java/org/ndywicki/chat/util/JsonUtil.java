package org.ndywicki.chat.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

	private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
	
	private final static String EMPTY_STRING = "";
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	
	/** Convert <code>Object</code> to JSON format string
	 * @param object
	 * @return JSON string
	 */
	public static String ObjectToJson(Object object) {
        String json = EMPTY_STRING;
		try {
			json = mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {			
			logger.error("Error:", e);
		}		
		return json;
	}
	
	/**
	 * Convert JSON format string to generic class
	 * @param json
	 * @return <code>Data</code>
	 */	
	public static <T> T JsonStrToClass(String json, Class<T> valueType) {
		T resultType = null;
		try {
			resultType = (T) mapper.readValue(json, valueType);
		} catch (IOException e) {
			logger.error("Error:", e);
		}
		
		return (T) resultType;
	}
}
