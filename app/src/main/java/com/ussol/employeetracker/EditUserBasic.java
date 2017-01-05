/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  

package com.ussol.employeetracker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.mail.Quota.Resource;
import javax.mail.search.DateTerm;

import com.ussol.employeetracker.helpers.CommonClass;
import com.ussol.employeetracker.helpers.ContactHelper;
import com.ussol.employeetracker.helpers.ConvertCursorToListString;
import com.ussol.employeetracker.helpers.DatabaseAdapter;
import com.ussol.employeetracker.helpers.DatabaseBk;
import com.ussol.employeetracker.helpers.DecodeTask;
import com.ussol.employeetracker.helpers.FileHelper;
import com.ussol.employeetracker.helpers.ImageHelper;
import com.ussol.employeetracker.helpers.SystemConfigItemHelper;
import com.ussol.employeetracker.models.Dept;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.Position;
import com.ussol.employeetracker.models.SelectUser;
import com.ussol.employeetracker.models.Team;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.models.UserCustomerGroup;
import com.ussol.employeetracker.models.UserHistory;
import com.ussol.employeetracker.models.UserImage;
import com.ussol.employeetracker.models.UserPositionGroup;
import com.ussol.employeetracker.utils.DateTimeUtil;
import com.ussol.employeetracker.utils.RoundImage;
import com.ussol.employeetracker.utils.Utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;

