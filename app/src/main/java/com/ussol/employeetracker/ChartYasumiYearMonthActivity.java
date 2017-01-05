package com.ussol.employeetracker;

import java.util.ArrayList;
import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.ussol.employeetracker.helpers.ChartBase;
import com.ussol.employeetracker.helpers.DatabaseAdapter;
import com.ussol.employeetracker.helpers.ExpAdapter;
import com.ussol.employeetracker.helpers.ExpGroupHelper;
import com.ussol.employeetracker.helpers.ExpParent;
import com.ussol.employeetracker.helpers.SystemConfigItemHelper;
import com.ussol.employeetracker.models.ChartItem;
import com.ussol.employeetracker.models.IExpGroup;
import com.ussol.employeetracker.models.User;
import com.ussol.employeetracker.utils.DateTimeUtil;
import com.ussol.employeetracker.utils.Utils;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.filter.Approximator;
import com.github.mikephil.charting.data.filter.Approximator.ApproximatorType;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.highlight.Highlight;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ChartYasumiYearMonthActivity extends ChartBase  implements OnSeekBarChangeListener,
OnChartValueSelectedListener {

	private ArrayList<ChartItem> arrGroup=null;
	private DatabaseAdapter mDatabaseAdapter;
	private ExpGroupHelper  grp =null;
	private LineChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;
    private SystemConfigItemHelper systemConfig ;
    private int currentYear;
    private boolean displayDescriptionOnChart;
    private boolean displayValueOnClick ;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.chart_yasumi_yearmonth_activity);

        //mo ket noi SQLite
        mDatabaseAdapter = new DatabaseAdapter(getBaseContext());
        grp = new ExpGroupHelper(getApplicationContext());
        getParentChildInGroup(IExpGroup.EXP_GROUP_YASUMI_YEARMONTH);
        
        systemConfig = new SystemConfigItemHelper(getApplicationContext());
    	currentYear = systemConfig.getYearProcessing();
    	if(currentYear==0){
    		currentYear = Calendar.getInstance().get(Calendar.YEAR);
    	}
        displayDescriptionOnChart= systemConfig.getDisplayDescriptionOnChart();
        displayValueOnClick = systemConfig.getDisplayValueOnClick();
        
        tvX = (TextView) findViewById(R.id.tvXMax);
        tvY = (TextView) findViewById(R.id.tvYMax);

        mSeekBarX = (SeekBar) findViewById(R.id.seekBar1);
        mSeekBarX.setOnSeekBarChangeListener(this);

        mSeekBarY = (SeekBar) findViewById(R.id.seekBar2);
        mSeekBarY.setOnSeekBarChangeListener(this);

        mChart = (LineChart) findViewById(R.id.chart_yasumiyearmonth);
        mChart.setOnChartValueSelectedListener(this);
        
        mChart.setDrawGridBackground(false);
        if(displayDescriptionOnChart){
        	mChart.setDescription("Thống kê nghỉ việc so với năm trước");
        }else{
        	mChart.setDescription("");
        }
        

        // mChart.setStartAtZero(true);

        // enable value highlighting
        mChart.setHighlightEnabled(true);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mSeekBarX.setProgress(20);
        mSeekBarY.setProgress(12);

        XAxis xLabels = mChart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.TOP);
        xLabels.setLabelsToSkip(0);

        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.BELOW_CHART_CENTER);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            /*
            case R.id.actionToggleValues: {
                for (DataSet<?> set : mChart.getData().getDataSets())
                    set.setDrawValues(!set.isDrawValuesEnabled());

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
            case R.id.actionToggleHighlight: {
                if (mChart.isHighlightEnabled())
                    mChart.setHighlightEnabled(false);
                else
                    mChart.setHighlightEnabled(true);
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleFilled: {
                ArrayList<LineDataSet> sets = (ArrayList<LineDataSet>) mChart.getData()
                        .getDataSets();

                for (LineDataSet set : sets) {
                    if (set.isDrawFilledEnabled())
                        set.setDrawFilled(false);
                    else
                        set.setDrawFilled(true);
                }
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleCircles: {
                ArrayList<LineDataSet> sets = (ArrayList<LineDataSet>) mChart.getData()
                        .getDataSets();

                for (LineDataSet set : sets) {
                    if (set.isDrawCirclesEnabled())
                        set.setDrawCircles(false);
                    else
                        set.setDrawCircles(true);
                }
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleFilter: {

                // the angle of filtering is 35ﾂｰ
                Approximator a = new Approximator(ApproximatorType.DOUGLAS_PEUCKER, 35);

                if (!mChart.isFilteringEnabled()) {
                    mChart.enableFiltering(a);
                } else {
                    mChart.disableFiltering();
                }
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleStartzero: {
                mChart.getAxisLeft().setStartAtZero(!mChart.getAxisLeft().isStartAtZeroEnabled());
                mChart.getAxisRight().setStartAtZero(!mChart.getAxisRight().isStartAtZeroEnabled());
                mChart.invalidate();
                break;
            }
            */
            case R.id.actionSave: {
                // mChart.saveToGallery("title"+System.currentTimeMillis());
                if (mChart.saveToGallery("Lưu" + System.currentTimeMillis(), 100)) {
                    Toast.makeText(getApplicationContext(), "Lưu thành công.", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Không thể lưu chart", Toast.LENGTH_SHORT).show();
                break;
            }
            /*
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
            */
        }
        return true;
    }

    private int[] mColors = new int[] {
            ColorTemplate.COLORFUL_COLORS[0], 
            ColorTemplate.COLORFUL_COLORS[1],
            ColorTemplate.COLORFUL_COLORS[2]
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        
        mChart.resetTracking();

        tvX.setText("" + (mSeekBarX.getProgress()));
        tvY.setText("" + (mSeekBarY.getProgress()));

        ArrayList<String> xVals = new ArrayList<String>();
        
        /*for (int i = 0; i < mSeekBarX.getProgress(); i++) {
            xVals.add((i) + "");
        }*/
        //truc X thi hien thi so thang (12 thang)
        
        /** Tao array gom 12 thang cua nam hien tai */
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        /** get năm xử lý của system */
        SystemConfigItemHelper config = new SystemConfigItemHelper(this);
        currentYear = config.getYearProcessing();
        
        ArrayList<ChartItem> listCurrentYearMonth = DateTimeUtil.get12MonthFromYear(currentYear);
        listCurrentYearMonth = FillData(listCurrentYearMonth, arrGroup);
        
        ArrayList<ChartItem> listPrevYearMonth = DateTimeUtil.get12MonthFromYear(currentYear-1);
        listPrevYearMonth = FillData(listPrevYearMonth, arrGroup);
        
        ArrayList<ChartItem> listPrevPrevYearMonth = DateTimeUtil.get12MonthFromYear(currentYear-2);
        listPrevPrevYearMonth = FillData(listPrevPrevYearMonth, arrGroup);
        
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

        for (int z = 0; z < 3; z++) {
        	ArrayList<ChartItem> listData= new ArrayList<ChartItem>();
        	String legend="";
        	switch(z){
	        	case 0:
	        		listData = listCurrentYearMonth;
	        		legend = String.valueOf(currentYear);
	        		break;
	        	case 1:
	        		listData = listPrevYearMonth;
	        		legend = String.valueOf(currentYear-1);
	        		break;
	        	case 2:
	        		listData = listPrevPrevYearMonth;
	        		legend = String.valueOf(currentYear-2);
	        		break;
        	}
            ArrayList<Entry> values = new ArrayList<Entry>();

            for (int i = 0; i < 12; i++) {
            	
                values.add(new Entry(Float.parseFloat(listData.get(i).ValueItem), i));
            }

            LineDataSet d = new LineDataSet(values, legend);
            d.setValueFormatter(new com.ussol.employeetracker.helpers.ChartValueFormatter());
            d.setLineWidth(2.5f);
            d.setCircleSize(4f);

            int color = mColors[z % mColors.length];
            d.setColor(color);
            d.setCircleColor(color);
            dataSets.add(d);
        }

        // make the first DataSet dashed
        dataSets.get(0).enableDashedLine(10, 10, 0);
        //dataSets.get(0).setColors(ColorTemplate.VORDIPLOM_COLORS);
        //dataSets.get(0).setCircleColors(ColorTemplate.VORDIPLOM_COLORS);

        LineData data = new LineData(mMonths, dataSets);
        mChart.setData(data);
        mChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * Lấy các item trong nhóm
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void getParentChildInGroup(int group){
    	ArrayList<ChartItem> arrGroupTemp=null;
    	
    	arrGroup =grp.getGroupChartItem(group);
        
    }
    
    public ArrayList<ChartItem> FillData(ArrayList<ChartItem> src , ArrayList<ChartItem> db){
    	ArrayList<ChartItem>  tmp = src;
    	
    	for(ChartItem item : tmp){
    		item.ValueItem=Utils.findValueItemFromChartItem(item.KeyItem,db);
    	}
    	return tmp;
    }
    
    public String findValueItemFromChartItem(String keyItem,ArrayList<ChartItem> db){
    	String value="0";
    	for(ChartItem item : db){
    		if(item.KeyItem.equalsIgnoreCase(keyItem)){
    			value = item.ValueItem;
    			break;
    		}
    	}
    	return value;
    }
}
