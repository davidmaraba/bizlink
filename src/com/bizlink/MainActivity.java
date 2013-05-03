package com.bizlink;





import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
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
	AlertDialogManager alert=new AlertDialogManager();
	ConnectionDetector cn;
	
	public SharedPreferences pref;
    SessionManager session;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		session=new SessionManager(getApplicationContext());
		
		//pref=new SharedPreferences
		
		setContentView(R.layout.dashboard_layout);
		
		Button btn_SubscribedHooks=(Button) findViewById(R.id.btn_subscribedHooks);
		Button btn_HotHooks=(Button) findViewById(R.id.btn_hotHooks);
		Button btn_HookAround=(Button) findViewById(R.id.btn_HooksAround);
		Button btn_Points=(Button) findViewById(R.id.btn_Points);
		Button btn_About=(Button) findViewById(R.id.btn_About);
		
		Button logout=(Button) findViewById(R.id.btn_logout);
		
		
		
		//On clicking the about button
		
		btn_About.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent aboutIntent=new Intent(getApplicationContext(),AboutActivity.class);
				startActivity(aboutIntent);
				
			}
		});
		btn_HotHooks.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent in=new Intent(getApplicationContext(), FeaturedActivity.class);
			startActivity(in);
				
			}
		});
		
		btn_SubscribedHooks.setOnClickListener(new View.OnClickListener() {
			
			

			@Override
			public void onClick(View v) {
				
				cn=new ConnectionDetector(getApplicationContext());
				
				
				if(cn.isConnectingToInternet())
				{
					
					//alert.showAlertDialog(MainActivity.this,"Connected","Phone connected to internet",true);
					Intent in=new Intent(getApplicationContext(),SubscribedActivity.class);
					startActivity(in);
					
				
						
					
				}
				else
				{
					alert.showAlertDialog(MainActivity.this, "Internet Connection","Make sure your internet connection is on before trying again.",false);
				}
				// TODO Auto-generated method stub
				
			}
		});
		btn_HookAround.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(getApplicationContext(), LinksAroundActivity.class);
                startActivity(i);
				// TODO Auto-generated method stub
				
			}
		});
		
		btn_Points.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
            cn=new ConnectionDetector(getApplicationContext());
				
				
				if(cn.isConnectingToInternet())
				{
					
					//alert.showAlertDialog(MainActivity.this,"Connected","Phone connected to internet",true);
					Intent in=new Intent(getApplicationContext(),PointsActivity.class);
					startActivity(in);
					
				
						
					
				}
				else
				{
					alert.showAlertDialog(MainActivity.this, "Internet Connection","Make sure your internet connection is on before trying again.",false);
				}
			}
		});
		
	}
//Initiaing menu xml
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.menu, menu);
        return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch (item.getItemId()) {
		case R.id.logout:
			session.logoutUser();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
		
	}

}
