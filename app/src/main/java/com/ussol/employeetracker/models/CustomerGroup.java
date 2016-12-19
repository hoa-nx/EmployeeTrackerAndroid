/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/


package com.ussol.employeetracker.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CustomerGroup extends Master {
	
	public static final Parcelable.Creator<CustomerGroup> CREATOR = new Parcelable.Creator<CustomerGroup>() {
    	public CustomerGroup createFromParcel(Parcel in) {
    		return new CustomerGroup(in);
    	}
 
        public CustomerGroup[] newArray(int size) {
        	return new CustomerGroup[size];
        }
    };
    
    public CustomerGroup() {}
    
    public CustomerGroup(Parcel in) {
    	super(in);
    	mkbn= MasterConstants.MASTER_MKBN_CUSTOMER_GROUP; //cố định là 6 
    };
    
    @Override
	public void writeToParcel(Parcel dest, int flags) {
    	dest.writeInt(id);
    	dest.writeInt(MasterConstants.MASTER_MKBN_CUSTOMER_GROUP);
    	dest.writeInt(code);
		dest.writeString(name);
		dest.writeString(ryaku);
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
}
