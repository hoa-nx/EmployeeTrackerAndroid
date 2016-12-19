package com.ussol.employeetracker.helpers;
 
import java.lang.reflect.Array;
import java.util.List;
 
import android.content.Context;
 
import com.ussol.employeetracker.models.CustomerGroup;
import com.ussol.employeetracker.models.Dept;
import com.ussol.employeetracker.models.Position;
import com.ussol.employeetracker.models.PositionGroup;
import com.ussol.employeetracker.models.Team;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.models.UserCustomerGroup;
import com.ussol.employeetracker.models.UserPositionGroup;
/**
 * 
 * @author hoa-nx
 * Dùng để get data cho các list màn hình search 
 */
public class GetListDataHelper {
 /** chuyển đổi Cursor thành List */
 ConvertCursorToListString mConvertCursorToListString;
 Context ctx;
 public GetListDataHelper(Context context){
  ctx = context;
 }
 /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get list user
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
 public List<User> getListUser(String xWhere){
  
  /** chuyển đổi từ Cursor thành List */
  mConvertCursorToListString = new ConvertCursorToListString(ctx);
  return mConvertCursorToListString.getUserList(xWhere);
 }
 
 /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get list dept 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
 public List<Dept> getListDept(String xWhere){
  
  /** chuyển đổi từ Cursor thành List */
  mConvertCursorToListString = new ConvertCursorToListString(ctx);
  return mConvertCursorToListString.getDeptList(xWhere);
 }
 
 /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get list Team 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
 public List<Team> getListTeam(String xWhere){
  
  /** chuyển đổi từ Cursor thành List */
  mConvertCursorToListString = new ConvertCursorToListString(ctx);
  return mConvertCursorToListString.getTeamList(xWhere);
 }
 
 /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * get list chức vụ 
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
 public List<Position> getListPosition(String xWhere){
  
  /** chuyển đổi từ Cursor thành List */
  mConvertCursorToListString = new ConvertCursorToListString(ctx);
  return mConvertCursorToListString.getPositionList(xWhere);
 }
 
 /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
  * get list nhóm chức danh 
  * 
  ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
 public List<PositionGroup> getListPositionGroup(String xWhere){

	/** chuyển đổi từ Cursor thành List */
	mConvertCursorToListString = new ConvertCursorToListString(ctx);
	return mConvertCursorToListString.getPositionGroupList(xWhere);
 }
 /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
  * get list nhóm chức danh của user
  * 
  ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
 public List<UserPositionGroup> getListUserPositionGroup(int user_code){

	/** chuyển đổi từ Cursor thành List */
	mConvertCursorToListString = new ConvertCursorToListString(ctx);
	return mConvertCursorToListString.getUSerPositionGroupList(user_code);
 }
 /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
  * get list khách hàng 
  * 
  ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
 public List<CustomerGroup> getListCustomerGroup(String xWhere){

	/** chuyển đổi từ Cursor thành List */
	mConvertCursorToListString = new ConvertCursorToListString(ctx);
	return mConvertCursorToListString.getCustomerGroupList(xWhere);
 }
 /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
  * get list nhóm khách hàng của user
  * 
  ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
 public List<UserCustomerGroup> getListUserCustomerGroup(int user_code){

	/** chuyển đổi từ Cursor thành List */
	mConvertCursorToListString = new ConvertCursorToListString(ctx);
	return mConvertCursorToListString.getUSerCustomerGroupList(user_code);
 }
 /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
 * @param xWhere : chỉ định điều kiện lọc data 
 * get mảng user theo điều kiện
 * 
 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
public User[] getArrayUser(String xWhere){
	List<User> _list;
	User[] _data ;
	_list=getListUser(xWhere);
	  
	GenericData<User> t = new GenericData<User>(User.class, _list);
	_data =(User[])t.getArray();
	     
	return _data;
     
 }
 
 /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
 * @param xWhere : chỉ định điều kiện lọc data 
 * get mảng phòng ban theo điều kiện
 * 
 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
public Dept[] getArrayDept(String xWhere){
	List<Dept> _list;
	Dept[] _data ;
	_list=getListDept(xWhere);
	      
	GenericData<Dept> t = new GenericData<Dept>(Dept.class, _list);
	_data =(Dept[])t.getArray();
	     
	return _data;
     
}
 
 /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
 * @param xWhere : chỉ định điều kiện lọc data 
 * get mảng team theo điều kiện
 * 
 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
public Team[] getArrayTeam(String xWhere){
	List<Team> _list;
	Team[] _data ;
	_list=getListTeam(xWhere);
	      
	GenericData<Team> t = new GenericData<Team>(Team.class, _list);
	_data =(Team[])t.getArray();
	     
	return _data;
	     
 }
 
 /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
 * @param xWhere : chỉ định điều kiện lọc data 
 * get mảng chức vụ theo điều kiện
 * 
 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
public Position[] getArrayPosition(String xWhere){
	List<Position> _list;
	Position[] _data ;
	_list=getListPosition(xWhere);
	      
	GenericData<Position> t = new GenericData<Position>(Position.class, _list);
	_data =(Position[])t.getArray();
	     
	return _data;
     
 }

 /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
  * @param xWhere : chỉ định điều kiện lọc data 
  * get mảng chức vụ theo điều kiện
  * 
  ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
public PositionGroup[] getArrayPositionGroup(String xWhere){
	List<PositionGroup> _list;
	PositionGroup[] _data ;
	_list=getListPositionGroup(xWhere);
	   
	GenericData<PositionGroup> t = new GenericData<PositionGroup>(PositionGroup.class, _list);
	_data =(PositionGroup[])t.getArray();
	  
	return _data;
  
}

/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
 * @param xWhere : chỉ định điều kiện lọc data 
 * get mảng khách hàng theo điều kiện
 * 
 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
public CustomerGroup[] getArrayCustomerGroup(String xWhere){
	List<CustomerGroup> _list;
	CustomerGroup[] _data ;
	_list=getListCustomerGroup(xWhere);
	   
	GenericData<CustomerGroup> t = new GenericData<CustomerGroup>(CustomerGroup.class, _list);
	_data =(CustomerGroup[])t.getArray();
	  
	return _data;
 
}

 /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
 * 
 * get array data đối tượng ( tùy theo class setting tại T)
 * 
 ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
 class GenericData<T>{
  private T[] array=null;
   @SuppressWarnings("unchecked")
  public GenericData(Class<T> clazz,List<T> _list) {
       array=(T[])Array.newInstance(clazz,_list.size());
       int index =0;
       for(T it : _list){
        array[index] = it;
        index++;
       }
    }
   
   /**
    * 
    * @return array đối tượng kiểu T
    */
   public T[] getArray(){
    return array;
   }
   
 }
 
 
}