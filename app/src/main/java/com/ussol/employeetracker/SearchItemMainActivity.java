package com.ussol.employeetracker;

import com.ussol.employeetracker.models.MasterConstants;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class SearchItemMainActivity extends TabActivity {
	TabHost tabHost;
	TabHost.TabSpec spec;
	Intent intent;
	
	public SearchItemMainActivity(){
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState==null){
			Resources res = getResources(); // Resource object to get Drawables
		    TabHost tabHost = getTabHost(); // The activity TabHost
		    TabHost.TabSpec spec; // Resusable TabSpec for each tab
		    Intent intent; // Reusable Intent for each tab
		    /** lấy kích thước của màn hình */
		    DisplayMetrics displaymetrics = new DisplayMetrics();
		    getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		    int height = displaymetrics.heightPixels;
		    int wwidth = displaymetrics.widthPixels;

		    // Create an Intent to launch an Activity for the tab (to be reused)
		    intent = new Intent().setClass(this, SearchItemTabFirstAcivity.class);
		    // Initialize a TabSpec for each tab and add it to the TabHost
		    spec = tabHost.newTabSpec(MasterConstants.TAB_SEARCH_ITEM_1_TAG ).setIndicator("Cơ bản", res.getDrawable(R.drawable.searchitem)).setContent(intent);
		    
		    tabHost.setMinimumHeight(height-100);
		    tabHost.setMinimumWidth(wwidth-80);
		    tabHost.addTab(spec);
	
		    // Do the same for the other tabs
		    intent = new Intent().setClass(this, SearchItemTabSecondAcivity.class);
		    spec = tabHost.newTabSpec(MasterConstants.TAB_SEARCH_ITEM_2_TAG).setIndicator("Khác", res.getDrawable(R.drawable.searchitem)).setContent(intent);
		    tabHost.setMinimumHeight(height-100);
		    tabHost.setMinimumWidth(wwidth-80);
		    tabHost.addTab(spec);
		    
	    
		}
		  
	    /*SearchItemTabFirstAcivity childAct = (SearchItemTabFirstAcivity) getTabHost().getChildAt(0).getContext();
	    childAct._depts;*/
	   /* tabHost.setOnTabChangedListener(new OnTabChangeListener(){
	        @Override
	        public void onTabChanged(String tabId) {
	            if(tabId.equals("tab1")) {
	                //tab1
	            }
	            else if(tabId.equals("tab2")) {
	                //tab2
	            }
	            else if(tabId.equals("tab3")) {
	                //tab3
	            }
	        }
	    });*/
	}

	@Override
	public void finish() {
		super.finish();
		/*SharedPreferences	settings = getSharedPreferences("search_item_preferences", Context.MODE_PRIVATE);
	    SearchItemTabSecondAcivity activity2 = (SearchItemTabSecondAcivity) getLocalActivityManager().getActivity(MasterConstants.TAB_SEARCH_ITEM_2_TAG);
	    if (activity2!=null){
	    	activity2.saveToPreferences(settings);
	    }*/
	}
	public  SearchItemTabSecondAcivity getTabSecondActivity(){
		return  (SearchItemTabSecondAcivity) getLocalActivityManager().getActivity(MasterConstants.TAB_SEARCH_ITEM_2_TAG);
	}
	
	private void addTab(String labelId, Drawable drawable, Class<?> c) {

		tabHost = getTabHost();
		intent = new Intent(this, c);
		spec = tabHost.newTabSpec("tab" + labelId);

		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);

		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageDrawable(drawable);
		icon.setScaleType(ImageView.ScaleType.FIT_CENTER);

		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
	}
}
