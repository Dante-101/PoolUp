package com.example.firstapp;

import java.io.InputStream;
import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.datatypes.Record;
import com.gtranslate.Audio;
import com.gtranslate.Language;


public class InteractiveArrayAdapter extends ArrayAdapter<Record> {

	private final List<Record> list;
	private final Activity context;
	private LoaderActivity activity;
	String timef(double d) {
		double time = d;
		String amorpm="AM";
		int hour= (int)time/60; 
		if(time/60>=12){
			hour-=12;
			amorpm="PM";
		}
		int mins = (int)time%60;
		return hour+":"+mins+" "+amorpm;
		
	}
	public InteractiveArrayAdapter(Activity context, List<Record> list) {
		super(context, android.R.layout.simple_list_item_1, list);
		this.context = context;
		this.activity = (LoaderActivity)context;
		this.list = list;
	}

	static class ViewHolder {
		protected TextView text;
		protected CheckBox checkbox;
		protected Button button;
		//protected TextView text2;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.rowbuttonlayout, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) view.findViewById(R.id.label);
			viewHolder.button=(Button)view.findViewById(R.id.viewmap);
			viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
			viewHolder.checkbox
			.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					Record element = (Record) viewHolder.checkbox
							.getTag();
					viewHolder.button.setVisibility(View.VISIBLE);
					activity.clickUser(element);
					Log.i("User selected",element.getUserId());
					
				}
			});
			view.setTag(viewHolder);
			viewHolder.checkbox.setTag(list.get(position));
		} else {
			view = convertView;
			((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		double time = list.get(position).getTime();
		holder.text.setText(list.get(position).getUserId()+"\n"+timef(time));
		holder.checkbox.setChecked(false);
		return view;
	}
}