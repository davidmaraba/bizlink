package com.bizlink;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class Contacts extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_layout);
		TextView txtCon=(TextView) findViewById(R.id.contactsId);
		txtCon.setText(Html.fromHtml("Satisfied with our services?<br><font color='#0000ff'> BizLink</font> team is happy to hear from you. <br> <b>Website:</b> http://bizlink.site40.net<br> <b>Email:</b> david.maraba@gmail.com<br> <b>Phone: </b>+25472700120"));
	}

}
