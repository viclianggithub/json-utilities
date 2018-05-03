package com.appiancorp.ps.plugins.jsonutilities;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.appiancorp.suiteapi.common.exceptions.AppianException;
import com.appiancorp.suiteapi.common.exceptions.ErrorCode;
import com.appiancorp.suiteapi.expression.annotations.Category;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;

@Category("jsonCategory")
public class JsonFunctions {
	
	private static final Logger log = Logger.getLogger(JsonFunctions.class);
	
	public static int PRETTY_PRINT_INDENT_FACTOR = 4;

	@Function
	public static String jsonValidation(@Parameter String jsonText) {
		String jsonError = "";
		
	    try {
	    	new JSONObject(jsonText);
	    } catch (JSONException ex) {
	        try {
	            new JSONArray(jsonText);
	        } catch (JSONException ex1) {
	            jsonError = ex1.getMessage();
	        }
	    }
	    return jsonError;
	}
	
	@Function
	public static boolean isJSONValid(@Parameter String jsonText) {
	    return !(jsonValidation(jsonText).length()>0);
	}

	@Function
	public static String jsonToPrettyPrint(@Parameter String jsonText, @Parameter Integer indentCount) throws AppianException {
		try {
			JSONObject jsonObject = new JSONObject(jsonText);
			return jsonObject.toString(indentCount);
		} catch (JSONException ex) {
			try {
				JSONArray jsonArray = new JSONArray(jsonText);
				return jsonArray.toString(indentCount);
			} catch (JSONException ex1) {
				log.error(ex1,ex1);
				throw new AppianException(ErrorCode.INVALID_JSON_OBJECT, ex1.getMessage());
			}
		}
	}
	
	@Function
	public static String jsonToXml(@Parameter String jsonText) throws AppianException {
		String xml = "";
		
		try {
			JSONObject jsonObject = new JSONObject(jsonText);
			xml = (String) XML.stringToValue(jsonText);
		} catch (JSONException ex) {
			log.error(ex,ex);
			throw new AppianException(ErrorCode.INVALID_JSON_OBJECT, ex.getMessage());
		}
		
		return xml;
	}
	
}
