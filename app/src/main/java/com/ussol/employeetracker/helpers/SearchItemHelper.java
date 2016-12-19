package com.ussol.employeetracker.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ShareActionProvider;

public class SearchItemHelper {
	private SharedPreferences settings ;
	private Context ctx;
	
	public SearchItemHelper(Context context){
		ctx = context;
	}
	public String getSearchItem(){
		String xWhere ="";
		if (settings==null){
		}else{
			
		}
		return xWhere;
	}
	public  SharedPreferences readSharedPreferencesSearchItem(){
		/** đọc thông tin lưu trữ tại xml */
		settings = ctx.getSharedPreferences("search_item_preferences", Context.MODE_PRIVATE);
		return settings;
	}
	
}
