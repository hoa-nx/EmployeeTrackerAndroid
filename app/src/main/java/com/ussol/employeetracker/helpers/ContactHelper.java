/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  

package com.ussol.employeetracker.helpers;

import java.io.IOException;
import java.util.ArrayList;

import com.ussol.employeetracker.ContactListActivity;
import com.ussol.employeetracker.models.SelectUser;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
 * onCreate
 * 
 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
public class ContactHelper {
	Context ctx;
	
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * khởi tạo ContactHelper
     * @param Context của ứng dụng 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
	public ContactHelper(Context context){
		ctx = context;
	}
	
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * nhận về các contact trong máy
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/	
	public void getAllContact(){
		Cursor cursor = ctx.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null); 
		while (cursor.moveToNext()) { 
		   String contactId = cursor.getString(cursor.getColumnIndex( 
		   ContactsContract.Contacts._ID)); 
		   String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)); 
		   if (Boolean.parseBoolean(hasPhone)) { 
		      // You know it has a number so now query it like this
		      Cursor phones = ctx.getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, null, null); 
		      while (phones.moveToNext()) { 
		         String phoneNumber = phones.getString(phones.getColumnIndex( ContactsContract.CommonDataKinds.Phone.NUMBER));                 
		      } 
		      phones.close(); 
		   }

		   Cursor emails = ctx.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null); 
		   while (emails.moveToNext()) { 
		      // This would allow you get several email addresses 
		      String emailAddress = emails.getString( 
		      emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)); 
		   } 
		   emails.close();
		   
		   Cursor nickname = ctx.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null); 
		   while (nickname.moveToNext()) { 
		      // This would allow you get several email addresses 
		      String nicknameData = nickname.getString( 
		    		  nickname.getColumnIndex(ContactsContract.CommonDataKinds.Nickname.DATA)); 
		   } 
		   nickname.close();
		   
		}
		cursor.close(); 
	}
     
     /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
      * nhận về hình ảnh tương ứng
      * 
      ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/	
	public Uri getPhotoUri(Long contactId) {
        Uri person = ContentUris.withAppendedId(
                ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri photo = Uri.withAppendedPath(person,
                ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

        Cursor cur = this.ctx
                .getContentResolver()
                .query(
                        ContactsContract.Data.CONTENT_URI,
                        null,
                        ContactsContract.Data.CONTACT_ID
                                + "="
                                + contactId
                                + " AND "
                                + ContactsContract.Data.MIMETYPE
                                + "='"
                                + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                                + "'", null, null);
        if (cur != null) {
            if (!cur.moveToFirst()) {
                return null; // no photo
            }
        } else {
            return null; // error in cursor process
        }
        return photo;
    }
	

	public  ArrayList<SelectUser>  getContactById(long contactId) {
        // Get Contact list from Phone
		ArrayList<SelectUser> selectUsers = new ArrayList<SelectUser>();
		
    	Uri dataUri = ContactsContract.Data.CONTENT_URI;    	
    	// Querying the table ContactsContract.Data to retrieve individual items like
        // home phone, mobile phone, work email etc corresponding to each contact
        Cursor dataCursor = ctx.getContentResolver().query(dataUri, null,
                            ContactsContract.Data.CONTACT_ID + "=" + contactId,
                            null, null);
        String displayName="";
        String nickName="";
        String homePhone="";
        String mobilePhone="";
        String workPhone="";
        String homeEmail="";
        String workEmail="";
        String companyName="";
        String title="";
        String birthday="";
        String googleContactId="";
        
        if(dataCursor.moveToFirst()){
            // Getting Display Name
            displayName = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME ));
            do{

                // Getting NickName
                if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE))
                    nickName = dataCursor.getString(dataCursor.getColumnIndex("data1"));

                // Getting Phone numbers
                if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)){
                    switch(dataCursor.getInt(dataCursor.getColumnIndex("data2"))){
                        case ContactsContract.CommonDataKinds.Phone.TYPE_HOME :
                            homePhone = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE :
                            mobilePhone = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                            break;
                        case ContactsContract.CommonDataKinds.Phone.TYPE_WORK :
                            workPhone = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                            break;
                    }
                }

                // Getting EMails
                if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE ) ) {
                    switch(dataCursor.getInt(dataCursor.getColumnIndex("data2"))){
                        case ContactsContract.CommonDataKinds.Email.TYPE_HOME :
                            homeEmail = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                            break;
                        case ContactsContract.CommonDataKinds.Email.TYPE_WORK :
                            workEmail = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                            break;
                    }
                }
             // Getting birthday
                if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE ) ) {
                    switch(dataCursor.getInt(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.TYPE))){
                        case ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY :
                            birthday = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE));
                            break;
                    }
                }

                // Getting Organization details
                if(dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)){
                    companyName = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                    title = dataCursor.getString(dataCursor.getColumnIndex("data4"));
                }

            }while(dataCursor.moveToNext());
        }
        
        dataCursor.moveToFirst();
        Bitmap bit_thumb = null;
        String id = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
        String name = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

        String note = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Note.DATA1));
        String phoneNumber = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        String image_thumb = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
        try {
            if (image_thumb != null) {
                bit_thumb = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), Uri.parse(image_thumb));
            } else {
                Log.e("No Image Thumb", "--------------");
            }
        } catch (IOException e) {
            dataCursor.close();
            e.printStackTrace();
        }
        dataCursor.close();
        
        SelectUser selectUser = new SelectUser();
        
        selectUser.setThumb(bit_thumb);
        selectUser.setName(name);
        selectUser.setNickname(nickName);
        selectUser.setPhone(mobilePhone);
        selectUser.setEmail(workEmail);
        selectUser.setBirthDay(birthday);
        selectUser.setGoogleId(String.valueOf(contactId));
        
        selectUser.setCheckedBox(false);
        
        selectUsers.add(selectUser);
        

    //phones.close();
    return selectUsers;
    }
}
