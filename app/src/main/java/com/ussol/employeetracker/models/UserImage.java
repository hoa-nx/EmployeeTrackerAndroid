package com.ussol.employeetracker.models;

public class UserImage {
	private int  id = 0;
	private int  user_code;
	private byte[] img;
	private String imgFullPath;
	private String name;
	private String ryaku;
	//public String myHash;
	private Integer isdeleted=0;/*1 : đã delete , ngược lại là chưa delete*/
	private boolean isselected =false;
	private String note="";
	/** các item dự bị */
	private int yobi_code1=0;
	private int  yobi_code2=0;
	private int  yobi_code3=0;
	private int  yobi_code4=0;
	private int  yobi_code5=0;
	
	private String yobi_text1="";  
	private String yobi_text2="";
	private String yobi_text3="";
	private String yobi_text4="";
	private String yobi_text5="";
	
	private String yobi_date1="";  
	private String yobi_date2="";
	private String yobi_date3="";
	private String yobi_date4="";
	private String yobi_date5="";
	
	private float yobi_real1=0;
	private float yobi_real2=0;
	private float yobi_real3=0;
	private float yobi_real4=0;
	private float yobi_real5=0;
	
	private String up_date="";
	private String ad_date="";
	private String opid="";
	public int getId() {
		return id;
	}
	public int getUser_code() {
		return user_code;
	}
	public byte[] getImg() {
		return img;
	}
	public String getImgFullPath() {
		return imgFullPath;
	}
	public String getName() {
		return name;
	}
	public String getRyaku() {
		return ryaku;
	}
	public Integer getIsdeleted() {
		return isdeleted;
	}
	public boolean isIsselected() {
		return isselected;
	}
	public String getNote() {
		return note;
	}
	public int getYobi_code1() {
		return yobi_code1;
	}
	public int getYobi_code2() {
		return yobi_code2;
	}
	public int getYobi_code3() {
		return yobi_code3;
	}
	public int getYobi_code4() {
		return yobi_code4;
	}
	public int getYobi_code5() {
		return yobi_code5;
	}
	public String getYobi_text1() {
		return yobi_text1;
	}
	public String getYobi_text2() {
		return yobi_text2;
	}
	public String getYobi_text3() {
		return yobi_text3;
	}
	public String getYobi_text4() {
		return yobi_text4;
	}
	public String getYobi_text5() {
		return yobi_text5;
	}
	public String getYobi_date1() {
		return yobi_date1;
	}
	public String getYobi_date2() {
		return yobi_date2;
	}
	public String getYobi_date3() {
		return yobi_date3;
	}
	public String getYobi_date4() {
		return yobi_date4;
	}
	public String getYobi_date5() {
		return yobi_date5;
	}
	public float getYobi_real1() {
		return yobi_real1;
	}
	public float getYobi_real2() {
		return yobi_real2;
	}
	public float getYobi_real3() {
		return yobi_real3;
	}
	public float getYobi_real4() {
		return yobi_real4;
	}
	public float getYobi_real5() {
		return yobi_real5;
	}
	public String getUp_date() {
		return up_date;
	}
	public String getAd_date() {
		return ad_date;
	}
	public String getOpid() {
		return opid;
	}
	/**setting value*/
	public void setId(int id) {
		this.id = id;
	}
	public void setUser_code(int user_code) {
		this.user_code = user_code;
	}
	public void setImg(byte[] img) {
		this.img = img;
	}
	public void setImgFullPath(String imgFullPath) {
		this.imgFullPath = imgFullPath;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setRyaku(String ryaku) {
		this.ryaku = ryaku;
	}
	public void setIsdeleted(Integer isdeleted) {
		this.isdeleted = isdeleted;
	}
	public void setIsselected(boolean isselected) {
		this.isselected = isselected;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public void setYobi_code1(int yobi_code1) {
		this.yobi_code1 = yobi_code1;
	}
	public void setYobi_code2(int yobi_code2) {
		this.yobi_code2 = yobi_code2;
	}
	public void setYobi_code3(int yobi_code3) {
		this.yobi_code3 = yobi_code3;
	}
	public void setYobi_code4(int yobi_code4) {
		this.yobi_code4 = yobi_code4;
	}
	public void setYobi_code5(int yobi_code5) {
		this.yobi_code5 = yobi_code5;
	}
	public void setYobi_text1(String yobi_text1) {
		this.yobi_text1 = yobi_text1;
	}
	public void setYobi_text2(String yobi_text2) {
		this.yobi_text2 = yobi_text2;
	}
	public void setYobi_text3(String yobi_text3) {
		this.yobi_text3 = yobi_text3;
	}
	public void setYobi_text4(String yobi_text4) {
		this.yobi_text4 = yobi_text4;
	}
	public void setYobi_text5(String yobi_text5) {
		this.yobi_text5 = yobi_text5;
	}
	public void setYobi_date1(String yobi_date1) {
		this.yobi_date1 = yobi_date1;
	}
	public void setYobi_date2(String yobi_date2) {
		this.yobi_date2 = yobi_date2;
	}
	public void setYobi_date3(String yobi_date3) {
		this.yobi_date3 = yobi_date3;
	}
	public void setYobi_date4(String yobi_date4) {
		this.yobi_date4 = yobi_date4;
	}
	public void setYobi_date5(String yobi_date5) {
		this.yobi_date5 = yobi_date5;
	}
	public void setYobi_real1(float yobi_real1) {
		this.yobi_real1 = yobi_real1;
	}
	public void setYobi_real2(float yobi_real2) {
		this.yobi_real2 = yobi_real2;
	}
	public void setYobi_real3(float yobi_real3) {
		this.yobi_real3 = yobi_real3;
	}
	public void setYobi_real4(float yobi_real4) {
		this.yobi_real4 = yobi_real4;
	}
	public void setYobi_real5(float yobi_real5) {
		this.yobi_real5 = yobi_real5;
	}
	public void setUp_date(String up_date) {
		this.up_date = up_date;
	}
	public void setAd_date(String ad_date) {
		this.ad_date = ad_date;
	}
	public void setOpid(String opid) {
		this.opid = opid;
	}
	
	
}
