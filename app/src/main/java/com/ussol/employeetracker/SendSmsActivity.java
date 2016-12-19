/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  

package com.ussol.employeetracker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.ussol.employeetracker.filedialog.FileDialog;
import com.ussol.employeetracker.mail.Mail;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.utils.ShowAlertDialog;
import com.ussol.employeetracker.utils.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class SendSmsActivity extends Activity implements OnClickListener, OnCheckedChangeListener{
	
	Button btnSendSms , btnSendCancel;
	ImageButton btnSendAttachFile;
	EditText txtSendSmsContent;
	CheckBox chkSendSms,chkSendMail;
	User[] userParam;
	ArrayList<User> userListParam=null;
	String attachFileName="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms_send);
		 /** get code user tu intent*/ 
		getControl();
		settingListener();
  		Intent request = getIntent();
  		Bundle param = request.getExtras();
  		if (param != null) {
  			//userParam = (User[])param.getParcelableArray(MasterConstants.SEND_SMS_TAG);
  			userListParam= param.getParcelableArrayList(MasterConstants.SEND_SMS_TAG);
  			List<User> listUser = userListParam;
  			
  		}
	}

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get các control trên màn hình
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void getControl(){
    	txtSendSmsContent = (EditText)findViewById(R.id.txtSendSmsContent);
    	btnSendSms=(Button)findViewById(R.id.btnBack);
    	btnSendAttachFile =(ImageButton)findViewById(R.id.btnSendSmsAttachFile);
    	chkSendSms = (CheckBox)findViewById(R.id.chkSendSms);
    	chkSendMail = (CheckBox)findViewById(R.id.chkSendMail);
    	btnSendCancel=(Button)findViewById(R.id.btnSendCancel);
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * gán sự kiện cho các control
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void settingListener(){
    	txtSendSmsContent.setOnClickListener(this);
    	btnSendAttachFile.setOnClickListener(this);
    	chkSendMail.setOnCheckedChangeListener(this);
    	chkSendSms.setOnCheckedChangeListener(this);
    	btnSendSms.setOnClickListener(this);
    	btnSendCancel.setOnClickListener(this);
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * set nội dung tin nhắn
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public void setMessageContent(String value){
    	txtSendSmsContent.setText(value);
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * lấy nội dung tin nhắn
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    
    public String getMessageContent(){
    	return txtSendSmsContent.getText().toString();
    }

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * Gửi message sử dụng SmSManager
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private void sendSmsManager(final ArrayList<User> userArr , final String message){
		
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		/** Setting Dialog Title */
        alertDialog.setTitle(getResources().getString(R.string.send_sms_titleInfo));

        /** Setting Dialog Message */
        alertDialog.setMessage("Bạn có muốn gửi tin nhắn cho các nhân viên không?");
        
        /** Setting Icon to Dialog*/
        alertDialog.setIcon(R.drawable.sendsms);
        /** Setting Positive "Yes" Button */
        alertDialog.setPositiveButton(getResources().getText(R.string.titleYes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	for(int i=0 ; i<userArr.size();i++){
        			try {
        				String phoneNo =  userArr.get(i).mobile;
        				if(phoneNo==null || phoneNo.equals("")){
        					//khong xu ly
        				}else{
        					SmsManager smsManager = SmsManager.getDefault();
        					smsManager.sendTextMessage(phoneNo, null, message, null, null);
        				}
        				
        			  } catch (Exception e) {
        				Toast.makeText(getApplicationContext(),"Không thể gửi tin nhắn cho " + userArr.get(i).full_name,
        					Toast.LENGTH_LONG).show();
        			  }
        		}
            	finish();

            }
        });
 
        /** Setting Negative "NO" Button */
        alertDialog.setNegativeButton(getResources().getText(R.string.titleNo), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
 
        /** Showing Alert Message */
        alertDialog.show();
        
		
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * Gửi message sử dụng build in application
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private void sendSmsBuildInApp(List<User> userList , String message){
		for(User usr : userList){
			try {
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);
			    sendIntent.putExtra("sms_body", "default content"); 
			    sendIntent.setType("vnd.android-dir/mms-sms");
			    startActivity(sendIntent);
				
			  } catch (Exception e) {
				Toast.makeText(getApplicationContext(),"Không thể gửi tin nhắn cho " + usr.full_name,
					Toast.LENGTH_LONG).show();
			  }
		}
	}
	
	private void sendMail(String subject , String emailContent, String[] recipents, String attachfile) throws Exception{
		/** đọc thông tin lưu trữ tại xml */
		//SharedPreferences prefs = getSharedPreferences(MasterConstants.PRE_SYSTEM_CONFIG_FILE, Context.MODE_PRIVATE);
		SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		String username = prefs.getString("config_EmailUser", "xuanhoa97");
		String password = prefs.getString("config_EmailPass", "xxx");
		String fromAddress = prefs.getString("config_EmailAccount", "xuanhoa97@gmail.com");

		//Mail m = new Mail(getApplicationContext(), username, password);
		Mail m  = new com.ussol.employeetracker.mail.Mail(this, username, password);
		String[] toArr = recipents ;
		m.setRecipients(toArr);
		if(attachfile==null || attachfile.equals("")){
		}else{
			m.addAttachment(attachfile);
		}
		
		m.setSender(fromAddress);
		m.setSubject(subject);
		m.setBody(emailContent);

		try {
			if (!(m.send())) {

			}
		} catch (Exception e) {
			Log.e("SEND MAIL",e.getMessage());
		}
	}
	
	private void sendMail2(final String subject , final String emailContent, final String[] recipents, final String attachfile) throws Exception{
		new AsyncTask<Void, Void, Boolean>()
	    {
	        ProgressDialog progressDialog;
	
	        @Override
			protected void onPreExecute() {
				progressDialog = new ProgressDialog(SendSmsActivity.this);
				progressDialog.setCancelable(false);
				progressDialog.setTitle("Gửi mail");
				progressDialog.setMessage("Xin vui lòng chờ...");
				progressDialog.show();
			}
	        @Override
	        protected Boolean doInBackground(Void... params)
	        {
	            try
	            {
	                /** gửi mail*/
	            	sendMail(subject,emailContent,recipents,attachfile);
	            	finish();
	            }
	            catch (Exception e)
	            {
	            	return false;
	            }
	            return true;
	        }
	
	        @Override
	        protected void onPostExecute(Boolean result)
	        {
	        	progressDialog.cancel();
	        	String message="";
				if (result==true){
					message="Gửi thành công.";
				}else{
					message="Gửi thất bại.Hãy thử lại.";
				}
	        	AlertDialog.Builder b = new AlertDialog.Builder(SendSmsActivity.this);
	            b.setTitle("Gửi mail");
	
	                b.setMessage(message);
	             
	                b.setPositiveButton(getString(android.R.string.ok),
	                        new DialogInterface.OnClickListener()
	                        {
	
	                            @Override
	                            public void onClick(DialogInterface dlg, int arg1)
	                            {
	                                dlg.dismiss();
	                            }
	                        });
	                b.create().show();
	            }
	        }.execute();
	}
		
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * onClick
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnBack:
				if(!getCheckedSmS() && !getCheckedEmail()){
					/** không có item nào được check */
					ShowAlertDialog.showTitleAndMessageDialog(this
							, getResources().getString(R.string.send_sms_titleInfo)
							, "Xin hãy chọn loại gửi tin nhắn hoặc mail.");
					break;
				}
				if(getMessageContent().equals("")){
					/** không có input nội dung */
					ShowAlertDialog.showTitleAndMessageDialog(this
							, getResources().getString(R.string.send_sms_titleInfo)
							, "Xin hãy nhập nội dung.");
					break;
				}
				if(getCheckedSmS()){
					/** gửi tin nhắn */
					sendSmsManager(userListParam, getMessageContent());
				}
				if(getCheckedEmail()){
					/** gửi mail */
					final String subject="Thong bao";
					final String emailContent = getMessageContent();
					
					ArrayList<String> arrListRec = new ArrayList<String>();
					//get cac dia chi mail can gui 
					for(int i=0 ; i<userListParam.size();i++){
						if(userListParam.get(i).email==null || userListParam.get(i).email.equals("")){
						}else{
							if(!Utils.validateEmail(userListParam.get(i).email)){
								/** địa chỉ mail sai*/
								ShowAlertDialog.showTitleAndMessageDialog(this
										, getResources().getString(R.string.send_sms_titleInfo)
										, "Địa chỉ email của nhân viên [" + userListParam.get(i).full_name + "] bị sai\n.Sau khi chỉnh sửa xong thì hãy thực hiện lại.");
								break;
							}else{
								arrListRec.add(userListParam.get(i).email);
							}
							
						}
					}
					if(arrListRec.size()==0){
						/** không có địa chỉ mail*/
						ShowAlertDialog.showTitleAndMessageDialog(this, getResources().getString(R.string.send_sms_titleInfo), "Các nhân viên đã chọn chưa nhập địa chỉ email.");
						break;
					}
					try {
						final String[] recipents = new String[arrListRec.size()];
						arrListRec.toArray(recipents);
						//allow auto network connect
						if(!isNetworkConnected(this)){
							enableInternet(getApplicationContext(), true);
							Handler handler = new Handler(); 
							handler.postDelayed(new Runnable() { 
								public void run() { 
									try {
										sendMail2(subject, emailContent, recipents, attachFileName);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							    } 
							}, 2000); 
						}else{
							AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
							/** Setting Dialog Title */
					        alertDialog.setTitle(getResources().getString(R.string.send_sms_titleInfo));

					        /** Setting Dialog Message */
					        alertDialog.setMessage("Bạn có muốn gửi email cho các nhân viên không?");
					        
					        /** Setting Icon to Dialog*/
					        alertDialog.setIcon(R.drawable.sendsms);
					        /** Setting Positive "Yes" Button */
					        alertDialog.setPositiveButton(getResources().getText(R.string.titleYes), new DialogInterface.OnClickListener() {
					            public void onClick(DialogInterface dialog,int which) {
					            	try {
					            		/**  gửi mail */
										sendMail2(subject, emailContent, recipents, attachFileName);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
					            	

					            }
					        });
					 
					        /** Setting Negative "NO" Button */
					        alertDialog.setNegativeButton(getResources().getText(R.string.titleNo), new DialogInterface.OnClickListener() {
					            public void onClick(DialogInterface dialog, int which) {
					            dialog.cancel();
					            }
					        });
					 
					        /** Showing Alert Message */
					        alertDialog.show();
						
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case R.id.btnSendCancel:
				finish();
				break;
			case R.id.btnSendSmsAttachFile:
				Intent intent = new Intent(getApplicationContext(), FileDialog.class);
				/** setting param cho file dialog */
				Bundle bundle = new Bundle();
				/** chi hien thi file *.txt */
				bundle.putStringArray(FileDialog.FORMAT_FILTER, new String[]{"txt",".zip","xls","xlsx","pdf","doc","docx"});
				intent.putExtras(bundle);
				startActivityForResult(intent, DatabaseToolActivity.DIALOG_LOAD_FILE);
				
				break;
			
		}	
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		final Intent finaldata=data;
		switch(requestCode){
			case DatabaseToolActivity.DIALOG_LOAD_FILE:
				if(resultCode==RESULT_OK){
					Bundle bundle = finaldata.getExtras();
					attachFileName = bundle.getString(FileDialog.RESULT_PATH);
				}
				
				break;
		}
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		switch (buttonView.getId()){
			case R.id.chkSendSms:
				if(isChecked){

				}else{
					
				}
				break;
			case R.id.chkSendMail:
				if(isChecked){
					btnSendAttachFile.setEnabled(true);
				}else{
					btnSendAttachFile.setEnabled(false);
				}
				break;
		}
		
	}
	
	public boolean getCheckedSmS(){
		return chkSendSms.isChecked();
	}
	
	public boolean getCheckedEmail(){
		return chkSendMail.isChecked();
	}
	
	private boolean isNetworkConnected(Context context) {         
		
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);         
		NetworkInfo network = cm.getActiveNetworkInfo();         
		if (network != null) {             
			return network.isAvailable();         
		}         
		
		return false;     
	} 

	private void enableInternet(Context context, boolean enable) {
		try {
			ConnectivityManager mgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE); 
			Method dataMtd;
			dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
			dataMtd.setAccessible(true); 
			dataMtd.invoke(mgr, enable);  
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}