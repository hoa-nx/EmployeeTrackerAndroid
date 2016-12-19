package com.ussol.employeetracker;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ussol.employeetracker.ChartStaffStatusStackedBarActivity.MyValueFormatter;
import com.ussol.employeetracker.helpers.ChartBase;
import com.ussol.employeetracker.helpers.ConvertCursorToArrayString;
import com.ussol.employeetracker.helpers.DatabaseAdapter;
import com.ussol.employeetracker.helpers.ExpGroupHelper;
import com.ussol.employeetracker.helpers.SystemConfigItemHelper;
import com.ussol.employeetracker.models.ChartItem;
import com.ussol.employeetracker.models.IExpGroup;
import com.ussol.employeetracker.utils.DateTimeUtil;
import com.ussol.employeetracker.utils.Utils;

public class ChartStaffStatusWorkingTrainingThreeYearActivity  extends ChartBase {

	private CombinedChart  mChart;
	private SeekBar mSeekBarX, mSeekBarY;
	private TextView tvX, tvY;
	private DatabaseAdapter mDatabaseAdapter; 
	private SystemConfigItemHelper systemConfig ;
    private int currentYear;
    private boolean displayDescriptionOnChart;
    /** chuyển đổi Cursor thành string array */
	private ConvertCursorToArrayString mConvertCursorToArrayString;
	private ArrayList<ChartItem> arrGroupTraining=null;
	private ArrayList<ChartItem> arrGroupContract=null;
	private ExpGroupHelper  grp =null;
	private int[] mColors = new int[] {
            ColorTemplate.COLORFUL_COLORS[0], 
            ColorTemplate.COLORFUL_COLORS[1],
            ColorTemplate.COLORFUL_COLORS[2]
    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.chart_working_training_threeyear_activity);
		
		mDatabaseAdapter = new DatabaseAdapter(this);
    	mConvertCursorToArrayString = new ConvertCursorToArrayString(this);
    	
        grp = new ExpGroupHelper(getApplicationContext());
        getParentChildInGroup(IExpGroup.EXP_GROUP_STAFF_STATUS_TRAINING_YEAR);
        getParentChildInGroup(IExpGroup.EXP_GROUP_STAFF_STATUS_CONTRACT_YEAR);
        
    	systemConfig = new SystemConfigItemHelper(this);
    	currentYear = systemConfig.getYearProcessing();
    	if(currentYear==0){
    		currentYear = Calendar.getInstance().get(Calendar.YEAR);
    	}
    	mMonths =   new String[] {
        		(currentYear-2)+ "", (currentYear-1)+"", currentYear+""};
        displayDescriptionOnChart= systemConfig.getDisplayDescriptionOnChart();
        
        mChart = (CombinedChart) findViewById(R.id.chart_working_training_threeyear);

