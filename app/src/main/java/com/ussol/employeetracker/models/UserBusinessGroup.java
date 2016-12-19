/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  
package com.ussol.employeetracker.models;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcelable;
import android.os.Parcel;

public class UserBusinessGroup implements Parcelable {
	
	public int code=0;
	public int user_code=0;
	public int business_group_code=0;
	public String name;
	public String ryaku;
	public int isdeleted=0;/*1 : đã delete , ngược lại là chưa delete*/
	public String note;
	
	/** các item dự bị */
	public int yobi_code1=0;
	public int  yobi_code2=0;
	public int  yobi_code3=0;
	public int  yobi_code4=0;
	public int  yobi_code5=0;
	
	public String yobi_text1;  
	public String yobi_text2;
	public String yobi_text3;
	public String yobi_text4;
	public String yobi_text5;
	
	public String yobi_date1;  
	public String yobi_date2;
	public String yobi_date3;
	public String yobi_date4;
	public String yobi_date5;
	
	public float yobi_real1=0;
	public float yobi_real2=0;
	public float yobi_real3=0;
	public float yobi_real4=0;
	public float yobi_real5=0;
	
	public String up_date;
	public String ad_date;
	public String opid;
	BitmapDrawable mMyDrawable;
	
	public static final Parcelable.Creator<UserBusinessGroup> CREATOR = new Parcelable.Creator<UserBusinessGroup>() {
    	public UserBusinessGroup createFromParcel(Parcel in) {
    		return new UserBusinessGroup(in);
    	}
 
        public UserBusinessGroup[] newArray(int size) {
        	return new UserBusinessGroup[size];
        }
    };
    
    public UserBusinessGroup() {}
    
    public UserBusinessGroup(Parcel in) {
    	code = in.readInt();
    	user_code = in.readInt();
    	business_group_code = in.readInt();
    	name= in.readString();
    	ryaku= in.readString();
    	note =in.readString();
    	isdeleted= in.readInt();
    	
    	yobi_code1= in.readInt();
    	yobi_code2= in.readInt();
    	yobi_code3= in.readInt();
    	yobi_code4= in.readInt();
    	yobi_code5= in.readInt();
    	yobi_text1= in.readString();
    	yobi_text2= in.readString();
    	yobi_text3= in.readString();
    	yobi_text4= in.readString();
    	yobi_text5= in.readString();
    	yobi_date1= in.readString();
    	yobi_date2= in.readString();
    	yobi_date3= in.readString();
    	yobi_date4= in.readString();
    	yobi_date5= in.readString();
    	yobi_real1= in.readFloat();
    	yobi_real2= in.readFloat();
    	yobi_real3= in.readFloat();
    	yobi_real4= in.readFloat();
    	yobi_real5= in.readFloat();
    	up_date= in.readString();
    	ad_date= in.readString();
    	opid= in.readString();
    }
    
    @Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
    	dest.writeInt(code);
    	dest.writeInt(user_code);
    	dest.writeInt(business_group_code);
    	dest.writeString(name);
    	dest.writeString(ryaku);
    	dest.writeString(note);
		dest.writeInt(isdeleted);

		dest.writeInt(yobi_code1);
		dest.writeInt(yobi_code2);
		dest.writeInt(yobi_code3);
		dest.writeInt(yobi_code4);
		dest.writeInt(yobi_code5);
		dest.writeString(yobi_text1);
		dest.writeString(yobi_text2);
		dest.writeString(yobi_text3);
		dest.writeString(yobi_text4);
		dest.writeString(yobi_text5);
		dest.writeString(yobi_date1);
		dest.writeString(yobi_date2);
		dest.writeString(yobi_date3);
		dest.writeString(yobi_date4);
		dest.writeString(yobi_date5);
		dest.writeFloat(yobi_real1);
		dest.writeFloat(yobi_real2);
		dest.writeFloat(yobi_real3);
		dest.writeFloat(yobi_real4);
		dest.writeFloat(yobi_real5);
		dest.writeString(up_date);
		dest.writeString(ad_date);
		dest.writeString(opid);
	}
    
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
		
}
