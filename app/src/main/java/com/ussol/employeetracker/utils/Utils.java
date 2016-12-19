package com.ussol.employeetracker.utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.itextpdf.text.pdf.codec.Base64.InputStream;
import com.ussol.employeetracker.R;
import com.ussol.employeetracker.helpers.DatabaseAdapter;
import com.ussol.employeetracker.models.ChartItem;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.telephony.SmsManager;
import android.widget.Toast;

public class Utils {
	/**
	 * check trang thai ket noi mang 
	 * @param context
	 * @return
	 */
	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(cm.getActiveNetworkInfo() == null) {return false;}
		return cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}
	/**
	 * MD5
	 * @return
	 */
	public static String getMD5() {
		String toEnc = Calendar.getInstance().getTimeInMillis()+""+100000 + (int)(Math.random() * ((999999 - 100000) + 1));
		try {
			MessageDigest mdEnc = MessageDigest.getInstance("MD5"); 
			mdEnc.update(toEnc.getBytes(), 0, toEnc.length());
			return String.format("%1$032X", new BigInteger(1, mdEnc.digest())).toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * get so ngau nhien trong pham vi chi dinh
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandomNumberBetween(int min, int max) {
        Random foo = new Random();
        int randomNumber = foo.nextInt(max - min) + min;
        if(randomNumber == min) {
            // Since the random number is between the min and max values, simply add 1
            return min + 1;
        }
        else {
            return randomNumber;
        }
    }
	/**
	 * trinh tu sort
	 */
	public static String[] mListContent={	  
		"Phòng ban"
		, "Nhóm-tổ"
		, "Chức vụ"
		, "Mã nhân viên"
		, "Tên nhân viên"
		, "Ngày sinh"
		, "Giới tính"
		, "Ngày ký HĐLĐ"
		, "Ngày vào nhóm labor"
		, "Trình độ nhật ngữ"
		, "Ngày vào thử việc"
		, "Ngày nghỉ việc"
		, "Lương cơ bản"
		};
	/**
	 * trinh tu sort ung voi item trong DB
	 */
	public static String[] mListColumn = {
		   	DatabaseAdapter.KEY_DEPT
		,	DatabaseAdapter.KEY_TEAM
		,	DatabaseAdapter.KEY_POSITION
		,	DatabaseAdapter.KEY_CODE
		,	DatabaseAdapter.KEY_FULL_NAME
		,	DatabaseAdapter.KEY_BIRTHDAY
		,	DatabaseAdapter.KEY_SEX
		,	DatabaseAdapter.KEY_IN_DATE
		,	DatabaseAdapter.KEY_JOIN_DATE
		,	DatabaseAdapter.KEY_JAPANESE
		,	DatabaseAdapter.KEY_TRAINING_DATE
		,	DatabaseAdapter.KEY_OUT_DATE
		,	DatabaseAdapter.KEY_SALARY_NOTALOWANCE
		};
	
	private static int findPositionByValue(String value){
		int orgPosition =-1;
		for(int i=0 ; i< mListContent.length;i++){
			if(mListContent[i].equals(value)){
				orgPosition = i;
				break;
			}
		}
		return orgPosition;
	}
	/**
	 * get ten cot trong DB dua vao ten co trong mListColumn[]
	 * @param value
	 * @return
	 */
	
	public static String getDatabaseColumn(String value){
		int pos =findPositionByValue(value);
		if (pos==-1){
			return "";
		}else{
			String sortCol =mListColumn[pos]; 
			if (sortCol.equals(DatabaseAdapter.KEY_BIRTHDAY)){
				sortCol =" case when "+ DatabaseAdapter.KEY_BIRTHDAY + " is null then 1 else 0 end , date(COALESCE("+ sortCol + ",'1900-01-01')) "; 
			}
			if (sortCol.equals(DatabaseAdapter.KEY_IN_DATE)){
				sortCol =" case when "+ DatabaseAdapter.KEY_IN_DATE +" is null then 1 else 0 end ,  date(COALESCE("+ sortCol + ",'1900-01-01'))  "; 
			}
			if (sortCol.equals(DatabaseAdapter.KEY_JOIN_DATE)){
				sortCol =" case when "+ DatabaseAdapter.KEY_JOIN_DATE + " is null then 1 else 0 end ,  date(COALESCE("+ sortCol + ",'1900-01-01'))  "; 
			}
			if (sortCol.equals(DatabaseAdapter.KEY_TRAINING_DATE)){
				sortCol =" case when "+ DatabaseAdapter.KEY_TRAINING_DATE + " is null then 1 else 0 end ,  date(COALESCE("+ sortCol + ",'1900-01-01'))  "; 
			}
			if (sortCol.equals(DatabaseAdapter.KEY_OUT_DATE)){
				sortCol =" case when "+ DatabaseAdapter.KEY_OUT_DATE + " is null then 1 else 0 end ,  date(COALESCE("+ sortCol + ",'1900-01-01'))  "; 
			}
			//return mListColumn[pos];
			return sortCol;
		}
	}
	/**
	 * get first name cua nhan vien
	 * @param value
	 * @return
	 */
	public static String getFirstName(String value){
		int start = value.indexOf(' ');
	    int end = value.lastIndexOf(' ');

	    String firstName = "";
	    String middleName = "";
	    String lastName = "";

	    if (start >= 0) {
	        firstName = value.substring(0, start);
	        if (end > start)
	            middleName = value.substring(start + 1, end);
	        lastName = value.substring(end + 1, value.length());
	    }
	    return firstName;
	}
	/**
	 * get last name cua nhan vien
	 * @param value
	 * @return
	 */
	public static String getLastName(String value){
		int start = value.indexOf(' ');
	    int end = value.lastIndexOf(' ');

	    String firstName = "";
	    String middleName = "";
	    String lastName = "";

	    if (start >= 0) {
	        firstName = value.substring(0, start);
	        if (end > start)
	            middleName = value.substring(start + 1, end);
	        lastName = value.substring(end + 1, value.length());
	    }
	    return lastName;		
	}
	/**
	 * get middle name cua nhan vien
	 * @param value
	 * @return
	 */
	public static String getMiddleName(String value){
		int start = value.indexOf(' ');
	    int end = value.lastIndexOf(' ');

	    String firstName = "";
	    String middleName = "";
	    String lastName = "";

	    if (start >= 0) {
	        firstName = value.substring(0, start);
	        if (end > start)
	            middleName = value.substring(start + 1, end);
	        lastName = value.substring(end + 1, value.length());
	    }
	    return middleName;		
	}
	/**
	 * Kiem tra hop le cua dia chi mail
	 * @param email
	 * @return
	 */
	public static boolean validateEmail(String email) {

		Pattern pattern;
		Matcher matcher;
		String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();

	}
	/**
	 * chuyen doi URL thanh bitmap
	 */
	public static Bitmap convertURLtoBitmap(String src) {

        try {

                        URL url = new URL(src);
                                        
                        HttpURLConnection connection = (HttpURLConnection) url
                                        .openConnection();

                        connection.setDoInput(true);
                        connection.connect();

                        InputStream input = (InputStream) connection.getInputStream();
                        Bitmap myBitmap = BitmapFactory.decodeStream(input);

                        return myBitmap;

        }

        catch (IOException e) {

                        e.printStackTrace();
                        return null;

        }
	}
	/**
	 * Clone object
	 * @param o
	 * @return
	 */
	public static Object clone(Object o){
		Object clone = null;
		 
		  try
		  {
		     clone = o.getClass().newInstance();
		  }
		  catch (InstantiationException e)
		  {
		     e.printStackTrace();
		  }
		  catch (IllegalAccessException e)
		  {
		     e.printStackTrace();
		  }
		 
		  // Walk up the superclass hierarchy
		  for (Class obj = o.getClass();
		    !obj.equals(Object.class);
		    obj = obj.getSuperclass())
		  {
		    Field[] fields = obj.getDeclaredFields();
		    for (int i = 0; i < fields.length; i++)
		    {
		      fields[i].setAccessible(true);
		      try
		      {
		        // for each class/suerclass, copy all fields
		        // from this object to the clone
		        fields[i].set(clone, fields[i].get(o));
		      }
		      catch (IllegalArgumentException e){}
		      catch (IllegalAccessException e){}
		    }
		  }
		  return clone;
	}
	
	/**
	 * Ham so lam tron
	 * @param value
	 * Tri muon lam tron
	 * @param decimal
	 * So thap phan se lam tron
	 * @param mode
	 * Mode lam tron so thap phan
	 * @return
	 * Tri sau khi lam tron
	 */
	public static  double  Round(float value , int decimal, RoundingMode mode){
		//MathContext NDecimals = new MathContext(decimal , mode);
    	BigDecimal bdNumber = new BigDecimal(value);
    	bdNumber=bdNumber.setScale(decimal, mode);
    	return bdNumber.doubleValue();
	}
	/**
	 *  Ham so lam tron
	 * @param value
	 *  Tri muon lam tron
	 * @param decimal
	 * So thap phan se lam tron
	 * @param mode
	 * Mode lam tron so thap phan
	 * @return
	 * Tri sau khi lam tron
	 */
	public static  double  Round(double value , int decimal, RoundingMode mode){
		BigDecimal bdNumber = new BigDecimal(value);
    	bdNumber=bdNumber.setScale(decimal, mode);
    	return bdNumber.doubleValue();
    	
	}
	/**
	 * Xoa noi dung Preferences
	 * @param sharedPrefs
	 */
	public static void ClearSharedPreferences(SharedPreferences sharedPrefs){
		Editor editor = sharedPrefs.edit();
		editor.clear();
		editor.commit();
	}
	
	/**
	 * Resize image 
	 * http://stackoverflow.com/questions/4821488/bad-image-quality-after-resizing-scaling-bitmap
	 * @param bitmap
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap resizerBitmap(Bitmap bitmap,int newWidth,int newHeight) {    
	    Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Config.ARGB_8888);
	
	    float ratioX = newWidth / (float) bitmap.getWidth();
	    float ratioY = newHeight / (float) bitmap.getHeight();
	    float middleX = newWidth / 2.0f;
	    float middleY = newHeight / 2.0f;
	
	    Matrix scaleMatrix = new Matrix();
	    scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
	
	    Canvas canvas = new Canvas(scaledBitmap);
	    canvas.setMatrix(scaleMatrix);
	    canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
	
	    return bitmap;

    }
	
	/**
	 * Scales the provided bitmap to have the height and width provided.
	 * (Alternative method for scaling bitmaps
	 * since Bitmap.createScaledBitmap(...) produces bad (blocky) quality bitmaps.)
	 * 
	 * @param bitmap is the bitmap to scale.
	 * @param newWidth is the desired width of the scaled bitmap.
	 * @param newHeight is the desired height of the scaled bitmap.
	 * @return the scaled bitmap.
	 */
	 public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
	  Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Config.ARGB_8888);

	  float scaleX = newWidth / (float) bitmap.getWidth();
	  float scaleY = newHeight / (float) bitmap.getHeight();
	  float pivotX = 0;
	  float pivotY = 0;

	  Matrix scaleMatrix = new Matrix();
	  scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

	  Canvas canvas = new Canvas(scaledBitmap);
	  canvas.setMatrix(scaleMatrix);
	  canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

	  return scaledBitmap;
	}

	 public static byte[] bitmapToArray(Bitmap b){
		//calculate how many bytes our image consists of.
		 int bytes = b.getByteCount();
		 //or we can calculate bytes this way. Use a different value than 4 if you don't use 32bit images.
		 //int bytes = b.getWidth()*b.getHeight()*4; 

		 ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
		 b.copyPixelsToBuffer(buffer); //Move the byte data to the buffer

		 byte[] array = buffer.array(); //Get the underlying array containing the data.
		 return array;
	 }
	 
	 /**
	  * Lay ngay thang nam gio phu giay cua he thong de cap nhat tri khi tao moi
	  * hoac update SQLite
	  * @return
	  * Tra ve kieu ngay(Date)
	  */
	 public static Date getCurrentDateTime(){
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		 Date date = new Date();
		 dateFormat.format(date);
		 return date;
	 }
	 /**
	  * Lay ngay thang nam gio phu giay cua he thong de cap nhat tri khi tao moi
	  * hoac update SQLite
	  * @return
	  * Tra ve dang chuoi
	  */
	 public static String getCurrentDateTimeToString(){
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		 Date date = new Date();
		 dateFormat.format(date);
		 return date.toString();
	 }
	 /**
	  * Tim gia tri tuong ung key trong ChartItem
	  * @param keyItem
	  * @param db
	  * @return
	  */
	 public static String findValueItemFromChartItem(String keyItem,ArrayList<ChartItem> db){
	    	String value="0";
	    	for(ChartItem item : db){
	    		if(item.KeyItem!=null){
		    		if(item.KeyItem.equalsIgnoreCase(keyItem)){
		    			value = item.ValueItem;
		    			break;
		    		}
	    		}
	    	}
	    	return value;
	    }
	 /**
	 * Kiem tra xem ngay hien tai co phai la ngay sinh nhat ?
	 * @param birthday
	 * @return
	 */
	 public static boolean isCurrentDateBirthday(String birthday){
		 if(birthday==null || birthday.equals("") ){
			 return false;
		 }
			 
		//get current date
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String formattedDate = df.format(c.getTime());
		Date birthDate = null ;
		if(DateTimeUtil.isDate(birthday, MasterConstants.DATE_VN_FORMAT)){
			try {
				birthDate = df.parse(birthday);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//birthDate = DateTimeUtil.convertStringToDate(birthday, MasterConstants.DATE_VN_FORMAT);
			if(birthDate!=null){
				//DateTime dtBirthday = new DateTime(Calendar.getInstance().get(Calendar.YEAR), birthDate.getMonth(), birthDate.getDay(), 0, 0);
				//DateTimeFormatter fmt = ISODateTimeFormat.yearMonthDay();
	        	
				//birthDate=birthDate.setYear(c.get(Calendar.YEAR));
				//kiem tra xem co dung la ngay sinh nhat khong
				if(formattedDate.substring(0, 5).equals(df.format(birthDate).substring(0, 5))){
					//hom nay la ngay sinh nhat
					return true;
				}
			}
			
		}
		return false;
	}
	 /**
	 * Kiem tra xem ngay hien tai co phai la ngay 
	 * giong voi ngay truyen vao ?
	 * @param birthday
	 * @return
	 */
	 public static boolean isDateMatch(String dateCompared){
		 if(dateCompared==null || dateCompared.equals("") ){
			 return false;
		 }
		//get current date
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String formattedDate = df.format(c.getTime());
		Date matchDate = null ;
		if(DateTimeUtil.isDate(dateCompared, MasterConstants.DATE_VN_FORMAT)){
			try {
				matchDate = df.parse(dateCompared);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(matchDate!=null){
				if(formattedDate.equals(df.format(matchDate))){
					//hom nay la ngay sinh nhat
					return true;
				}
			}
			
		}
		return false;
	}
		  
	/**
	 * Kiem tra xem thang dua vao co phai la thang xet luong ?
	 * @param salaryMonth
	 * @return
	 */
	public static boolean isCurentMonthSalaryConfirmed(String salaryMonth){
		if(salaryMonth==null || salaryMonth.equals("") ){
			 return false;
		 }
		//get current date
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = df.format(c.getTime());
		Date currentMonth = DateTimeUtil.convertStringToDate(formattedDate, MasterConstants.DATE_JP_FORMAT);
		currentMonth.setDate(1);
		
		Date salaryDate ;
		if(DateTimeUtil.isDate(salaryMonth, MasterConstants.DATE_JP_FORMAT)){
			salaryDate = DateTimeUtil.convertStringToDate(salaryMonth, MasterConstants.DATE_JP_FORMAT);
			salaryDate.setDate(1);
			//Thang hien tai co phai la thang xet luong khong
			
			if(currentMonth.equals(salaryDate)){
				//La thang can xet luong
				return true;
			}
		}
		return false;
	}
	/**
	 * Gui SMS
	 * @param phonenumber
	 * @param message
	 * @param isBinary
	 */
	public static void sendSmsMessage(String phonenumber,String message, String fromPhone){
		
		SmsManager.getDefault().sendTextMessage(phonenumber, fromPhone, message, null,null);
	}
	
	public static void sendSMS2(Context ctx, String phonenumber,String message, String fromPhone){
		ContentValues values = new ContentValues(); 
		values.put("address", phonenumber); 
		values.put("body",message); 
		ctx.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
	}
	public static void sendSMS(final Context ctx ,String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(ctx, 0, new Intent(
                SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(ctx, 0,
                new Intent(DELIVERED), 0);

        // ---when the SMS has been sent---
        ctx.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                case Activity.RESULT_OK:
                    /*ContentValues values = new ContentValues();
                    for (int i = 0; i < MobNumber.size() - 1; i++) {
                        values.put("address", MobNumber.get(i).toString());// txtPhoneNo.getText().toString());
                        values.put("body", MessageText.getText().toString());
                    }
                    ctx.getContentResolver().insert(
                            Uri.parse("content://sms/sent"), values);
                    Toast.makeText(ctx, "SMS sent",
                            Toast.LENGTH_SHORT).show();*/
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(ctx, "Generic failure",
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Toast.makeText(ctx, "No service",
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Toast.makeText(ctx, "Null PDU",
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Toast.makeText(ctx, "Radio off",
                            Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }, new IntentFilter(SENT));

        // ---when the SMS has been delivered---
        ctx.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(ctx, "SMS delivered",
                            Toast.LENGTH_SHORT).show();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(ctx, "SMS not delivered",
                            Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * Gửi message sử dụng SmSManager
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public static void sendSmsManager(Context ctx ,final List<User> userArr , final String message){
		
    	for(int i=0 ; i<userArr.size();i++){
			try {
				String phoneNo =  userArr.get(i).mobile;
				if(phoneNo==null || phoneNo.equals("")){
					//khong xu ly
				}else{
					SmsManager smsManager = SmsManager.getDefault();
					PendingIntent sentPI;
					String SENT = "SMS_SENT";

					sentPI = PendingIntent.getBroadcast(ctx, 0,new Intent(SENT), 0);
					smsManager.sendTextMessage(phoneNo, null, message, sentPI, null);
				}
				
			  } catch (Exception e) {
				Toast.makeText( ctx,"Không thể gửi tin nhắn cho " + userArr.get(i).full_name,
					Toast.LENGTH_LONG).show();
			  }
		}

	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * Gửi message sử dụng build in application
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public static void sendSmsBuildInApp(Context ctx , List<User> userList , String message){
		for(User usr : userList){
			try {
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);
			    sendIntent.putExtra("sms_body", "default content"); 
			    sendIntent.setType("vnd.android-dir/mms-sms");
			    ctx.startActivity(sendIntent);
				
			  } catch (Exception e) {
				Toast.makeText(ctx,"Không thể gửi tin nhắn cho " + usr.full_name,
					Toast.LENGTH_LONG).show();
			  }
		}
	}
	
	
	/*public static void sendSms(Context ctx ,String phonenumber,String message, boolean isBinary)
	{
	    SmsManager manager = SmsManager.getDefault();

	    PendingIntent piSend = PendingIntent.getBroadcast(ctx, 0, new Intent(SMS_SENT), 0);
	    PendingIntent piDelivered = PendingIntent.getBroadcast(ctx, 0, new Intent(SMS_DELIVERED), 0);

	    if(isBinary)
	    {
	            byte[] data = new byte[message.length()];

	            for(int index=0; index<message.length() && index < 160; ++index)
	            {
	                    data[index] = (byte)message.charAt(index);
	            }

	            manager.sendDataMessage(phonenumber, null, (short) SMS_PORT, data,piSend, piDelivered);
	    }
	    else
	    {
	            int length = message.length();

	            if(length > 160)
	            {
	                    ArrayList<String> messagelist = manager.divideMessage(message);

	                    manager.sendMultipartTextMessage(phonenumber, null, messagelist, null, null);
	            }
	            else
	            {
	                    manager.sendTextMessage(phonenumber, null, message, piSend, piDelivered);
	            }
	    }
	}*/
	/**
	 * Add text to image
	 * @param gContext
	 * @param gResId
	 * @param gText
	 * @return
	 */
	public static Bitmap drawTextToBitmap(Context gContext, 
		  int gResId, 
		  String gText) {
		  Resources resources = gContext.getResources();
		  float scale = resources.getDisplayMetrics().density;
		  Bitmap bitmap = 
		      BitmapFactory.decodeResource(resources, gResId);
			
		  android.graphics.Bitmap.Config bitmapConfig =
		      bitmap.getConfig();
		  // set default bitmap config if none
		  if(bitmapConfig == null) {
		    bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
		  }
		  // resource bitmaps are imutable, 
		  // so we need to convert it to mutable one
		  bitmap = bitmap.copy(bitmapConfig, true);
			
		  Canvas canvas = new Canvas(bitmap);
		  // new antialised Paint
		  Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		  // text color - #3D3D3D
		  paint.setColor(Color.rgb(61, 61, 61));
		  // text size in pixels
		  paint.setTextSize((int) (14 * scale));
		  // text shadow
		  paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
			
		  // draw text to the Canvas center
		  Rect bounds = new Rect();
		  paint.getTextBounds(gText, 0, gText.length(), bounds);
		  int x = (bitmap.getWidth() - bounds.width())/2;
		  int y = (bitmap.getHeight() + bounds.height())/2;
			
		  canvas.drawText(gText, x, y, paint);
			
		  return bitmap;
	}
	/**
	 * Add text to bitmap 
	 * @param gContext
	 * @param bm
	 * @param gText
	 * @return
	 */
	public static Bitmap drawTextToBitmap(Context gContext, Bitmap bitmap,String gText) {
			  Resources resources = gContext.getResources();
			  float scale = resources.getDisplayMetrics().density;

			  android.graphics.Bitmap.Config bitmapConfig =
			      bitmap.getConfig();
			  // set default bitmap config if none
			  if(bitmapConfig == null) {
			    bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
			  }
			  // resource bitmaps are imutable, 
			  // so we need to convert it to mutable one
			  bitmap = bitmap.copy(bitmapConfig, true);
				
			  Canvas canvas = new Canvas(bitmap);
			  // new antialised Paint
			  Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			  // text color - #3D3D3D
			  paint.setColor(Color.rgb(61, 61, 61));
			  // text size in pixels
			  paint.setTextSize((int) (12 * scale));
			  // text shadow
			  paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
				
			  // draw text to the Canvas center
			  Rect bounds = new Rect();
			  paint.getTextBounds(gText, 0, gText.length(), bounds);
			  int x = (bitmap.getWidth() - bounds.width())/2;
			  int y = (bitmap.getHeight() + bounds.height())/2;
				
			  x = (bitmap.getWidth() - bounds.width())/2;
			  y = (bitmap.getHeight());
			  
			  canvas.drawText(gText, x, y, paint);
				
			  return bitmap;
		}
	
}
