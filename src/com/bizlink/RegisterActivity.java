package com.bizlink;
 
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.DTDHandler;

import com.bizlink.LoginActivity.LoadSubscriptions;


import android.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
 
public class RegisterActivity extends Activity {
	
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

	EditText name,email,password,phone,country,town,confirmPassword;
	Button register;
	
	
	SessionManager sessionManager;
	//JSON Response node names
	private static String KEY_SUCCESS="success";
	private static String KEY_ERROR="error";
	private static String KEY_ERROR_MSG="error_msg";
	private static String KEY_EMAIL="email";
	private static String KEY_NAME="name";
	
	ConnectionDetector cn;
	
	private JSONParser jsonParser;
	
private static String registerURL="http://10.0.2.2/bizlink/index2.php";
	
	private static String login_tag="login";
	private static String register_tag="register";
	ProgressDialog pDialog;
	//drop down
	private PopupWindow pw;
	private boolean expanded; 		//to  store information whether the selected values are displayed completely or in shortened representatn
	public static boolean[] checkSelected;	// store select/unselect information about the values in the list
	
	String[] selection=new String[10];
	AlertDialogManager alert;
	
	String selected = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.register);
        
        sessionManager=new SessionManager(getApplicationContext());
        
        cn=new ConnectionDetector(getApplicationContext());
        alert=new AlertDialogManager();
        
        name=(EditText) findViewById(R.id.reg_fullname);
        email=(EditText) findViewById(R.id.reg_email);
        password=(EditText) findViewById(R.id.reg_password);
        phone=(EditText) findViewById(R.id.reg_Phone);
        country=(EditText) findViewById(R.id.reg_Country);
        town=(EditText) findViewById(R.id.reg_town);
        
        confirmPassword=(EditText) findViewById(R.id.reg_Confirmpassword);
        
        register=(Button) findViewById(R.id.btnRegister);
        
        
 
        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);
 
        //initialize the drop down
        initialize();
        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View arg0) {
            	Intent i=new Intent(getApplicationContext(), LoginActivity.class);
            	startActivity(i);
                                // Closing registration screen
                // Switching to Login Screen/closing register screen
                finish();
            }
        });
        
        register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				//new LoadSubscriptions().execute();
				String fullConfirmPassword=confirmPassword.getText().toString();
				String fullPassword=password.getText().toString();
				String fullEmail=email.getText().toString();
				
				String fullName=name.getText().toString();
      			String fullPhone=phone.getText().toString();
    			String fullCountry=country.getText().toString();
    			String fullTown=town.getText().toString();
    			
    			String selection=DropDownListAdapter.getSelected();
    			
    			String[] items=selection.split(",");
    			
    			if(!(cn.isConnectingToInternet())){
					alert.showAlertDialog(RegisterActivity.this,"No internet","Check your internet connection so as to register",false);

				}
    			else if(fullPassword.equals("")||fullEmail.equals("")||fullCountry.equals("")||fullPhone.equals("")||
						fullTown.equals("")||fullName.equals(""))
				{
					alert.showAlertDialog(RegisterActivity.this,"Empty field(s)","Fill all the field so as to register",false);

				}
    			else if(!(fullPassword.equals(fullConfirmPassword)))
				{
					alert.showAlertDialog(RegisterActivity.this, "Password", "Make sure that the passwords are similar", false);
				}
				else if(!(isEmailValid(fullEmail)))
				{
					alert.showAlertDialog(RegisterActivity.this,"Invalid email","Enter a valid email adderss and try again",false);
				}
				
			
				else if(fullPassword.length()<5)
				{
					alert.showAlertDialog(RegisterActivity.this,"Short password","The password length should be at least 5 characters",false);

				}
				else if(fullPhone.length()<8)
				{
					alert.showAlertDialog(RegisterActivity.this,"Invalid phone number","Enter a valid phone number to proceed",false);

				}
				else if(items.length==1&&!items[0].equals("insurance"))
				{
			alert.showAlertDialog(RegisterActivity.this,"No selection","Select atleast on product to subscribe to from the drop-down",false);

				}
				else
				{
					 // servercode comes here
                    Thread trd = new Thread(new Runnable() {

                        private String result;
						

						@Override
                        public void run() {
                            // TODO Auto-generated method stub
                            Looper.prepare();
                            String url = registerURL;

                            Log.e("Registration",
                                    "gone to connectin class with url "
                                            + url);

                            HttpResponse response = null;
                            // code to do the HTTP request
                            InputStream is = null;
                            Log.d("res",
                                    "in addListenerToButton func 5");
                            // Creating HTTP client
                            final HttpParams httpParams = new BasicHttpParams();
                            HttpConnectionParams.setConnectionTimeout(
                                    httpParams, 10000);
                            Log.d("res",
                                    "in addListenerToButton func 6");

                            HttpClient httpClient = new DefaultHttpClient(
                                    httpParams);
                            Log.d("res",
                                    "in addListenerToButton func 7");

                            // Creating HTTP Post
                            HttpPost httpPost = new HttpPost(url);
                            //Log.e("Registration", "go on..........with vic as. " + vic);
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            String fullName=name.getText().toString();
                			String fullEmail=email.getText().toString();
                			String fullPassword=password.getText().toString();
                			String fullPhone=phone.getText().toString();
                			String fullCountry=country.getText().toString();
                			String fullTown=town.getText().toString();
                			
                			String selection=DropDownListAdapter.getSelected();
                			
                			 params.add(new BasicNameValuePair("tag", register_tag));
                			    params.add(new BasicNameValuePair("name", fullName));
                			    params.add(new BasicNameValuePair("email", fullEmail));
                			    params.add(new BasicNameValuePair("phone", fullPhone));
                			    params.add(new BasicNameValuePair("country", fullCountry));
                			    params.add(new BasicNameValuePair("town", fullTown));
                			    params.add(new BasicNameValuePair("password", fullPassword));
                			    params.add(new BasicNameValuePair("selection", selection));

                			    
                			    
                            try {
                                httpPost.setEntity(new UrlEncodedFormEntity(
                                        params));
                            } catch (UnsupportedEncodingException e) {
                                // writing error to Log
                                e.printStackTrace();
                            }

                            try {
                                response = httpClient.execute(httpPost);

                                HttpEntity entity = response
                                        .getEntity();

                                is = entity.getContent();

                                // writing response to log
                                Log.d("res", "1 " + response.toString());

                            } catch (ConnectTimeoutException ctEx) {

                                Toast.makeText(getApplicationContext(),
                                        "Server not responding",
                                        Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                // writing exception to log
                                Log.d("res", "2 " + e.toString());
                                // e.printStackTrace();
                            }

                            try {
                                BufferedReader reader = new BufferedReader(
                                        new InputStreamReader(is,
                                                "iso-8859-1"), 8);
                                StringBuilder sb = new StringBuilder();
                                String line = null;
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line + "\n");
                                }
                                is.close();
                                
                                result = sb.toString().trim();
                                pDialog.dismiss();
                                
                                Toast.makeText(RegisterActivity.this, result,
                                        Toast.LENGTH_LONG).show();
                               // Log.d("res", "4" + result);
                                
                                if(result.equals(KEY_SUCCESS))
                                {
                                	sessionManager.logoutUser();
            						sessionManager.createLoginSession(fullPassword, fullEmail);
            						
            						//Launch Dashboard screen
            						Intent i=new Intent(getApplicationContext(),MainActivity.class);
            						
            						//Close all views before launching DSashboard
            						
            						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            						startActivity(i);
            						//close registration screen
            						//alert.showAlertDialog(RegisterActivity.this,"Successfully ","Registered", false);
            						finish();
                                }
                                else
                                	alert.showAlertDialog(RegisterActivity.this,"Not registered",result, false);
                                
                                
                               
                                // call the method that will return a
                                // value

                            } catch (Exception e) {
                                Log.e("log_tag",
                                        "Error converting result "
                                                + e.toString());
                            }
                            Looper.loop();

                        }

                    });
                    trd.start();
                    openDialog();
			}
			}
		});
    }
    
    private Handler closeHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (pDialog != null)
                pDialog.dismiss();

        }
    };

    public void openDialog() {
        // Open the dialog
        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setMessage(Html
                .fromHtml("<b>Please wait...</b><br/>Registering"));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        closeHandler.sendEmptyMessageDelayed(0, 12000);
        // show toast

    }
    /*
     * Function to set up initial settings: Creating the data source for drop-down list, initialising the checkselected[], set the drop-down list
     * */
    private void initialize(){
    	//data source for drop-down list
        final ArrayList<String> items = new ArrayList<String>();
    	items.add("insurance");
    	items.add("payTV");
    	items.add("car");
    	items.add("house");
    	

    	
    	
    	checkSelected = new boolean[items.size()];
    	//initialize all values of list to 'unselected' initially
    	for (int i = 0; i < checkSelected.length; i++) {
    		checkSelected[i] = false;
    	}

	/*SelectBox is the TextView where the selected values will be displayed in the form of "Item 1 & 'n' more".
    	 * When this selectBox is clicked it will display all the selected values 
    	 * and when clicked again it will display in shortened representation as before.
    	 * */
    	final TextView tv = (TextView) findViewById(R.DropDownList.SelectBox);
    	tv.setOnClickListener(new OnClickListener() {

    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			if(!expanded){
    				//display all selected values
    			
    			int flag = 0;
    			for (int i = 0; i < items.size(); i++) {
    				if (checkSelected[i] == true) {
    					 selected += items.get(i);
    					 selected += ", ";
    					selection[i]=items.get(i);
    					flag = 1;
    				}
    			}
    			if(flag==1)
    			tv.setText(selected);
    			expanded =true;
    			}
    			else{
    				//display shortened representation of selected values
    				tv.setText(DropDownListAdapter.getSelected());
    				expanded = false;
    			}
    		}
    	});
        
        //onClickListener to initiate the dropDown list
        Button createButton = (Button)findViewById(R.DropDownList.create);
        createButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initiatePopUp(items,tv);
			}
		});
      // return selected;
    }
    
    /*
     * Function to set up the pop-up window which acts as drop-down list
     * */
    private void initiatePopUp(ArrayList<String> items, TextView tv){
    	LayoutInflater inflater = (LayoutInflater)RegisterActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	
    	//get the pop-up window i.e.  drop-down layout
    	LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.pop_up_window, (ViewGroup)findViewById(R.id.PopUpView));
    	
    	//get the view to which drop-down layout is to be anchored
    	RelativeLayout layout1 = (RelativeLayout)findViewById(R.id.relativeLayout1);
    	pw = new PopupWindow(layout, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
    	
    	//Pop-up window background cannot be null if we want the pop-up to listen touch events outside its window
    	pw.setBackgroundDrawable(new BitmapDrawable());
    	pw.setTouchable(true);
    	
    	//let pop-up be informed about touch events outside its window. This  should be done before setting the content of pop-up
    	pw.setOutsideTouchable(true);
    	pw.setHeight(LayoutParams.WRAP_CONTENT);
    	
    	//dismiss the pop-up i.e. drop-down when touched anywhere outside the pop-up
    	pw.setTouchInterceptor(new OnTouchListener() {
    		
    		public boolean onTouch(View v, MotionEvent event) {
    			// TODO Auto-generated method stub
    			if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
    				pw.dismiss();
        			return true;    				
    			}
    			return false;
    		}
    	});
    	
    	//provide the source layout for drop-down
    	pw.setContentView(layout);
    	
    	//anchor the drop-down to bottom-left corner of 'layout1'
    	pw.showAsDropDown(layout1);
    	
    	//populate the drop-down list
    	final ListView list = (ListView) layout.findViewById(R.DropDownList.dropDownList);
    	DropDownListAdapter adapter = new DropDownListAdapter(this, items, tv);
    	list.setAdapter(adapter);
    }
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
