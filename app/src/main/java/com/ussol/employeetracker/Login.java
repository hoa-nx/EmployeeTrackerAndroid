package com.ussol.employeetracker;

import com.ussol.employeetracker.helpers.SystemConfigItemHelper;
import com.ussol.employeetracker.models.MasterConstants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Login extends Activity implements OnClickListener {
	/** Called when the activity is first created. */

	EditText txtpass;
	Button btnOK, btnCancel;
	SystemConfigItemHelper sysConfig;
	String txtPw = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		/** init UI */
		Initialization();
		//sysConfig = new SystemConfigItemHelper(getApplicationContext());
		/** đọc thông tin lưu trữ tại xml */
		//SharedPreferences prefs = getSharedPreferences(MasterConstants.PRE_SYSTEM_CONFIG_FILE, Context.MODE_PRIVATE);
		/** lay mat ma login */
		//txtPw = sysConfig.getPassword();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		txtPw= prefs.getString("config_PassLogin", "12345");
	}

	private void Initialization() {
		// TODO Auto-generated method stub
		txtpass = (EditText) findViewById(R.id.txt_pass);
		btnOK = (Button) findViewById(R.id.btn_ok);
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		
		btnOK.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_ok:

			String pass = txtpass.getText().toString();
			/*
			SharedPreferences.Editor editit = sharedPreferences.edit();
			editit.putString("password", pass);
			editit.commit();
			*/
			if (pass==""){
				Toast.makeText(this, "Chưa input mật mã.", Toast.LENGTH_LONG).show();
				return;
			}
			
			if (pass.equals(txtPw)){
				//Main screen display
				Intent intent = new Intent(getBaseContext(),MainActivity.class);
				startActivity(intent);
				this.finish();
			}else{
				Toast.makeText(this, "Mật mã không đúng.Xin hãy thử lại.", Toast.LENGTH_LONG).show();
				txtpass.setText("");
			}
			break;
		case R.id.btn_cancel:
			this.finish();
			//android.os.Process.killProcess(android.os.Process.myPid());
			break;
		}
	}
}
