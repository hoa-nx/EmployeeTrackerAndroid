package com.ussol.employeetracker.models;

import java.util.ArrayList;
import java.util.List;

public interface IExpGroup<E> {
	public int EXP_GROUP_SEX =0;
	public int EXP_GROUP_JAPANESE =1;
	public int EXP_GROUP_DEPT =2;
	public int EXP_GROUP_TEAM =3;
	public int EXP_GROUP_POSITION =4;
	public int EXP_GROUP_BUSINESS_KBN =5;
	public int EXP_GROUP_KEIKEN =6;/** theo kinh nghiem */
	public int EXP_GROUP_HISTORY = 7;
	public int EXP_GROUP_LABOUR_USER =8; /** theo tung khach hang(user nhóm labour hay khong phai nhom labour) */
	public int EXP_GROUP_YASUMI = 9;/** theo nghi viec -chưa nghỉ việc*/
	public int EXP_GROUP_SALARY_BASIC = 10; /** luong co ban */
	public int EXP_GROUP_KEIKEN_LABOR =11;/** theo kinh nghiem labor*/
	public int EXP_GROUP_CUSTOMER =12;/** theo khách hàng*/
	public int EXP_GROUP_YASUMI_YEARMONTH =13;/** chart theo nghi viec - tung thang nam*/
	public int EXP_GROUP_STAFF_STATUS_YEARMONTH =14;/** chart thong ke theo tung nhan vien lam viec , pd ,training theo thang nam*/
	public int EXP_GROUP_STAFF_CONTRACT_STATUS_YEARMONTH =15;/** chart thong ke nhan chinh thuc nhan vien theo thang nam*/
	public int EXP_GROUP_STAFF_STATUS_TRAINING_YEAR =16;/** chart thong ke so nhan vien thu viec trong moi nam*/
	public int EXP_GROUP_STAFF_STATUS_CONTRACT_YEAR =17;/** chart thong ke so nhan vien nhận chính thức trong moi nam*/

	/** 
	 * thong ke nhan thu viectheo nam
	 * */
	public int EXP_GROUP_TRAINING_YEAR =18;
	/** 
	 * thong ke nhan chinh thuc theo nam
	 * */
	public int EXP_GROUP_CONTRACT_YEAR =19;
	/** 
	 * thong ke khong nhan sau thu viec
	 * */
	public int EXP_GROUP_NOTCONTRACT_YEAR =20;
	/** 
	 * thong ke nhan chinh thuc nho hon N thang
	 * */
	public int EXP_GROUP_CONTRACT_LESS_MONTH =21;

	public int EXP_GROUP_STAFF_CURRENT_POSITION_NOT_SATIFIED =22;/** thong ke so nhan vien co ngach khong phu hop so voi tham nien*/
	/** theo nghi viec -  nam hien tai*/
	public int EXP_GROUP_YASUMI_YEAR =23;

	/** phu cap nghiep vu*/
	public int EXP_GROUP_BUSSINESS_ALLOWANCE =24;

	public String[] getGroup(int group);
	public ArrayList<?> getGroupArrayList(int group);
	public List<E> getChildGroup(int group);
	public List<E> getChildGroup(int group,String groupValue);
	public List<E> getChildUserHisGroup(int group,String groupValue);
}
