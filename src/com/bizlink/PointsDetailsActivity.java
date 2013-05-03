package com.bizlink;



import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
 
public class PointsDetailsActivity extends Activity {
 
  
 
    static final String TAG_PID = "pid";
    TextView txtProductName, txtBusinessName, txtCountry, txtTown, txtComments, txtNumberOfPoints, txtBusinessType;
    String pid;
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
 
    // single product url
    private static final String url_product_detials = "http://10.0.2.2/bizlink/get_points_details.php";
 

 
    
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
	// JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT_NAME = "productName";
    private static final String TAG_BUSINESS_NAME="businessName";
    private static final String TAG_BUSINESS_COUNTRY="businessCountry";
    private static final String TAG_NO_POINTS="numberOfPoints";
    private static final String TAG_BUSINESS_TOWN="businessTown";
    private static final String TAG_COMMENTS="comments";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_BUSINESS_TYPE="businessType";
	protected static final int PICK_CONTACT = 1001;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.points_details);
 
        // save button
       txtProductName=(TextView) findViewById(R.id.productName);
      // txtBusinessName, txtCountry, txtTown, txtComments, txtNumberOfPoints, txtBusinessType;
       txtBusinessName=(TextView) findViewById(R.id.businessName);
       txtCountry=(TextView) findViewById(R.id.country);
       txtTown=(TextView) findViewById(R.id.town);
       txtComments=(TextView) findViewById(R.id.comments);
       txtNumberOfPoints=(TextView) findViewById(R.id.numberPoints);
       txtBusinessType=(TextView) findViewById(R.id.businessType);
     
        // getting product details from intent
        Intent i = getIntent();
 
        // getting product id (pid) from intent
        pid = i.getStringExtra(TAG_PID);
 
        // Getting complete product details in background thread
        new GetProductDetails().execute();

    }
	/**
     * Background Async Task to Get complete product details
     * */
    class GetProductDetails extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PointsDetailsActivity.this);
            pDialog.setMessage("Loading points details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {
 
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("pid", pid));
 
                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_product_detials, "POST", params);
 
                        // check your log for json response
                        Log.d("Single Product Details", json.toString());
 
                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_PRODUCT); // JSON Array
 
                            // get first product object from JSON Array
                            JSONObject product = productObj.getJSONObject(0);
 
                            txtProductName=(TextView) findViewById(R.id.productName);
                            // txtBusinessName, txtCountry, txtTown, txtComments, txtNumberOfPoints, txtBusinessType;
                             txtBusinessName=(TextView) findViewById(R.id.businessName);
                             txtCountry=(TextView) findViewById(R.id.country);
                             txtTown=(TextView) findViewById(R.id.town);
                             txtComments=(TextView) findViewById(R.id.comments);
                             txtNumberOfPoints=(TextView) findViewById(R.id.numberPoints);
                             txtBusinessType=(TextView) findViewById(R.id.businessType);
                            // display product data in EditText
                             txtProductName.setText(product.getString(TAG_PRODUCT_NAME));
                             txtBusinessName.setText(product.getString(TAG_BUSINESS_NAME));
                             txtBusinessType.setText(product.getString(TAG_BUSINESS_TYPE));
                             txtCountry.setText(product.getString(TAG_BUSINESS_COUNTRY));
                             txtTown.setText(product.getString(TAG_BUSINESS_TOWN));
                             txtComments.setText(product.getString(TAG_COMMENTS));
                             txtNumberOfPoints.setText(product.getString(TAG_NO_POINTS));
                             
 
                        }else{
                        	
                        	showAlertDialogOk(PointsDetailsActivity.this,"No details","Can't find the full details, Get back shortly",false);
                            // product with pid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
        public void showAlertDialogOk(final Context context, String title, String message,
                Boolean status) {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
     
            // Setting Dialog Title
            alertDialog.setTitle(title);
     
            // Setting Dialog Message
            alertDialog.setMessage(message);
     
            if(status != null)
                // Setting alert dialog icon
                alertDialog.setIcon((status) ? R.drawable.checked3:R.drawable.error);
     
            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                	Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
            });
     
            // Showing Alert Message
            alertDialog.show();
        }
    }
 
    
}
