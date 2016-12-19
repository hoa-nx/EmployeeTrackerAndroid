/*
 * Copyright (C) 2010 Eric Harlow
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ussol.employeetracker;

import java.util.ArrayList;

import com.ussol.employeetracker.R;
import com.ussol.employeetracker.dragdrop.DragListener;
import com.ussol.employeetracker.dragdrop.DragNDropAdapter;
import com.ussol.employeetracker.dragdrop.DragNDropListView;
import com.ussol.employeetracker.dragdrop.DropListener;
import com.ussol.employeetracker.dragdrop.RemoveListener;
import com.ussol.employeetracker.helpers.DatabaseAdapter;
import com.ussol.employeetracker.models.MasterConstants;
import com.ussol.employeetracker.utils.Utils;

import android.app.ListActivity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DragNDropListActivity extends ListActivity {
	DragNDropAdapter adapter;
	SharedPreferences settings;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dragndroplistview);
        
        ArrayList<String> content = new ArrayList<String>(Utils.mListContent.length);
        /*for (int i=0; i < Utils.mListContent.length; i++) {
        	content.add(Utils.mListContent[i]);
        }*/
        
        /** get file preferences*/
        //PreferenceManager.setDefaultValues(this, R.xml.sort_item_preferences, true);
        
        settings = getSharedPreferences(MasterConstants.PRE_SORT_FILE, Context.MODE_PRIVATE);
        /**Xoa noi dung file */
        //Utils.ClearSharedPreferences(settings);
        
        /** get thông tin từ setting và hiển thị lên màn hình */
        content= readPreferencesAndDisplay(settings);
        
        adapter=  new DragNDropAdapter(this, new int[]{R.layout.dragitem}, new int[]{R.id.lblUserHisNewSalary}, content);
        setListAdapter(adapter);//new DragNDropAdapter(this,content)
        ListView listView = getListView();
        
        if (listView instanceof DragNDropListView) {
        	((DragNDropListView) listView).setDropListener(mDropListener);
        	((DragNDropListView) listView).setRemoveListener(mRemoveListener);
        	((DragNDropListView) listView).setDragListener(mDragListener);
        }
         
        /** Save seeting button event */ 
		Button btn = (Button) findViewById(R.id.btnSortSave);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveToPreferences(settings);
				/** trả về kết quả*/
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		/** Save seeting button event */ 
		Button btnCancel = (Button) findViewById(R.id.btnSortCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		
    }

	private DropListener mDropListener = 
		new DropListener() {
        public void onDrop(int from, int to) {
        	ListAdapter adapter = getListAdapter();
        	if (adapter instanceof DragNDropAdapter) {
        		((DragNDropAdapter)adapter).onDrop(from, to);
        		getListView().invalidateViews();
        	}
        }
    };
    
    private RemoveListener mRemoveListener =
        new RemoveListener() {
        public void onRemove(int which) {
        	ListAdapter adapter = getListAdapter();
        	if (adapter instanceof DragNDropAdapter) {
        		((DragNDropAdapter)adapter).onRemove(which);
        		getListView().invalidateViews();
        	}
        }
    };
    
    private DragListener mDragListener =
    	new DragListener() {

    	int backgroundColor = 0xe0103010;
    	int defaultBackgroundColor;
    	
			public void onDrag(int x, int y, ListView listView) {
				// TODO Auto-generated method stub
			}

			public void onStartDrag(View itemView) {
				itemView.setVisibility(View.INVISIBLE);
				defaultBackgroundColor = itemView.getDrawingCacheBackgroundColor();
				itemView.setBackgroundColor(backgroundColor);
				ImageView iv = (ImageView)itemView.findViewById(R.id.cookie_128);
				if (iv != null) iv.setVisibility(View.INVISIBLE);
			}

			public void onStopDrag(View itemView) {
				itemView.setVisibility(View.VISIBLE);
				itemView.setBackgroundColor(defaultBackgroundColor);
				ImageView iv = (ImageView)itemView.findViewById(R.id.cookie_128);
				if (iv != null) iv.setVisibility(View.VISIBLE);
			}
    	
    };
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * Lưu thông tin trên màn hình xuống file
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void saveToPreferences(SharedPreferences pre){
    	Editor edit = pre.edit();
    	ArrayList<String> mDataSorted = adapter.getDataSort();
    	if (mDataSorted.size()==0){
    		
    	}else{
    		String xKey = "pre_SortItem_";
    		for(int i=0 ; i<mDataSorted.size();i++){
    			edit.putString(xKey + Integer.valueOf(i).toString(), mDataSorted.get(i));
    		}
    	}
    	edit.commit();
    }
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * Đọc thông tin từ file và hiển thị lên màn hình
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    private ArrayList<String>  readPreferencesAndDisplay(SharedPreferences pre){
    	ArrayList<String> mDataSorted = new ArrayList<String>();
    	String xKey = "pre_SortItem_";
    	
    	for(int i=0 ; i<Utils.mListContent.length;i++){
    		mDataSorted.add(pre.getString(xKey + Integer.valueOf(i).toString(), Utils.mListContent[i]));
    		
    	}
    	return mDataSorted;
    }
    
}