package com.ussol.employeetracker.utils;

import com.ussol.employeetracker.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class ShowAlertDialog {
	/* 
	   * Displays a Toast. The context parameter is filled with getApplicationContext() from the Activity 
	   * you're calling this from. Duration is with Toast.LENGTH_SHORT or Toast.LENGTH_LONG
	  */
	  public static void showToast(Context context, String message, int duration) {
	    Toast toast = Toast.makeText(context, message, duration);
	    toast.show();
	  }
	  
	  /*
	   * Displays a simple Dialog with an OK button. Used fot the common task of giving some information to the 
	   * user without switching to another Activity. Needs Activity context, ApplicationContext will make 
	   * the caller crash.
	   */
	@SuppressWarnings("deprecation")
	public static void showTitleAndMessageDialog(Context context, String title, String message) {
	    AlertDialog alert = new AlertDialog.Builder(context,R.style.CustomDialog).create();  
	    alert.setCancelable(false);
	    alert.setTitle(title);  
	    alert.setMessage(message);
	    alert.setButton("OK", new DialogInterface.OnClickListener() {  
	        @Override  
	        public void onClick(DialogInterface dialog, int which) {  
	            dialog.dismiss();                      
	        }  
	    });  
	    alert.show();
	  }
}
