package com.bizlink;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class Info extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_layout);
		TextView txtInfo=(TextView) findViewById(R.id.infoId);
		txtInfo.setText(Html.fromHtml("<font color='#0000ff'>BizLink</font> is mobile applications that simplifies the sales and marketing workload of companies by making interested app users their Agents.<br> It works in a link-reward scheme.<br>This is whereby the agents earn points for every successfull link of a client to a product or business.<br> These points are redeemable for the various awards set up by the listed businesses.<br> For more visit http://www.bizlink.site40.net"));
		
	}

}
