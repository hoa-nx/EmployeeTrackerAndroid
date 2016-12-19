/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/


package com.ussol.employeetracker.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.ussol.employeetracker.utils.Utils;

public class Team extends Master {
	
	
	public Long timeInMillis;
	public String favorite = null;
	
	public static final Parcelable.Creator<Team> CREATOR = new Parcelable.Creator<Team>() {
    	public Team createFromParcel(Parcel in) {
    		return new Team(in);
    	}
 
        public Team[] newArray(int size) {
        	return new Team[size];
        }
    };
    
    public Team() {}
    
    public Team(Parcel in) {
    	super(in);
    	mkbn= MasterConstants.MASTER_MKBN_TEAM; //cố định là 2 
    };
    
    @Override
	public void writeToParcel(Parcel dest, int flags) {
    	dest.writeInt(id);
    	dest.writeInt(MasterConstants.MASTER_MKBN_TEAM);
    	dest.writeInt(code);
		dest.writeString(name);
		dest.writeString(ryaku);
		dest.writeString(create_date);
		dest.writeInt(isdeleted);
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
	public Team clone()
	{
		return (Team) Utils.clone(this);
	}
}
