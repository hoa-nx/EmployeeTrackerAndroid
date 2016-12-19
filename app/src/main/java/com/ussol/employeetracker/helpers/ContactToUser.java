package com.ussol.employeetracker.helpers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.ussol.employeetracker.R;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.models.SelectUser;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.utils.DateTimeUtil;
import com.ussol.employeetracker.utils.Utils;

public class ContactToUser {
	User user ;
	DatabaseAdapter mDatabaseAdapter ;
	Context _context ;
	public ContactToUser(Context context){
		mDatabaseAdapter = new DatabaseAdapter(context);
		_context = context;
	}
	/** */
	public void InsertContactToDB(SelectUser contact) throws IOException
	{
		mDatabaseAdapter.open();
		user = new User();
		/** google contact id */
		user.google_id = contact.getGoogleId();
		/** tên nhân viên */
		user.full_name = contact.getNickname();
		user.first_name = Utils.getFirstName(user.full_name) + " " + Utils.getMiddleName(user.full_name);
		user.last_name= Utils.getLastName(user.full_name);

		/** giới tính */
		user.sex =1;//mac dinh de lam NAM
		/** ngày tháng năm sinh */
		String birthday ="";
		/*
		if(contact.getBirthDay()==""){
			birthday="";
		}else if (DateTime.isDate(contact.getBirthDay().replace("/", MasterConstants.DATE_SEPERATE_CHAR), MasterConstants.DATE_VN_FORMAT)){
			// format theo lich viet 
			birthday = DateTime.formatDate2String(contact.getBirthDay().replace("/", MasterConstants.DATE_SEPERATE_CHAR), MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT);
		}else if (DateTime.isDate(contact.getBirthDay().replace("/", MasterConstants.DATE_SEPERATE_CHAR), MasterConstants.DATE_JP_FORMAT)){
			// format theo lich nhật
			birthday=contact.getBirthDay();
		}
		*/
		if(contact.getBirthDay()!=""){
			user.birthday = DateTimeUtil.formatDate2String(contact.getBirthDay().replace("/", MasterConstants.DATE_SEPERATE_CHAR), MasterConstants.DATE_JP_FORMAT, MasterConstants.DATE_VN_FORMAT);
		}
		
		/** địa chỉ */
		user.address = contact.getAddress();
		/** điện thoại  */
		user.mobile = contact.getPhone();
		/** email */
		user.email = contact.getEmail();
		
		/** nghề nghiệp */
		user.business_kbn = "1";//developer
		
		/** ngày vào công ty */
		String training_date ="";
		/*
		if(contact.getTraining_date()==""){
			training_date="";
		}else if (DateTime.isDate(contact.getTraining_date().replace("/", MasterConstants.DATE_SEPERATE_CHAR), MasterConstants.DATE_VN_FORMAT)){
			// format theo lich viet 
			training_date = DateTime.formatDate2String(contact.getTraining_date().replace("/", MasterConstants.DATE_SEPERATE_CHAR), MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT);
		}else if (DateTime.isDate(contact.getTraining_date().replace("/", MasterConstants.DATE_SEPERATE_CHAR), MasterConstants.DATE_JP_FORMAT)){
			//format theo lich nhật 
			training_date=contact.getTraining_date();
		}
		*/
		//user.training_date=  contact.getTraining_date().replace("/", MasterConstants.DATE_SEPERATE_CHAR);
		/** ngày ky HD */
		String in_date ="";
		/*
		if(contact.getIn_date()==""){
			in_date="";
		}else if (DateTime.isDate(contact.getIn_date().replace("/", MasterConstants.DATE_SEPERATE_CHAR), MasterConstants.DATE_VN_FORMAT)){
			// format theo lich viet 
			in_date = DateTime.formatDate2String(contact.getIn_date().replace("/", MasterConstants.DATE_SEPERATE_CHAR), MasterConstants.DATE_VN_FORMAT, MasterConstants.DATE_JP_FORMAT);
		}else if (DateTime.isDate(contact.getIn_date().replace("/", MasterConstants.DATE_SEPERATE_CHAR), MasterConstants.DATE_JP_FORMAT)){
			/// format theo lich nhật 
			in_date=contact.getIn_date();
		}
		*/
		
		//user.in_date = contact.getIn_date().replace("/", MasterConstants.DATE_SEPERATE_CHAR);

		/** note */
		user.note = contact.getNote();
		//xu ly get image
		//MEDIA GALLERY 
		
        /** get id của image vừa chụp */
        int imageID =Utils.getRandomNumberBetween(100000, 999999999);
        /** get path của image vừa chụp */
        String destPath = MasterConstants.DIRECTORY + MasterConstants.DIRECTORY_PICTURE + imageID + MasterConstants.IMAGE_SMALL_SUFFIX;
        /** tạo file để copy */
        File destination= new File( destPath);
        destination.createNewFile();
        
        //Convert bitmap to byte array
        Bitmap bitmap = contact.getThumb();
        if(bitmap==null){
        	bitmap = BitmapFactory.decodeResource(_context.getResources(),R.drawable.user);
        }
        /** chinh sua image */
        Bitmap disBitmap = bitmap;	//BitmapFactory.decodeFile(destination.getAbsolutePath());
        int desiredImageWidth = 1000;  // pixels
		int desiredImageHeight = 1200; // pixels

		BitmapFactory.Options o = new BitmapFactory.Options();
		Bitmap newImage=Bitmap.createScaledBitmap(disBitmap, desiredImageWidth, desiredImageHeight, false);
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        newImage.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = new FileOutputStream(destination);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();
		
		user.img_fullpath = destination.getAbsolutePath();
		
		mDatabaseAdapter.insertToUserTable(user);
	    		    
	    mDatabaseAdapter.close();

	}
}
