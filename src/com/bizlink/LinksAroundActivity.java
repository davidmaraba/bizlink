package com.bizlink;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.api.client.http.UrlEncodedParser;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class LinksAroundActivity extends ListActivity{

	

	ProgressDialog pDialog;
	AlertDialogManager alert;
	JSONParser jParser=new JSONParser();
	
	ArrayList<HashMap<String,String>> subscriptionList;
	private static String url_all_products = "http://10.0.2.2/bizlink/businesses_around.php";

	//private static String url_all_products = "http://10.0.2.2/bizlink/businesses_around.php";
    
    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    static final String TAG_PID = "pid";
    static final String TAG_NAME = "name";
    static final String KEY_THUMB_URL = "thumb_url";
    
    static final String TAG_BUSINESS_NAME="business_name";
    static final String TAG_PRODUCT_NAME="product_name";
    static final String TAG_BUSINESS_COUNTRY="business_country";
   static final String TAG_LOGO="product_logo";
   static final String TAG_TOWN="business_town";
   static final String TAG_CATEGORY="product_category";
    
    
    private JSONParser jsonParser;
    SessionManager session;
    SharedPreferences pref;
    
    LinksAroundAdapter adapter;
    ListView list;
    
    JSONArray subscriptions=null;
    GetGPSLocation gps;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subscribed_layout);
		
		jsonParser=new JSONParser();
		
		gps=new GetGPSLocation(getApplicationContext());
		
		subscriptionList=new ArrayList<HashMap<String,String>>();
		
		session=new SessionManager(getApplicationContext());
		
        new LoadSubscriptions().execute();
        
        // Get listview
        ListView lv = getListView();
 
        // on seleting single product
        // launching Edit Product Screen
		  lv.setOnItemClickListener(new OnItemClickListener() {
			  
	            public void onItemClick(AdapterView<?> parent, View view,
	                    int position, long id) {
	                // getting values from selected ListItem
	                String pid = ((TextView) view.findViewById(R.id.pid)).getText()
	                        .toString();
	 
	                // Starting new intent
	                Intent in = new Intent(getApplicationContext(),
	                        ProductActivity.class);
	                // sending pid to next activity
	                in.putExtra(TAG_PID, pid);
	                
	                //Intent in=new Intent(getApplicationContext(),SubscribedActivity.class);
					//startActivity(in);
	 
	                // starting new activity and expecting some response back
	                startActivityForResult(in, 100);
	            }
	        });
	 
		
	}
    
    // Response from Edit Product Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
 
    }
    class LoadSubscriptions extends AsyncTask<String, String, String> {
    	 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LinksAroundActivity.this);
            pDialog.setMessage("Loading the business within range. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
        	
        	
        	
        	HashMap<String,String> user=session.getUserDetails();
			
			
			String email=user.get(SessionManager.KEY_EMAIL);

			double latl=((gps.getLatitude())-0.054008275);
			
			double lath=gps.getLatitude()+0.054008275;
			
			double longl=gps.getLongitude()-0.054008275;
			
			double longh=gps.getLongitude()+0.054008275;
			

        	
        	//new AlertDialogManager().showAlertDialog(SubscribedActivity.this,"Email",email,true);
        	
        	//new AlertDialogManager().showAlertDialog(SubscribedActivity.this,"Email",email,true);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            
            params.add( new BasicNameValuePair("latl",Double.toString(latl)));
            params.add(new BasicNameValuePair("lath",Double.toString(lath)));
            params.add(new BasicNameValuePair("longl",Double.toString(longl)));
            params.add(new BasicNameValuePair("longh",Double.toString(longh)));
            // getting JSON string from URL
            //JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);
 
            JSONObject json = jsonParser.getJSONFromUrl(url_all_products, params);
            // return json
           // return json;
            // Check your log cat for JSON reponse
            Log.d("The Subscriptions: ", json.toString());
 
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    subscriptions = json.getJSONArray(TAG_PRODUCTS);
 
                    // looping through All Products
                    for (int i = 0; i < subscriptions.length(); i++) {
                        JSONObject c = subscriptions.getJSONObject(i);
 
                    
                        // Storing each json item in variable
                        String id = c.getString(TAG_PID);
                        String name = c.getString(TAG_BUSINESS_NAME);
                        String productName=c.getString(TAG_PRODUCT_NAME);
                        String businessCountry=c.getString(TAG_BUSINESS_COUNTRY);
                        String logo=c.getString(TAG_LOGO);
                        String town=c.getString(TAG_TOWN);
                        String category=c.getString(TAG_CATEGORY);
                        //String imageUrl=c.rea
                       // URLEncodedUtils.format(params, "utf-8");
                        String imageUrl=URLDecoder.decode(c.getString(KEY_THUMB_URL));
                       
                        HashMap<String, String> map = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        map.put(TAG_PID, id);
                        map.put(TAG_BUSINESS_NAME, name);
                        map.put(TAG_BUSINESS_COUNTRY, businessCountry);
                        map.put(TAG_CATEGORY, category);
                        map.put(TAG_LOGO, logo);
                        map.put(TAG_PRODUCT_NAME, productName);
                        map.put(TAG_TOWN, town);
 
                        // adding HashList to ArrayList
                        subscriptionList.add(map);
                    }
                } else {
                	//alert=new AlertDialogManager();
                	//alert.showAlertDialog(LinksAroundActivity.this, "No Businesses", "There are no businesses within range at the moment", false);
                	Intent in=new Intent(getApplicationContext(),MainActivity.class);
                	
                	startActivity(in);
                	
                   // new AlertDialogManager().showAlertDialog(in, "No Businesses", "There are no businesses within range at the moment", false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                	for(int i=0;i<=subscriptionList.size();i++)
                	{
                		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                				LinearLayout.LayoutParams.WRAP_CONTENT);
                
                	}
                   
            		list=(ListView)findViewById(R.id.list);
            		
            		// Getting adapter by passing xml data ArrayList
                //  adapter=new LazyAdapter(new MainActivity(),subscriptionList);
                  
                    adapter=new LinksAroundAdapter(LinksAroundActivity.this, subscriptionList);        
                    
                    // updating listview
                    setListAdapter(adapter);
                }
            });
 
        }
 
    }
    
    
}
