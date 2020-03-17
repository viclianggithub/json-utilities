package com.appiancorp.ps.plugin.jsonutilities;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import com.appiancorp.suiteapi.expression.annotations.Category;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

@Category("jsonCategory")
public class JsonFunctions {
	
	private static final Logger log = Logger.getLogger(JsonFunctions.class);
	
	public static int PRETTY_PRINT_INDENT_FACTOR = 4;

	@Function
	public static String jsonValidation(@Parameter String jsonText) {
		String jsonError = "";
		
		try{
			JsonParser parser = new JsonParser();
			parser.parse(jsonText);
		} catch(JsonSyntaxException jse){
			jsonError = jse.getMessage();
		}
		
	    return jsonError;
	}
	
	@Function
	public static boolean isJSONValid(@Parameter String jsonText) {
	    return !(jsonValidation(jsonText).length()>0);
	}

	@Function
	public static String jsonToPrettyPrint(@Parameter String jsonText) throws Exception {
		try{
            JsonObject jsonObject = new Gson().fromJson(jsonText, JsonObject.class);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(jsonObject);
        } catch (Exception ex){
        	log.error("Error setting the JSON to pretty print. Make sure that the input is a valid JSON",ex);
            throw new Exception("Error setting the JSON to pretty print. Make sure that the input is a valid JSON");
        }
	}
	
	@Function
    public String removeJsonNulls (@Parameter String jsonText) throws Exception {	
        try{
            JsonObject jsonObject = new Gson().fromJson(jsonText, JsonObject.class);
            Gson gson = new GsonBuilder().create();
            return gson.toJson(jsonObject);
        } catch (Exception ex){
        	log.error("Error cleaning JSON",ex);
            throw new Exception("Error cleaning JSON. Make sure that the input is a valid JSON");
        }
    }
	
	@Function
	public String removeJsonEmptyPairs (@Parameter String jsonText) throws Exception {
		try {
			Type type = new TypeToken<Map<String, Object>>() {}.getType();
			Map<String, Object> data = new Gson().fromJson(jsonText, type);

			for (Iterator<Map.Entry<String, Object>> it = data.entrySet().iterator(); it.hasNext();) {
			    Map.Entry<String, Object> entry = it.next();
			    if (entry.getValue() == null) {
			        it.remove();
			    } else if (entry.getValue() instanceof ArrayList) {
			        if (((ArrayList<?>) entry.getValue()).isEmpty()) {
			            it.remove();
			        }
			    }
			}
			return new GsonBuilder().setPrettyPrinting().create().toJson(data);
		} catch (Exception ex){
        	log.error("Error cleaning JSON",ex);
            throw new Exception("Error cleaning JSON. Make sure that the input is a valid JSON");
        }
		
	}
	
}
