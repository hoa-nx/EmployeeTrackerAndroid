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

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.List;

import com.ussol.employeetracker.R;
import com.ussol.employeetracker.helpers.DatabaseAdapter.MESSAGE_STATUS;
import com.ussol.employeetracker.models.MasterConstants.MESSAGE_TYPE;
import com.ussol.employeetracker.services.AlarmJobService;
import com.ussol.employeetracker.swipe.SwipeListView;
import com.ussol.employeetracker.utils.Strings;
import com.ussol.employeetracker.utils.Utils;

public class MessageAdapter extends BaseAdapter {

    private List<MessageItem> data;
    private Context context;
    /** chuyển đổi Cursor thành List */
	private ConvertCursorToListString mConvertCursorToListString;
	//DB adapter
	private DatabaseAdapter mDatabaseAdapter;
	private MessageAdapter adapter;
    public MessageAdapter(Context context, List<MessageItem> data) {
        this.context = context;
        this.data = data;
        this.adapter = this;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public MessageItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    @Override
//    public boolean isEnabled(int position) {
//        if (position == 2) {
//            return false;
//        } else {
//            return true;
//        }
//    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MessageItem item = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.package_row, parent, false);
            holder = new ViewHolder();
            holder.ivImage = (ImageView) convertView.findViewById(R.id.example_row_iv_image);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.example_row_tv_title);
            holder.tvDescription = (TextView) convertView.findViewById(R.id.example_row_tv_description);
            holder.bAction1 = (Button) convertView.findViewById(R.id.example_row_b_action_1);
            holder.bAction2 = (Button) convertView.findViewById(R.id.example_row_b_action_2);
            holder.bAction3 = (Button) convertView.findViewById(R.id.example_row_b_action_3);
            holder.tvCode = (TextView) convertView.findViewById(R.id.example_row_tv_code);
            holder.tvEmpCode = (TextView) convertView.findViewById(R.id.example_row_tv_empcode);
            holder.tvDate = (TextView) convertView.findViewById(R.id.example_row_tv_date);
            holder.tvStatus = (TextView) convertView.findViewById(R.id.example_row_tv_status);
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ((SwipeListView)parent).recycle(convertView, position);

        holder.ivImage.setImageDrawable(item.getIcon());
        holder.tvTitle.setText(item.getEmpName());
        if(item.getEmpInfo()!=null){
        	holder.tvDescription.setText(item.getEmpInfo().birthday);
        	holder.tvEmpCode .setText(String.valueOf(item.getEmpInfo().code));
        }
        
        holder.tvCode.setText(String.valueOf(item.getId()));
        holder.tvDate.setText(item.getSendDate());
        holder.tvStatus.setText(String.valueOf(item.getMessageStatus().name()));
        holder.bAction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	//set skip message
            	AlarmJobService.updateMessageStatus(context, item.getId(), MESSAGE_STATUS.SKIP);
            	//cap nhat lai listview 
        		data.remove(item);
        		adapter.notifyDataSetChanged();
            }
        });

        holder.bAction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send sms
            	//set skip message
            	SystemConfigItemHelper systemConfig  = new SystemConfigItemHelper(context);
            	String sim =  systemConfig.getSimSendSms();
            	
        		int msgTemplateId = 1;
        		if(item.getMsgTemplateCode() == MESSAGE_TYPE.BIRTHDAY.ordinal()){
        			msgTemplateId = Integer.parseInt(systemConfig.getBirthdayMsgCode());	
        		}
        		if(item.getMsgTemplateCode() == MESSAGE_TYPE.YASUMI.ordinal()){
        			msgTemplateId = Integer.parseInt(systemConfig.getYasumiMsgCode());
        		}
        		if(item.getMsgTemplateCode() == MESSAGE_TYPE.TRAIL.ordinal()){
        			msgTemplateId = Integer.parseInt(systemConfig.getTrainingMsgCode());
        		}
        		
            	String msg = AlarmJobService.getMessageContent(context,msgTemplateId);
            	
            	AlarmJobService.sendSmsMessage(context, item.getId(), msg, sim);
            	//cap nhat lai listview 
        		data.remove(item);
        		adapter.notifyDataSetChanged();
            }
        });

        holder.bAction3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete message status
            	/** chuyển đổi từ Cursor thành List */
            	mDatabaseAdapter = new DatabaseAdapter(context);
        		mConvertCursorToListString = new ConvertCursorToListString(context);
        		mDatabaseAdapter.open();
        		mDatabaseAdapter.deletePernamentUserMessageStatus(item.getId());
        		mDatabaseAdapter.close();
        		//cap nhat lai listview 
        		data.remove(item);
        		
        		adapter.notifyDataSetChanged();
            }
        });


        return convertView;
    }

    static class ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
        TextView tvDescription;
        Button bAction1;
        Button bAction2;
        Button bAction3;
        TextView tvCode;
        TextView tvEmpCode;
        TextView tvDate;
        TextView tvStatus;
    }

    private boolean isPlayStoreInstalled() {
        Intent market = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=dummy"));
        PackageManager manager = context.getPackageManager();
        List<ResolveInfo> list = manager.queryIntentActivities(market, 0);

        return list.size() > 0;
    }

}
