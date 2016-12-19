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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;


public class ChartStaffStatusStackedBarChartFrag extends ChartFragmentData implements OnChartGestureListener {
	private SystemConfigItemHelper systemConfig ;
    private int currentYear;
    private boolean displayDescriptionOnChart;
    private boolean displayValueOnClick ;
    public static Fragment newInstance() {
        return new ChartStaffStatusStackedBarChartFrag();
    }

    private BarChart mChart;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_simple_bar, container, false);
        systemConfig = new SystemConfigItemHelper(getActivity());
    	currentYear = systemConfig.getYearProcessing();
    	displayValueOnClick = systemConfig.getDisplayValueOnClick();
    	if(currentYear==0){
    		currentYear = Calendar.getInstance().get(Calendar.YEAR);
    	}
        displayDescriptionOnChart= systemConfig.getDisplayDescriptionOnChart();
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
        
        mChart.setData(getBarDataDevWorkingDevTrainingDevYasumi(getActivity()));
        if(displayDescriptionOnChart){
        	mChart.setDescription("Thống kê số LTV chính thức-thử việc-nghỉ việc");	
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
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.bar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		/*case R.id.actionToggleValues: {
			for (DataSet<?> set : mChart.getData().getDataSets())
				set.setDrawValues(!set.isDrawValuesEnabled());

			mChart.invalidate();
			break;
		}
		case R.id.actionToggleHighlight: {
			if (mChart.isHighlightEnabled())
				mChart.setHighlightEnabled(false);
			else
				mChart.setHighlightEnabled(true);
			mChart.invalidate();
			break;
		}
		case R.id.actionTogglePinch: {
			if (mChart.isPinchZoomEnabled())
				mChart.setPinchZoom(false);
			else
				mChart.setPinchZoom(true);

			mChart.invalidate();
			break;
		}
		case R.id.actionToggleAutoScaleMinMax: {
			mChart.setAutoScaleMinMaxEnabled(!mChart.isAutoScaleMinMaxEnabled());
			mChart.notifyDataSetChanged();
			break;
		}
		case R.id.actionToggleHighlightArrow: {
			if (mChart.isDrawHighlightArrowEnabled())
				mChart.setDrawHighlightArrow(false);
			else
				mChart.setDrawHighlightArrow(true);
			mChart.invalidate();
			break;
		}
		case R.id.actionToggleStartzero: {
			mChart.getAxisLeft().setStartAtZero(!mChart.getAxisLeft().isStartAtZeroEnabled());
			mChart.getAxisRight().setStartAtZero(!mChart.getAxisRight().isStartAtZeroEnabled());
			mChart.invalidate();
			break;
		}
		case R.id.animateX: {
			mChart.animateX(3000);
			break;
		}
		case R.id.animateY: {
			mChart.animateY(3000);
			break;
		}
		case R.id.animateXY: {

			mChart.animateXY(3000, 3000);
			break;
		}
		case R.id.actionToggleFilter: {

			Approximator a = new Approximator(ApproximatorType.DOUGLAS_PEUCKER, 25);

			if (!mChart.isFilteringEnabled()) {
				mChart.enableFiltering(a);
			} else {
				mChart.disableFiltering();
			}
			mChart.invalidate();
			break;
		}*/
		case R.id.actionSave: {
			if (mChart.saveToGallery("Lưu" + System.currentTimeMillis(), 50)) {
				Toast.makeText(getActivity().getApplicationContext(), "Lưu thành công", Toast.LENGTH_SHORT).show();
			} else
				Toast.makeText(getActivity().getApplicationContext(), "Không thể lưu chart", Toast.LENGTH_SHORT).show();
			break;
		}
		}
		return true;
	}
}