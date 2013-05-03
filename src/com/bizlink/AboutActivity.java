package com.bizlink;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class AboutActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		TabHost tabHost=getTabHost();
		
		//Tab fo info
		TabSpec infoSpec=tabHost.newTabSpec("Info");
		//setting Title and icon for the tab
		infoSpec.setIndicator("Info",getResources().getDrawable(R.drawable.icon_info_tab));
		Intent infoIntent=new Intent(this,Info.class);
		infoSpec.setContent(infoIntent);
		
		//Tab for contacts
		TabSpec contactSpec=tabHost.newTabSpec("Contacts");
		contactSpec.setIndicator("Contacts",getResources().getDrawable(R.drawable.icon_contacts_tab));
		Intent contactIntent=new Intent(this,Contacts.class);
		contactSpec.setContent(contactIntent);
		
		
		//Tab for help
		TabSpec helpSpec=tabHost.newTabSpec("Help");
		helpSpec.setIndicator("Help",getResources().getDrawable(R.drawable.icon_help_tab));
		Intent helpIntent=new Intent(this,Help.class);
		helpSpec.setContent(helpIntent);
		
		//Adding all TabSpec to Tabhost
		tabHost.addTab(infoSpec);
		tabHost.addTab(contactSpec);
		tabHost.addTab(helpSpec);
		
	}

}
