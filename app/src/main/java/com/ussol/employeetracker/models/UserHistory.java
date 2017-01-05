/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/     


package com.ussol.employeetracker.models;

import javax.mail.internet.NewsAddress;

import android.os.Parcel;
import android.os.Parcelable;

public class UserHistory implements Parcelable {

	public int  id = 0;
	public int  mkbn;
	public int  code;
	public String name;
	public String ryaku;
	public int user_code=0;
	public String full_name;
	public String img_fullpath;
	public String date_from="";
	public String date_to="";
	public int new_dept_code=0;
	public String new_dept_name="";
	public int new_team_code=0;
	public String new_team_name="";
	public int new_position_code=0;
	public String new_position_name="";
	public String new_japanese;
	public String new_japanese_name;
	public String new_allowance_business;
	public String new_allowance_business_name;
	public String new_allowance_bse;
	public String new_allowance_bse_name;

	public float new_salary=0;
	public float new_salary_standard=0;
	public float new_salary_percent=0;
	public float new_salary_actual_up=0;
	public String new_salary_next_ym;

	public String reason="";
	//public String myHash;
	public Integer isdeleted=0;/*1 : đã delete , ngược lại là chưa delete*/
	public boolean isselected =false;
	public String note="";
	/** các item dự bị */
	public int yobi_code1=0;
	public int  yobi_code2=0;
	public int  yobi_code3=0;
	public int  yobi_code4=0;
	public int  yobi_code5=0;
	
	public String yobi_text1="";  
	public String yobi_text2="";
	public String yobi_text3="";
	public String yobi_text4="";
	public String yobi_text5="";
	
	public String yobi_date1="";  
	public String yobi_date2="";
	public String yobi_date3="";
	public String yobi_date4="";
	public String yobi_date5="";
	
	public float yobi_real1=0;
	public float yobi_real2=0;
	public float yobi_real3=0;
	public float yobi_real4=0;
	public float yobi_real5=0;
	
	public String up_date="";
	public String ad_date="";
	public String opid="";
	
	public static final Parcelable.Creator<UserHistory> CREATOR = new Parcelable.Creator<UserHistory>() {
    	public UserHistory createFromParcel(Parcel in) {
    		return new UserHistory(in);
    	}
 
        public UserHistory[] newArray(int size) {
        	return new UserHistory[size];
        }
    };
    
    public UserHistory() {}
    
    public UserHistory(Parcel in) {
    	id = in.readInt();
    	mkbn= in.readInt();
    	code= in.readInt();
    	user_code= in.readInt();
    	name= in.readString();
    	ryaku= in.readString();
    	full_name= in.readString();
    	img_fullpath=in.readString();
    	date_from= in.readString();
    	date_to= in.readString();
    	
    	new_dept_code= in.readInt();
    	new_dept_name= in.readString();
    	new_team_code= in.readInt();
    	new_team_name= in.readString();
    	new_position_code= in.readInt();
    	new_position_name= in.readString();
    	new_japanese = in.readString();
    	new_japanese_name = in.readString();
    	new_allowance_business = in.readString();
    	new_allowance_business_name = in.readString();
		new_allowance_bse = in.readString();
		new_allowance_bse_name = in.readString();

    	new_salary= in.readFloat();
    	new_salary_standard= in.readFloat();
    	new_salary_percent= in.readFloat();
    	new_salary_actual_up= in.readFloat();
    	new_salary_next_ym = in.readString();
    	
    	reason= in.readString();
    	    	
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
    	dest.writeInt(user_code);
		dest.writeString(name);
		dest.writeString(ryaku);
		dest.writeString(full_name);
		dest.writeString(img_fullpath);
		dest.writeString(date_from);
		dest.writeString(date_to);
		
		dest.writeInt(new_dept_code);
		dest.writeString(new_dept_name);
		dest.writeInt(new_team_code);
		dest.writeString(new_team_name);
		dest.writeInt(new_position_code);
		dest.writeString(new_position_name);
		dest.writeString(new_japanese);
		dest.writeString(new_japanese_name);
		dest.writeString(new_allowance_business);
		dest.writeString(new_allowance_business_name);

		dest.writeString(new_allowance_bse);
		dest.writeString(new_allowance_bse_name);

		dest.writeFloat(new_salary);
		dest.writeFloat(new_salary_standard);
		dest.writeFloat(new_salary_percent);
		dest.writeFloat(new_salary_actual_up);
		dest.writeString(new_salary_next_ym);
		
		dest.writeString(reason);
		    	    	
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
