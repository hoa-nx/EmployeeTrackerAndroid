/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  
package com.ussol.employeetracker;

import java.util.List;

import com.ussol.employeetracker.models.Dept;
import com.ussol.employeetracker.models.Master;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DeptAdapter extends ArrayAdapter<Dept> {

	private Context context;
	private List<Dept> items;

	public DeptAdapter(Context context, int textViewResourceId,
			List<Dept> items) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(android.R.layout.simple_list_item_1, null);
		TextView textView = (TextView) view.findViewById(android.R.id.text1);
		textView.setText(items.get(position).name);

		return view;
	}
}
