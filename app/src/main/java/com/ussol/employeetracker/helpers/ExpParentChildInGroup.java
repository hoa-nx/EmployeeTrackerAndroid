package com.ussol.employeetracker.helpers;

import android.content.Context;

import com.ussol.employeetracker.EmployeeTrackerApplication;
import com.ussol.employeetracker.models.IExpGroup;
import com.ussol.employeetracker.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HOA-NX on 2016/12/31.
 */

public final class ExpParentChildInGroup {
    /**
     * Setting nam tai chinh
     */
    public static int currentYear;
    public static float keikenMonthSystem;
    /**
     *Expandale Group
     */
    public static ExpGroupHelper grpExp =null;
    /**
     * Title cua group
     */
    public static String[] arrGroupTitle =null;
    /**
     * List user trong group
     */
    public static List<User> listUser;
    private static Context _context;

    /**
     * Get list user trong group
     * @param group
     * @param context
     * @return
     */
    public static  ArrayList<ExpParent>   getParentChildInGroup(int group , Context context){

        String[] arrGroupTemp=null;
        grpExp = new ExpGroupHelper(context);
        _context = context;
        arrGroupTitle = grpExp.getGroup(group);
        arrGroupTemp = copyArray(arrGroupTitle);

        currentYear= ((EmployeeTrackerApplication)context.getApplicationContext()).getYearProcessing();
        keikenMonthSystem=(float) ((EmployeeTrackerApplication)context.getApplicationContext()).getKeikenMonthProcessing();

        /** trường hợp là giới tính thì phải setting lại text hiển thị */
        if (group == IExpGroup.EXP_GROUP_SEX){
            for (int i=0 ; i<arrGroupTemp.length;i++){
                if(arrGroupTemp[i]==null|| Integer.parseInt( arrGroupTemp[i])==0 ){
                    arrGroupTemp[i] ="Nữ";
                }else{
                    arrGroupTemp[i] ="Nam";
                }
            }
        }

        /** trường hợp là nhom labour thì phải setting lại text hiển thị */
        if (group == IExpGroup.EXP_GROUP_LABOUR_USER){
            for (int i=0 ; i<arrGroupTemp.length;i++){
                if(arrGroupTemp[i]==null|| Integer.parseInt( arrGroupTemp[i])==0 ){
                    arrGroupTemp[i] ="Không thuộc nhóm labor";
                }else{
                    arrGroupTemp[i] ="Thành viên nhóm labor";
                }
            }
        }

        /** trường hợp là phu cap nghiep vu thì phải setting lại text hiển thị */
        if (group == IExpGroup.EXP_GROUP_BUSSINESS_ALLOWANCE){
            for (int i=0 ; i<arrGroupTemp.length;i++){
                if(arrGroupTemp[i]==null|| ( arrGroupTemp[i])=="" ){
                    arrGroupTemp[i] ="Không có PC";
                }else{
                    //arrGroupTemp[i] ="";
                }
            }
        }

        /** trường hợp là nghỉ việc -chưa nghỉ iệc thì phải setting lại text hiển thị */
        if (group == IExpGroup.EXP_GROUP_YASUMI){
            for (int i=0 ; i<arrGroupTemp.length;i++){
                if(arrGroupTemp[i]==null|| ( arrGroupTemp[i])=="" ){
                    arrGroupTemp[i] ="Đang làm việc";
                }else{
                    arrGroupTemp[i] ="Đã nghỉ việc";
                }
            }
        }

        /** trường hợp là thâm niên thì phải setting lại text hiển thị */
        if (group == IExpGroup.EXP_GROUP_KEIKEN){
            for (int i=0 ; i<arrGroupTemp.length;i++){
                if( arrGroupTemp[i]==null|| Integer.parseInt( arrGroupTemp[i])==0){
                    arrGroupTemp[i] ="Nhỏ hơn 1 năm";
                }else if (Integer.parseInt(arrGroupTemp[i])==1){
                    arrGroupTemp[i] ="1 ～ 2 năm";
                }else if (Integer.parseInt(arrGroupTemp[i])==2){
                    arrGroupTemp[i] ="2 ～ 3 năm";
                }else if (Integer.parseInt(arrGroupTemp[i])==3){
                    arrGroupTemp[i] ="3 ～4 năm";
                }else if (Integer.parseInt(arrGroupTemp[i])==4){
                    arrGroupTemp[i] ="4 ～5 năm";
                }else if (Integer.parseInt(arrGroupTemp[i])==5){
                    arrGroupTemp[i] ="Lớn hơn 5 năm";
                }

            }
        }
        /** trường hợp là thâm niên nhóm labor thì phải setting lại text hiển thị */
        if (group == IExpGroup.EXP_GROUP_KEIKEN_LABOR){
            for (int i=0 ; i<arrGroupTemp.length;i++){
                if( arrGroupTemp[i]==null|| Integer.parseInt( arrGroupTemp[i])==0){
                    arrGroupTemp[i] ="Nhỏ hơn 1 năm";
                }else if (Integer.parseInt(arrGroupTemp[i])==1){
                    arrGroupTemp[i] ="1 ～ 2 năm";
                }else if (Integer.parseInt(arrGroupTemp[i])==2){
                    arrGroupTemp[i] ="2 ～ 3 năm";
                }else if (Integer.parseInt(arrGroupTemp[i])==3){
                    arrGroupTemp[i] ="3 ～4 năm";
                }else if (Integer.parseInt(arrGroupTemp[i])==4){
                    arrGroupTemp[i] ="4 ～5 năm";
                }else if (Integer.parseInt(arrGroupTemp[i])==5){
                    arrGroupTemp[i] ="Lớn hơn 5 năm";
                }

            }
        }
        /** trường hợp là lương thì phải setting lại text hiển thị */
        if (group == IExpGroup.EXP_GROUP_SALARY_BASIC){
            for (int i=0 ; i<arrGroupTemp.length;i++){
                if( arrGroupTemp[i]==null|| Integer.parseInt( arrGroupTemp[i])==0){
                    arrGroupTemp[i] ="Nhỏ hơn 300$";
                }else if (Integer.parseInt(arrGroupTemp[i])==1){
                    arrGroupTemp[i] ="300 ～ 399.9$";
                }else if (Integer.parseInt(arrGroupTemp[i])==2){
                    arrGroupTemp[i] ="400 ～ 499.9$";
                }else if (Integer.parseInt(arrGroupTemp[i])==3){
                    arrGroupTemp[i] ="500 ～ 599.9$";
                }else if (Integer.parseInt(arrGroupTemp[i])==4){
                    arrGroupTemp[i] ="600 ～ 699.9$";
                }else if (Integer.parseInt(arrGroupTemp[i])==5){
                    arrGroupTemp[i] ="Lớn hơn 700$";
                }

            }
        }

        /** trường hợp là chuyên môn thì phải setting lại text hiển thị */
        if (group == IExpGroup.EXP_GROUP_BUSINESS_KBN){
            for (int i=0 ; i<arrGroupTemp.length;i++){
                if( arrGroupTemp[i]==null|| Integer.parseInt( arrGroupTemp[i])==0){
                    arrGroupTemp[i] ="Chưa chỉ định";
                }else if (Integer.parseInt(arrGroupTemp[i])==1){
                    arrGroupTemp[i] ="Lập trình viên";
                }else if (Integer.parseInt(arrGroupTemp[i])==2){
                    arrGroupTemp[i] ="Phiên dịch";
                }else if (Integer.parseInt(arrGroupTemp[i])==3){
                    arrGroupTemp[i] ="Khác(tổng vụ, QA....)";
                }else if (Integer.parseInt(arrGroupTemp[i])==9){
                    arrGroupTemp[i] ="Lập trình viên-Phiên dịch...";
                }

            }
        }
        /** trường hợp số nhân viên thử việc trong năm setting tại system */
        if (group == IExpGroup.EXP_GROUP_TRAINING_YEAR){
            for (int i=0 ; i<arrGroupTemp.length;i++){
                if( arrGroupTemp[i]==null|| Integer.parseInt( arrGroupTemp[i])==0){
                    arrGroupTemp[i] ="Số LTV thử việc năm " + currentYear;
                }
            }
        }

        /** trường hợp số nhân viên được nhận chính thức trong năm setting tại system */
        if (group == IExpGroup.EXP_GROUP_CONTRACT_YEAR){
            for (int i=0 ; i<arrGroupTemp.length;i++){
                if( arrGroupTemp[i]==null|| Integer.parseInt( arrGroupTemp[i])==0){
                    arrGroupTemp[i] ="Số LTV nhận CT năm " + currentYear;
                }else if (Integer.parseInt(arrGroupTemp[i])==1){
                    arrGroupTemp[i] ="Số LTV nhận CT năm " + currentYear+"(TV năm "+ (currentYear-1) +")";
                }
            }
        }
        /** trường hợp số nhân viên thử việc nhưng không được nhận*/
        if (group == IExpGroup.EXP_GROUP_NOTCONTRACT_YEAR){
            for (int i=0 ; i<arrGroupTemp.length;i++){
                if( arrGroupTemp[i]==null|| Integer.parseInt( arrGroupTemp[i])==0){
                    arrGroupTemp[i] ="Số LTV không nhận sau thử việc năm " + currentYear;
                }
            }
        }

        /** trường hợp số nhân viên chính thức có thâm niên nhỏ hơn hoặc bằng N tháng ( setting tại system)*/
        if (group == IExpGroup.EXP_GROUP_CONTRACT_LESS_MONTH){
            for (int i=0 ; i<arrGroupTemp.length;i++){
                if( arrGroupTemp[i]==null|| Integer.parseInt( arrGroupTemp[i])==0){
                    arrGroupTemp[i] ="Số LTV có thâm niên <= " + keikenMonthSystem + " tháng";
                }
            }
        }
        /** so nhan vien co chuc vu khong phu hop voi tham nien*/
        if (group == IExpGroup.EXP_GROUP_STAFF_CURRENT_POSITION_NOT_SATIFIED){
            for (int i=0 ; i<arrGroupTemp.length;i++){
                if( arrGroupTemp[i]==null|| Integer.parseInt( arrGroupTemp[i])==0){
                    arrGroupTemp[i] ="Số LTV có chức vụ không phù hợp thâm niên";
                }
            }
        }

        /** trường hợp là thong ke nghi viec theo tung nam */
        if (group == IExpGroup.EXP_GROUP_YASUMI_YEAR) {
            for (int i = 0; i < arrGroupTemp.length; i++) {
                if (arrGroupTemp[i] == null || Integer.parseInt(arrGroupTemp[i]) == 0) {
                    arrGroupTemp[i] = "Nghỉ việc năm " + (currentYear - 2);
                } else if (Integer.parseInt(arrGroupTemp[i]) == 1) {
                    arrGroupTemp[i] = "Nghỉ việc năm " + (currentYear - 1);
                } else if (Integer.parseInt(arrGroupTemp[i]) == 2) {
                    arrGroupTemp[i] = "Nghỉ việc năm " + (currentYear);
                }
            }
        }
        ArrayList<ExpParent> arrayParents = new ArrayList<ExpParent>();

        /** here we set the parents and the children */
        for (int i = 0; i < arrGroupTitle.length; i++){
            ArrayList<User> arrayChildren = new ArrayList<User>();
            /** tạo Object để lưu trữ data tại node cha và con */
            ExpParent parent = new ExpParent();
            /** insert data cho node cha */
            if (arrGroupTemp[i]==null){
                if(group == IExpGroup.EXP_GROUP_STAFF_CONTRACT_STATUS_YEARMONTH){
                    parent.setTitle("Chưa nhận chính thức");
                }else{
                    parent.setTitle("");
                }

            }else{
                parent.setTitle(arrGroupTemp[i].toString());
            }
            /** insert data cho node con */
            if (arrGroupTemp[i]==null){
                listUser = grpExp.getChildGroup(group, "");
            }else{
                if( arrGroupTitle[i]==null || arrGroupTitle[i].equals("")){
                    listUser = grpExp.getChildGroup(group, "");
                }else{
                    listUser = grpExp.getChildGroup(group, arrGroupTitle[i].toString());
                }

            }

            if (listUser !=null){
                for(User usr : listUser){
                    arrayChildren.add(usr);
                }
                parent.setArrayChildren(arrayChildren);
                arrayParents.add(parent);
            }
        }

        return arrayParents;
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * copy Array
     *
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private static String[] copyArray(String[] source){
        String[] des={""};
        if(source==null){
            return des;
        }
        des=new String[source.length];
        for(int i=0 ; i<source.length; i++){
            des[i] = source[i];
        }
        return des;
    }
}
