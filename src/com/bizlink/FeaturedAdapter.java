package com.bizlink;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FeaturedAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public FeaturedAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
        
    }

  

	public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_item, null);

        TextView pid = (TextView)vi.findViewById(R.id.pid); // title
        TextView name = (TextView)vi.findViewById(R.id.name); // artist name
        TextView businessName=(TextView) vi.findViewById(R.id.business);
        
        TextView duration = (TextView)vi.findViewById(R.id.duration); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
        
        
        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);
        
        // Setting all values in listview
        pid.setText(song.get(FeaturedActivity.TAG_PID));
        name.setText(song.get(FeaturedActivity.TAG_PRODUCT_NAME));
        businessName.setText(song.get(FeaturedActivity.TAG_BUSINESS_NAME).concat(",").concat(song.get(FeaturedActivity.TAG_BUSINESS_TOWN)));
       //duration.setText(song.get(FeaturedActivity.TAG_BUSINESS_TYPE));
        imageLoader.DisplayImage(song.get(FeaturedActivity.KEY_THUMB_URL), thumb_image);
        //imageLoader.DisplayImage(song.get(SubscribedActivity.KEY_THUMB_URL), thumb_image);
        
        return vi;
    }
}
