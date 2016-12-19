package com.ussol.employeetracker.helpers;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ussol.employeetracker.mail.Mail;
/** Class su dung de gui mail */
public final class SendMail {
	static Context ctx=null;
	
	public SendMail(Context context){
		ctx=context;
	}
	/** gui mail ma khong co hien thi dialog */
	public static void sendMail(String subject , String emailContent, String[] recipents, String attachfile) throws Exception{
		/** đọc thông tin lưu trữ tại xml */
		//SharedPreferences prefs = getSharedPreferences(MasterConstants.PRE_SYSTEM_CONFIG_FILE, Context.MODE_PRIVATE);
		SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(ctx);
		
		String username = prefs.getString("config_EmailUser", "xuanhoa97");
		String password = prefs.getString("config_EmailPass", "???");
		String fromAddress = prefs.getString("config_EmailAccount", "xuanhoa97@gmail.com");
		
		Mail m = new Mail(ctx, username, password);

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
			throw e;
		}
	}
	/** gui mail co hien thi dialog */
	public static  void sendMail2(final String subject , final String emailContent, final String[] recipents, final String attachfile) throws Exception{
		new AsyncTask<Void, Void, Boolean>()
	    {
	        ProgressDialog progressDialog;
	
	        @Override
			protected void onPreExecute() {
				progressDialog = new ProgressDialog(ctx);
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
	        	AlertDialog.Builder b = new AlertDialog.Builder(ctx);
	            b.setTitle("Gửi mail");
	
	                b.setMessage(message);
	             
	                b.setPositiveButton("OK",
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

	public static boolean isNetworkConnected(Context context) {         
		
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);         
		NetworkInfo network = cm.getActiveNetworkInfo();         
		if (network != null) {             
			return network.isAvailable();         
		}         
		
		return false;     
	} 

	public static void enableInternet(Context context, boolean enable) {
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

	@TargetApi(22)
	@SuppressLint("NewApi")
	public static void setMobileNetworkfromLollipop(Context context) throws Exception {
	    String command = null;
	    int state = 0;
	    try {
	        // Get the current state of the mobile network.
	        state = isMobileDataEnabledFromLollipop(context) ? 0 : 1;
	        // Get the value of the "TRANSACTION_setDataEnabled" field.
	        String transactionCode = getTransactionCode(context);
	        // Android 5.1+ (API 22) and later.
	        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
	            SubscriptionManager mSubscriptionManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
	            // Loop through the subscription list i.e. SIM list.
	            for (int i = 0; i < mSubscriptionManager.getActiveSubscriptionInfoCountMax(); i++) {                    
	                if (transactionCode != null && transactionCode.length() > 0) {
	                    // Get the active subscription ID for a given SIM card.
	                    int subscriptionId = mSubscriptionManager.getActiveSubscriptionInfoList().get(i).getSubscriptionId();
	                    // Execute the command via `su` to turn off
	                    // mobile network for a subscription service.
	                    command = "service call phone " + transactionCode + " i32 " + subscriptionId + " i32 " + state;
	                    executeCommandViaSu(context, "-c", command);
	                }
	            }
	        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
	            // Android 5.0 (API 21) only.
	            if (transactionCode != null && transactionCode.length() > 0) {
	                // Execute the command via `su` to turn off mobile network.                     
	                command = "service call phone " + transactionCode + " i32 " + state;
	                executeCommandViaSu(context, "-c", command);
	            }
	        }
	    } catch(Exception e) {
	        // Oops! Something went wrong, so we throw the exception here.
	        throw e;
	    }           
	}
	
	@SuppressLint("NewApi")
	private static boolean isMobileDataEnabledFromLollipop(Context context) {
	    boolean state = false;
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
	        state = Settings.Global.getInt(context.getContentResolver(), "mobile_data", 0) == 1;
	    }
	    return state;
	}
	
	private static String getTransactionCode(Context context) throws Exception {
	    try {
	        final TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); 
	        final Class<?> mTelephonyClass = Class.forName(mTelephonyManager.getClass().getName());
	        final Method mTelephonyMethod = mTelephonyClass.getDeclaredMethod("getITelephony");
	        mTelephonyMethod.setAccessible(true);
	        final Object mTelephonyStub = mTelephonyMethod.invoke(mTelephonyManager);
	        final Class<?> mTelephonyStubClass = Class.forName(mTelephonyStub.getClass().getName());
	        final Class<?> mClass = mTelephonyStubClass.getDeclaringClass();
	        final Field field = mClass.getDeclaredField("TRANSACTION_setDataEnabled");
	        field.setAccessible(true);
	        return String.valueOf(field.getInt(null));
	    } catch (Exception e) {
	        // The "TRANSACTION_setDataEnabled" field is not available,
	        // or named differently in the current API level, so we throw
	        // an exception and inform users that the method is not available.
	        throw e;
	    }
	}
	
	private static void executeCommandViaSu(Context context, String option, String command) {
	    boolean success = false;
	    String su = "su";
	    for (int i=0; i < 3; i++) {
	        // Default "su" command executed successfully, then quit.
	        if (success) {
	            break;
	        }
	        // Else, execute other "su" commands.
	        if (i == 1) {
	            su = "/system/xbin/su";
	        } else if (i == 2) {
	            su = "/system/bin/su";
	        }       
	        try {
	            // Execute command as "su".
	            Runtime.getRuntime().exec(new String[]{su, option, command});
	        } catch (IOException e) {
	            success = false; 
	            // Oops! Cannot execute `su` for some reason.
	            // Log error here.
	        } finally {
	            success = true;
	        }
	    }
	}
	
}
