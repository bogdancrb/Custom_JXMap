package ro.bogdancrb.main;

//necessary components are imported
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.json.JSONException;
import org.json.JSONObject;

public class UserLocation 
{
	// essential URL structure is built using constants
	public static final String BASE_URL = "http://ip-api.com/json";
	public static final String GOOGLE_BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json";
	public static final String GOOGLE_APIKEY = "AIzaSyDtaXz9q5ngsJFcyn0CEbdTtNkb-OXkDwI";
	
	private static HttpGet get;
	private static CloseableHttpResponse response;
	private static HttpEntity entity;
	private static JSONObject ipInfo;
	private static JSONObject googleInfo;
	
	public static String userIP;
	public static String userCity;
	public static String userCountry;
	public static GeoPosition userLocation;
	
	// this object is used for executing requests to the (REST) API
	static CloseableHttpClient httpClient = HttpClients.createDefault();


	/**
	* 
	* Notes:
	* 
	* A JSON response of the form {"key":"value"} is considered a simple Java JSONObject.
	* To get a simple value from the JSONObject, use: <JSONObject identifier>.get<Type>("key");
	* 
	* A JSON response of the form {"key":{"key":"value"}} is considered a complex Java JSONObject.
	* To get a complex value like another JSONObject, use: <JSONObject identifier>.getJSONObject("key")
	* 
	* Values can also be JSONArray Objects. JSONArray objects are simple, consisting of multiple JSONObject Objects.
	* 
	* 
	*/


	// sendLiveRequest() function is created to request and retrieve the data
	public static void getUserInfo()
	{ 
		try 
		{
	 		// The following line initializes the HttpGet Object with the URL in order to send a request
	 		get = new HttpGet(BASE_URL);
	 		response = httpClient.execute(get);
			entity = response.getEntity();
			
			// the following line converts the JSON Response to an equivalent Java Object
			ipInfo = new JSONObject(EntityUtils.toString(entity));
			
			userIP = ipInfo.getString("query");
			userCity = ipInfo.getString("city");
			userCountry = ipInfo.getString("country");
			
			String googleurl = GOOGLE_BASE_URL + "?address=" + userCity + ",+" + userCountry + "&key=" + GOOGLE_APIKEY;
			setGoogleInfo(googleurl);
			
			userLocation = new GeoPosition(googleInfo.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat"), 
										   googleInfo.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
		} 
		catch (ClientProtocolException e) { e.printStackTrace(); }
		catch (IOException e) { e.printStackTrace(); } 
		catch (ParseException e) { e.printStackTrace(); } 
		catch (JSONException e) { e.printStackTrace(); }
	}
	
	public static void setGoogleInfo(String googleurl)
	{
		try
		{
			get = new HttpGet(googleurl);
	 		response =  httpClient.execute(get);
			entity = response.getEntity();
			
			// the following line converts the JSON Response to an equivalent Java Object
			googleInfo = new JSONObject(EntityUtils.toString(entity));
		}
		catch (ClientProtocolException e) { e.printStackTrace(); }
		catch (IOException e) { e.printStackTrace(); } 
		catch (ParseException e) { e.printStackTrace(); } 
		catch (JSONException e) { e.printStackTrace(); }
	}

	public static String getUserIP()
	{
		return userIP;
	}
	
	public static String getUserCity()
	{
		return userCity;
	}
	
	public static String getUserCountry()
	{
		return userCountry;
	}
	
	public static GeoPosition getUserCoordonates()
	{
		return userLocation;
	}
}