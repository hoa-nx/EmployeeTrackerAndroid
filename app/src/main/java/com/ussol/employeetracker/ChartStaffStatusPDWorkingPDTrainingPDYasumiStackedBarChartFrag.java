package com.ussol.employeetracker;

import java.util.Calendar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.ussol.employeetracker.helpers.ChartFragmentData;
import com.ussol.employeetracker.helpers.ChartMarkerView;
import com.ussol.employeetracker.helpers.SystemConfigItemHelper;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


public class ChartStaffStatusPDWorkingPDTrainingPDYasumiStackedBarChartFrag extends ChartFragmentData implements OnChartGestureListener {

	private SystemConfigItemHelper systemConfig ;
    private int currentYear;
    private boolean displayDescriptionOnChart;
    private boolean displayValueOnClick ;
    public static Fragment newInstance() {
        return new ChartStaffStatusPDWorkingPDTrainingPDYasumiStackedBarChartFrag();
    }

    private BarChart mChart;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_simple_bar, container, false);
        
        systemConfig = new SystemConfigItemHelper(getActivity());
    	currentYear = systemConfig.getYearProcessing();
    	if(currentYear==0){
    		currentYear = Calendar.getInstance().get(Calendar.YEAR);
    	}
        displayDescriptionOnChart= systemConfig.getDisplayDescriptionOnChart();
        displayValueOnClick = systemConfig.getDisplayValueOnClick();
        // create a new chart object
        mChart = new BarChart(getActivity());
        mChart.setDescription("");
        mChart.setOnChartGestureListener(this);
        
        if(displayValueOnClick){
        	ChartMarkerView mv = new ChartMarkerView(getActivity(), R.layout.custom_marker_view);

        	mChart.setMarkerView(mv);
        }
        
        mChart.setHighlightEnabled(false);

        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"OpenSans-Light.ttf");
        mChart.setData(getBarDataPDWorkingPDTrainingPDYasumi(getActivity()));
        if(displayDescriptionOnChart){
        	mChart.setDescription("Thống kê số nhân viên PD chính thức-thử việc-nghỉ việc");	
        }

        // scaling can now only be done on x- and y-axis separately
 		mChart.setPinchZoom(false);

 		mChart.setDrawGridBackground(false);
 		mChart.setDrawBarShadow(false);

 		mChart.setDrawValueAboveBar(false);
 		
 		// change the position of the y-labels
 		YAxis yLabels = mChart.getAxisLeft();
 		//yLabels.setValueFormatter(new MyYAxisValueFormatter());
 		mChart.getAxisRight().setEnabled(false);

 		XAxis xLabels = mChart.getXAxis();
 		xLabels.setPosition(XAxisPosition.TOP);

 		Legend l = mChart.getLegend();
 		l.setPosition(LegendPosition.BELOW_CHART_RIGHT);
 		l.setFormSize(8f);
 		l.setFormToTextSpace(4f);
 		l.setXEntrySpace(6f);
        
        // programatically add the chart
        FrameLayout parent = (FrameLayout) v.findViewById(R.id.parentLayout);
        parent.addView(mChart);
        
        return v;
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart longpressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
    }
   
    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

	@Override
	public void onChartTranslate(MotionEvent me, float dX, float dY) {
		Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
	}

}