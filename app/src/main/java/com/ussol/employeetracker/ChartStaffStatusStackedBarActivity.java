package com.ussol.employeetracker;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ussol.employeetracker.helpers.ChartBase;
import com.ussol.employeetracker.helpers.ConvertCursorToArrayString;
import com.ussol.employeetracker.helpers.DatabaseAdapter;
import com.ussol.employeetracker.helpers.SystemConfigItemHelper;
import com.ussol.employeetracker.models.ChartItem;
import com.ussol.employeetracker.utils.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ChartStaffStatusStackedBarActivity extends ChartBase implements OnSeekBarChangeListener, OnChartValueSelectedListener {

	private BarChart mChart;
	private SeekBar mSeekBarX, mSeekBarY;
	private TextView tvX, tvY;
	private DatabaseAdapter mDatabaseAdapter; 
	private SystemConfigItemHelper systemConfig ;
    private int currentYear;
    private boolean displayDescriptionOnChart;
    /** chuyển đổi Cursor thành string array */
	private ConvertCursorToArrayString mConvertCursorToArrayString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.chart_staff_status_stacked_bar_activity);

		mDatabaseAdapter = new DatabaseAdapter(this);
    	mConvertCursorToArrayString = new ConvertCursorToArrayString(this);
    	systemConfig = new SystemConfigItemHelper(this);
    	currentYear = systemConfig.getYearProcessing();
    	if(currentYear==0){
    		currentYear = Calendar.getInstance().get(Calendar.YEAR);
    	}
        displayDescriptionOnChart= systemConfig.getDisplayDescriptionOnChart();
        
        
		tvX = (TextView) findViewById(R.id.tvXMax);
		tvY = (TextView) findViewById(R.id.tvYMax);

		mSeekBarX = (SeekBar) findViewById(R.id.seekBar1);
		mSeekBarX.setOnSeekBarChangeListener(this);

		mSeekBarY = (SeekBar) findViewById(R.id.seekBar2);
		mSeekBarY.setOnSeekBarChangeListener(this);

		mChart = (BarChart) findViewById(R.id.chart_staff_status);
		mChart.setOnChartValueSelectedListener(this);
		if(displayDescriptionOnChart){
			mChart.setDescription("Biểu đồ so sánh số nhân sự theo từng tháng");
		}else{
			mChart.setDescription("");
		}
		// if more than 60 entries are displayed in the chart, no values will be
		// drawn
		mChart.setMaxVisibleValueCount(150);

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
		xLabels.setLabelsToSkip(0);
		// mChart.setDrawXLabels(false);
		// mChart.setDrawYLabels(false);

		// setting data
		mSeekBarX.setProgress(12);
		mSeekBarY.setProgress(100);

		Legend l = mChart.getLegend();
		l.setPosition(LegendPosition.BELOW_CHART_RIGHT);
		l.setFormSize(8f);
		l.setFormToTextSpace(4f);
		l.setXEntrySpace(6f);

		// mChart.setDrawLegend(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.bar, menu);
		return true;
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
			if (mChart.saveToGallery("Lưu" + System.currentTimeMillis(), 100)) {
				Toast.makeText(getApplicationContext(), "Lưu thành công.", Toast.LENGTH_SHORT).show();
			} else
				Toast.makeText(getApplicationContext(), "Không thể lưu chart", Toast.LENGTH_SHORT).show();
			break;
		}
		}
		return true;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

		tvX.setText("" + (mSeekBarX.getProgress() + 1));
		tvY.setText("" + (mSeekBarY.getProgress()));

		/*ArrayList<String> xVals = new ArrayList<String>();
		for (int i = 0; i < mSeekBarX.getProgress() + 1; i++) {
			xVals.add(mMonths[i % mMonths.length]);
		}*/
		/** Tao array gom 12 thang cua nam hien tai */
          
        ArrayList<ChartItem> devWorkingList = new ArrayList<ChartItem>();
        ArrayList<ChartItem> pdWorkingList =new ArrayList<ChartItem>();
        ArrayList<ChartItem> devTrainingList =new ArrayList<ChartItem>();
        ArrayList<ChartItem> pdTrainingList =new ArrayList<ChartItem>();
        ArrayList<ChartItem> pdYasumiList =new ArrayList<ChartItem>();
        ArrayList<ChartItem> devYasumiList =new ArrayList<ChartItem>();

        devWorkingList =mConvertCursorToArrayString.getDevWorkingFromViewChartItem(currentYear, "");
        pdWorkingList =mConvertCursorToArrayString.getPDWorkingFromViewChartItem(currentYear, "");
        devTrainingList =mConvertCursorToArrayString.getDevTrainingFromViewChartItem(currentYear);
        pdTrainingList =mConvertCursorToArrayString.getPDTrainingFromViewChartItem(currentYear);
        pdYasumiList =mConvertCursorToArrayString.getPDYasumiFromViewChartItem();	
        devYasumiList =mConvertCursorToArrayString.getDevYasumiFromViewChartItem();	
  
		ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
		//loop qua 12 tháng
		String yearMonth="";
		DateTime firstMonthCurrentYear = new DateTime(currentYear, 1, 1, 0, 0);
        DateTime firstMonthCurrentYearFix = new DateTime(currentYear, 1, 1, 0, 0);
        for(int month=0;month<12;month++){
        	firstMonthCurrentYear =firstMonthCurrentYearFix.plusMonths(month);
        	DateTimeFormatter fmt = ISODateTimeFormat.yearMonth();
        	yearMonth =fmt.print(firstMonthCurrentYear);
        	
        	float devWork = Float.parseFloat(Utils.findValueItemFromChartItem(yearMonth, devWorkingList));
        	float pdWork = Float.parseFloat(Utils.findValueItemFromChartItem(yearMonth, pdWorkingList));
        	float devTraining = Float.parseFloat(Utils.findValueItemFromChartItem(yearMonth, devTrainingList));
        	float pdTraining = Float.parseFloat(Utils.findValueItemFromChartItem(yearMonth, pdTrainingList));
        	float devYasumi = Float.parseFloat(Utils.findValueItemFromChartItem(yearMonth, devYasumiList));
        	float pdYasumi = Float.parseFloat(Utils.findValueItemFromChartItem(yearMonth, pdYasumiList));

        	yVals1.add(new BarEntry(new float[] { devWork, pdWork, devTraining + pdTraining, devYasumi + pdYasumi  }, month));
        }
        
        /*
		for (int i = 0; i <12 ; i++) {
			float mult = (mSeekBarY.getProgress() + 1);
			float val1 = (float) (Math.random() * mult) + mult / 3;
			float val2 = (float) (Math.random() * mult) + mult / 3;
			float val3 = (float) (Math.random() * mult) + mult / 3;

			yVals1.add(new BarEntry(new float[] { devWork, val2, val3 }, i));
		}
		*/
        
		BarDataSet set1 = new BarDataSet(yVals1, "  --Năm " + String.valueOf(currentYear));
		set1.setColors(getColors());
		set1.setStackLabels(new String[] { "LTV", "PD", "Thử việc(LT&PD)" ,"Nghỉ việc(LT&PD)"});

		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		dataSets.add(set1);

		BarData data = new BarData(super.mMonths, dataSets);
		data.setValueFormatter(new MyValueFormatter());

		mChart.setData(data);
		mChart.invalidate();
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

		BarEntry entry = (BarEntry) e;

		if (entry.getVals() != null)
			Log.i("VAL SELECTED", "Value: " + entry.getVals()[h.getStackIndex()]);
		else
			Log.i("VAL SELECTED", "Value: " + entry.getVal());
	}

	@Override
	public void onNothingSelected() {
		// TODO Auto-generated method stub

	}

	private int[] getColors() {

		int stacksize = 4;

		// have as many colors as stack-values per entry
		int[] colors = new int[stacksize];

		for (int i = 0; i < stacksize; i++) {
			colors[i] = ColorTemplate.COLORFUL_COLORS[i+1];
		}

		return colors;
	}
	
	public class MyValueFormatter implements com.github.mikephil.charting.formatter.ValueFormatter {

	    private DecimalFormat mFormat;
	    
	    public MyValueFormatter() {
	        mFormat = new DecimalFormat("###");
	    }

	    @Override
	    public String getFormattedValue(float value, Entry entry, int dataSetIndex, com.github.mikephil.charting.utils.ViewPortHandler viewPortHandler) {
	    	if(value==0){
	    		return "";
	    	}
	        return mFormat.format(value);
	    }
	}
}