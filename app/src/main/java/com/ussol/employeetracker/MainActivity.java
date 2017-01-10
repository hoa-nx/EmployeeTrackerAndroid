/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  
package com.ussol.employeetracker;

import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.Util;
import com.ussol.employeetracker.helpers.ConvertCursorToListString;
import com.ussol.employeetracker.helpers.DatabaseAdapter;
import com.ussol.employeetracker.models.*;
import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import android.graphics.Color;

import java.util.List;

/**
 * 
 * @author hoa-nx
 *
 */
public class MainActivity extends Activity {
	//tag
	private final static String TAG =MainActivity.class.getName();
	public static final String  KEY_TYPE = "type";
	//DB adapter
	DatabaseAdapter mDatabaseAdapter;
	ConvertCursorToListString mConvertCursorToListString;
	/** lưu tên của package chính */
	public static  String PACKAGE_NAME;
	private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 112; 
	private static String[] MULTIPLE_PERMISSION = {
		Manifest.permission.CAMERA,
		
		Manifest.permission.READ_PHONE_STATE,
		Manifest.permission.CALL_PHONE,
		Manifest.permission.USE_SIP,
		Manifest.permission.PROCESS_OUTGOING_CALLS,
		
		Manifest.permission.READ_CONTACTS,
		Manifest.permission.WRITE_CONTACTS,
		Manifest.permission.GET_ACCOUNTS,
		
		Manifest.permission.SEND_SMS,
		Manifest.permission.RECEIVE_SMS,
		Manifest.permission.READ_SMS,
		Manifest.permission.RECEIVE_WAP_PUSH,
		
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
        
	};
	private static int[] imageResources = new int[]{ R.drawable.searchitem, R.drawable.sort_list_small,
		R.drawable.gnome_system_config, R.drawable.backup };
	private BoomMenuButton bmb;
	private TextView txt_main_listuser_badge_count ;
	/** List  data  */
	private List<User> listUser;

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onCreate
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	try{
    		 	super.onCreate(savedInstanceState);
    		 	
				//ask for the permission in android M--START
                checkCameraPermission();
                checkContactPermission();
                checkSMSPermission();
                checkStoragePermission();
			    //CHECK QUYEN HAN --END

    		 	//AppLockManager.getInstance().enableDefaultAppLockIfAvailable(getApplication());
    		 	/** lưu tên của package */
    		 	PACKAGE_NAME = getApplicationContext().getPackageName();
    		 	//requestWindowFeature(Window.FEATURE_NO_TITLE);
    	        setContentView(R.layout.main);
				//boom menu
				//bmbMainMenuConfig();
    	        //create DB
    	        mDatabaseAdapter = new DatabaseAdapter(this);
    	        /** cap nhat kinh nghiem cho User*/
    	        mDatabaseAdapter.open();
    	        mDatabaseAdapter.updateKeikenByYear();
    	        mDatabaseAdapter.close();
    			//mDatabaseAdapter.open();
    			//insertDept();
    			//long id = mDatabaseAdapter.insertToEntryTable(list);
    			
    			//mDatabaseAdapter.close();
/*    			InitDatabase init = new InitDatabase(getBaseContext());
    			init.InitAllData();*/

				/* get control */
				getControl();

    			/** chức năng phòng ban */
    			Button btnDepartment = (Button) findViewById(R.id.main_deparment);
    			btnDepartment.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					/** lưu giữ chức năng nào được chọn*/
    					SharedVariables.CURRENT_TAB_INDEX = 0;
    					Intent settingsActivity = new Intent(getBaseContext(),MasterActivity.class);
    					Bundle bundleFunc = new Bundle();
    	    			bundleFunc.putInt(MasterConstants.MAIN_TO_TAB_CALL, 0);
    					settingsActivity.putExtras(bundleFunc);
    					/** gọi màn hình bao gồm các TAB phòng ban , nhóm , chức vụ */
    					startActivity(settingsActivity);
    				}
    			});
    			
    			/** chức năng nhóm */
    			Button btnTeam = (Button) findViewById(R.id.main_team);
    			btnTeam.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					SharedVariables.CURRENT_TAB_INDEX = 1;
    					Intent settingsActivity = new Intent(getBaseContext(),MasterActivity.class);
    					Bundle bundleFunc = new Bundle();
    	    			bundleFunc.putInt(MasterConstants.MAIN_TO_TAB_CALL, 1);
    					settingsActivity.putExtras(bundleFunc);
    					/** gọi màn hình bao gồm các TAB phòng ban , nhóm , chức vụ */
    					startActivity(settingsActivity);
    				}
    			});
    			
    			/** chức năng chức vụ */ 
    			Button btnPosition = (Button) findViewById(R.id.main_position);
    			btnPosition.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					SharedVariables.CURRENT_TAB_INDEX = 2;
    					Intent settingsActivity = new Intent(getBaseContext(),MasterActivity.class);
    					Bundle bundleFunc = new Bundle();
    	    			bundleFunc.putInt(MasterConstants.MAIN_TO_TAB_CALL, 2);
    					settingsActivity.putExtras(bundleFunc);
    					/** gọi màn hình bao gồm các TAB phòng ban , nhóm , chức vụ */
    					startActivity(settingsActivity);
    				}
    			});

    			/** chức năng add thêm user*/ 
    			Button btnAddUser = (Button) findViewById(R.id.main_user_add);
    			btnAddUser.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					Intent settingsActivity = new Intent(getBaseContext(),EditUserMainActivity.class);
    					/*Bundle bundleFunc = new Bundle();
    	    			bundleFunc.putInt(MasterConstants.MAIN_TO_TAB_CALL, 2);
    					settingsActivity.putExtras(bundleFunc);*/
    					/** gọi màn hình edit thông tin nhân viên */
    					startActivity(settingsActivity);
    				}
    			});
    			
    			/** chức năng thao tác dữ liệur*/ 
    			/*Button btnDatabaseTool = (Button) findViewById(R.id.main_backup);
    			btnDatabaseTool.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					Intent settingsActivity = new Intent(getBaseContext(),DatabaseToolActivity.class);
    					Bundle bundleFunc = new Bundle();
    	    			bundleFunc.putInt(MasterConstants.MAIN_TO_TAB_CALL, 2);
    					settingsActivity.putExtras(bundleFunc);
    					*//** gọi màn hình edit thông tin nhân viên *//*
    					startActivity(settingsActivity);
    				}
    			});*/
    			
    			/** chức năng tao data master*/ 
    			Button btnDatabaseTool = (Button) findViewById(R.id.main_master_data);
    			btnDatabaseTool.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					Intent settingsActivity = new Intent(getBaseContext(),MessageTemplateListActivity.class);
    					startActivity(settingsActivity);
    				}
    			});
    			
    			/** chức năng danh sách nhân viên */ 
    			Button btnUserList = (Button) findViewById(R.id.main_listuser);
    			btnUserList.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					Intent settingsActivity = new Intent(getBaseContext(),ListUserMainActivity.class);
    					/*Bundle bundleFunc = new Bundle();
    	    			bundleFunc.putInt(MasterConstants.MAIN_TO_TAB_CALL, 2);
    					settingsActivity.putExtras(bundleFunc);*/
    					/** gọi màn hình list thông tin nhân viên */
    					//startActivity(settingsActivity);
    					startActivityForResult(settingsActivity, MasterConstants.RESULT_CLOSE_ALL);
    				}
    			});
    			
    			/** chức năng danh sách nhân viên */ 
    			Button btnExp = (Button) findViewById(R.id.main_expandable);
    			btnExp.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					Intent settingsActivity = new Intent(getBaseContext(),ExpandableListUserActivity.class);
    					/*Bundle bundleFunc = new Bundle();
    	    			bundleFunc.putInt(MasterConstants.MAIN_TO_TAB_CALL, 2);
    					settingsActivity.putExtras(bundleFunc);*/
    					/** gọi màn hình list thông tin nhân viên */
    					//startActivity(settingsActivity);
    					startActivityForResult(settingsActivity, MasterConstants.RESULT_CLOSE_ALL);
    				}
    			});
    			
    			
    			/** chức năng report */ 
    			Button btnUserReport= (Button) findViewById(R.id.main_report);
    			btnUserReport.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					startGenerateReportActivity();
    				}
    			});
    			
    			/** chức năng quản lý thông báo */ 
    			Button btnNotifi= (Button) findViewById(R.id.main_notification);
    			btnNotifi.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					Intent intNotifi = new Intent(getBaseContext(), SwipeListViewActivity.class);
    					startActivity(intNotifi);
    				}
    			});
    			
    			/** chức năng lịch sử */
    			Button btnAddHistory= (Button) findViewById(R.id.main_history_department);
    			btnAddHistory.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					Intent hisInt = new Intent(getBaseContext(), HisUserMainActivity.class);
    					startActivity(hisInt);
    				}
    			});

				/** chức năng google drive */
				Button btnChart= (Button) findViewById(R.id.main_chart);
				btnChart.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent chartInt = new Intent(getBaseContext(), ChartActivity.class);
    					startActivity(chartInt);
					}
				});
    			

    			/** chức năng danh bạ */ 
    			Button btnContact= (Button) findViewById(R.id.main_search);
    			btnContact.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					Intent contactInt = new Intent(getBaseContext(), ContactListActivity.class);
    					startActivity(contactInt);
    				}
    			});

				/* hien thi so nhan vien */
				setBadgeCountListUser();

    	}catch (Exception e){
    		Log.v(TAG,e.getMessage());
    	}

    }

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * get data cho list
	 *
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public List<User> getListUser(String xWhere){
		/** chuyển đổi từ Cursor thành List */
		mConvertCursorToListString = new ConvertCursorToListString(this);
		listUser = mConvertCursorToListString.getUserList(xWhere);
		return listUser;
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     *  xử lý các báo cáo
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/  
    protected void startGenerateReportActivity() {
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			Intent generateReport = new Intent(this, GenerateReport.class);
			//startActivity(generateReport);
			startActivityForResult(generateReport, MasterConstants.RESULT_CLOSE_ALL);
		} else {
			Toast.makeText(this, "sdcard not available", Toast.LENGTH_LONG).show();
		}
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
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onCreateOptionsMenu
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.menu_search_item_settings:
        	Intent intent = new Intent(this, SearchItemMainActivity.class);
			startActivityForResult(intent,MasterConstants.CALL_SEARCH_ITEM_ACTIVITY_CODE);
            return true;
        case R.id.menu_sort_item_settings:
        	Intent intSort = new Intent(this, DragNDropListActivity.class);
			startActivityForResult(intSort , MasterConstants.CALL_SORT_ITEM_ACTIVITY_CODE);
            return true;
        case R.id.menu_config_item_settings:
			Intent intConfig = new Intent(this, SystemConfigPreferencesActivity.class);
			startActivityForResult(intConfig , MasterConstants.CALL_CONFIG_ITEM_ACTIVITY_CODE);
            return true;
        case R.id.menu_backup_item_settings:
			Intent intBak = new Intent(this, DatabaseToolActivity.class);
			startActivityForResult(intBak , MasterConstants.CALL_BACKUP_ITEM_ACTIVITY_CODE);
            return true;
            
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * onActivityResult
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		/* hien thi so nhan vien */
		setBadgeCountListUser();

		switch(resultCode){
			case MasterConstants.RESULT_CLOSE_ALL:
				setResult(MasterConstants.RESULT_CLOSE_ALL);
		        finish();
		        Intent intent = new Intent(getBaseContext(),Login.class);
				startActivity(intent);
				break;
		}
		switch(requestCode){
			case MasterConstants.CALL_USER_ACTIVITY_CODE:
				if (resultCode==RESULT_OK){
	    	     
				}
				break;
			case MasterConstants.CALL_SEARCH_ITEM_ACTIVITY_CODE:
				if (resultCode==RESULT_OK){

				}
				break;
			case MasterConstants.CALL_SORT_ITEM_ACTIVITY_CODE:
				if (resultCode==RESULT_OK){

				}
				break;
			case MasterConstants.CALL_CONFIG_ITEM_ACTIVITY_CODE:
				if (resultCode==RESULT_OK){

				}
				break;
			case MasterConstants.CALL_BACKUP_ITEM_ACTIVITY_CODE:
				if (resultCode==RESULT_OK){

				}
				break;
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		/* hien thi so nhan vien */
		//setBadgeCountListUser();
	}

	@Override
	protected void onResume() {
		super.onResume();
		/* hien thi so nhan vien */
		setBadgeCountListUser();
	}

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * get các control trên màn hình
	 *
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private void getControl(){
		txt_main_listuser_badge_count= (TextView) findViewById(R.id.txt_main_listuser_badge_count);
	}

	/**
	 * Hien thi so nhan vien
	 */
	private void setBadgeCountListUser(){
		int user_count =0;
		/** get list user */
		listUser = getListUser("");
		if(listUser!=null){
			user_count = listUser.size();
		}
		if(txt_main_listuser_badge_count!=null){
			txt_main_listuser_badge_count.setText(String.valueOf(user_count));
		}
	}

	/***
	 * kiem tra quyen doc va ghi file
	 */
	private void checkStoragePermission(){
		int permission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied");

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            	
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Permission to access the SD-CARD is required for this app.")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "Clicked");
                        makeRequestPermissions();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            } else {
                makeRequestPermissions();
            }
        }
	}
	

	/***
	 * kiem tra quyen contact
	 */
	private void checkContactPermission(){
		int permission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_CONTACTS);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to read contact denied");

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.READ_CONTACTS)) {
            	
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Permission to access the Contact is required for this app.")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "Clicked");
                        makeRequestPermissions();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            } else {
                makeRequestPermissions();
            }
        }
	}
	
	/***
	 * kiem tra quyen SMS
	 */
	private void checkSMSPermission(){
		int permission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.SEND_SMS);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to send sms denied");

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.SEND_SMS)) {
            	
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Permission to access the SMS is required for this app.")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "Clicked");
                        makeRequestPermissions();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            } else {
                makeRequestPermissions();
            }
        }
	}
	
	
	/***
	 * kiem tra quyen CAMERA
	 */
	private void checkCameraPermission(){
		int permission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to use CAMERA denied");

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.CAMERA)) {
            	
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Permission to access the Camera is required for this app.")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "Clicked");
                        makeRequestPermissions();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            } else {
                makeRequestPermissions();
            }
        }
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode,
	                                       String permissions[], int[] grantResults) {
	    switch (requestCode) {
	        case ASK_MULTIPLE_PERMISSION_REQUEST_CODE: {
	            if (grantResults.length == 0
	                    || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

	                Log.i(TAG, "Permission has been denied by user");
	                /*
	                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
	                alertDialog.setTitle("Quyền hạn");
	                alertDialog.setMessage("Do không được chấp nhận các quyền nên \n vài chức năng sẽ không hoạt động.");
	                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
	                    new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog, int which) {
	                            dialog.dismiss();
	                        }
	                    });
	                alertDialog.show();
	                */
	            } else {
	                Log.i(TAG, "Permission has been granted by user");
	            	
	            }
	            return;
	        }
	    }
	}
	
	/**
	 * yêu cầu quyền hạn
	 */
	protected void makeRequestPermissions() {
        ActivityCompat.requestPermissions(this,
                MULTIPLE_PERMISSION,
                ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
    }

	/*protected void bmbMainMenuConfig(){
		bmb = (BoomMenuButton) findViewById(R.id.bmb);
		assert bmb != null;
		bmb.setButtonEnum(ButtonEnum.SimpleCircle);
		bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_4_1);
		bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_4_1);

		for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++)
		{
			SimpleCircleButton.Builder builder = new SimpleCircleButton.Builder()
					.normalImageRes(getImageResource())
					.listener(new OnBMClickListener() {
						@Override
						public void onBoomButtonClick(int index) {
							// When the boom-button corresponding this builder is clicked.
							Toast.makeText(MainActivity.this, "Clicked " + index, Toast.LENGTH_SHORT).show();
						}
					})
					// Whether the image-view should rotate.
					.rotateImage(false)

					// Whether the boom-button should have a shadow effect.
					.shadowEffect(true)

					// Set the horizontal shadow-offset of the boom-button.
					.shadowOffsetX(20)

					// Set the vertical shadow-offset of the boom-button.
					.shadowOffsetY(0)

					// Set the radius of shadow of the boom-button.
					.shadowRadius(Util.dp2px(20))

					// Set the color of the shadow of boom-button.
					.shadowColor(Color.parseColor("#c5e26d"))

					// Set the image resource when boom-button is at normal-state.
					//.normalImageRes(R.drawable.jellyfish)

					// Set the image drawable when boom-button is at normal-state.
					//.normalImageDrawable(getResources().getDrawable(R.drawable.jellyfish, null))

					// Set the image resource when boom-button is at highlighted-state.
					//.highlightedImageRes(R.drawable.bat)

					// Set the image drawable when boom-button is at highlighted-state.
					//.highlightedImageDrawable(getResources().getDrawable(R.drawable.bat, null))

					// Set the image resource when boom-button is at unable-state.
					//.unableImageRes(R.drawable.butterfly)

					// Set the image drawable when boom-button is at unable-state.
					//.unableImageDrawable(getResources().getDrawable(R.drawable.butterfly, null))

					// Set the rect of image.
					// By this method, you can set the position and size of the image-view in boom-button.
					// For example, builder.imageRect(new Rect(0, 50, 100, 100)) will make the
					// image-view's size to be 100 * 50 and margin-top to be 50 pixel.
					//.imageRect(new Rect(Util.dp2px(10), Util.dp2px(10), Util.dp2px(70), Util.dp2px(70)))

					// Set the padding of image.
					// By this method, you can control the padding in the image-view.
					// For instance, builder.imagePadding(new Rect(10, 10, 10, 10)) will make the
					// image-view content 10-pixel padding to itself.
					//.imagePadding(new Rect(0, 0, 0, 0))

					// Whether the boom-button should have a ripple effect.
					.rippleEffect(true)

					// The color of boom-button when it is at normal-state.
					//.normalColor(Color.RED)

					// The color of boom-button when it is at highlighted-state.
					//.highlightedColor(Color.BLUE)

					// The color of boom-button when it is at unable-state.
					//.unableColor(Color.BLACK)

					// Whether the boom-button is unable, default value is false.
					.unable(false)

					// The radius of boom-button, in pixel.
					.buttonRadius(Util.dp2px(40));

			bmb.addBuilder(builder);
		}
	}*/

	private static int imageResourceIndex = 0;

	static int getImageResource() {
		if (imageResourceIndex >= imageResources.length) imageResourceIndex = 0;
		return imageResources[imageResourceIndex++];
	}

}
