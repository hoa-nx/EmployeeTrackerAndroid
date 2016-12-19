/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/ 
package com.ussol.employeetracker;


import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import com.ussol.employeetracker.filedialog.FileDialog;
import com.ussol.employeetracker.helpers.CsvHelper;
import com.ussol.employeetracker.helpers.DatabaseAdapter;
import com.ussol.employeetracker.helpers.DatabaseBk;
import com.ussol.employeetracker.helpers.InitDatabase;
import com.ussol.employeetracker.helpers.OpenDialog;
import com.ussol.employeetracker.helpers.SystemConfigItemHelper;
import com.ussol.employeetracker.models.MasterConstants;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.os.Environment;

public class DatabaseToolActivity extends Activity {
	public static final String TAG = DatabaseToolActivity.class.getName();
	public static final int LIST_REQUEST_CODE=100;
	private int returnCode =0;
	private String[] mFileList;
	private File mPath = new File(MasterConstants.DIRECTORY + MasterConstants.DIRECTORY_PICTURE);
	private String mChosenFile;
	private static final String FTYPE = ".txt";    
	public static final int DIALOG_LOAD_FILE = 1000;
	private Boolean enableGoogleDriveBackup;
	
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * Called when the activity is first created. 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.databasetool);
	
		/** get thong tin setting */
		SystemConfigItemHelper sysSetting = new SystemConfigItemHelper(this);
		enableGoogleDriveBackup = sysSetting.getEnableGoogleDriveBackup();
		/** backup button */
		Button btnBackup = (Button) findViewById(R.id.btnBackup);
		btnBackup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                AlertDialog.Builder alertDialogBk = new AlertDialog.Builder(DatabaseToolActivity.this);
				/** Setting Dialog Title */
				alertDialogBk.setTitle("Xác nhận");

		        /** Setting Dialog Message */
				alertDialogBk.setMessage("Bạn có muốn thực hiện sao lưu dữ liệu không?");
		        
		        /** Setting Icon to Dialog*/
		        //alertDialog.setIcon(R.drawable.database);
		        /** Setting Positive "Yes" Button */
				alertDialogBk.setPositiveButton(getResources().getText(R.string.titleYes), new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog,int which) {
						// backup DB 
		            	
						DatabaseBk bk = new DatabaseBk(getApplicationContext());
						bk.Backup();
						//kiem ra xem co save len goole Drive khong
						if(enableGoogleDriveBackup){
							bk.UploadToGoogleDrive();	
						}
		            }
		        });
		 
		        /** Setting Negative "NO" Button */
				alertDialogBk.setNegativeButton(getResources().getText(R.string.titleNo), new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            dialog.cancel();
		            }
		        });
		        /** Showing Alert Message */
				alertDialogBk.show();
            
			}
		});
		
		/** restore button */
		Button btnRestore =(Button) findViewById(R.id.btnRestore);
		/*btnRestore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// restore DB 
				DatabaseBk bk = new DatabaseBk(getApplicationContext());
				bk.Restore();
			}
		});*/
		btnRestore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				AlertDialog.Builder alertDialogRt = new AlertDialog.Builder(DatabaseToolActivity.this);
				/** Setting Dialog Title */
				alertDialogRt.setTitle("Xác nhận");

		        /** Setting Dialog Message */
				alertDialogRt.setMessage("Bạn có muốn thực hiện phục hồi lại dữ liệu từ file đã backup không?");
		        
		        /** Setting Icon to Dialog*/
		        //alertDialog.setIcon(R.drawable.database);
		        /** Setting Positive "Yes" Button */
				alertDialogRt.setPositiveButton(getResources().getText(R.string.titleYes), new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog,int which) {
		            	restoreData();
		            }
		        });
		 
		        /** Setting Negative "NO" Button */
				alertDialogRt.setNegativeButton(getResources().getText(R.string.titleNo), new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            dialog.cancel();
		            }
		        });
		 
		        /** Showing Alert Message */
				alertDialogRt.show();
				
		}		
		});
	
		
		/** import data button */
		Button btnCsvImport =(Button) findViewById(R.id.btnImportUserCsv);
		btnCsvImport.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/** import data cho table user */
				loadFileList();
				//onCreateDialog(DIALOG_LOAD_FILE);
				Intent intent = new Intent(getApplicationContext(), FileDialog.class);
				/** setting param cho file dialog */
				Bundle bundle = new Bundle();
				/** chi hien thi file *.txt */
				bundle.putStringArray(FileDialog.FORMAT_FILTER, new String[]{"txt"});
				intent.putExtras(bundle);
				startActivityForResult(intent, DIALOG_LOAD_FILE);
			}
		});
		
		/** init data button */
		Button btnInitData =(Button) findViewById(R.id.btnInitData);
		btnInitData.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(DatabaseToolActivity.this);
				/** Setting Dialog Title */
		        alertDialog.setTitle("Xác nhận");

		        /** Setting Dialog Message */
		        alertDialog.setMessage("Toàn bộ dữ liệu phòng ban, nhóm...sẽ bị xóa hết và khởi tạo lại.Bạn có muốn thực hiện không?");
		        
		        /** Setting Icon to Dialog*/
		        //alertDialog.setIcon(R.drawable.database);
		        /** Setting Positive "Yes" Button */
		        alertDialog.setPositiveButton(getResources().getText(R.string.titleYes), new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog,int which) {
		            	initMasterData();
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
		});
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		final Intent finaldata=data;
		switch(requestCode){
			case DIALOG_LOAD_FILE:
				if(resultCode==RESULT_OK){
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(DatabaseToolActivity.this);
					/** Setting Dialog Title */
			        alertDialog.setTitle("Xác nhận");

			        /** Setting Dialog Message */
			        alertDialog.setMessage("Bạn có muốn import dữ liệu từ file đã chọn không?");
			        
			        /** Setting Icon to Dialog*/
			        //alertDialog.setIcon(R.drawable.database);
			        /** Setting Positive "Yes" Button */
			        alertDialog.setPositiveButton(getResources().getText(R.string.titleYes), new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog,int which) {
			            	Bundle bundle = finaldata.getExtras();
							String file_path = bundle.getString(FileDialog.RESULT_PATH);
							if (!file_path.equals("")){
								CsvHelper csv = new CsvHelper(getApplicationContext());
								try {
									csv.readUserFromCsv( file_path );
									Toast.makeText(getApplicationContext(), "Import dữ liệu thành công!",  Toast.LENGTH_SHORT).show();
								} catch (IOException e) {
									e.printStackTrace();
									Toast.makeText(getApplicationContext(), "Import dữ liệu thất bại!",  Toast.LENGTH_SHORT).show();
								}
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
				
				break;
		}
	}

	private void loadFileList() {
	    try {
	        mPath.mkdirs();
	    }
	    catch(SecurityException e) {
	        //Log.e(TAG, "unable to write on the sd card " + e.toString());
	    }
	    if(mPath.exists()) {
	        FilenameFilter filter = new FilenameFilter() {
	            public boolean accept(File dir, String filename) {
	                File sel = new File(dir, filename);
	                return filename.contains(FTYPE) || sel.isDirectory();
	            }
	        };
	        mFileList = mPath.list(filter);
	    }
	    else {
	        mFileList= new String[0];
	    }
	}
	
	private void initMasterData(){
		new AsyncTask<Void, Void, Boolean>()
	    {
	        ProgressDialog progressDialog;
	
	        @Override
			protected void onPreExecute() {
				progressDialog = new ProgressDialog(DatabaseToolActivity.this);
				progressDialog.setCancelable(false);
				progressDialog.setTitle("Khởi tạo data master");
				progressDialog.setMessage("Xin vui lòng chờ...");
				progressDialog.show();
			}
	        @Override
	        protected Boolean doInBackground(Void... params)
	        {
	            try
	            {
	                /** khởi tạo dữ liệu master */
	    			InitDatabase init = new InitDatabase(getBaseContext());
	    			init.InitAllData();
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
					message="Khởi tạo thành công.";
				}else{
					message="Khởi tạo thất bại.";
				}
	        	AlertDialog.Builder b = new AlertDialog.Builder(DatabaseToolActivity.this);
	            b.setTitle("Khởi tạo data master");
	
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
	
	private void restoreData(){
		new AsyncTask<Void, Void, Boolean>()
	    {
	        ProgressDialog progressDialog;
	
	        @Override
			protected void onPreExecute() {
				progressDialog = new ProgressDialog(DatabaseToolActivity.this);
				progressDialog.setCancelable(false);
				progressDialog.setTitle("Phục hồi dữ liệu");
				progressDialog.setMessage("Xin vui lòng chờ...");
				progressDialog.show();
			}
	        @Override
	        protected Boolean doInBackground(Void... params)
	        {
	            try
	            {
	                /** phục hồi dữ liệu*/
	            	DatabaseBk bk = new DatabaseBk(getApplicationContext());
					bk.Restore();
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
					message="Phục hồi thành công.";
				}else{
					message="Phục hồi thất bại.";
				}
	        	AlertDialog.Builder b = new AlertDialog.Builder(DatabaseToolActivity.this);
	            b.setTitle("Phục hồi dữ liệu");
	
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
	
	protected Dialog onCreateDialog(int id) {
	    Dialog dialog = null;
	    AlertDialog.Builder builder = new Builder(this);

	    switch(id) {
	        case DIALOG_LOAD_FILE:
	            builder.setTitle("Choose your file");
	            if(mFileList == null) {
	                //Log.e(TAG, "Showing file picker before loading the file list");
	                dialog = builder.create();
	                return dialog;
	            }
	            builder.setItems(mFileList, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                    mChosenFile =mFileList[which];
	                    //you can do stuff with the file here too
	                    if (!mChosenFile.equals("")){
	    					CsvHelper csv = new CsvHelper(getApplicationContext());
	    					try {
	    						csv.readUserFromCsv( MasterConstants.DIRECTORY + MasterConstants.DIRECTORY_PICTURE + "/" + mChosenFile );
	    						Toast.makeText(getApplicationContext(), "Import dữ liệu thành công!",  Toast.LENGTH_SHORT).show();
	    					} catch (IOException e) {
	    						e.printStackTrace();
	    						Toast.makeText(getApplicationContext(), "Import dữ liệu thất bại!",  Toast.LENGTH_SHORT).show();
	    					}
	    				}
	                }
	            });
	            break;
	    }
	    dialog = builder.show();
	    return dialog;
	}
	
}