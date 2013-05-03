package com.bizlink;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class Help extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_layout);
		TextView txtHelp=(TextView) findViewById(R.id.helpId);
		txtHelp.setText(Html.fromHtml("The steps for using the app are: &#8226;Register (If new) and select the categories of products/deals you would like to be an agent to.<br>&#8226;Check on the deals that you have an idea of an interested party<br>&#8226;Send the deal to the individual.<br>&#8226;Upon the client taking up the product your points will be updated<br>&#8226;These points can be redeemed from the listing company(details provided). <br>Its that simple! For more check: http://www.bizlink.site40.net"));
	}

}
