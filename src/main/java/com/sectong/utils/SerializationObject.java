package com.sectong.utils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class SerializationObject {

	public static final <T> String serialize(T t) {

		if (t == null) {
			return null;
		}
		ObjectMapper mapper = JacksonMapper.getInstance();
		try {
			return mapper.writeValueAsString(t);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static final <T> T deserialize(String jsonStr, Class<T> clazz) {
		if (jsonStr == null) {
			return null;
		}
		ObjectMapper mapper = JacksonMapper.getInstance();
		try {
			return mapper.readValue(jsonStr, clazz);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
