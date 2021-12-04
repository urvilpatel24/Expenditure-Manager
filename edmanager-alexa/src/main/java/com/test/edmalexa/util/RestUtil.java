package com.test.edmalexa.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RestUtil {

	private static final String URL = "http://3.91.248.38:80/edmapi";
	
	public static JsonObject call(String type, String url) throws Exception 
	{
		String u = URL+url;
		URL urlRequest = new URL(u);
	    String readLine = null;
	    HttpURLConnection conection = (HttpURLConnection) urlRequest.openConnection();
	    conection.setRequestMethod(type);
	    
	    System.out.println("URL : "+u);
	    
	    if (conection.getResponseCode() == HttpURLConnection.HTTP_OK) {
	        BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
	        StringBuffer response = new StringBuffer();
	        while ((readLine = in .readLine()) != null)
	            response.append(readLine);
	        in .close();
	        System.out.println("Response : " + response.toString());
	        
	        return ((new JsonParser()).parse(response.toString())).getAsJsonObject();
	    } 
	    else
	        throw new Exception("Exception in rest call");
	}
	
	public static JsonArray callArray(String type, String url) throws Exception 
	{
		String u = URL+url;
		URL urlRequest = new URL(u);
	    String readLine = null;
	    HttpURLConnection conection = (HttpURLConnection) urlRequest.openConnection();
	    conection.setRequestMethod(type);
	    
	    System.out.println("URL : "+u);
	    
	    if (conection.getResponseCode() == HttpURLConnection.HTTP_OK) {
	        BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
	        StringBuffer response = new StringBuffer();
	        while ((readLine = in .readLine()) != null)
	            response.append(readLine);
	        in .close();
	        System.out.println("Response : " + response.toString());
	        
	        return ((new JsonParser()).parse(response.toString())).getAsJsonArray();
	    } 
	    else
	        throw new Exception("Exception in rest call");
	}
}
