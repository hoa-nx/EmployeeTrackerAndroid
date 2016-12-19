/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  
package com.ussol.employeetracker;

import com.ussol.employeetracker.helpers.ConvertCursorToListString;
import com.ussol.employeetracker.helpers.DatabaseAdapter;
import com.ussol.employeetracker.models.Dept;
import com.ussol.employeetracker.models.SharedVariables;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DetailDepartmentActivity extends Activity {
	//tag
	private final static String TAG =DetailDepartmentActivity.class.getName();
	//DB adapter
	ConvertCursorToListString mConvertCursorToListString;
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onCreate
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try{
			super.onCreate(savedInstanceState);

			// Need to check if Activity has been switched to landscape mode
			// If yes, finished and go back to the start Activity
			//lưu màn hình ngang hay dọc
			SharedVariables.CURRENT_ORIENTATION = getResources().getConfiguration().orientation ;
			
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				finish();
				return;
			}
			setContentView(R.layout.activity_department_detail);
			
			Intent request = getIntent();
			Bundle bundle = request.getExtras();
			int code = bundle.getInt(DatabaseAdapter.KEY_CODE);
			
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				//String code = extras.getString("code");
				/*DetailDepartmentFragment detailFragment = (DetailDepartmentFragment) getFragmentManager()
						.findFragmentById(R.id.detailDepartmentFragment);*/
				
				Dept item =null;
				mConvertCursorToListString = new ConvertCursorToListString(getApplicationContext());
				item= mConvertCursorToListString.getDeptByCode(code);
				if ( item==null  ){
					/** setting trị init cho các item */
					initData();
				}else{
					/** hiển thị data lên màn hình */
					displayData(item);
				}
			}
			
			//Edit button event 
			Button btn = (Button) findViewById(R.id.btnDeptUpdate);
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(), EditDepartmentActivity.class);
					Bundle bundle = new Bundle();
					//lấy code của phòng ban
					TextView tv =(TextView) findViewById(R.id.txtDepartmentCode);
					bundle.putString(DatabaseAdapter.KEY_CODE, tv.getText().toString());
					//gán vào bundle để gửi cùng với intent
					intent.putExtras(bundle);
					//khởi tạo activity dùng để edit 
					startActivity(intent);
					
				}
			});
			
		}catch (Exception e){
			Log.v(TAG,e.getMessage());
		}
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * hiển thị init data ( toàn bộ rỗng)
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private void initData(){
		//String code = extras.getString("code");
		DetailDepartmentFragment detailFragment = (DetailDepartmentFragment) getFragmentManager()
				.findFragmentById(R.id.detailDepartmentFragment);
		detailFragment.setDepartmentCode("");
		detailFragment.setDepartmentName("");
		detailFragment.setDepartmentManager("");
		detailFragment.setDepartmentVice("");
		detailFragment.setDepartmentCreateDate("");
		detailFragment.setDepartmentNote("");
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * hiển thị init data ( từ DB )
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private void displayData(Dept item){
		//String code = extras.getString("code");
		DetailDepartmentFragment detailFragment = (DetailDepartmentFragment) getFragmentManager()
				.findFragmentById(R.id.detailDepartmentFragment);
		detailFragment.setDepartmentCode( String.valueOf(item.code));
		detailFragment.setDepartmentName(item.name);
		detailFragment.setDepartmentManager(item.yobi_text1);
		detailFragment.setDepartmentVice(item.yobi_text2);
		detailFragment.setDepartmentCreateDate(item.create_date);
		detailFragment.setDepartmentNote(item.note);
	}	
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onConfigurationChanged
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		//lưu màn hình ngang hay dọc
		SharedVariables.CURRENT_ORIENTATION = getResources().getConfiguration().orientation ;
	}
}