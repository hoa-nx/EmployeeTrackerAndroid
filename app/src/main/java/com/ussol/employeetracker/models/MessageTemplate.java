/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/


package com.ussol.employeetracker.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MessageTemplate extends Master {
	
	
	public Long timeInMillis;
	public String favorite = null;
	public String content;
	public String telephone;/** arrray */
	public String email;
	public int isvalidated=0;/** 0 còn hiệu lực; 1: không còn hiệu lực */ 
	
	public static final Parcelable.Creator<MessageTemplate> CREATOR = new Parcelable.Creator<MessageTemplate>() {
    	public MessageTemplate createFromParcel(Parcel in) {
    		return new MessageTemplate(in);
    	}
 
        public MessageTemplate[] newArray(int size) {
        	return new MessageTemplate[size];
        }
    };
    
    public MessageTemplate() {}
    
    public MessageTemplate(Parcel in) {
    	super(in);
    	
    	content=in.readString();
    	telephone =in.readString();
    	email = in.readString();
    	isvalidated = in.readInt();
    };
    
    @Override
	public void writeToParcel(Parcel dest, int flags) {
    	dest.writeInt(id);
    	dest.writeInt(code);
		dest.writeString(name);
		dest.writeString(ryaku);
		dest.writeString(content);
		dest.writeString(telephone);
		dest.writeString(email);
		dest.writeInt(isdeleted);
		dest.writeInt(isvalidated);
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
}
