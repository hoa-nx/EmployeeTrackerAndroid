package com.ussol.employeetracker.services;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SmsAlarmService extends Service {
 
 String smsNumberToSend, smsTextToSend;

 @Override
 public void onCreate() {
  // TODO Auto-generated method stub
  
  Toast.makeText(this, "SmsAlarmService.onCreate()", Toast.LENGTH_LONG).show();
 }

 @Override
 public IBinder onBind(Intent arg0) {
  // TODO Auto-generated method stub
  Toast.makeText(this, "SmsAlarmService.onBind()", Toast.LENGTH_LONG).show();
  return null;
 }
 
 @Override
 public void onDestroy() {
  // TODO Auto-generated method stub
   super.onDestroy();
   Toast.makeText(this, "SmsAlarmService.onDestroy()", Toast.LENGTH_LONG).show();
 }

 @Override
 public void onStart(Intent intent, int startId) {
  // TODO Auto-generated method stub
  super.onStart(intent, startId);
  
  Bundle bundle = intent.getExtras();
       smsNumberToSend = (String) bundle.getCharSequence("extraSmsNumber");
       smsTextToSend = (String) bundle.getCharSequence("extraSmsText");
  
  Toast.makeText(this, "SmsAlarmService.onStart()", Toast.LENGTH_LONG).show();
  Toast.makeText(this,
         "MyAlarmService.onStart() with \n" +
         "smsNumberToSend = " + smsNumberToSend + "\n" +
         "smsTextToSend = " + smsTextToSend,
         Toast.LENGTH_LONG).show();
  
  SmsManager smsManager = SmsManager.getDefault();
  smsManager.sendTextMessage(smsNumberToSend, null, smsTextToSend, null, null);
 }

 @Override
 public boolean onUnbind(Intent intent) {
  // TODO Auto-generated method stub
  Toast.makeText(this, "SmsAlarmService.onUnbind()", Toast.LENGTH_LONG).show();
  return super.onUnbind(intent);
 }

}