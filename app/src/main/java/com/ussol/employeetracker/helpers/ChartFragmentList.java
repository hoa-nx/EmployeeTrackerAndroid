package com.ussol.employeetracker.helpers;

import com.ussol.employeetracker.ChartStaffStatusPDWorkingPDTrainingPDYasumiStackedBarChartFrag;
import com.ussol.employeetracker.ChartStaffStatusStackedBarChartFrag;
import com.ussol.employeetracker.ChartStaffStatusWorkingStackedBarChartFrag;
import com.ussol.employeetracker.ChartStaffStatusWorkingTrainingStackedBarChartFrag;
import com.ussol.employeetracker.ChartStaffStatusWorkingTrainingYasumiStackedBarChartFrag;
import com.ussol.employeetracker.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

public class ChartFragmentList extends ChartBase{
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_awesomedesign);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        
        PageAdapter a = new PageAdapter(getSupportFragmentManager());
        pager.setAdapter(a);
        
        
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Hướng dẫn");
        b.setMessage("Xem các biểu đồ bằng cách kéo sang trái/phải.");
        b.setPositiveButton("OK", new OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.show();
    }
       
    private class PageAdapter extends FragmentPagerAdapter {

        public PageAdapter(FragmentManager fm) {
            super(fm); 
        }

        @Override
        public Fragment getItem(int pos) {  
            Fragment f = null;
            
            switch(pos) {
            case 0:
                f = ChartStaffStatusStackedBarChartFrag.newInstance();
                break;
            case 1:
                f = ChartStaffStatusPDWorkingPDTrainingPDYasumiStackedBarChartFrag.newInstance();
                break;
            case 2:
                f = ChartStaffStatusWorkingTrainingYasumiStackedBarChartFrag.newInstance();
                break;
            case 3:
                f = ChartStaffStatusWorkingTrainingStackedBarChartFrag.newInstance();
                break;
            case 4:
                f = ChartStaffStatusWorkingStackedBarChartFrag.newInstance();
                break;
            }

            return f;
        }

        @Override
        public int getCount() {
            return 5;
        }       
    }
}
