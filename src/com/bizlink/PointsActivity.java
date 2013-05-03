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
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
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

public class PointsActivity extends ListActivity{

	

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
	ProgressDialog pDialog;
	
	JSONParser jParser=new JSONParser();
	
	ArrayList<HashMap<String,String>> subscriptionList;
	
    private static String url_all_products = "http://10.0.2.2/bizlink/get_points.php";
    
    private static final String TAG_SUCCESS = "success";
    
    private static final String TAG_MESSAGE="message";
    private static final String TAG_PRODUCTS = "product";
    static final String TAG_PID = "pid";
    static final String TAG_NAME = "name";
  static final String KEY_THUMB_URL = "thumb_url";
    
    static final String TAG_PRODUCT_ID = "productId";
    static final String TAG_NO_POINTS="numberOfPoints";
    static final String TAG_COMMENT="comments";
    static final String TAG_BUSINESS_NAME = "businessName";
   
    static final String TAG_PRODUCT_NAME = "productName";
    static final String TAG_BUSINESS_COUNTRY="businessCountry";
    static final String TAG_BUSINESS_TOWN="businessTown";
    static final String TAG_BUSINESS_TYPE="businessType";
    String pid;
    
    private JSONParser jsonParser;
    SessionManager session;
    SharedPreferences pref;
    
    PointsAdapter adapter;
    ListView list;
    
    JSONArray subscriptions=null;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product);
		
		jsonParser=new JSONParser();
		
        
        // getting product details from intent
        Intent i = getIntent();
 
        // getting product id (pid) from intent
       // pid = i.getStringExtra(TAG_PID);
        
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
	                String pid = ((TextView) view.findViewById(R.id.pointsId)).getText()
	                        .toString();
	 
	                Log.d("PID",pid);
	                // Starting new intent
	                Intent in = new Intent(getApplicationContext(),
	                        PointsDetailsActivity.class);
	                // sending pid to next activity
	                in.putExtra(TAG_PID, pid);
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
            pDialog = new ProgressDialog(PointsActivity.this);
            pDialog.setMessage("Checking your points. Please wait...");
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

        	
        	//new AlertDialogManager().showAlertDialog(SubscribedActivity.this,"Email",email,true);
        	
        	//new AlertDialogManager().showAlertDialog(SubscribedActivity.this,"Email",email,true);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            
            params.add( new BasicNameValuePair("email", email));
            
           // params.add(new BasicNameValuePair("pid",pid));
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
                                                                   
                        String points_id=c.getString(TAG_PID);
                        String number_points=c.getString(TAG_NO_POINTS);
                        String comments=c.getString(TAG_COMMENT);
                        String product_id=c.getString(TAG_PRODUCT_ID);
                        String business_name=c.getString(TAG_BUSINESS_NAME);
                        String product_name=c.getString(TAG_PRODUCT_NAME);
                        String business_country=c.getString(TAG_BUSINESS_COUNTRY);
                        String business_town=c.getString(TAG_BUSINESS_TOWN);
                        String business_type=c.getString(TAG_BUSINESS_TYPE);
                        String thumb_url=c.getString(KEY_THUMB_URL);
                       
                       // String logo=c.getString(TAG_LOGO);
                        
                     //  String imageUrl=URLDecoder.decode(c.getString(KEY_THUMB_URL));
                        
                        HashMap<String, String> map = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        
                        map.put(TAG_PRODUCT_ID, product_id);
                        map.put(TAG_BUSINESS_NAME, business_name);
                        map.put(TAG_PID, points_id);
                        map.put(TAG_NO_POINTS, number_points);
                        map.put(TAG_COMMENT, comments);
                        map.put(TAG_PRODUCT_NAME, product_name);
                        map.put(TAG_BUSINESS_COUNTRY, business_country);
                        map.put(TAG_BUSINESS_TOWN, business_town);
                        map.put(TAG_BUSINESS_TYPE, business_type);
                        map.put(KEY_THUMB_URL,thumb_url);
                        // adding HashList to ArrayList
                        subscriptionList.add(map);
                    }
                } else {
                	//pDialog.dismiss();
                	//finish();
                	//new AlertDialogManager().showAlertDialog(getApplicationContext(),"No Points",json.getString(TAG_MESSAGE), false);
                	
                    
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
                   if(subscriptionList.isEmpty())
                   {
                       new AlertDialogManager().showAlertDialog(PointsActivity.this,"No Points","You got zero points at the moment. Link up clients and earn", false);

                	  // finish();
                	                    	
                   }
                   else
                   {
            		list=(ListView)findViewById(R.id.list);
            		
            	
                  
                    adapter=new PointsAdapter(PointsActivity.this, subscriptionList);        
                   
                    // updating listview
                    setListAdapter(adapter);
                   }
                }
            });
 
        }
 
    }
    
    
}