public class EditUserBasic extends Fragment implements OnClickListener , OnTouchListener,OnCheckedChangeListener{
	/** tag */
	private final static String TAG =EditUserBasic.class.getName();
    static final int DATE_DIALOG_ID = 0;
    //private TextView lblUserCode , lblUserDeptCode , lblUserTeamCode , lblUserPositionCode ;
    private TextView txtUserCode, imgUserFullPath,txtUserTitle  ;
    private EditText txtUserBirthday,txtUserMarriedday , txtUserSalary_NotAllowance, txtUserSalary_Allowance, txtUserSalaryTotal;
    private EditText txtUserFullName, txtUserAddress , txtUserMobile, txtUserEmail,txtGoogleId;
    private EditText txtUserEstimatePoint;
    private Button btnUserSave , btnUserCancel , btnUserBirthday,btnUserMarriedday;
    private CheckBox chkGetMarried;
    private Switch txtUserSex;
    static final int CAMERA_REQUEST= 101;
	private float scale;
	private int width;
	private int height;
	private Bitmap bitmap;
	private ImageView imageView , pic , imgPrev, imgNext;
	private ImageButton btnGoogleId;
	private Uri mImageCaptureUri;
	CommonClass commClass;
	private boolean isFirstRun = true;
	private static final int CAMERA_PIC_REQUEST = 1337;   
    private static final int SELECT_PICTURE = 1;  
    String fileManagerString,imagePath;  
    String selectedImagePath="";  
    private int columnIndex;  
	/** kết nối Database */
	DatabaseAdapter mDatabaseAdapter;
	/** Trị code nhân viên  */
	private int nCode = -1;
	public static User userInfo ;
	public static String imgPathSave=""; 
	/** chuyển đổi Cursor thành List */
	private ConvertCursorToListString mConvertCursorToListString;
	List<User> _listUserChecked =null;
	private enum JapanseLevel {N1,N2,N3,N4,N5,};
	private String listViewCurrentPosition ="0";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		isFirstRun = true;
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * onCreateView
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_user_edit_basic, null);	
		/** setting tab để có thể truy xuất từ các Tab khác*/
		((EditUserMainActivity)getActivity()).setTabFragmentBasic(getTag());
		return root;
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * onActivityCreated
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        
		mDatabaseAdapter = new DatabaseAdapter(getActivity().getApplicationContext());
		/** chuyển đổi từ Cursor thành List */
		mConvertCursorToListString = new ConvertCursorToListString(getActivity().getApplicationContext());
		nCode=EditUserMainActivity.nCode;
		userInfo =EditUserMainActivity.userInfo;
		listViewCurrentPosition = EditUserMainActivity.listViewCurrentPosition;
		/** get các control date và gán sự kiện khi click vào textbox */
		getControl();
		/** gán sự kiện cho các control */ 
		settingListener();
		/** setting không hiển thị keyboard */
		settingInputType();
		addTextChangedListener();
		/** setting lại path của image */
		if(userInfo!=null){
			setImgFullPath(userInfo.img_fullpath);
			imgPathSave = userInfo.img_fullpath;
		}
		viewUserImg();
		if (savedInstanceState!=null){
			
		}else{
			if (nCode==-1){
				/** trường hợp tạo mới*/
				/** setting code user*/
				setUserCode(getUserCode(-1));
			}else if (nCode==0){
				/** hiển thị copy data*/
				if (isFirstRun){
					setValueTabBasic(userInfo);
				}
				setUserCode(getUserCode(-1));
			}else{
				/** hiển thị chi tiết */
				setUserCode(userInfo.code);
				if (isFirstRun){
					setValueTabBasic(userInfo);
				}
				
			}
		}
		isFirstRun = false;
		/*final boolean customTitleSupported = getActivity().requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        if ( customTitleSupported ) {
        	getActivity().getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
            }

        final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
        if ( myTitleText != null ) {
            myTitleText.setText("NEW TITLE");

            // user can also set color using "Color" and then "Color value constant"
           // myTitleText.setBackgroundColor(Color.GREEN);
        }
        */
        /** camera */
       /* ImageButton photoButton = (ImageButton) getView().findViewById(R.id.imgCamera);
        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            	mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
            		    "pic_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
            	
            	Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
               // cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST); 
            }
        });*/
        /** hien thi thong tin nhan vien len title bar */
		setUserTitleBar(getFullName());
		/** hien thi hinh anh nhan vien */
        pic=(ImageView)getView().findViewById(R.id.imgUser);  
        pic.setOnClickListener(this);  
        
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * onPause
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		View target = getView().findFocus();
        if (target != null) 
        {
            InputMethodManager mgr = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(target.getWindowToken(), 0);
        }
        String TabOfFragmentWork =EditUserMainActivity.getTabFragmentWork();
		   
    	EditUserWork fgUserWork = (EditUserWork)getFragmentManager().findFragmentByTag(TabOfFragmentWork);
    	if(fgUserWork!=null){
    		fgUserWork.onPause();
    	}
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * onActivityResult
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	 @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/*
		scale = this.getResources().getDisplayMetrics().density;
		width = (int) (84 * scale + 0.5f);
		height = (int) (111 * scale + 0.5f);
		
       if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK) {  
           Bitmap photo = (Bitmap) data.getExtras().get("data"); 
           //pathToImage = mImageCaptureUri.getPath();
           
           bitmap = BitmapFactory.decodeFile(mImageCaptureUri.getPath());
           imageView = (ImageView)getView().findViewById(R.id.imgUser);
           imageView.setImageBitmap(photo);
           
           //setImageResource();
       }
       */
		if (resultCode == getActivity().RESULT_OK)  
        {  
			imageView = (ImageView)getView().findViewById(R.id.imgUser);
            if(requestCode == SELECT_PICTURE)   
            {  
     	
                Uri selectedImageUri = data.getData();  
                  
                //OI FILE Manager  
                fileManagerString = selectedImageUri.getPath();  
                
                //MEDIA GALLERY  
                selectedImagePath =getPath(selectedImageUri);  
                
                if(selectedImagePath==null || selectedImagePath.equals(""))
                {
                	imageView.setImageResource(R.drawable.user);
                	return;
                }
                /** get id của image vừa chụp */
                int imageID =Utils.getRandomNumberBetween(100000, 999999999);
                /** get path của image vừa chụp */
                String sourcePath =selectedImagePath;
                String destPath = MasterConstants.DIRECTORY + MasterConstants.DIRECTORY_PICTURE + imageID + MasterConstants.IMAGE_SMALL_SUFFIX;
                /** tạo file để copy */
                File source = new File(sourcePath );
                File destination= new File( destPath);
                
                FileHelper fHelper = new FileHelper();
                /** copy file sang external memory */
                fHelper.copy(source, destination);
                /** xóa file image trong thư viện của máy */
                /** hiển thị image từ path */
                Bitmap disBitmap = BitmapFactory.decodeFile(destination.getAbsolutePath());
                int desiredImageWidth = 1000;  // pixels
				int desiredImageHeight = 1200; // pixels

				BitmapFactory.Options o = new BitmapFactory.Options();
				Bitmap newImage=Bitmap.createScaledBitmap(disBitmap, desiredImageWidth, desiredImageHeight, false);
				setImgFullPath(destination.getAbsolutePath());
				
                //pic.setImageURI(selectedImageUri);  
				pic.setImageBitmap(newImage);
				RoundImage roundedImage = new RoundImage(newImage);
				pic.setImageDrawable(roundedImage);
	            /*  
                Bitmap thumbnail=null;
				try {
					thumbnail = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
					//pic.setImageBitmap(thumbnail);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
 
                
                pic.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                pic.setAdjustViewBounds(true);
                FileInputStream fis;
				try {
					
					fis = new FileInputStream(selectedImagePath);
					BitmapFactory.Options options=new BitmapFactory.Options();
	                //options.inSampleSize=1; //try to decrease decoded image
	                options.inPurgeable=true; //if necessary purge pixels into disk
	                options.inScaled=true; //scale down image to actual device density
	                options.outHeight = 85;
	                options.outWidth = 70;
	                Bitmap bm=BitmapFactory.decodeStream(fis, null, options);
	                pic.setImageBitmap(bm);
	                try {
						fis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			
				}
				
                
				int desiredImageWidth = 100;  // pixels
				int desiredImageHeight = 150; // pixels

				BitmapFactory.Options o = new BitmapFactory.Options();
				Bitmap newImage=Bitmap.createScaledBitmap(thumbnail, desiredImageWidth, desiredImageHeight, false);
				
                //pic.setImageURI(selectedImageUri);  
				pic.setImageBitmap(newImage);
                //imagePath.getBytes();  */				
           }  
            if(requestCode == CAMERA_PIC_REQUEST)  
            {  
            	try{
            		Bitmap image = (Bitmap) data.getExtras().get("data");  
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();  
                    //image.createScaledBitmap(image, 1800, 2000, false);
                    image.compress(Bitmap.CompressFormat.PNG, 80, stream);  
                    /*CommonClass commClass = ((CommonClass)getActivity().getApplication());  
                    commClass.profileImageInBytes = stream.toByteArray();  
                    pic.setImageBitmap(BitmapFactory.decodeByteArray(commClass.profileImageInBytes, 0, commClass.profileImageInBytes.length));*/
                    //pic.setImageBitmap(image);
                    /** copy image vừa tạo và xóa */
                    /** get id của image vừa chụp */
                    int imageID =getIdLastImage();
                    /** get path của image vừa chụp */
                    String sourcePath =getPathLastImage() ;
                    String destPath = MasterConstants.DIRECTORY + MasterConstants.DIRECTORY_PICTURE + imageID + MasterConstants.IMAGE_SMALL_SUFFIX;
                    /** tạo file để copy */
                    File source = new File(sourcePath );
                    File destination= new File( destPath);
                    
                    FileHelper fHelper = new FileHelper();
                    /** copy file sang external memory */
                    fHelper.copy(source, destination);
                    /** xóa file image trong thư viện của máy */
                    removeImage(imageID);
                    /** hiển thị image từ path */
                    Bitmap disBitmap = BitmapFactory.decodeFile(destination.getAbsolutePath());
                    int desiredImageWidth = 1000;  // pixels
    				int desiredImageHeight = 1200; // pixels

    				BitmapFactory.Options o = new BitmapFactory.Options();
    				Bitmap newImage=Bitmap.createScaledBitmap(disBitmap, desiredImageWidth, desiredImageHeight, false);
    				setImgFullPath(destination.getAbsolutePath());
                    //pic.setImageURI(selectedImageUri);  
    				pic.setImageBitmap(newImage);
    				RoundImage roundedImage = new RoundImage(newImage);
    				pic.setImageDrawable(roundedImage);
    				
                    //pic.setImageBitmap(disBitmap);
            	}catch (Exception e){
            		//set hinh mac dinh
            		imageView.setImageResource(R.drawable.user);
            		Log.d(TAG, e.getMessage());
            	}
                
            }  
        }  
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * nhận về Id của image vừa chụp 
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	 private int getIdLastImage(){
	    final String[] imageColumns = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };
	    final String imageOrderBy = MediaStore.Images.Media._ID+" DESC";
	    Cursor imageCursor =getActivity().managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns, null, null, imageOrderBy);
	    if(imageCursor.moveToFirst()){
	        int id = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.Media._ID));
	        String fullPath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
	        Log.d(TAG, "getLastImageId::id " + id);
	        Log.d(TAG, "getLastImageId::path " + fullPath);
	        imageCursor.close();
	        return id;
	    }else{
	        return 0;
	    }
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * nhận về path của image vừa chụp 
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	 private String getPathLastImage(){
	    final String[] imageColumns = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };
	    final String imageOrderBy = MediaStore.Images.Media._ID+" DESC";
	    Cursor imageCursor =getActivity().managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns, null, null, imageOrderBy);
	    if(imageCursor.moveToFirst()){
	        int id = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.Media._ID));
	        String fullPath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
	        Log.d(TAG, "getLastImageId::id " + id);
	        Log.d(TAG, "getLastImageId::path " + fullPath);
	        imageCursor.close();
	        return fullPath;
	    }else{
	        return "";
	    }
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * xóa image theo id
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	 private void removeImage(int id) {
	   ContentResolver cr =getActivity().getContentResolver();
	   cr.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media._ID + "=?", new String[]{ Long.toString(id) } );
	}
	 
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * setImageResource
	 * 
	▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	private void setImageResource() {
		/*if(bitmap.getHeight() > bitmap.getWidth()) {
			imageView.setLayoutParams(new LayoutParams(width, height));
		} else {
			imageView.setLayoutParams(new LayoutParams(height, width));
		}*/
		imageView.setImageBitmap(bitmap);
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * onClick
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onClick(View v) {
		
		/** phân nhánh xử lý theo từng button*/
		switch (v.getId()) {
			case R.id.txtBirthday:
				//selectDate(v);
			break;
			case R.id.btnPrev:
				/** get trị nhỏ hơn trị hiện tại */
				mDatabaseAdapter.open();
				int prevCode = mDatabaseAdapter.getPrevCodeUser(getUserCode());
				mDatabaseAdapter.close();
				String xWhere =" AND " + DatabaseAdapter.KEY_CODE + " =" + prevCode;
				if(prevCode!=0 ||prevCode!=-1 ){
					List<User> listUser= mConvertCursorToListString.getUserList(xWhere,false, false);
					if (listUser!=null && listUser.size()>0){
						EditUserMainActivity.nCode = prevCode;
						nCode = prevCode;
						EditUserMainActivity.userInfo = listUser.get(0);
						userInfo= listUser.get(0);
						
						//setValueTabBasic(listUser.get(0));
						
						//setValueTabWork(listUser.get(0));
						
						//setValueTabOther(listUser.get(0));
						
						Intent intent = new Intent(getActivity(), EditUserMainActivity.class);
						Bundle bundle = new Bundle();
						/**lấy code của user*/
						bundle.putInt(DatabaseAdapter.KEY_CODE, listUser.get(0).code);
						bundle.putParcelable(MasterConstants.TAB_USER_TAG, listUser.get(0));
						/**gán vào bundle để gửi cùng với intent */
						intent.putExtras(bundle);
						
						getActivity().finish();
						/**khởi tạo activity dùng để edit  */
						startActivity(intent);
						
					}

				}
				
				break;
				
			case R.id.btnNext:
				/** get trị nhỏ hơn trị hiện tại */
				mDatabaseAdapter.open();
				int nextCode = mDatabaseAdapter.getNextCodeUser(getUserCode());
				mDatabaseAdapter.close();
				String xWhere1 =" AND " + DatabaseAdapter.KEY_CODE + " =" + nextCode;
				if(nextCode!=0 ||nextCode!=-1 ){
					/** chuyển đổi từ Cursor thành List */
					mConvertCursorToListString = new ConvertCursorToListString(getActivity().getApplicationContext());
					
					List<User> listUser= mConvertCursorToListString.getUserList(xWhere1,false,false);
					if (listUser!=null && listUser.size()>0){
						EditUserMainActivity.nCode = nextCode;
						nCode = nextCode;
						EditUserMainActivity.userInfo = listUser.get(0);
						userInfo= listUser.get(0);
						/*setValueTabBasic(listUser.get(0));
						setValueTabWork(listUser.get(0));
						setValueTabOther(listUser.get(0));*/
						Intent intent = new Intent(getActivity(), EditUserMainActivity.class);
						Bundle bundle = new Bundle();
						/**lấy code của user*/
						bundle.putInt(DatabaseAdapter.KEY_CODE, listUser.get(0).code);
						bundle.putParcelable(MasterConstants.TAB_USER_TAG, listUser.get(0));
						/**gán vào bundle để gửi cùng với intent */
						intent.putExtras(bundle);
						
						getActivity().finish();
						/**khởi tạo activity dùng để edit  */
						startActivity(intent);
					}

				}
				break;
				
			case R.id.btnUserSave:
				//check input before save.
				if(checkInput()){
					saveUserToDbEntryPoint();
				}
				break;
			case R.id.btnUserCancel:
				EditUserMainActivity.nCode = 0;
				EditUserMainActivity.userInfo = null;
				/** trả về kết quả*/
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("btn", R.id.btnUserSave);
				bundle.putInt(MasterConstants.EXP_USER_GROUP_TAG, 0);
				intent.putExtras(bundle);

				getActivity().setResult(getActivity().RESULT_CANCELED, intent);
				getActivity().finish();

				break;
			case R.id.imgUser:
				showalert();
				break;
			case R.id.btnUserDelete:
				//deleteUser(userInfo.code);
				break;
			case R.id.btnCreateDate:
				txtUserBirthday.setText("");
				break;

			case R.id.btnMarriedDate:
				txtUserMarriedday.setText("");
				break;
			case R.id.btnGoogleId:
				ArrayList<SelectUser> listResult = new ArrayList<SelectUser>();
				ContactHelper helper = new ContactHelper(getActivity());
				String contactId =getGoogleId();
				if(!contactId.equals("")){
					try{
						listResult =helper.getContactById(Long.parseLong( contactId));
					}catch (Exception ex){
						break;
					}
					if(listResult.size()>=0){
						/** họ tên đầy đủ **/
						if(getFullName().equals("")&& !listResult.get(0).getNickname().equals("")){
							setFullName(listResult.get(0).getNickname());
						}
						if(getMobile().equals("")&& !listResult.get(0).getPhone().equals("")){
							setMobile(listResult.get(0).getPhone());
						}
						if(getEmail().equals("")&& !listResult.get(0).getEmail().equals("")){
							setEmail(listResult.get(0).getEmail());
						}
												
						/*if(getAddress().equals("")&& !listResult.get(0).getAddress().equals("")){
							setAddress(listResult.get(0).getAddress());
						}*/
						
						if(getBirthday().equals("")&& !listResult.get(0).getBirthDay().equals("")){
							if(listResult.get(0).getBirthDay()!=""){
								setBirthday(DateTimeUtil.formatDate2String(listResult.get(0).getBirthDay().replace("/", MasterConstants.DATE_SEPERATE_CHAR), MasterConstants.DATE_JP_FORMAT, MasterConstants.DATE_VN_FORMAT));
							}
						}
						
						/**setting image **/
						/** get id của image vừa chụp */
				        int imageID =Utils.getRandomNumberBetween(100000, 999999999);
				        /** get path của image vừa chụp */
				        String destPath = MasterConstants.DIRECTORY + MasterConstants.DIRECTORY_PICTURE + imageID + MasterConstants.IMAGE_SMALL_SUFFIX;
				        /** tạo file để copy */
				        File destination= new File( destPath);
				        try {
							destination.createNewFile();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

				        //Convert bitmap to byte array
				        Bitmap bitmap = listResult.get(0).getThumb();
				        if(bitmap==null){
				        	bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.user);
				        }
				        /** chinh sua image */
				        
				        /*Bitmap disBitmap = bitmap;	//BitmapFactory.decodeFile(destination.getAbsolutePath());
				        int desiredImageWidth = 1000;  // pixels
						int desiredImageHeight = 1200; // pixels
						//whatever algorithm here to compute size
			            float ratio = (float) bitmap.getHeight() / (float) bitmap.getWidth();
			            float heightFloat = ((float) desiredImageWidth) * ratio;
			            
						//BitmapFactory.Options o = new BitmapFactory.Options();
						//Bitmap newImage=Bitmap.createScaledBitmap(disBitmap, desiredImageWidth, desiredImageHeight, false);
						Bitmap newImage = Utils.scaleBitmap(disBitmap, desiredImageWidth, desiredImageHeight);
						//Bitmap newImage = Utils.resizerBitmap(disBitmap, desiredImageWidth, desiredImageHeight);
				        ByteArrayOutputStream bos = new ByteArrayOutputStream();
				        newImage.compress(CompressFormat.PNG, 100 ignored for PNG, bos);
				        */
				        ByteArrayOutputStream bos = new ByteArrayOutputStream();
				        bitmap.compress(CompressFormat.JPEG, 100 , bos);
				        byte[] bitmapdata = bos.toByteArray();
				        
				        //write the bytes in file
				        FileOutputStream fos;
						try {
							fos = new FileOutputStream(destination);
							fos.write(bitmapdata);
					        fos.flush();
					        fos.close();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

				        setImgFullPath(destination.getAbsolutePath());
						
		                //pic.setImageURI(selectedImageUri);  
						pic.setImageBitmap(bitmap );
						RoundImage roundedImage = new RoundImage(bitmap);
						pic.setImageDrawable(roundedImage);
						
					}
				}
				
				break;
				
		}
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * addTextChangedListener
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public void addTextChangedListener(){
		txtUserFullName.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				//getActivity().setTitle(s.toString());
				setUserTitleBar(s.toString());
			}
		});
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * selectDate
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public void selectDate(View view) {
	        DialogFragment newFragment = new SelectDateFragment(view.getId());
	        newFragment.show(getActivity().getFragmentManager(), "Birthday");
	 }
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * populateSetDate
	 * 
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void populateSetDate(int control,int year, int month, int day) {
    	//mDateDisplay = (EditText)findViewById(R.id.editText1);
    	switch (control){
    		case R.id.txtBirthday:
    			txtUserBirthday.setText(day + MasterConstants.DATE_SEPERATE_CHAR + month+ MasterConstants.DATE_SEPERATE_CHAR +year);
    			break;
			case R.id.txtMarriedDate:
				txtUserMarriedday.setText(day + MasterConstants.DATE_SEPERATE_CHAR + month+ MasterConstants.DATE_SEPERATE_CHAR +year);
				break;
    	}
    	
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * SelectDateFragment
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    @SuppressLint("ValidFragment")
	public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    	private int mControl ;
    	public SelectDateFragment(int control){
    		mControl = control;
    	}
    	@Override
    	public Dialog onCreateDialog(Bundle savedInstanceState) {
			/*final Calendar calendar = Calendar.getInstance();
			int yy = calendar.get(Calendar.YEAR);
			int mm = calendar.get(Calendar.MONTH);
			int dd = calendar.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(getActivity(), this, yy, mm, dd);*/
    		String date =null;
    		switch (mControl){
	    		case R.id.txtBirthday:
	    			date = txtUserBirthday.getText().toString();
	    			break;
				case R.id.txtMarriedDate:
					date = txtUserMarriedday.getText().toString();
					break;
    		}
    		int yy;
    		int mm;
    		int dd;
    		/** trường hợp mà chưa có input thì sẽ là ngày hiện tại */
    		if (date ==null || date.equals("")){
    			final Calendar calendar = Calendar.getInstance();
				yy = calendar.get(Calendar.YEAR);
				mm = calendar.get(Calendar.MONTH);
				dd = calendar.get(Calendar.DAY_OF_MONTH);
    		}else{
    			/** hiển thị ngày mà đã chọn trước đó */
    			
    			//Date datefmt = DateTime.convertStringToDate(date, "dd/MM/yyyy");
    			Date datefmt = DateTimeUtil.convertStringToDate(date, MasterConstants.DATE_VN_FORMAT);
    			yy = datefmt.getYear() + 1900;
				mm = datefmt.getMonth();
				dd = datefmt.getDate();
    		}
			return new DatePickerDialog(getActivity(), this, yy, mm, dd);
	
    	}
    	
    	
    	public void onDateSet(DatePicker view, int yy, int mm, int dd) {
    		populateSetDate(mControl,yy, mm+1, dd);
    	}
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get các control trên màn hình
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void getControl(){
    	txtUserCode = (TextView)getView().findViewById(R.id.txtUserCode);
    	if(imgUserFullPath==null){
    		imgUserFullPath = (TextView)getView().findViewById(R.id.txtImgFullPath);
    	}
    	txtUserFullName= (EditText)getView().findViewById(R.id.txtUserName);
    	txtUserSex = (Switch)getView().findViewById(R.id.txtSex);
    	txtUserBirthday = (EditText)getView().findViewById(R.id.txtBirthday);
		txtUserMarriedday = (EditText)getView().findViewById(R.id.txtMarriedDate);
    	txtUserAddress= (EditText)getView().findViewById(R.id.txtUserAddress);
    	txtUserMobile= (EditText)getView().findViewById(R.id.txtUserMobile);
    	txtUserEmail= (EditText)getView().findViewById(R.id.txtUserEmail);
    	txtGoogleId= (EditText)getView().findViewById(R.id.txtGoogleContactId);
    	
    	btnUserBirthday = (Button)getView().findViewById(R.id.btnCreateDate);
		btnUserMarriedday= (Button)getView().findViewById(R.id.btnMarriedDate);
    	btnGoogleId = (ImageButton)getView().findViewById(R.id.btnGoogleId);
    	txtUserTitle = (TextView) getView().findViewById(R.id.txtUserTitle);
    	imgPrev = (ImageView)getView().findViewById(R.id.btnPrev);
    	imgNext= (ImageView)getView().findViewById(R.id.btnNext);
    	
    	txtUserSalary_Allowance= (EditText)getView().findViewById(R.id.txtUserTitleSalary_Allowance);
    	txtUserSalary_NotAllowance= (EditText)getView().findViewById(R.id.txtUserKeikenConvertFJN);
    	txtUserSalaryTotal= (EditText)getView().findViewById(R.id.txtUserTitleSalaryTotal);
    	txtUserEstimatePoint= (EditText)getView().findViewById(R.id.txtUserTitleEstimatePoint);
    	
    	chkGetMarried = (CheckBox)getView().findViewById(R.id.chkGetMarried);
    	
		btnUserSave = (Button)getView().findViewById(R.id.btnUserSave);
		btnUserCancel= (Button)getView().findViewById(R.id.btnUserCancel);
		//btnUserDelete = (Button)getView().findViewById(R.id.btnUserDelete);
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * gán sự kiện cho các control
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void settingListener(){
    	txtUserBirthday.setOnClickListener(this);
    	txtUserBirthday.setOnTouchListener(this);
		txtUserMarriedday.setOnClickListener(this);
		txtUserMarriedday.setOnTouchListener(this);
    	btnUserBirthday.setOnClickListener(this);
		btnUserMarriedday.setOnClickListener(this);
    	btnGoogleId.setOnClickListener(this);
		btnUserSave.setOnClickListener(this);
		btnUserCancel.setOnClickListener(this);
		//btnUserDelete.setOnClickListener(this);
		imgPrev.setOnClickListener(this);
		imgNext.setOnClickListener(this);
		chkGetMarried.setOnCheckedChangeListener(this);
		
		/** khi lost focus thi se tinh toan lai luong co bao gom phu cap nghiep vu va tieng Nhat*/
		txtUserSalary_NotAllowance.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(!hasFocus){
					salaryCalc();
				}
			}
		});
    }
    /** tinh toan lai so tien luong*/
	public void salaryCalc(){
		String TabOfFragmentOther =((EditUserMainActivity)getActivity()).getTabFragmentOther();
		EditUserOther fgUserOther= (EditUserOther)getActivity().getSupportFragmentManager().findFragmentByTag(TabOfFragmentOther);
		
		String japanese="";
		String allowance_business="";
		String allowance_room="";
		
		if(userInfo==null || userInfo.equals(null)){
			/** truong hop tao moi*/
		}else{
			/** truong hop chinh sua */
			japanese=userInfo.japanese;
			allowance_business=userInfo.allowance_business;
		}
		if (fgUserOther!=null){
			/** trình độ nhật ngữ*/
			japanese=fgUserOther.getJapaneseLevel();
			/** phụ cấp nghiệp vụ bậc mấy  */
			allowance_business=fgUserOther.getAllowance_Business();
		}
		float japanese_allowUSD =0;
		float business_allowUSD =0;
		float room_allowUSD =0;
		float salaryWithAllowance =0 ;
		/** get thong tin setting */
		SystemConfigItemHelper sysSetting = new SystemConfigItemHelper(getActivity().getApplicationContext());
		
		if (japanese.equals("")){
			japanese_allowUSD =0;
		}else{
			JapanseLevel japaneseLevel = JapanseLevel.valueOf(japanese);
			
			/** get phu cap tieng Nhat hien tai */
			switch (japaneseLevel){
				case N1:
					japanese_allowUSD=sysSetting.getJapaneseAllowanceN1();
					break;
				case N2:
					japanese_allowUSD=sysSetting.getJapaneseAllowanceN2();
					break;
				case N3:
					japanese_allowUSD=sysSetting.getJapaneseAllowanceN3();
					break;
				case N4:
					japanese_allowUSD=sysSetting.getJapaneseAllowanceN4();
					break;
				case N5:
					japanese_allowUSD=sysSetting.getJapaneseAllowanceN5();
					break;
			}
		}
		
		/** get phu cap nghiep vu */
		if (allowance_business.equals("Bậc 1")){
			business_allowUSD = sysSetting.getBusinessAllowanceLevel1();
		}
		if (allowance_business.equals("Bậc 2")){
			business_allowUSD = sysSetting.getBusinessAllowanceLevel2();
		}
		if (allowance_business.equals("Bậc 3")){
			business_allowUSD = sysSetting.getBusinessAllowanceLevel3();
		}
		if (allowance_business.equals("Bậc 4")){
			business_allowUSD = sysSetting.getBusinessAllowanceLevel4();
		}
		if (allowance_business.equals("Bậc 5")){
			business_allowUSD = sysSetting.getBusinessAllowanceLevel5();
		}
		
		/** get phu cap phong lam viec */
		if (allowance_room.equals("Mức 1")){
			room_allowUSD = sysSetting.getRoomAllowanceLevel1();
		}
		if (allowance_room.equals("Mức 2")){
			room_allowUSD = sysSetting.getRoomAllowanceLevel2();
		}
		if (allowance_room.equals("Mức 3")){
			room_allowUSD = sysSetting.getRoomAllowanceLevel3();
		}
		if (allowance_room.equals("Mức 4")){
			room_allowUSD = sysSetting.getRoomAllowanceLevel4();
		}
		if (allowance_room.equals("Mức 5")){
			room_allowUSD = sysSetting.getRoomAllowanceLevel5();
		}
		/** Tinh toan luong gom cac phu cap */
		salaryWithAllowance = getSalaryBasic() + japanese_allowUSD + business_allowUSD + room_allowUSD;
		setSalaryWithAllowance(salaryWithAllowance);
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * setting input cho control(không hiển thị bàn phím khi nhận focus)
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void settingInputType(){
		txtUserBirthday.setInputType(InputType.TYPE_NULL);
		txtUserMarriedday.setInputType(InputType.TYPE_NULL);
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * hiển thị dialog date khi chạm vào màn hình
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    @Override
    public boolean onTouch (View v, MotionEvent event){
    	/** phân nhánh xử lý theo từng button*/
		switch (v.getId()) {
			case R.id.txtBirthday:
				if(event.getAction()==MotionEvent.ACTION_UP){
					selectDate(v);
				}
				
			break;
			case R.id.txtMarriedDate:
				if(event.getAction()==MotionEvent.ACTION_UP){
					selectDate(v);
				}

				break;
		}
    	return true; 
    }

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * xử lý thay đổi item màn hình theo check box kết hôn
	 *
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch(buttonView.getId()){
			case R.id.chkGetMarried:
				/*txtUserMarriedday.setEnabled(isChecked);
				if(!isChecked){
					*//** xoa tri neu nhu chuyen tu kết hôn sang chua kết hôn*//*
					setMarriedday("");
				}*/
				break;
		}
	}

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * hiển thị dialog để chọn chụp hình hay là hiển thị hình từ media library
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void showalert()  
    {  
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());  
        builder.setTitle("Lựa chọn hình ảnh từ...")  
               //.setMessage("Select Pictures From Media Library")  
               .setCancelable(false)  
               .setPositiveButton(R.string.titleCancel, new DialogInterface.OnClickListener() {  
                   public void onClick(DialogInterface dialog, int id) {  
                      
  
                   }  
               })  
               .setNeutralButton("Lựa chọn từ thư viện", new DialogInterface.OnClickListener() {  
                   public void onClick(DialogInterface dialog, int id1) {  
                	   
                      Intent intent = new Intent();  
                            intent.setType("image/*");  
                            intent.setAction(Intent.ACTION_GET_CONTENT);  
                            startActivityForResult(Intent.createChooser(intent, "select picture"), SELECT_PICTURE);
                            
						/*if (Build.VERSION.SDK_INT <19){
						    //Intent intent = new Intent(); 
						    intent.setType("image/jpeg");
						    intent.setAction(Intent.ACTION_GET_CONTENT);
						    startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_picture)),GALLERY_INTENT_CALLED);
						} else {
						    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
						    intent.addCategory(Intent.CATEGORY_OPENABLE);
						    intent.setType("image/jpeg");
						    startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
						}*/
                   }  
               })  
                 .setNegativeButton("Chụp hình", new DialogInterface.OnClickListener() {  
                //camera function call  
                public void onClick(DialogInterface dialog, int id2) {  
                    Intent intentCamera  = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    //ContentValues values = new ContentValues();
                    //imageUri =getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    //intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    //intentCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    startActivityForResult(intentCamera,CAMERA_PIC_REQUEST);  
                      
                }  
                 });  
        AlertDialog alert = builder.create();  
        alert.show();  
    }  
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get path từ URI
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private String getPath(Uri uri)  
    {  
        // TODO Auto-generated method stub  
    	//2015.06.18 cap nhat cach get path from URI
        /*String[] projection = { MediaColumns.DATA };  
        Cursor cursor =getActivity().managedQuery(uri, projection, null, null, null);  
        columnIndex = cursor.getColumnIndexOrThrow(MediaColumns.DATA);  
        cursor.moveToFirst();  
        imagePath = cursor.getString(columnIndex);  
      
        return cursor.getString(columnIndex);*/
        //2015.06.18 cap nhat cach get path from URI
        //ImageHelper imgHelper = new ImageHelper();
        //return  ImageHelper.getRealPathFromURI(this.getActivity(), uri);
        return  ImageHelper.getPath(this.getActivity(), uri);
    }  
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * delete User 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public void deleteUser(final int code){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		/** Setting Dialog Title */
        alertDialog.setTitle(getResources().getString(R.string.titleDelete));

        /** Setting Dialog Message */
        alertDialog.setMessage("Bạn có muốn xóa nhân viên này không?");
        
        /** Setting Icon to Dialog*/
        alertDialog.setIcon(R.drawable.delete);
        /** Setting Positive "Yes" Button */
        alertDialog.setPositiveButton(getResources().getText(R.string.titleYes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	/** delete user */
            	
            	mDatabaseAdapter.open();
            	mDatabaseAdapter.deletePernamentUserByCode(code);
            	mDatabaseAdapter.close();
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
    /**
     * Kiem tra tri input
     * @return true : neu la khong co loi
     * 			false : co loi xay ra
     */
     
	public boolean checkInput(){
		//check xem ten co duoc nhap chua
		String name = txtUserFullName.getText().toString() ;
		if(name == null || name.isEmpty()){
			AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
			alertDialog.setTitle(getResources().getString(R.string.titleSave));
			alertDialog.setMessage("Hãy nhập họ tên nhân viên");
			alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
			    new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) {
			            dialog.dismiss();
			            txtUserFullName.requestFocus();
			        }
			    });
			alertDialog.show();
			return false;
		}
		else{
			return true;
		}
	}
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * save thong tin user da input 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public void saveUserToDbEntryPoint(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		/** Setting Dialog Title */
        alertDialog.setTitle(getResources().getString(R.string.titleSave));

        /** Setting Dialog Message */
        alertDialog.setMessage("Bạn có muốn lưu thông tin nhân viên này không?");
        
        /** Setting Icon to Dialog*/
        alertDialog.setIcon(R.drawable.save_back);
        /** Setting Positive "Yes" Button */
        alertDialog.setPositiveButton(getResources().getText(R.string.titleYes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	/** tinh lai luong */
            	salaryCalc();
            	saveUserToDbTask();
            }
        });
 
        /** Setting Negative "NO" Button */
        alertDialog.setNegativeButton(getResources().getText(R.string.titleNo), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
 
        /** Showing Alert Message */
        alertDialog.create().show();
        
	}
	private void saveUserToDbTask(){
		new AsyncTask<Void, Void, Boolean>()
	    {
	        ProgressDialog progressDialog;
	
	        @Override
			protected void onPreExecute() {
				progressDialog = new ProgressDialog(getActivity());
				progressDialog.setCancelable(false);
				progressDialog.setTitle("Lưu dữ liệu");
				progressDialog.setMessage("Xin vui lòng chờ...");
				progressDialog.show();
			}
	        
	        @Override
	        protected Boolean doInBackground(Void... params)
	        {
	            try
	            {
	            	
	            	/** Lưu vào DB */
					saveUserToDb();
					/** trả về kết quả*/
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putInt("btn", R.id.btnUserSave);
					bundle.putInt(MasterConstants.EXP_USER_GROUP_TAG, 0);
					bundle.putString(MasterConstants.LISTVIEW_CURRENT_POSITION, listViewCurrentPosition);
					intent.putExtras(bundle);
					//intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					getActivity().setResult(getActivity().RESULT_OK, intent);
					getActivity().finish();

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
					message="Lưu thành công.";
				}else{
					message="Lưu thất bại.";
				}
	        	AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
	            b.setTitle("Lưu dữ liệu");
	
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
     * 
     * lưu thông tin vào DB
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void saveUserToDb(){
    	try{

			User user = new User();
			mDatabaseAdapter.open();
			/** mã nhân viên sẽ update */
			if (nCode ==-1 || nCode ==0 ){
				/** tạo mới */
			}else{
				/** chỉnh sửa */
				user.code =nCode;
				/** get thong tin cua user */
				User sonzaiUser=getSonzaiUser(user.code);
				if(!(sonzaiUser==null)){
					user=sonzaiUser;
				}
			}
			/** google contact id  */
			user.google_id = txtGoogleId.getText().toString() ;
			/** tên  nhân viên  */
			user.full_name = txtUserFullName.getText().toString() ;
			user.first_name = Utils.getFirstName(user.full_name) + " " + Utils.getMiddleName(user.full_name);
			user.last_name= Utils.getLastName(user.full_name);
			/** giới tính  */
			user.sex=Boolean.toString( txtUserSex.isChecked()).equals("true")?1:0;
			/** hôn nhân */
			user.married = getGetMarried(); 
			/** ngày sinh */
			user.birthday=txtUserBirthday.getText().toString();
			/** ngày ket hon */
			user.married_date=txtUserMarriedday.getText().toString();
			/**address */
			user.address =txtUserAddress.getText().toString();
			/**tel */
			user.mobile =txtUserMobile.getText().toString();
			/**email */
			user.email =txtUserEmail.getText().toString();
			/**path of picture*/
			user.img_fullpath = imgUserFullPath.getText().toString();
			/** lương cơ bản */
			user.salary_notallowance =getSalaryBasic();
			/** lương bao gồm phụ cấp tiếng Nhật + nghiệp vụ */
			user.salary_allowance =getSalaryWithAllowance();
			/** hệ số đánh giá */
			user.estimate_point =getEstimatePoint();
			
			String TabOfFragmentWork =((EditUserMainActivity)getActivity()).getTabFragmentWork();
			EditUserWork fgUserWork = (EditUserWork)getActivity().getSupportFragmentManager().findFragmentByTag(TabOfFragmentWork);
			if (fgUserWork!=null){
				/**dept*/
				user.dept =fgUserWork.getDeptCode();
				user.dept_name =fgUserWork.getDeptName();
				user.team=fgUserWork.getTeamCode();
				user.team_name =fgUserWork.getTeamName();
				user.position=fgUserWork.getPositionCode();
				user.position_name=fgUserWork.getPositionName();
				user.training_date=fgUserWork.getTraining_date();
				user.training_dateEnd=fgUserWork.getTraining_dateEnd();
				
				user.learn_training_date=fgUserWork.getLearnTraining_date();
				user.learn_training_dateEnd=fgUserWork.getLearnTraining_dateEnd();
				
				user.convert_keiken = fgUserWork.getKeikenConvert();
				
				user.in_date=fgUserWork.getIn_date();
				user.join_date=fgUserWork.getJoin_date();
				user.out_date=fgUserWork.getOut_date();
				user.labour_out_date=fgUserWork.getLabourOut_date();
				user.isLabour=fgUserWork.getIsCurrentLabour();
			}
			
			String TabOfFragmentOther =((EditUserMainActivity)getActivity()).getTabFragmentOther();
			EditUserOther fgUserOther= (EditUserOther)getActivity().getSupportFragmentManager().findFragmentByTag(TabOfFragmentOther);
			if (fgUserOther!=null){
				/** trình độ nhật ngữ*/
				user.japanese=fgUserOther.getJapaneseLevel();
				/** phụ cấp nghiệp vụ bậc mấy  */
				user.allowance_business=fgUserOther.getAllowance_Business();
				/** phụ cấp phòng chuyên biệt  */
				user.allowance_room=fgUserOther.getAllowance_Room();
				/** phụ cấp BSE  */
				user.allowance_bse=fgUserOther.getAllowance_BSE();
				/** nghề nghiệp : LTV hay phiên dịch  */
				user.business_kbn =String.valueOf(fgUserOther.getBusinessKbn());
				/** năng suất lập trình */
				user.program =fgUserOther.getProgram();
				/** ghi chu */
				user.note =fgUserOther.getNote();
				
				/** trình độ nhật ngữ*/
				user.japanese=fgUserOther.getJapaneseLevel();
				/** nhóm chức danh : dùng để filter data tại màn hình search */
				List<UserPositionGroup> userPositionGroup =fgUserOther.getUserPositionGroupList();
				mDatabaseAdapter.deleteUserPositionGroup(user.code);
				for (UserPositionGroup userGroup : userPositionGroup){
					/** mã nhân viên sẽ update */
					if (nCode ==-1 || nCode ==0 ){
						/** tạo mới */
						userGroup.user_code=getUserCode(-1);
					}else{
						/** chỉnh sửa */
						userGroup.user_code=user.code;
					}
					
					mDatabaseAdapter.insertToUserPositionGroupTable(userGroup);
				}
				
				/** nhóm chức danh : dùng để filter data tại màn hình search */
				
				List<UserCustomerGroup> userCustomerGroup =fgUserOther.getUserCustomerGroupList();
				mDatabaseAdapter.deleteUserCustomerGroup(user.code);
				for (UserCustomerGroup userGroup : userCustomerGroup){
					/** mã nhân viên sẽ update */
					if (nCode ==-1 || nCode ==0 ){
						/** tạo mới */
						userGroup.user_code=getUserCode(-1);
					}else{
						/** chỉnh sửa */
						userGroup.user_code=user.code;
					}
					
					mDatabaseAdapter.insertToUserCustomerGroupTable(userGroup);
				}
			}
			
			if (nCode ==-1 || nCode ==0 ){
				/** tạo mới */
				mDatabaseAdapter.insertToUserTable(user);
			}else{
				/** insert vào DB */			
	    		mDatabaseAdapter.editToUserTable(user);
			}
			/** save image */
			saveUserImgToDB(user);
			/** update data lịch sử */
			saveUserHisToDb(user);
			
    		/** đóng connection */
    		mDatabaseAdapter.close();
    		
    	}catch ( Exception e){
    		Log.v(TAG,e.getMessage());
    		/** đóng connection */
    		mDatabaseAdapter.close();
    	}
    }
    private User getSonzaiUser(int code){
    	List<User> lstUser;
    	lstUser=mConvertCursorToListString.getUserList(" AND " + mDatabaseAdapter.KEY_CODE + "=" + code);
    	if(lstUser.size() >0){
    		return lstUser.get(0);
    	}else{
    		return null;
    	}
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * chuyển đổi bitmap file thành byte array 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public static byte[] getBitmapAsByArray(Bitmap bitmap){
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    	bitmap.compress(CompressFormat.PNG , 0, outputStream);
    	return outputStream.toByteArray();
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * save hình ảnh nhân viên
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void saveUserImgToDB(User user){
    	if(!user.img_fullpath.equals("")){
        	UserImage userImage = new UserImage();
        	userImage.setUser_code(user.code);
        	userImage.setImgFullPath(user.img_fullpath);
        	userImage.setName(user.full_name);
    		userImage.setImg(getBitmapAsByArray(BitmapFactory.decodeFile(user.img_fullpath)));
        	try{
        		mDatabaseAdapter.open();
        		/** xóa trước khi insert */
        		String xWhere =" AND " + DatabaseAdapter.KEY_USER_CODE + " = " + user.code;
        		mDatabaseAdapter.deleteUserImage(user.code);
        		/** insert */
        		mDatabaseAdapter.insertToUserImageTable(userImage);
        		mDatabaseAdapter.close();
        	}catch(Exception e){
        		mDatabaseAdapter.close();
        	}	
    	}
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getUserCode
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public int  getUserCode(int userCode){
    	int code ;
    	if (userCode==-1){
    		/** tạo mới */
    		mDatabaseAdapter.open();
    		code= mDatabaseAdapter.getMaxCodeUser();
    		mDatabaseAdapter.close();
    	}else{
    		/** chỉnh sửa */
    		code =userCode;
    	}
    	return code;
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setValueTabBasic
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void setValueTabBasic(User item){
		/**thông tin của tab Basic*/
    	
    	setUserCode(item.code);
    	setGoogleId(item.google_id);
		setFullName(item.full_name);
		setAddress(item.address);
		setBirthday(item.birthday);
		setMarriedday(item.married_date);
		setEmail(item.email);
		setImgFullPath(item.img_fullpath);
		setMobile(item.mobile);
		setSex(String.valueOf(item.sex ));
		setGetMarried(String.valueOf(item.married));
		setChkMarried_date(item.married_date);
		//viewUserImg();
		setSalaryBasic(item.salary_notallowance);
		setSalaryWithAllowance(item.salary_allowance);
		setEstimatePoint(item.estimate_point);
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * hiển thị image 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void viewUserImg(){
    	imageView = (ImageView)getView().findViewById(R.id.imgUser);
    	/** nếu không có hình */
    	if (getImgFullPath()==null || getImgFullPath().equals("")){
    		imageView.setImageResource(R.drawable.user);
    		return;
    	}
    	DecodeTask task = new DecodeTask(imageView);
        task.execute(getImgFullPath());
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setting trị cho các item của Tab Work
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void setValueTabWork(User item){
    	String TabOfFragmentWork =EditUserMainActivity.getTabFragmentWork();
		   
    	EditUserWork fgUserWork = (EditUserWork)getFragmentManager().findFragmentByTag(TabOfFragmentWork);
		   
		if (fgUserWork!=null){
			/**thông tin của tab Work*/
			fgUserWork.setDeptCode(String.valueOf(item.dept));
			fgUserWork.setDeptName(item.dept_name);
			fgUserWork.setTeamCode(String.valueOf(item.team));
			fgUserWork.setTeamName(item.team_name);
			fgUserWork.setPositionCode(String.valueOf(item.position));
			fgUserWork.setPositionName(item.position_name);
			
			fgUserWork.setTraining_date(item.training_date);
			fgUserWork.setTraining_dateEnd(item.training_dateEnd);
			
			fgUserWork.setLearnTraining_date(item.learn_training_date);
			fgUserWork.setLearnTraining_dateEnd(item.learn_training_dateEnd);
			
			fgUserWork.setKeikenConvert(item.convert_keiken);
			
			fgUserWork.setIn_date(item.in_date);
			fgUserWork.setOut_date(item.out_date);
			fgUserWork.setJoin_date(item.join_date);
			fgUserWork.setLabourOut_date(item.labour_out_date);
			
			fgUserWork.setIsCurrentLabour(String.valueOf(item.isLabour));
		}
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setting trị cho các item của tab Other
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void setValueTabOther(User item){
    	String TabOfFragmentOther =EditUserMainActivity.getTabFragmentOther();
		   
    	EditUserOther fgUserOther = (EditUserOther)getFragmentManager().findFragmentByTag(TabOfFragmentOther);
		   
		if (fgUserOther!=null){
			/**thông tin của tab Other*/
			fgUserOther.setJapaneseLevel(item.japanese);
			fgUserOther.setAllowance_Business(item.allowance_business);
			fgUserOther.setAllowance_Room(item.allowance_room);
			fgUserOther.setAllowance_BSE(String.valueOf(item.allowance_bse ));
			fgUserOther.setBusinessKbn(String.valueOf(item.business_kbn ));
			fgUserOther.setProgram((float)item.program);
			fgUserOther.setDetailDesign((float)item.detaildesign);
			fgUserOther.setPositionGroupList();
			fgUserOther.setNote(item.note);
		}
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * Tạo mới User
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private void createNewUser(User currentUser , boolean isCopy){
    	
		if(isCopy){
			EditUserMainActivity.nCode = 0;
			nCode = 0; /** tạo mới */
			EditUserMainActivity.userInfo = currentUser;
			userInfo= currentUser;
		}else{
			EditUserMainActivity.nCode = -1;
			nCode = -1; /** tạo mới */
			EditUserMainActivity.userInfo = null;
			userInfo= null;
		}

		Intent intent = new Intent(getActivity(), EditUserMainActivity.class);
		Bundle bundle = new Bundle();
		/**lấy code của user*/
		bundle.putInt(DatabaseAdapter.KEY_CODE, userInfo.code);
		bundle.putParcelable(MasterConstants.TAB_USER_TAG, userInfo);
		/**gán vào bundle để gửi cùng với intent */
		intent.putExtras(bundle);
		
		getActivity().finish();
		/**khởi tạo activity dùng để edit  */
		startActivity(intent);
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setUserCode
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setUserCode(int userCode){
    	txtUserCode.setText(String.valueOf(userCode) );
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getUserCode
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public int getUserCode(){
    	return Integer.parseInt(txtUserCode.getText().toString());
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * lấy path của image
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public String   getImgFullPath(){
    	return imgUserFullPath.getText().toString();
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setting path của image
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setImgFullPath(String value){
    	imgUserFullPath.setText(value );
    	imgPathSave = value;
    	if(userInfo!=null){
    		userInfo.img_fullpath =value;
    	}
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getGoogleId
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public String   getGoogleId(){
    	return txtGoogleId.getText().toString();
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setGoogleId
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setGoogleId(String value){
    	if(!value.equals("")){
    		txtGoogleId.setText(value );	
    	}else{
    		//doc tu application
    		for(SelectUser item: EmployeeTrackerApplication.googleContactList) {
    			if(item!=null && item.getNickname()!=null && userInfo!=null){
	    			if(item.getNickname().equals(userInfo.full_name)){
	    				txtGoogleId.setText(item.getGoogleId());
	    				break;
	    			}
    			}
    		}
    	}
    	

    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getFullName
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public String   getFullName(){
    	return txtUserFullName.getText().toString();
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setFullName
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setFullName(String value){
    	txtUserFullName.setText(value );
    	getActivity().setTitle(value);
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getSex
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public int  getSex(){
    	return Integer.parseInt( Boolean.toString(txtUserSex.isChecked()).toString());
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setSex
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setSex(String value){
    	if (value.equals("0")){
    		txtUserSex.setChecked(false);
    	}else{
    		txtUserSex.setChecked(true);
    	}
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setBirthday
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setBirthday(String value){
    	txtUserBirthday.setText(value );
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getBirthday
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public String   getBirthday(){
    	return txtUserBirthday.getText().toString();
    }

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 *
	 * setMarriedday
	 *
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public void  setMarriedday(String value){
		txtUserMarriedday.setText(value );
	}

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 *
	 * getMarriedday
	 *
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public String   getMarriedday(){
		return txtUserMarriedday.getText().toString();
	}

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 * set checkbox ngày kết hôn
	 *
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/

	public void setChkMarried_date(String value){
		if(value==null || value.equals("")){
			//chkGetMarried.setChecked(false);
			//txtUserMarriedday.setEnabled(false);
		}else{
			//chkGetMarried.setChecked(true);
			//txtUserMarriedday.setEnabled(true);
		}
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setAddress
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setAddress(String value){
    	txtUserAddress.setText(value );
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getAddress
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public String   getAddress(){
    	return txtUserAddress.getText().toString();
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setGetMarried
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setGetMarried(String value){
    	if(value.equals("1")){
    		chkGetMarried.setChecked(true);
    	}else{
    		chkGetMarried.setChecked(false);
    	}
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getGetMarried
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public int   getGetMarried(){
    	int value = 0;//chua ket hon
    	if(chkGetMarried.isChecked())
    		value =1;
    	return value;
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setMobile
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setMobile(String value){
    	txtUserMobile.setText(value );
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getMobile
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public String   getMobile(){
    	return txtUserMobile.getText().toString();
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setEmail
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setEmail(String value){
    	txtUserEmail.setText(value );
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getEmail
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public String   getEmail(){
    	return txtUserEmail.getText().toString();
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setSalaryBasic
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setSalaryBasic(float value){
    	txtUserSalary_NotAllowance.setText(String.valueOf( value ));
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getSalaryBasic
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public float   getSalaryBasic(){
    	String value =txtUserSalary_NotAllowance.getText().toString();
    	if(value==null || value.equals("")){
    		value="0";
    	}
    	return Float.parseFloat( value);
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setSalaryWithAllowance
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setSalaryWithAllowance(float value){
    	txtUserSalary_Allowance.setText(String.valueOf( value ));
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getSalaryWithAllowance
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public float   getSalaryWithAllowance(){
    	String value =txtUserSalary_Allowance.getText().toString();
    	if(value==null || value.equals("")){
    		value="0";
    	}
    	return Float.parseFloat( value);
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setSalaryTotal
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setSalaryTotal(float value){
    	txtUserSalaryTotal.setText(String.valueOf( value ));
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getSalaryTotal
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public float   getSalaryTotal(){
    	String value =txtUserSalaryTotal.getText().toString();
    	if(value==null || value.equals("")){
    		value="0";
    	}
    	return Float.parseFloat( value);
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setEstimatePoint
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void  setEstimatePoint(float value){
    	txtUserEstimatePoint.setText(String.valueOf( value ));
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * getEstimatePoint
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public float   getEstimatePoint(){
    	String value =txtUserEstimatePoint.getText().toString();
    	if(value==null || value.equals("")){
    		value="0";
    	}
    	return Float.parseFloat( value);
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * setIsFirstRun
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void setIsFirstRun(boolean value){
    	this.isFirstRun=value;
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * 
     * set tri cho title bar cua man hinh dang ky user
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void setUserTitleBar(String value){
    	if(value.equals("") || value==null){
    		txtUserTitle.setText(getResources().getString(R.string.userTitleInfor));
    	}else{
    		txtUserTitle.setText(value);
    	}
    	
    }
    /**▽▽▽▽▽▽▽▽▽▽▽▽▽Phần update lịch sử start ▽▽▽▽▽▽▽▽▽▽▽▽▽▽*/
    
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	* 
	* lưu thông tin vào DB (Xu ly giong voi man hinh cap nhat lich su )
	* 
	▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public void saveUserHisToDb(User user){
	try{
	
		UserHistory userInsert ;
		Date dat = DateTimeUtil.getCurrentDate(MasterConstants.DATE_VN_FORMAT);
		String date_from = DateTimeUtil.convertDateToString(dat, MasterConstants.DATE_VN_FORMAT);
				
		/** cap nhat cho truong hop la phong ban thay doi */
		if(isDeptInput(user)){
			/**get thong tin phong ban hien tai trong lich su ) */ 
			int deptHis = getCurrentDeptHis(user.code);
			/**neu co su khac nhau ve phong ban*/
			if(deptHis != user.dept){
				/** xoa neu nhu da co data tuong ung voi ngay thang nam tren man hinh*/
				deleteUserHisByDate(date_from,user.code,MasterConstants.MASTER_MKBN_DEPT_HIS);
				/** tạo đối tượng dùng để update*/
				userInsert = getUserHistory(MasterConstants.MASTER_MKBN_DEPT_HIS,user);
				/** thực thi update */
				mDatabaseAdapter.open();
				mDatabaseAdapter.insertToUserHisTable(userInsert);
				mDatabaseAdapter.close();
				/** chỉnh sửa lại ngày tháng năm start -end cho đúng */
				correctHisData(MasterConstants.MASTER_MKBN_DEPT_HIS,userInsert.user_code);	
			}
		}
		
		/** cap nhat cho truong hop la nhom thay doi */
		if(isTeamInput(user)){
			/**get thong tin nhom (team) hien tai trong lich su ) */
			int teamHis = getCurrentTeamHis(user.code);
			/**neu co su khac nhau ve nhom to */
			if (teamHis != user.team){
				/** xoa neu nhu da co data tuong ung voi ngay thang nam tren man hinh*/
				deleteUserHisByDate(date_from,user.code,MasterConstants.MASTER_MKBN_TEAM_HIS);
				/** tạo đối tượng dùng để update*/
				userInsert = getUserHistory(MasterConstants.MASTER_MKBN_TEAM_HIS,user);
				
				/** thực thi update */
				mDatabaseAdapter.open();
				mDatabaseAdapter.insertToUserHisTable(userInsert);
				mDatabaseAdapter.close();
				/** chỉnh sửa lại ngày tháng năm start -end cho đúng */
				correctHisData(MasterConstants.MASTER_MKBN_TEAM_HIS,userInsert.user_code);
			}
		}
		/** cap nhat cho truong hop la cấp bậc thay doi */
		if(isPositionInput(user) ){
			/**get thong tin chuc vu hien tai trong lich su ) */
			int positionHis = getCurrentPositionHis(user.code);
			/**neu co su khac nhau ve chuc vu */
			if (positionHis != user.position){
				/** xoa neu nhu da co data tuong ung voi ngay thang nam tren man hinh*/
				deleteUserHisByDate(date_from,user.code,MasterConstants.MASTER_MKBN_POSITION_HIS);
				/** tạo đối tượng dùng để update*/
				userInsert = getUserHistory(MasterConstants.MASTER_MKBN_POSITION_HIS,user);
				/** thực thi update */
				mDatabaseAdapter.open();
				mDatabaseAdapter.insertToUserHisTable(userInsert);
				mDatabaseAdapter.close();
				/** chỉnh sửa lại ngày tháng năm start -end cho đúng */
				correctHisData(MasterConstants.MASTER_MKBN_POSITION_HIS,userInsert.user_code);	
			}
		}
		/** cap nhat cho truong hop la chứng chỉ tiếng Nhật thay doi */
		if(isJapaneseInput(user) ){
			/**get thong tin bang cap tieng Nhat hien tai trong lich su ) */
			String japaneseHis = getCurrentJapaneseHis(user.code);
			/**Neu nhu co su thay doi */
			if(!japaneseHis.equals(user.japanese)){
				/** xoa neu nhu da co data tuong ung voi ngay thang nam tren man hinh*/
				deleteUserHisByDate(date_from,user.code,MasterConstants.MASTER_MKBN_JAPANESE_HIS);
				/** tạo đối tượng dùng để update*/
				userInsert = getUserHistory(MasterConstants.MASTER_MKBN_JAPANESE_HIS,user);
				/** thực thi update */
				mDatabaseAdapter.open();
				mDatabaseAdapter.insertToUserHisTable(userInsert);
				mDatabaseAdapter.close();
				/** chỉnh sửa lại ngày tháng năm start -end cho đúng */
				correctHisData(MasterConstants.MASTER_MKBN_JAPANESE_HIS,userInsert.user_code);
			}
		}
		/** cap nhat cho truong hop la trợ cấp nghiệp vụ thay doi */
		if(isAllowance_BusinessInput(user) ){
			/**get thong tin tro cap nghiep vu hien tai trong lich su ) */
			String allowanceHis = getCurrentAllowanceBusinessHis(user.code);
			/**Neu nhu co su thay doi */
			Collator compare = Collator.getInstance(new Locale("vi", "vn"));
			int comparison = compare.compare(allowanceHis, user.allowance_business);
			//if(allowanceHis !=user.allowance_business ){
			if(comparison!=0 ){
				/** xoa neu nhu da co data tuong ung voi ngay thang nam tren man hinh*/
				deleteUserHisByDate(date_from,user.code,MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS);
				/** tạo đối tượng dùng để update*/
				userInsert = getUserHistory(MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS,user);
				/** thực thi update */
				mDatabaseAdapter.open();
				mDatabaseAdapter.insertToUserHisTable(userInsert);
				mDatabaseAdapter.close();
				/** chỉnh sửa lại ngày tháng năm start -end cho đúng */
				correctHisData(MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS,userInsert.user_code);	
			}
			
		}
		/** cap nhat cho truong hop la trợ cấp BSR thay doi */
		if(isAllowance_BSEInput(user) ){
			/**get thong tin tro cap nghiep vu hien tai trong lich su ) */
			String allowanceBSEHis = getCurrentAllowanceBSEHis(user.code);
			/**Neu nhu co su thay doi */
			Collator compare = Collator.getInstance(new Locale("vi", "vn"));
			int comparison = compare.compare(allowanceBSEHis, user.allowance_bse);
			//if(allowanceHis !=user.allowance_business ){
			if(comparison!=0 ){
				/** xoa neu nhu da co data tuong ung voi ngay thang nam tren man hinh*/
				deleteUserHisByDate(date_from,user.code,MasterConstants.MASTER_MKBN_ALLOWANCE_BSE_HIS);
				/** tạo đối tượng dùng để update*/
				userInsert = getUserHistory(MasterConstants.MASTER_MKBN_ALLOWANCE_BSE_HIS,user);
				/** thực thi update */
				mDatabaseAdapter.open();
				mDatabaseAdapter.insertToUserHisTable(userInsert);
				mDatabaseAdapter.close();
				/** chỉnh sửa lại ngày tháng năm start -end cho đúng */
				correctHisData(MasterConstants.MASTER_MKBN_ALLOWANCE_BSE_HIS,userInsert.user_code);
			}

		}
		/** cap nhat cho truong hop la salary thay doi */

		/**get thong tin luong hien tai trong lich su ) */
		float salaryHis = getCurrentSalaryHis(user.code);
		/**Neu nhu co su thay doi */

		if(salaryHis !=user.salary_notallowance ){
			/** xoa neu nhu da co data tuong ung voi ngay thang nam tren man hinh*/
			deleteUserHisByDate(date_from,user.code,MasterConstants.MASTER_MKBN_SALARY_HIS);
			/** tạo đối tượng dùng để update*/
			userInsert = getUserHistory(MasterConstants.MASTER_MKBN_SALARY_HIS,user);
			/** thực thi update */
			mDatabaseAdapter.open();
			mDatabaseAdapter.insertToUserHisTable(userInsert);
			mDatabaseAdapter.close();
			/** chỉnh sửa lại ngày tháng năm start -end cho đúng */
			correctHisData(MasterConstants.MASTER_MKBN_SALARY_HIS,userInsert.user_code);	
		}
		
		/** cap nhat lai tri moi nhat cua phong ban ---cho nhan vien */
		//updateUserMaster(user.code);
		
		
	}catch ( Exception e){
		Log.v(TAG,e.getMessage());
		/** đóng connection */
		mDatabaseAdapter.close();
	}
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	* 
	* xóa thông tin lịch sử dựa vào code nhân viên và ngày tháng năm hữu hiệu.
	* 
	▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public UserHistory getUserHistory(int mkbn, User user){
	UserHistory userInsert; 
	userInsert = new UserHistory();
	
	userInsert.mkbn = mkbn;
	userInsert.user_code = user.code;
	userInsert.date_from = DateTimeUtil.convertDateToString(DateTimeUtil.getCurrentDate(MasterConstants.DATE_VN_FORMAT), MasterConstants.DATE_VN_FORMAT);
	userInsert.new_dept_code = user.dept;
	userInsert.new_dept_name = user.dept_name;
	userInsert.new_team_code = user.team;
	userInsert.new_team_name = user.team_name;
	userInsert.new_position_code = user.position;
	userInsert.new_position_name = user.position_name;
	userInsert.new_japanese= user.japanese;
	userInsert.new_allowance_business= user.allowance_business;
	
	userInsert.yobi_real1 = user.salary_notallowance;//salary
	userInsert.new_salary = user.salary_notallowance;//salary
	
	return userInsert;
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	* 
	* xóa thông tin lịch sử dựa vào code nhân viên và ngày tháng năm hữu hiệu.
	* 
	▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/ 
	public void deleteUserHisByDate(String datefrom, int user_code, int hisType){
	String xWhere ="";
	xWhere =xWhere	+	" AND " + DatabaseAdapter.KEY_USER_CODE + " = " + user_code;
	xWhere =xWhere  + 	" AND " + DatabaseAdapter.KEY_DATE_FROM + " = '" + DateTimeUtil.formatDate2String(datefrom , MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT) + "'";
	try{
		mDatabaseAdapter.open();
		switch(hisType){
			case MasterConstants.MASTER_MKBN_DEPT_HIS:
				mDatabaseAdapter.deleteUserHisDeptByCode(xWhere);
				break;
			case MasterConstants.MASTER_MKBN_TEAM_HIS:
				mDatabaseAdapter.deleteUserHisTeamByCode(xWhere);
				break;
			case MasterConstants.MASTER_MKBN_POSITION_HIS:
				mDatabaseAdapter.deleteUserHisPositionByCode(xWhere);
				break;
			case MasterConstants.MASTER_MKBN_JAPANESE_HIS:
				mDatabaseAdapter.deleteUserHisJapaneseByCode(xWhere);
				break;
			case MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS:
				mDatabaseAdapter.deleteUserHisAllowance_BusinessByCode(xWhere);
				break;
		}
		
		mDatabaseAdapter.close();
	}catch(Exception e ){
		mDatabaseAdapter.close();
	}
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	* 
	* chinh sua lai data cho dung theo nhu trinh tu cua thang nam thay doi
	* 
	▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	@SuppressWarnings("deprecation")
	public void correctHisData(int type,int user_code){
	String xWhere="";
	String xOrderBy="";
	
	xWhere = " AND " + DatabaseAdapter.KEY_USER_CODE + " = " + user_code; 
	xOrderBy = DatabaseAdapter.KEY_DATE_FROM + " ASC ";
	/** get danh sách các data lịch sử của user */
	List<UserHistory> userhis = mConvertCursorToListString.getUserHisList(type, xWhere,xOrderBy);
	if(userhis.size()==0){
		//khong co du lieu
		
	} else if(userhis.size()==1){
		//chi co 1 dong nên không cần update lại ngày end
		
	} else if(userhis.size()> 1){
		UserHistory hisnext;
		for(int i=0 ; i<userhis.size();i++){
			if(i+1 <userhis.size()){
				//get recoed tiep theo
				hisnext = userhis.get(i+1);
			}else{
				hisnext=null;
			}
			if(hisnext==null){
				userhis.get(i).date_to ="";
			}else{
				Date dt = DateTimeUtil.convertStringToDate(hisnext.date_from, MasterConstants.DATE_VN_FORMAT);
				Calendar cal = Calendar.getInstance(); 
				cal.set(dt.getYear()+ 1900, dt.getMonth(), dt.getDate());
				
				cal.add(Calendar.DATE, -1);
				String date =DateTimeUtil.convertDateToString(cal.getTime(),MasterConstants.DATE_VN_FORMAT);
				userhis.get(i).date_to =date;
				//userhis.get(i).date_to =hisnext.date_from;
			}
			try{
				mDatabaseAdapter.open();
				mDatabaseAdapter.editToUserHisTable(userhis.get(i));
				mDatabaseAdapter.close();
			}catch(Exception e){
				mDatabaseAdapter.close();
			}
		}
	}
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	* 
	* cập nhật lại thông tin cho master nhân viên
	* 
	▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public void updateUserMaster(User user){
	/** cap nhat lai cac thong tin phong ban -team-chuc vu moi nhat cho user */
	try{
		List<User> userList = mConvertCursorToListString.getUserList(" AND " + DatabaseAdapter.KEY_CODE + "=" + user.code);
		if(userList.size()>0){
			/** update */
			User usr = userList.get(0);
			if(isDeptInput(user)){
				Dept dept = getNewestUserHisDeptInfo(user.code);
				usr.dept =dept.code;
	    		usr.dept_name = dept.name;
			}
			if(isTeamInput(user)){
				Team team = getNewestUserHisTeamInfo(user.code);
				usr.team = team.code;
	    		usr.team_name = team.name;
			}
			if(isPositionInput(user)){
				Position position= getNewestUserHisPositionInfo(user.code);
				usr.position = position.code;
	    		usr.position_name = position.name;
			}
			if(isJapaneseInput(user)){
				String jp= getNewestUserHisJapaneseInfo(user.code);
				usr.japanese= jp;
			}
			
			if(isAllowance_BusinessInput(user)){
				String al= getNewestUserHisAllowance_BusinessInfo(user.code);
				usr.allowance_business= al;
			}
			
			mDatabaseAdapter.open();
			mDatabaseAdapter.editToUserTable(usr);
			mDatabaseAdapter.close();
		}
	}catch(Exception e){
		mDatabaseAdapter.close();
	}
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	* 
	* get thông tin về code phòng ban mới nhất của nhân viên
	* 
	▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public Dept getNewestUserHisDeptInfo(int user_code ){
	String xWhere ="";
	String xOrderBy ="";
	Dept dept=null;
	dept= new Dept();
	
	xWhere = " AND " + DatabaseAdapter.KEY_USER_CODE + " = " + user_code; 
	xOrderBy = "date(" + DatabaseAdapter.KEY_DATE_FROM + ") DESC ";
	/** get danh sách các data lịch sử của user */
	List<UserHistory> userhis = mConvertCursorToListString.getUserHisList(MasterConstants.MASTER_MKBN_DEPT_HIS, xWhere,xOrderBy);
	if(userhis.size()==0){
	}else{
		dept.code = userhis.get(0).new_dept_code;
		dept.name = userhis.get(0).new_dept_name;
	}
	
	return dept;
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	* 
	* get thông tin về code phòng ban mới nhất của nhân viên
	* 
	▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public Team getNewestUserHisTeamInfo(int user_code ){
	String xWhere ="";
	String xOrderBy ="";
	Team team=null;
	team= new Team();
	
	xWhere = " AND " + DatabaseAdapter.KEY_USER_CODE + " = " + user_code; 
	xOrderBy = "date(" + DatabaseAdapter.KEY_DATE_FROM + ") DESC ";
	/** get danh sách các data lịch sử của user */
	List<UserHistory> userhis = mConvertCursorToListString.getUserHisList(MasterConstants.MASTER_MKBN_TEAM_HIS, xWhere,xOrderBy);
	if(userhis.size()==0){
	}else{
		team.code = userhis.get(0).new_team_code;
		team.name = userhis.get(0).new_team_name;
	}
	
	return team;
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	* 
	* get thông tin về code phòng ban mới nhất của nhân viên
	* 
	▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public Position getNewestUserHisPositionInfo(int user_code ){
		String xWhere ="";
		String xOrderBy ="";
		Position position=null;
		position= new Position();
		
		xWhere = " AND " + DatabaseAdapter.KEY_USER_CODE + " = " + user_code; 
		xOrderBy = "date(" + DatabaseAdapter.KEY_DATE_FROM + ") DESC ";
		/** get danh sách các data lịch sử của user */
		List<UserHistory> userhis = mConvertCursorToListString.getUserHisList(MasterConstants.MASTER_MKBN_POSITION_HIS, xWhere,xOrderBy);
		if(userhis.size()==0){
		}else{
			position.code = userhis.get(0).new_position_code;
			position.name = userhis.get(0).new_position_name;
		}
		return position;
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	* 
	* get thông tin về chứng chỉ tiếng Nhật mới nhất của nhân viên
	* 
	▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public String getNewestUserHisJapaneseInfo(int user_code ){
		String xWhere ="";
		String xOrderBy ="";
		String japanese ="";
		
		xWhere = " AND " + DatabaseAdapter.KEY_USER_CODE + " = " + user_code; 
		xOrderBy = "date(" + DatabaseAdapter.KEY_DATE_FROM + ") DESC ";
		/** get danh sách các data lịch sử của user */
		List<UserHistory> userhis = mConvertCursorToListString.getUserHisList(MasterConstants.MASTER_MKBN_JAPANESE_HIS, xWhere,xOrderBy);
		if(userhis.size()==0){
		}else{
			japanese= userhis.get(0).new_japanese;
			
		}
		return japanese;
	
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	* 
	* get thông tin về trợ cấp nghiệp vụ mới nhất của nhân viên
	* 
	▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public String getNewestUserHisAllowance_BusinessInfo(int user_code ){
		String xWhere ="";
		String xOrderBy ="";
		String allowance_business ="";
		
		xWhere = " AND " + DatabaseAdapter.KEY_USER_CODE + " = " + user_code; 
		xOrderBy = "date(" + DatabaseAdapter.KEY_DATE_FROM + ") DESC ";
		/** get danh sách các data lịch sử của user */
		List<UserHistory> userhis = mConvertCursorToListString.getUserHisList(MasterConstants.MASTER_MKBN_ALLOWANCE_BUSINESS_HIS, xWhere,xOrderBy);
		if(userhis.size()==0){
		}else{
			allowance_business= userhis.get(0).new_allowance_business;
			
		}
		return allowance_business;
	
	}

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 *
	 * get thông tin về trợ cấp BSE mới nhất của nhân viên
	 *
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public String getNewestUserHisAllowance_BSEInfo(int user_code ){
		String xWhere ="";
		String xOrderBy ="";
		String allowance_bse ="";

		xWhere = " AND " + DatabaseAdapter.KEY_USER_CODE + " = " + user_code;
		xOrderBy = "date(" + DatabaseAdapter.KEY_DATE_FROM + ") DESC ";
		/** get danh sách các data lịch sử của user */
		List<UserHistory> userhis = mConvertCursorToListString.getUserHisList(MasterConstants.MASTER_MKBN_ALLOWANCE_BSE_HIS, xWhere,xOrderBy);
		if(userhis.size()==0){
		}else{
			allowance_bse= userhis.get(0).new_allowance_bse;

		}
		return allowance_bse;

	}

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	* 
	* get thông tin về luong mới nhất của nhân viên
	* 
	▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public float getNewestUserHisSalaryInfo(int user_code ){
		String xWhere ="";
		String xOrderBy ="";
		float salary =0;
		
		xWhere = " AND " + DatabaseAdapter.KEY_USER_CODE + " = " + user_code; 
		xOrderBy = "date(" + DatabaseAdapter.KEY_DATE_FROM + ") DESC ";
		/** get danh sách các data lịch sử của user */
		List<UserHistory> userhis = mConvertCursorToListString.getUserHisList(MasterConstants.MASTER_MKBN_SALARY_HIS, xWhere,xOrderBy);
		if(userhis.size()==0){
		}else{
			salary= userhis.get(0).yobi_real1;
			//salary= userhis.get(0).new_salary;
		}
		return salary;
	
	}
	
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	* 
	* kiểm tra xem item code phong ban có input chưa ?
	* 
	▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public boolean isDeptInput(User item){
		return true;
		/*if(item.dept!=0){
			return true;
		}else{
			return false;
		}*/
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	* 
	* kiểm tra xem item code nhóm có input chưa ?
	* 
	▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public boolean isTeamInput(User item){
		return true;
		/*if(item.team!=0 ){
			return true;
		}else{
			return false;
		}*/
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	* 
	* kiểm tra xem item code chức vụ có input chưa ?
	* 
	▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public boolean isPositionInput(User item){
		return true;
		/*if(item.position!=0 ){
			return true;
		}else{
			return false;
		}*/
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	* 
	* kiểm tra xem item chứng chỉ tiếng Nhật có input chưa ?
	* 
	▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public boolean isJapaneseInput(User item){
		return true;
		/*if(item.japanese!="" && item.japanese!=null){
			return true;
		}else{
			return false;
		}*/
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	* 
	* kiểm tra xem item trợ cấp nghiệp vụ có input chưa ?
	* 
	▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public boolean isAllowance_BusinessInput(User item){
		return true;
		/*if(item.allowance_business!="" && item.allowance_business!=null){
			return true;
		}else{
			return false;
		}*/
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 *
	 * kiểm tra xem item PC BSE có input chưa ?
	 *
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public boolean isAllowance_BSEInput(User item){
		return true;
		/*if(item.allowance_bse!="" && item.allowance_bse!=null){
			return true;
		}else{
			return false;
		}*/
	}
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 *
	 * kiểm tra xem item PC phòng chuyên biệt có input chưa ?
	 *
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public boolean isAllowance_RoomInput(User item){
		return true;
		/*if(item.allowance_room!="" && item.allowance_room!=null){
			return true;
		}else{
			return false;
		}*/
	}

    /**▽▽▽▽▽▽▽▽▽▽▽▽▽Phần update lịch sử end ▽▽▽▽▽▽▽▽▽▽▽▽▽▽*/
	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	* 
	* get thong tin bo phan moi nhat tai lich su phong ban
	* 
	▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public int getCurrentDeptHis(int user_code ){
    	Dept dept= null;
    	dept= getNewestUserHisDeptInfo(user_code);
    	return dept.code;
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	* 
	* get thong tin nhom moi nhat tai lich su phong ban
	* 
	▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public int getCurrentTeamHis(int user_code ){
    	Team team= null;
    	team= getNewestUserHisTeamInfo(user_code);
    	return team.code;
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	* 
	* get thong tin chuc vu moi nhat tai lich su phong ban
	* 
	▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public int getCurrentPositionHis(int user_code ){
    	Position position= null;
    	position= getNewestUserHisPositionInfo(user_code);
    	return position.code;
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	* 
	* get thong tin bang cap tieng nhat moi nhat tai lich su phong ban
	* 
	▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public String getCurrentJapaneseHis(int user_code ){
    	String kekka="";
    	kekka= getNewestUserHisJapaneseInfo(user_code);
    	return kekka;
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	* 
	* get thong tin phu cap nghiep vu moi nhat tai lich su phong ban
	* 
	▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public String getCurrentAllowanceBusinessHis(int user_code ){
    	String kekka="";
    	kekka= getNewestUserHisAllowance_BusinessInfo(user_code);
    	return kekka;
    }

	/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	 *
	 * get thong tin phu cap BSE moi nhat tai lich su phong ban
	 *
	 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public String getCurrentAllowanceBSEHis(int user_code ){
		String kekka="";
		kekka= getNewestUserHisAllowance_BSEInfo(user_code);
		return kekka;
	}

    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
	* 
	* get thong tin phu cap nghiep vu moi nhat tai lich su phong ban
	* 
	▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public float getCurrentSalaryHis(int user_code ){
    	float kekka=0;
    	kekka= getNewestUserHisSalaryInfo(user_code);
    	return kekka;
    }


	
}
