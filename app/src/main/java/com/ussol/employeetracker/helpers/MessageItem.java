/*
 * Copyright (C) 2013 47 Degrees, LLC
 *  http://47deg.com
 *  hello@47deg.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.ussol.employeetracker.helpers;

import com.ussol.employeetracker.helpers.DatabaseAdapter.MESSAGE_STATUS;
import com.ussol.employeetracker.models.MasterConstants.MESSAGE_TYPE;
import com.ussol.employeetracker.models.User;

import android.graphics.drawable.Drawable;

public class MessageItem {

    private Drawable icon;

    private String name;

    private String packageName;

    private String empName;
    
    private User userInfo;
    
    private int msgType ;
    
    private int msgCode ;
    
    private int msgEmpCode ;
    
    private String msgSend_Date ;
    
    private MESSAGE_STATUS msgStatus;
    
    private int msgTemplateCode ;
    
    public int getId() {
        return msgCode;
    }

    public void setId(int msgCode) {
        this.msgCode = msgCode;
    }
    
    public int getMsgTemplateCode() {
        return msgTemplateCode;
    }

    public void setMsgTemplateCode(int msgTemplateCode) {
        this.msgTemplateCode = msgTemplateCode;
    }
    
    
    public MESSAGE_STATUS getMessageStatus() {
        return msgStatus;
    }

    public void setMessageStatus(MESSAGE_STATUS msgStatus) {
        this.msgStatus = msgStatus;
    }
    
    public int getEmpCode() {
        return msgEmpCode;
    }

    public void setEmpCode(int msgEmpCode) {
        this.msgEmpCode = msgEmpCode;
    }
    public String getSendDate() {
        return msgSend_Date;
    }

    public void setSendDate(String msgSend_Date) {
        this.msgSend_Date = msgSend_Date;
    }
    
    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }
    
    public int getMessageType() {
        return msgType ;
    }

    public void setMessageType(MESSAGE_TYPE msgType ) {
        this.msgType = msgType.ordinal();
    }
    public void setMessageType(int msgType ) {
        this.msgType = msgType;
    }
    public User getEmpInfo() {
        return userInfo;
    }

    public void setEmpInfo(User empInfo) {
        this.userInfo = empInfo;
    }
    
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
