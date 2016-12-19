/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/     


package com.ussol.employeetracker.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.ussol.employeetracker.utils.Utils;

public class Master implements Parcelable ,Cloneable{

	public int  id = 0;
	public int  mkbn;
	public int  code;
	public String name;
	public String ryaku;
	public String create_date;
	//public String myHash;
	public Integer isdeleted=0;/*1 : đã delete , ngược lại là chưa delete*/
	public boolean isselected =false;
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
	
	public static final Parcelable.Creator<Master> CREATOR = new Parcelable.Creator<Master>() {
    	public Master createFromParcel(Parcel in) {
    		return new Master(in);
    	}
 
        public Master[] newArray(int size) {
        	return new Master[size];
        }
    };
    
    public Master() {}
    
    public Master(Parcel in) {
    	id = in.readInt();
    	mkbn= in.readInt();
    	code= in.readInt();
    	name= in.readString();
    	ryaku= in.readString();
    	create_date = in.readString();
    	isdeleted= in.readInt();
    	isselected =in.readByte() == 1; 
    	note = in.readString();
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
    };
    
    @Override
	public void writeToParcel(Parcel dest, int flags) {
    	dest.writeInt(id);
    	dest.writeInt(mkbn);
    	dest.writeInt(code);
		dest.writeString(name);
		dest.writeString(ryaku);
		dest.writeString(create_date);
		dest.writeInt(isdeleted);
		dest.writeByte((byte) (isselected ? 1 : 0));
		dest.writeString(note);
		
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
		return 0;
	}

	@Override
	public Master clone()
	{
		return (Master) Utils.clone(this);
	}

}
