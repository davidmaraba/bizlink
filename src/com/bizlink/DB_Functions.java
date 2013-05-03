package com.bizlink;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class DB_Functions {
	
	private JSONParser jsonParser;
	
	private static String loginURL="http://www.bizlink.site40.net/index2.php";
	private static String registerURL="http://www.bizlink.site40.net/index2.php";
	
	private static String login_tag="login";
	private static String register_tag="register";
	
	
	//constructor
	public DB_Functions(){
		jsonParser=new JSONParser();
	}
	/**
	 * funcion make login request
	 * @param email
	 * @param password
	 */
public JSONObject loginAgent(String email, String password)
{
	//Building parameters
	List<NameValuePair> params=new ArrayList<NameValuePair>();
	params.add(new BasicNameValuePair("tag", login_tag));
	params.add(new BasicNameValuePair("email", email));
	params.add(new BasicNameValuePair("password", password));
	
	//return json
	JSONObject json=jsonParser.getJSONFromUrl(loginURL,params);
	
	//return json
	return json;
	
	
}
/**
 * function make Login Request
 * @param name
 * @param email
 * @param password
 * */

public JSONObject registerAgent(String name, String email, String password, String phone,String country, String town,
		String selection){
    // Building Parameters
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("tag", register_tag));
    params.add(new BasicNameValuePair("name", name));
    params.add(new BasicNameValuePair("email", email));
    params.add(new BasicNameValuePair("phone", phone));
    params.add(new BasicNameValuePair("country", country));
    params.add(new BasicNameValuePair("town", town));
    params.add(new BasicNameValuePair("password", password));
    params.add(new BasicNameValuePair("selection", selection));
    
    JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
    // return json
    return json;
}
}
