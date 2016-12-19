/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  

package com.ussol.employeetracker.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.ussol.employeetracker.DatabaseToGoogleDriveActivity;
import com.ussol.employeetracker.MainActivity;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import android.widget.Toast;
/**
 * 
 * @author hoa-nx
 *
 */
public class DatabaseBk  {

	Context context;
	public static final String TAG = DatabaseBk.class.getName();
	protected static final File  DATABASE_DIRECTORY = new File(Environment.getExternalStorageDirectory(),"employeetracker");
	/** File path of Db to be imported **/
	protected static final File IMPORT_FILE =new File(DATABASE_DIRECTORY,DatabaseAdapter.DATABASE_NAME);
 
	public static final String PACKAGE_NAME = MainActivity.PACKAGE_NAME;
	public static final String DATABASE_NAME = DatabaseAdapter.DATABASE_NAME;
	// Storage Permissions
	private static final int REQUEST_EXTERNAL_STORAGE = 1;
	private static final int REQUEST_WRITE_STORAGE = 112; 
	private static String[] PERMISSIONS_STORAGE = {
	        Manifest.permission.READ_EXTERNAL_STORAGE,
	        Manifest.permission.WRITE_EXTERNAL_STORAGE
	};
   
    
	/**
	 * Constructor method
	 * @param context
	 */
	public DatabaseBk(Context context){
		this.context = context;
	}
	
	
	/**
	 * Database Backup
	 */
	public void Backup(){
		
		InputStream myInput;
		 
		try {
				//myInput = new FileInputStream("/data/data/com.packagename/databases/database_name");//this is
				myInput = new FileInputStream( Environment.getDataDirectory()+"/data/"+ PACKAGE_NAME + "/databases/"+ DATABASE_NAME);
				//the path for all apps
				//insert your package instead packagename,ex:com.mybusiness.myapp
	
				if (!isSdCardReady()){
					Toast.makeText(this.context, "SD Card not ready!",  Toast.LENGTH_SHORT).show();
				}
			    // Set the output folder on the SDcard
			    //File directory = new File("/sdcard/smsforwarder");
			    // Create the folder if it doesn't exist:
			    if (!DATABASE_DIRECTORY.exists()) 
			    {
			    	DATABASE_DIRECTORY.mkdirs();
			    } 
			    // Set the output file stream up:
	
			    OutputStream myOutput = new FileOutputStream(DATABASE_DIRECTORY.getPath() +	"/" + DatabaseAdapter.DATABASE_NAME + ".bk");
			
			    // Transfer bytes from the input file to the output file
			    byte[] buffer = new byte[1024];
			    int length;
			    while ((length = myInput.read(buffer))>0)
			    {
			        myOutput.write(buffer, 0, length);
			    }
			    // Close and clear the streams

			    myOutput.flush();
	
			    myOutput.close();
	
			    myInput.close();

		} catch (FileNotFoundException e) {
				Toast.makeText(this.context, "Backup Unsuccesfull!",  Toast.LENGTH_SHORT).show();
				return;
		} catch (IOException e) {
			Toast.makeText(this.context, "Backup Unsuccesfull!",  Toast.LENGTH_SHORT).show();
				return;
		}
		Toast.makeText(this.context, "Backup Done Succesfully!",  Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * UploadToGoogleDrive
	 */
	public void UploadToGoogleDrive(){
		Intent i = new Intent(this.context,DatabaseToGoogleDriveActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.context.startActivity(i);

	}
	
	/**
	 * Database Restore 
	 */
	public void Restore(){
		OutputStream myOutput;
		 
		try {
 
			myOutput = new FileOutputStream( Environment.getDataDirectory()+"/data/"+ PACKAGE_NAME + "/databases/"+ DATABASE_NAME);
  
		    // Set the folder on the SDcard
		    //File directory = new File("/sdcard/some_folder");
		    // Set the input file stream up:
 
		    InputStream myInputs = new FileInputStream(DATABASE_DIRECTORY.getPath() + "/" + DatabaseAdapter.DATABASE_NAME + ".bk");
		    
		    // Transfer bytes from the input file to the output file
		    byte[] buffer = new byte[1024];
		    int length;
		    while ((length = myInputs.read(buffer))>0)
		    {
		        myOutput.write(buffer, 0, length);
		    }
 		    // Close and clear the streams
		    myOutput.flush();
 		    myOutput.close();
 		    myInputs.close();	
 		    /** download image to file */
 		   downloadImage();
		} catch (FileNotFoundException e) {
			//Toast.makeText(this.context, "Import Unsuccesfull!File not found.", Toast.LENGTH_LONG).show();
			return;
		} catch (IOException e) {	
			//Toast.makeText(this.context, "Import Unsuccesfull!", Toast.LENGTH_LONG).show();
			return;
		}
		//Toast.makeText(this.context, "Import Done Succesfully!", Toast.LENGTH_LONG).show();
	}
	
	/** Returns whether an SD card is present and writable **/
	public static boolean isSdCardReady() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}
	/** download all image of user */
	public void downloadImage(){
		DatabaseAdapter mDatabaseAdapter = new DatabaseAdapter(context);
		mDatabaseAdapter.open();
		/** get list image cua nhan vien */
		Cursor cur = mDatabaseAdapter.getUserImageList("");
		/** nếu như có data */
		if (cur.getCount() >= 1) {
			/** di chuyển đến vị trí đầu tiên */
			cur.moveToFirst();
			/**lặp lại cho đến khi không còn record nào */
			do {
				byte[] imgByte = cur.getBlob(cur.getColumnIndex(DatabaseAdapter.KEY_IMG));
		        /*Object[] param=new Object[2];
		        param[0]= imgByte;
		        param[1]=cur.getString(cur.getColumnIndex(DatabaseAdapter.KEY_IMG_FULLPATH));*/
		        File photo=new File(cur.getString(cur.getColumnIndex(DatabaseAdapter.KEY_IMG_FULLPATH)));

		        if (photo.exists()) {
		              photo.delete();
		        }
		        try {
				    FileOutputStream fos=new FileOutputStream(photo.getPath());
				    /** GHI XUONG FILE */
				    fos.write(imgByte);
				    fos.close();
				}
				catch (java.io.IOException e) {
				    Log.e("SavePhotoTask", "Exception in save photo", e);
				}
		        
		        //new SavePhotoTask().execute(param);
			    //return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
				/**di chuyển đến record tiếp theo */
				cur.moveToNext();
			}while (!cur.isAfterLast());
		}
		mDatabaseAdapter.close();
		/** đóng cursor */
	    if (cur != null && !cur.isClosed()) {
	        cur.close();
	    }       
	} 
	/** save image to file */
	class SavePhotoTask extends AsyncTask<Object, String, String> {
	    @Override
	    protected String doInBackground(Object... param) {
	    	byte[] img=(byte[])param[0];
	    	String fullpath = (String) param[1];
	    		    	
	    	File photo=new File(fullpath);

	        if (photo.exists()) {
	              photo.delete();
	        }
	        
			/*File photo=new File(Environment.getExternalStorageDirectory(),
			               "photo.jpg");
			
			if (photo.exists()) {
				photo.delete();
			}*/
			
			try {
			    FileOutputStream fos=new FileOutputStream(photo.getPath());
			
			    fos.write(img);
			    fos.close();
			}
			catch (java.io.IOException e) {
			    Log.e("SavePhotoTask", "Exception in save photo", e);
			}
			
			return(null);
		    }
	  }
}
