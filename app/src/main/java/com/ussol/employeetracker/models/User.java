/**
 * Copyright (c) 2012 Fujinet Software Department 5 ( USSOL team)  
 * See the file license.txt for copying permission.
*/  
package com.ussol.employeetracker.models;

import com.ussol.employeetracker.utils.Utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcelable;
import android.os.Parcel;

public class User implements Parcelable, Cloneable {
	/*public int code=0;
	public String first_name="";
	public String last_name="";
	public String full_name="";
	public int sex=0;
	public String birthday="";
	public String address="";
	public String home_tel="";
	public String mobile="";
	public String fax="";
	public int position=0;
	public String position_name="";
	public int dept=0;
	public String dept_name="";
	public int team=0;
	public String team_name="";
	public int tant=0;
	public String tant_name="";
	public String training_date="";
	public String in_date="";
	public String join_date="";
	public String out_date="";
	public String note="";
	public Byte[] img=null;
	
	//public String myHash;
	public int isdeleted=0;1 : đã delete , ngược lại là chưa delete
	public String email="";
	public String japanese="";
	public String allowance_business="";
	public int married=0;
	public float salary_notallowance=0;
	public float salary_allowance=0;
	public String tag1="";
	public String tag2="";
	
	public String img_fullpath="";
	public int isLabour=0;
	public String labour_join_date="";
	public String labour_out_date="";
	public String business_kbn="";
	
	
	*//** các item dự bị *//*
	
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
	BitmapDrawable mMyDrawable;*/
	
	public int code=0;
	public String google_id;
	public String first_name;
	public String last_name;
	public String full_name;
	public int sex=0;
	public String birthday;
	public String address;
	public String home_tel;
	public String mobile;
	public String fax;
	public int position=0;
	public String position_name;
	public int dept=0;
	public String dept_name;
	public int team=0;
	public String team_name;
	public int tant=0;
	public String tant_name;
	public String training_date;/*ngay bat dau nhan vao training tai dept*/
	public String training_dateEnd;/*ngay bat dau nhan vao training tai dept*/
	public String learn_training_date;/*ngay bat dau nhan vao hoc viec tai dept*/
	public String learn_training_dateEnd;/*ngay ket thuc hoc viec tai dept*/
	public String in_date;/* ngay vao cong ty chinh thuc-nhan vao lam nhan vien chinh thuc*/
	public String join_date;/* ngay tham gia vao nhom labour */
	public String out_date; /*ngay nghi viec*/
	public String init_keiken;/*so thang kinh nghiem truoc khi vao cong ty */
	public String convert_keiken;/*so thang kinh nghiem duoc qui doi khi vao FJN*/
	public int user_kbn;
	public String note;
	public Byte[] img=null;
	
	//public String myHash;
	public int isdeleted=0;/*1 : đã delete , ngược lại là chưa delete*/
	public boolean isselected =false;
	public String email;
	public String japanese;
	public String allowance_business;/* phu cap nghiep vu */
	public String allowance_room;/* phu cap phòng chuyên biệt */
	public int married=0;
	public float salary_notallowance=0;
	public float salary_allowance=0;
	public float estimate_point=0;
	public String tag1;
	public String tag2;
	
	public String img_fullpath;
	public int isLabour=0;/*co phai thanh vien nhom labour?*/
	public String labour_join_date;
	public String labour_out_date;
	public String business_kbn;
	public float basicdesign=0;/*kha nang thiet ke co ban*/
	public float detaildesign=0;/*kha nang thiet ke chi tiet*/
	public float program=0;/*kha nang tao PG*/
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

	/** add them cac item su dung khi can get them thong tin*/
	public String NEW_POSITION_CODE; // khong ton tai trong DB
	public String NEW_POSITION_RYAKU; // khong ton tai trong DB
	public String NEW_POSITION_LEVEL; // khong ton tai trong DB

	BitmapDrawable mMyDrawable;
	
	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
    	public User createFromParcel(Parcel in) {
    		return new User(in);
    	}
 
