package com.bizlink;

import org.json.JSONObject;

import com.bizlink.SubscribedActivity.LoadSubscriptions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
 
public class LoginActivity extends Activity {
   
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

	//TextView loginErrorMsg;
    //Email, password
    EditText txtUsername,txtPassword;
    
    //ProgressDialog p
    ProgressDialog pDialog;
    //login Button
    Button btnLogin;
    
    ConnectionDetector cn;
    //Alert Dialog Manager
    AlertDialogManager alert=new AlertDialogManager();
    
    //Session Manager Class
    SessionManager session;
    
    //JSON Response node names
    private static String KEY_SUCCESS="success";
    private static String KEY_ERROR="error";
    private static String KEY_ERROR_MSG="error_msg";
    private static String KEY_EMAIL="email";
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting default screen to login.xml
        
        cn=new ConnectionDetector(getApplicationContext());
        //session manager
       session=new SessionManager(getApplicationContext());
       
      
       if(session.isLoggedIn())
       {
    	   //setContentView(R.layout.dashboard_layout);
    	   Intent mainIn=new Intent(getApplicationContext(), MainActivity.class);
    	   startActivity(mainIn);
       }
       else
       {
    	   setContentView(R.layout.login);
       //Email,password input text
       txtUsername=(EditText) findViewById(R.id.email);
       txtPassword=(EditText) findViewById(R.id.password);
       
       Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
 
       btnLogin=(Button) findViewById(R.id.btnLogin);
       
       TextView registerScreen = (TextView) findViewById(R.id.link_to_register);
 
        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });
        
        btnLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				String username=txtUsername.getText().toString();
			   String password=txtPassword.getText().toString();
			   if (username.equals("")||password.equals("")) {
					new AlertDialogManager().showAlertDialog(LoginActivity.this,"Empty field(s)", "Enter your username and password to login", false);
			   }
			   else if(cn.isConnectingToInternet())
				{
					 new LoadSubscriptions().execute();
				}
				
				
				else
					new AlertDialogManager().showAlertDialog(LoginActivity.this,"No internet", "Ensure that you are connected to the internet before attempting again", false);
				
				
			}
			
		});
        
    }
    }
    
    class LoadSubscriptions extends AsyncTask<String, String, String> {

		@Override
		protected void onPostExecute(String result) {
			// dismiss the dialog after getting all products
            pDialog.dismiss();
            final String res=doInBackground();
    		final String username=txtUsername.getText().toString();
			final String password=txtPassword.getText().toString();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                	
                   
                	if(Integer.parseInt(res)==1)
					{
						//user successfully logged in
						//Store in shared prefference
						//JSONObject json_user=json.getJSONObject("user");
						
						//clearb the data in shared preference
						session.logoutUser();
					
						session.createLoginSession(password, username);
						
						Intent i=new Intent(getApplicationContext(), MainActivity.class);
						
						//close all views before launching dashboard
						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);
						
						//close the login screen
						finish();
						
					}
					else
						alert.showAlertDialog(LoginActivity.this,"Unsuccessful login.." ,"Enter the correct username and password", false);
                }
            });
 
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Loging in. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		public String doInBackground(String... params) {
			// TODO Auto-generated method stub
			//Get username,password to EditText
			String username=txtUsername.getText().toString();
			String password=txtPassword.getText().toString();
			DB_Functions dbFunctions=new DB_Functions();
			JSONObject json=dbFunctions.loginAgent(username, password);
			
			//check login response
			try
			{
					String res=json.getString(KEY_SUCCESS);
					return res;
					
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return null;
		}
    	
    }
}
