package com.ussol.employeetracker.helpers;

import java.util.ArrayList;

import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.models.UserHistory;

public class ExpParent {
	private String mTitle;
    private ArrayList<User> mArrayChildren;
    private ArrayList<UserHistory> mArrayChildrenUserHis;
    
    public ExpParent(){
    	
    }
    public ExpParent(String title , ArrayList<User> userList){
    	this.mTitle =title;
    	this.mArrayChildren = userList;
    }
    
    public ExpParent(String title , ArrayList<UserHistory> userList, String dummy){
    	this.mTitle =title;
    	this.mArrayChildrenUserHis = userList;
    }
    
    public String getTitle() {
        return mTitle;
    }
 
    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }
 
    public ArrayList<User> getArrayChildren() {
        return mArrayChildren;
    }
    
    public void setArrayChildren(ArrayList<User> mArrayChildren) {
        this.mArrayChildren = mArrayChildren;
    }
    
    public void removeChildrenUser(int position){
    	mArrayChildren.remove(position);
    }
    
    public void removeChildrenUser(Object obj){
    	mArrayChildren.remove(obj);
    }
    
    /** thông tin lịch sử */
    public ArrayList<UserHistory> getArrayChildrenUserHis() {
        return mArrayChildrenUserHis;
    }
    /** thông tin lịch sử */
    public void setArrayChildrenUserHis(ArrayList<UserHistory> mArrayChildren) {
        this.mArrayChildrenUserHis = mArrayChildren;
    }
    /** thông tin lịch sử */
    public void removeChildrenUserHis(int position){
    	mArrayChildrenUserHis.remove(position);
    }
    /** thông tin lịch sử */
    public void removeChildrenUserHis(Object obj){
    	mArrayChildrenUserHis.remove(obj);
    }
}
