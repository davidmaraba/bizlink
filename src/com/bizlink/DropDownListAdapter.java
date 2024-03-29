package com.bizlink;



import java.util.ArrayList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class DropDownListAdapter extends BaseAdapter {

	private ArrayList<String> mListItems;
	private LayoutInflater mInflater;
	private TextView mSelectedItems;
	private static int selectedCount = 0;
	private static String firstSelected = "";
	private ViewHolder holder;
	private static String selected = "";	//shortened selected values representation
	
	private String[] second=new String[5];
	
	public static String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		DropDownListAdapter.selected = selected;
	}

	public DropDownListAdapter(Context context, ArrayList<String> items,
			TextView tv) {
		mListItems = new ArrayList<String>();
		mListItems.addAll(items);
		mInflater = LayoutInflater.from(context);
		mSelectedItems = tv;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.drop_down_list_row, null);
			holder = new ViewHolder();
			holder.tv = (TextView) convertView.findViewById(R.DropDownList.SelectOption);
			holder.chkbox = (CheckBox) convertView.findViewById(R.DropDownList.checkbox);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv.setText(mListItems.get(position));

		final int position1 = position;
		
		//whenever the checkbox is clicked the selected values textview is updated with new selected values
		holder.chkbox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setText(position1);
			}
		});

		if(RegisterActivity.checkSelected[position])
			holder.chkbox.setChecked(true);
		else
			holder.chkbox.setChecked(false);	 
		return convertView;
	}


	/*
	 * Function which updates the selected values display and information(checkSelected[])
	 * */
	private void setText(int position1){
		if (!RegisterActivity.checkSelected[position1]) {
			RegisterActivity.checkSelected[position1] = true;
			selectedCount++;
		} else {
			RegisterActivity.checkSelected[position1] = false;
			selectedCount--;
		}

		if (selectedCount == 0) {
			mSelectedItems.setText(R.string.select_string);
		} else if (selectedCount == 1) {
			for (int i = 0; i < RegisterActivity.checkSelected.length; i++) {
				if (RegisterActivity.checkSelected[i] == true) {
					firstSelected = mListItems.get(i);
					break;
				}
			}
			mSelectedItems.setText(firstSelected);
			setSelected(firstSelected);
		} else if (selectedCount > 1) {
			for (int i = 0; i < RegisterActivity.checkSelected.length; i++) {
				if (RegisterActivity.checkSelected[i] == true) {
					firstSelected = mListItems.get(i);
					second[i]=mListItems.get(i);
					//break;
					//firstSelected =firstSelected.concat( mListItems.get(i));
				}
			}
			String items="";
			for(int i=0;i<second.length;i++)
			{
                            String g="";
                           // g=second[i];
				items+=second[i]+",";
			}
			mSelectedItems.setText(items);//firstSelected + " & "+ (selectedCount - 1) + " more"+items);
			setSelected(items);//firstSelected + " & "+ (selectedCount - 1) + " more"+items);
		}
	}

	private class ViewHolder {
		TextView tv;
		CheckBox chkbox;
	}
}