		if(displayDescriptionOnChart){
			mChart.setDescription("Biểu đồ thống kê nhân viên theo năm");
		}else{
			mChart.setDescription("");
		}
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        
        // draw bars behind lines
        mChart.setDrawOrder(new DrawOrder[] {
                DrawOrder.BAR, DrawOrder.BUBBLE, DrawOrder.CANDLE, DrawOrder.LINE, DrawOrder.SCATTER
        });

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTH_SIDED);

        CombinedData data = new CombinedData(mMonths);

        data.setData(generateLineData());
        data.setData(generateBarData());

        mChart.setData(data);
        mChart.invalidate();
	}

	private LineData generateLineData() {
		/** Tao array gom 12 thang cua nam hien tai */
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        /** get năm xử lý của system */
        SystemConfigItemHelper config = new SystemConfigItemHelper(this);
        currentYear = config.getYearProcessing();
        
        ArrayList<ChartItem> listCurrentYearMonth = DateTimeUtil.get12MonthFromYear(currentYear);
        listCurrentYearMonth = FillData(listCurrentYearMonth, arrGroupContract);
        ArrayList<ChartItem> currentYearContract = SumaryDataByYear(listCurrentYearMonth, currentYear);
        
        
        ArrayList<ChartItem> listPrevYearMonth = DateTimeUtil.get12MonthFromYear(currentYear-1);
        listPrevYearMonth = FillData(listPrevYearMonth, arrGroupContract);
        ArrayList<ChartItem> currentPrevYearContract = SumaryDataByYear(listPrevYearMonth, currentYear-1);
        
        
        ArrayList<ChartItem> listPrevPrevYearMonth = DateTimeUtil.get12MonthFromYear(currentYear-2);
        listPrevPrevYearMonth = FillData(listPrevPrevYearMonth, arrGroupContract);
        ArrayList<ChartItem> currentPrevPrevYearContract = SumaryDataByYear(listPrevPrevYearMonth, currentYear-2);
        
        
        ArrayList<Entry> values = new ArrayList<Entry>();
        
        for (int z = 0; z < 3; z++) {
        	ArrayList<ChartItem> listData= new ArrayList<ChartItem>();
        	String legend="";
        	switch(z){
	        	case 0:
	        		listData = currentPrevPrevYearContract;
	        		legend = String.valueOf(currentYear-2);
	        		
	        		break;
	        	case 1:
	        		listData = currentPrevYearContract;
	        		legend = String.valueOf(currentYear-1);
	        		break;
	        	case 2:
	        		listData = currentYearContract;
	        		legend = String.valueOf(currentYear);
	        		break;
        	}
           	
        	values.add(new Entry(Float.parseFloat(listData.get(0).ValueItem), z));           
        }
        
        LineDataSet d = new LineDataSet(values, "LTV HĐ mới");
        d.setValueFormatter(new com.ussol.employeetracker.helpers.ChartValueFormatter());
        d.setLineWidth(1.5f);
        d.setCircleSize(2f);

        d.setColor(Color.rgb(4, 150, 167));
        d.setLineWidth(1.5f);
        d.setCircleColor(Color.rgb(4, 150, 167));
        d.setCircleSize(2f);
        d.setFillColor(Color.rgb(4, 150, 167));
        d.setDrawCubic(true);
        d.setDrawValues(true);
        d.setValueTextSize(12f);
        d.setValueTextColor(Color.rgb(4, 150, 167));
        d.setAxisDependency(YAxis.AxisDependency.LEFT);
                
        LineData data = new LineData();
        data.addDataSet(d);
        //Add them thong ke nghi viec cua nhan vien
        /** Tao array gom 12 thang cua nam hien tai */
        
        ArrayList<Entry> valuesDevYasumi = new ArrayList<Entry>();
        ArrayList<Entry> valuesDevWorking = new ArrayList<Entry>();
        ArrayList<ChartItem> devWorkingList = new ArrayList<ChartItem>();
        ArrayList<ChartItem> devYasumiList =new ArrayList<ChartItem>();

        devYasumiList =mConvertCursorToArrayString.getDevYasumiFromViewChartItem();	
        
        for (int z = 0; z < 3; z++) {
        	//loop qua 3 nam 
        	
        	//loop qua 12 tháng
    		String yearMonth="";
    		float devWork = 0;
        	float devYasumi = 0;
    		DateTime firstMonthCurrentYear = new DateTime(currentYear+z-2, 1, 1, 0, 0);
            DateTime firstMonthCurrentYearFix = new DateTime(currentYear+z-2, 1, 1, 0, 0);
            devWorkingList =mConvertCursorToArrayString.getDevWorkingFromViewChartItem(currentYear+z-2, "");
            
            for(int month=0;month<12;month++){
            	firstMonthCurrentYear =firstMonthCurrentYearFix.plusMonths(month);
            	DateTimeFormatter fmt = ISODateTimeFormat.yearMonth();
            	yearMonth =fmt.print(firstMonthCurrentYear);
            	            	
            	devYasumi += Float.parseFloat(Utils.findValueItemFromChartItem(yearMonth, devYasumiList));
            	
            	if(month==11){
            		devWork += Float.parseFloat(Utils.findValueItemFromChartItem(yearMonth, devWorkingList));
            	}
            }
            //data cua 1 nam cua nhan vien nghi viec
            valuesDevYasumi.add(new Entry(devYasumi, z)); 
          //data cua 1 nam cua nhan vien dang lam viec
            valuesDevWorking.add(new Entry(devWork, z));
            
        }
        
        LineDataSet dYasumi = new LineDataSet(valuesDevYasumi, "LTV nghỉ việc");
        
        dYasumi.setValueFormatter(new com.ussol.employeetracker.helpers.ChartValueFormatter());
        dYasumi.setLineWidth(1.5f);
        dYasumi.setCircleSize(2f);

        dYasumi.setColor(Color.rgb(255, 51, 51));
        dYasumi.setLineWidth(1.5f);
        dYasumi.setCircleColor(Color.rgb(255, 51, 51));
        dYasumi.setCircleSize(2f);
        dYasumi.setFillColor(Color.rgb(255, 51, 51));
        dYasumi.setDrawCubic(true);
        dYasumi.setDrawValues(true);
        dYasumi.setValueTextSize(10f);
        dYasumi.setValueTextColor(Color.rgb(225, 225, 225));
        dYasumi.setAxisDependency(YAxis.AxisDependency.LEFT);
                
        data.addDataSet(dYasumi);
        //nha vien dang lam viec trong nam
        LineDataSet dWorking = new LineDataSet(valuesDevWorking, "LTV chính thức");
        
        dWorking.setValueFormatter(new com.ussol.employeetracker.helpers.ChartValueFormatter());
        dWorking.setLineWidth(1.5f);
        dWorking.setCircleSize(2f);

        dWorking.setColor(Color.rgb(0, 46, 184));
        dWorking.setLineWidth(1.5f);
        dWorking.setCircleColor(Color.rgb(0, 46, 184));
        dWorking.setCircleSize(2f);
        dWorking.setFillColor(Color.rgb(0, 46, 184));
        dWorking.setDrawCubic(true);
        dWorking.setDrawValues(true);
        dWorking.setValueTextSize(11f);
        dWorking.setValueTextColor(Color.rgb(0, 46, 184));
        dWorking.setAxisDependency(YAxis.AxisDependency.LEFT);
                
        data.addDataSet(dWorking);
        
        //mChart.setData(data);
        //mChart.invalidate();
        return data;
    }

    private BarData generateBarData() {

        BarData d = new BarData();

        /** Tao array gom 12 thang cua nam hien tai */
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        /** get năm xử lý của system */
        SystemConfigItemHelper config = new SystemConfigItemHelper(this);
        currentYear = config.getYearProcessing();
        
        ArrayList<ChartItem> listCurrentYearMonth = DateTimeUtil.get12MonthFromYear(currentYear);
        listCurrentYearMonth = FillData(listCurrentYearMonth, arrGroupTraining);
        ArrayList<ChartItem> currentYearContract = SumaryDataByYear(listCurrentYearMonth, currentYear);
        
        
        ArrayList<ChartItem> listPrevYearMonth = DateTimeUtil.get12MonthFromYear(currentYear-1);
        listPrevYearMonth = FillData(listPrevYearMonth, arrGroupTraining);
        ArrayList<ChartItem> currentPrevYearContract = SumaryDataByYear(listPrevYearMonth, currentYear-1);
        
        
        ArrayList<ChartItem> listPrevPrevYearMonth = DateTimeUtil.get12MonthFromYear(currentYear-2);
        listPrevPrevYearMonth = FillData(listPrevPrevYearMonth, arrGroupTraining);
        ArrayList<ChartItem> currentPrevPrevYearContract = SumaryDataByYear(listPrevPrevYearMonth, currentYear-2);
        
        
        ArrayList<BarEntry> values = new ArrayList<BarEntry>();
        
        for (int z = 0; z < 3; z++) {
        	ArrayList<ChartItem> listData= new ArrayList<ChartItem>();
        	String legend="";
        	switch(z){
	        	case 0:
	        		listData = currentPrevPrevYearContract;
	        		legend = String.valueOf(currentYear-2);
	        		
	        		break;
	        	case 1:
	        		listData = currentPrevYearContract;
	        		legend = String.valueOf(currentYear-1);
	        		break;
	        	case 2:
	        		listData = currentYearContract;
	        		legend = String.valueOf(currentYear);
	        		break;
        	}
           	
        	values.add(new BarEntry(Float.parseFloat(listData.get(0).ValueItem), z));           
        }
        BarDataSet set = new BarDataSet(values, "LTV thử việc");
        set.setColor(Color.rgb(153, 0, 153));
        set.setValueTextColor(Color.rgb(153, 0, 153));
        set.setValueTextSize(11f);

        d.addDataSet(set);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        return d;
        
    }
    
    
    /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
     * Lấy các item trong nhóm
     * 
     ▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲*/
    public void getParentChildInGroup(int group){
    	ArrayList<ChartItem> arrGroupTemp=null;
    	switch (group){
    		case IExpGroup.EXP_GROUP_STAFF_STATUS_TRAINING_YEAR:
    			arrGroupTraining =grp.getGroupChartItem(group);
    			break;
    		
    		case IExpGroup.EXP_GROUP_STAFF_STATUS_CONTRACT_YEAR:
    			arrGroupContract =grp.getGroupChartItem(group);
    			break;
    	}
    	
        
    }
    
    public ArrayList<ChartItem> FillData(ArrayList<ChartItem> src , ArrayList<ChartItem> db){
    	ArrayList<ChartItem>  tmp = src;
    	
    	for(ChartItem item : tmp){
    		item.ValueItem=Utils.findValueItemFromChartItem(item.KeyItem,db);
    	}
    	return tmp;
    }
    
    public ArrayList<ChartItem> SumaryDataByYear(ArrayList<ChartItem> src , int year){
    	ArrayList<ChartItem>  tmp = new ArrayList<ChartItem>() ;
    	int totalStaffbyYear=0;
    	
    	ChartItem item = new ChartItem(year+"", totalStaffbyYear+"");
    	
    	for(ChartItem itemSub : src){
    		totalStaffbyYear = totalStaffbyYear + Integer.parseInt(itemSub.ValueItem);
    	}
    	item.ValueItem = totalStaffbyYear +"";
    	tmp.add(item);
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
