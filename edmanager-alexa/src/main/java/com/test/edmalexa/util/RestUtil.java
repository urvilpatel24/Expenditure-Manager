package com.test.edmalexa.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RestUtil {

	private static final String URL = "http://localhost:7777/edmapi";
	
	public static JsonObject call(String type, String url) throws Exception 
	{
		URL urlForGetRequest = new URL(URL+url);
	    String readLine = null;
	    HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
	    conection.setRequestMethod(type);
//	    conection.setRequestProperty("userId", "a1bcdef");
	    
	    if (conection.getResponseCode() == HttpURLConnection.HTTP_OK) {
	        BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
	        StringBuffer response = new StringBuffer();
	        while ((readLine = in .readLine()) != null)
	            response.append(readLine);
	        in .close();
	        System.out.println("Response : " + response.toString());
	        
	        return (new JsonParser()).parse(response.toString()).getAsJsonObject();
	    } 
	    else
	        throw new Exception("Exception in rest call");
	}
}