        public User[] newArray(int size) {
        	return new User[size];
        }
    };
    
    public User() {}
    
    public User(Parcel in) {
    	code = in.readInt();
    	google_id =in.readString();
    	first_name= in.readString();
    	last_name= in.readString();
    	full_name= in.readString();
    	sex = in.readInt();
    	birthday= in.readString();
    	address= in.readString();
    	home_tel= in.readString();
    	mobile= in.readString();
    	fax= in.readString();
    	position= in.readInt();
    	position_name =in.readString();
    	dept= in.readInt();
    	dept_name =in.readString();
    	team= in.readInt();
    	team_name =in.readString();
    	tant= in.readInt();
    	tant_name =in.readString();
    	
    	training_date =in.readString();
    	training_dateEnd =in.readString();
    	
    	learn_training_date =in.readString();
    	learn_training_dateEnd =in.readString();
    	
    	in_date =in.readString();
    	join_date =in.readString();
    	out_date =in.readString();
    	init_keiken=in.readString();
    	convert_keiken=in.readString();
    	user_kbn= in.readInt();
    	note =in.readString();
    	//img =in.readByte();
    	// Deserialize Parcelable and cast to Bitmap first:
		//Bitmap bitmap = (Bitmap)in.readParcelable(getClass().getClassLoader());
		// Convert Bitmap to Drawable:
		//mMyDrawable = new BitmapDrawable(bitmap);
    	  
    	isdeleted= in.readInt();
    	isselected =in.readByte() == 1; 
    	
    	email=in.readString();
    	japanese=in.readString();
    	allowance_business=in.readString();
    	allowance_room=in.readString();
    	married=in.readInt();
    	salary_notallowance=in.readFloat();
    	salary_allowance=in.readFloat();
    	estimate_point=in.readFloat();
    	tag1=in.readString();
    	tag2=in.readString();
    	
    	img_fullpath=in.readString();
    	isLabour=in.readInt();
    	labour_join_date=in.readString();
    	labour_out_date=in.readString();
    	business_kbn=in.readString();
    	basicdesign =in.readFloat();
    	detaildesign=in.readFloat();
    	program = in.readFloat();
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
    	dest.writeString(google_id);
    	dest.writeString(first_name);
    	dest.writeString(last_name);
    	dest.writeString(full_name);
    	dest.writeInt(sex);
    	dest.writeString(birthday);
    	dest.writeString(address);
    	dest.writeString(home_tel);
    	dest.writeString(mobile);
    	dest.writeString(fax);
    	dest.writeInt(position);
    	dest.writeString(position_name);
    	dest.writeInt(dept);
    	dest.writeString(dept_name);
    	dest.writeInt(team);
    	dest.writeString(team_name);
    	dest.writeInt(tant);
    	dest.writeString(tant_name);
    	dest.writeString(training_date);
    	dest.writeString(training_dateEnd);
    	dest.writeString(learn_training_date);
    	dest.writeString(learn_training_dateEnd);
    	dest.writeString(in_date);
    	dest.writeString(join_date);
    	dest.writeString(out_date);
    	dest.writeString(init_keiken);
    	dest.writeString(convert_keiken);
    	dest.writeInt(user_kbn);
    	dest.writeString(note);
    	
    	
		// Convert Drawable to Bitmap first:
    	//  Bitmap bitmap = (Bitmap)((BitmapDrawable) mMyDrawable).getBitmap();
    	//dest.writeString(img);
    	//dest.writeParcelable(bitmap, flags);
		dest.writeInt(isdeleted);
		dest.writeByte((byte) (isselected ? 1 : 0));
		dest.writeString(email);
		dest.writeString(japanese);
		dest.writeString(allowance_business);
		dest.writeString(allowance_room);
		dest.writeInt(married);
		dest.writeFloat(salary_notallowance);
		dest.writeFloat(salary_allowance);
		dest.writeFloat(estimate_point);
		dest.writeString(tag1);
		dest.writeString(tag2);
    	
		dest.writeString(img_fullpath);
		dest.writeInt(isLabour);
		dest.writeString(labour_join_date);
		dest.writeString(labour_out_date);
		dest.writeString(business_kbn);
    	dest.writeFloat(basicdesign);
    	dest.writeFloat(detaildesign);
    	dest.writeFloat(program);
    	
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
		dest.writeFloat(yobi_real1);/*Su dung de luu so thang(nam) kinh nghiem???*/
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
	
	@Override
	public User clone()
	{
	  return (User)Utils.clone(this);
	}
	
	
}
