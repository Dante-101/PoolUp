package com.example.firstapp;

import java.util.List;

import com.example.datatypes.Record;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


public class InteractiveArrayAdapter extends ArrayAdapter<Record> {

	private final List<Record> list;
	private final Activity context;

	public InteractiveArrayAdapter(Activity context, List<Record> list) {
		super(context, android.R.layout.simple_list_item_1, list);
		this.context = context;
		this.list = list;
	}

	static class ViewHolder {
		protected TextView text;
		protected CheckBox checkbox;
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
			
			viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
			viewHolder.checkbox
			.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					Model element = (Model) viewHolder.checkbox
							.getTag();
					element.setSelected(buttonView.isChecked());

				}
			});
			view.setTag(viewHolder);
			viewHolder.checkbox.setTag(list.get(position));
		} else {
			view = convertView;
			((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.text.setText(list.get(position).getUserId());
		//holder.text2.setText(list.get(position).
			//	getCurrentLat()+ "  "+list.get(position).getCurrentLong());
		holder.checkbox.setChecked(false);
		return view;
	}
}