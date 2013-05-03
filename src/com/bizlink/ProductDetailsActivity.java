package com.bizlink;



import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.Activity;
import android.app.ProgressDialog;
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
 
public class ProductDetailsActivity extends Activity {
 
    TextView txtName;
    TextView txtPrice,txtBusinessTown,txtBusinessCountry,txtBusinessName;
    TextView txtDesc;
    TextView txtCreatedAt, txtPhone;
    Button btnSave;
    Button btnDelete, btnPickContact;
 
    String pid;
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
 
    // single product url
    private static String url_all_products = "http://10.0.2.2/bizlink/get_subscriptions.php";
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

	private static final String url_product_detials = "http://10.0.2.2/bizlink/get_product_details.php";
 
    // url to update product
   // private static final String url_update_product = "http://10.0.2.2/AndroidHive/update_product.php";
 
    // url to delete product
   // private static final String url_delete_product = "http://10.0.2.2/AndroidHive/delete_product.php";
 
    
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";
    private static final String TAG_DESCRIPTION = "description";
	protected static final int PICK_CONTACT = 1001;
	private static final String TAG_BUSINESS_NAME="business_name";
	private static final String TAG_BUSINESS_COUNTRY="business_country";
	private static final String TAG_BUSINESS_TOWN="business_town";
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);
 
        // save button
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        
        btnPickContact=(Button) findViewById(R.id.do_phone_picker);
        
        txtPhone=(TextView) findViewById(R.id.invite_phone);
 
        
        txtPrice=(TextView) findViewById(R.id.inputPrice);
        txtDesc=(TextView) findViewById(R.id.inputDesc);
        txtName=(TextView) findViewById(R.id.inputName);
        
        txtBusinessCountry=(TextView) findViewById(R.id.Locations);
        txtBusinessName=(TextView) findViewById(R.id.businessName);
        // getting product details from intent
        Intent i = getIntent();
 
        // getting product id (pid) from intent
        pid = i.getStringExtra(TAG_PID);
 
        // Getting complete product details in background thread
        new GetProductDetails().execute();
 
        // save button click event
        
        btnPickContact.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				 Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
	             startActivityForResult(intent, PICK_CONTACT);
				
			}
		});
        btnSave.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View arg0) {
            	String businessName=txtBusinessName.getText().toString();
            	String location=txtBusinessCountry.getText().toString();
            	String phoneNo = txtPhone.getText().toString();
            	String pricing=txtPrice.getText().toString();
            	String description=txtDesc.getText().toString();
            	String productName=txtName.getText().toString();
				String sms ="Product:".concat(productName).concat(" Price: ").concat(pricing).concat(" Description: ").concat(description)
						.concat("BizLink http://www.bizlink.com");

				try {
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(phoneNo, null, sms, null, null);
					Toast.makeText(getApplicationContext(), "SMS Sent!",
							Toast.LENGTH_LONG).show();
					//new AlertDialogManager().showAlertDialog(ProductDetailsActivity.this,"Referral Sent",sms,true);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							"SMS faild, please try again later!",
							Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
                // starting background task to update product
              //  new SaveProductDetails().execute();
            }
        });
 
        // Delete button click event
        btnDelete.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View arg0) {
                // deleting product in background thread
               // new DeleteProduct().execute();
            }
        });
 
    }
 
    @Override
	protected void onActivityResult(int reqCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(reqCode, resultCode, data);
		
		switch(reqCode)
		{
		   case (PICK_CONTACT):
		     if (resultCode == Activity.RESULT_OK)
		     {
		         Uri contactData = data.getData();
		         Cursor c = managedQuery(contactData, null, null, null, null);
		      if (c.moveToFirst())
		      {
		      String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

		      String hasPhone =
		      c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

		      if (hasPhone.equalsIgnoreCase("1")) 
		      {
		     Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, 
		      ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
		        phones.moveToFirst();
		        String cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		        Toast.makeText(getApplicationContext(), cNumber, Toast.LENGTH_SHORT).show();
		        //setCn(cNumber);
		        txtPhone.setText(cNumber);
		      }
		     }
		   }
		     else
		     {
		    	new AlertDialogManager().showAlertDialog(ProductDetailsActivity.this, "No contact", "Couldn't find", false); 
		     }
		}
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
            pDialog = new ProgressDialog(ProductDetailsActivity.this);
            pDialog.setMessage("Loading product details. Please wait...");
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
 
                            // product with this pid found
                            // Edit Text
                            txtName = (TextView) findViewById(R.id.inputName);
                            txtPrice = (TextView) findViewById(R.id.inputPrice);
                            txtDesc = (TextView) findViewById(R.id.inputDesc);
                            txtBusinessCountry=(TextView) findViewById(R.id.Locations);
                            txtBusinessName=(TextView) findViewById(R.id.businessName);
                            
                            String country=product.getString(TAG_BUSINESS_COUNTRY);
                            String town=product.getString(TAG_BUSINESS_TOWN).concat(",").concat(country);
  
                            // display product data in EditText
                            txtName.setText(product.getString(TAG_NAME));
                            txtPrice.setText(product.getString(TAG_PRICE));
                            txtDesc.setText(product.getString(TAG_DESCRIPTION));
                            txtBusinessCountry.setText(town);
                            txtBusinessName.setText(product.getString(TAG_BUSINESS_NAME));
 
                        }else{
                        	new AlertDialogManager().showAlertDialog(ProductDetailsActivity.this,"No details","Can't find the full details, Get back shortly",false);
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
    }
 
    
}
