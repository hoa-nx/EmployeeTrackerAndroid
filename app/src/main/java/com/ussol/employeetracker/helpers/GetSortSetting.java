package com.ussol.employeetracker.helpers;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.utils.Utils;

public class GetSortSetting {
	static SharedPreferences settings , systemconfig;
	
	static Context context;
	
	public GetSortSetting(Context ctx ){
		context=ctx;
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * lấy về SQL sort ( ORDER BY AAA,BBB,CCC)
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public static  String  getSortSQL(){
    	String xSort ="";
    	String xSortBy=" ASC ";
		int theFirstItemSort =0;

    	 /** đọc thông tin từ file preferences*/
    	settings = context.getSharedPreferences(MasterConstants.PRE_SORT_FILE, Context.MODE_PRIVATE);
    	systemconfig = context.getSharedPreferences(MasterConstants.PRE_SYSTEM_CONFIG_FILE, Context.MODE_PRIVATE);
		//settings.edit().remove(MasterConstants.PRE_SORT_FILE).commit();
		/*File file= new File("data/user/0/com.ussol.employeetracker/shared_prefs/sort_item_preferences.xml");  //here the package should be your activity package "com.attinad.shared"
		if(file.delete())
			Log.e("Shared", "File Deleted");
			///data/user/0/com.ussol.employeetracker/shared_prefs/sort_item_preferences.xml*/
		/** get thông tin cấu hình có cho phép sort theo tất cả item hay chỉ item đầu tiên */
		SystemConfigItemHelper configHelp = new SystemConfigItemHelper(context);
		theFirstItemSort = Integer.parseInt(configHelp.getTheFirstItemSort());

		ArrayList<String> mDataSorted = new ArrayList<String>();
		String xKey = "pre_SortItem_";
		for(int i=0 ; i<theFirstItemSort;i++){
			mDataSorted.add(settings.getString(xKey + Integer.valueOf(i).toString(), Utils.mListContent[i]));
		}
		xSortBy = " " + settings.getString("pre_SortBy", " ASC ");
		/** chuyển thành dạng SQL tương ứng với cột tại master user */
		for(int i=0 ; i<mDataSorted.size();i++){
			if (i==0){
				xSort = Utils.getDatabaseColumn(mDataSorted.get(i)) + xSortBy;
			}else{
				xSort =  xSort + "," + Utils.getDatabaseColumn(mDataSorted.get(i)) + xSortBy;
			}
		}
		return xSort;
	}
    
}
