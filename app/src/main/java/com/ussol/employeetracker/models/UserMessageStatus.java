/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/     


package com.ussol.employeetracker.models;

import android.os.Parcel;
import android.os.Parcelable;

public class UserMessageStatus implements Parcelable {

	public int  id = 0;
	public int  mkbn;/* loai message */
	public int  code;
	public int  message_template_code=0;
	public int  message_type=0;
	public int  message_emp_code=0;
	public String name;
	public String ryaku;
	public int sender_code;
	public String sender_phone;
	public String sender_mail;
	public int receiver_code;
	public String receiver_phone;
	public String receiver_mail;
	public String send_package;/** No de cho biet la gửi cùng 1 lượt */
	public String send_datetime;/**thời gian gửi email /sms*/
	public Integer issms;/** 0 : không phải sms ; 1 : là sms */
	public Integer isemail;/** 0 : không phải email ; 1 : là email */
	public String attach_file;/** tên file đính kèm */
	public Integer send_status;/** 0: chưa gửi ; 1 : đã ửi thành công ; 2 : gửi thất bại */
	public String schedule_send_datetime; /** trường hợp là tạo schedule gửi tự động tại thời gian đã setting trước */
	public String schedule_send_loop;/** 0: không loop ; 1 : loop theo ngày ; 2 : loop theo tháng ; loop theo năm  */
	public Integer isactive;/** 0 : không hữu hiệu; 1 : là hữu hiệu */

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
	
	public static final Parcelable.Creator<UserMessageStatus> CREATOR = new Parcelable.Creator<UserMessageStatus>() {
    	public UserMessageStatus createFromParcel(Parcel in) {
    		return new UserMessageStatus(in);
    	}
 
        public UserMessageStatus[] newArray(int size) {
        	return new UserMessageStatus[size];
        }
    };
    
    public UserMessageStatus() {}
    
    public UserMessageStatus(Parcel in) {
    	id = in.readInt();
    	mkbn= in.readInt();
    	code= in.readInt();
    	message_type= in.readInt();
    	message_template_code= in.readInt(); 
    	message_emp_code= in.readInt();
	   	name= in.readString();
    	ryaku= in.readString();
    	sender_code= in.readInt();
    	sender_phone= in.readString();
    	sender_mail= in.readString();
    	
    	receiver_code= in.readInt();
    	receiver_phone= in.readString();
    	receiver_mail= in.readString();
    	
    	send_package= in.readString();
    	send_datetime = in.readString();
    	issms = in.readInt();
    	isemail = in.readInt();
    	isactive = in.readInt();
    	attach_file = in.readString();
    	send_status = in.readInt();
    	schedule_send_datetime= in.readString();
    	schedule_send_loop= in.readString();
    	    	    	
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
    	dest.writeInt(message_type);
    	dest.writeInt(message_template_code);
    	dest.writeInt(message_emp_code);
    	dest.writeString(name);
		dest.writeString(ryaku);
		dest.writeInt(sender_code);
		dest.writeString(sender_phone);
		dest.writeString(sender_mail);
		dest.writeInt(receiver_code);
		dest.writeString(receiver_phone);
		dest.writeString(receiver_mail);
		
		dest.writeString(send_package);
		dest.writeString(send_datetime);
    	dest.writeInt(issms);
    	dest.writeInt(isemail);
    	dest.writeInt(isactive);
    	dest.writeString(attach_file);
    	dest.writeInt(send_status);
    	dest.writeString(schedule_send_datetime);
    	dest.writeString(schedule_send_loop);
    	
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
