package com.ussol.employeetracker.helpers;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ussol.employeetracker.models.ChartItem;
import com.ussol.employeetracker.utils.Utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ChartFragmentData extends Fragment {
    
    private Typeface tf;
	private DatabaseAdapter mDatabaseAdapter; 
    /** chuyển đổi Cursor thành string array */
	private ConvertCursorToArrayString mConvertCursorToArrayString;
	protected String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };
	
    public ChartFragmentData() {
    	mDatabaseAdapter = new DatabaseAdapter(getContext());
    	mConvertCursorToArrayString = new ConvertCursorToArrayString(getContext());
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected BarData getBarDataDevWorkingDevTrainingDevYasumi(Context ct) {
    	mConvertCursorToArrayString = new ConvertCursorToArrayString(ct);
    	/** Tao array gom 12 thang cua nam hien tai */
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        /** get năm xử lý của system */
        SystemConfigItemHelper config = new SystemConfigItemHelper(ct);
        currentYear = config.getYearProcessing();

        ArrayList<ChartItem> devWorkingList = new ArrayList<ChartItem>();
        //ArrayList<ChartItem> pdWorkingList =new ArrayList<ChartItem>();
        ArrayList<ChartItem> devTrainingList =new ArrayList<ChartItem>();
        //ArrayList<ChartItem> pdTrainingList =new ArrayList<ChartItem>();
        //ArrayList<ChartItem> pdYasumiList =new ArrayList<ChartItem>();
        ArrayList<ChartItem> devYasumiList =new ArrayList<ChartItem>();

        devWorkingList =mConvertCursorToArrayString.getDevWorkingFromViewChartItem(currentYear, "");
        //pdWorkingList =mConvertCursorToArrayString.getPDWorkingFromViewChartItem(currentYear, "");
        devTrainingList =mConvertCursorToArrayString.getDevTrainingFromViewChartItem(currentYear);
        //pdTrainingList =mConvertCursorToArrayString.getPDTrainingFromViewChartItem(currentYear);
        //pdYasumiList =mConvertCursorToArrayString.getPDYasumiFromViewChartItem();	
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
        	//float pdWork = Float.parseFloat(Utils.findValueItemFromChartItem(yearMonth, pdWorkingList));
        	float devTraining = Float.parseFloat(Utils.findValueItemFromChartItem(yearMonth, devTrainingList));
        	//float pdTraining = Float.parseFloat(Utils.findValueItemFromChartItem(yearMonth, pdTrainingList));
        	float devYasumi = Float.parseFloat(Utils.findValueItemFromChartItem(yearMonth, devYasumiList));
        	//float pdYasumi = Float.parseFloat(Utils.findValueItemFromChartItem(yearMonth, pdYasumiList));

        	yVals1.add(new BarEntry(new float[] { devWork, devTraining , devYasumi }, month));
        }
      
		BarDataSet set1 = new BarDataSet(yVals1, "  --Năm " + String.valueOf(currentYear));
		set1.setColors(getColors(3));
		set1.setStackLabels(new String[] { "LTV", "Thử việc(LTV)" ,"Nghỉ việc(LTV)"});

		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		dataSets.add(set1);

		BarData data = new BarData(mMonths, dataSets);
		data.setValueFormatter(new MyValueFormatter());
		data.setValueTypeface(tf);

        return data;
    }
   
protected BarData getBarDataPDWorkingPDTrainingPDYasumi(Context ct) {
		mConvertCursorToArrayString = new ConvertCursorToArrayString(ct);
        
    	/** Tao array gom 12 thang cua nam hien tai */
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        /** get năm xử lý của system */
        SystemConfigItemHelper config = new SystemConfigItemHelper(ct);
        currentYear = config.getYearProcessing();
        //ArrayList<ChartItem> devWorkingList = new ArrayList<ChartItem>();
        ArrayList<ChartItem> pdWorkingList =new ArrayList<ChartItem>();
        //ArrayList<ChartItem> devTrainingList =new ArrayList<ChartItem>();
        ArrayList<ChartItem> pdTrainingList =new ArrayList<ChartItem>();
        ArrayList<ChartItem> pdYasumiList =new ArrayList<ChartItem>();
        //ArrayList<ChartItem> devYasumiList =new ArrayList<ChartItem>();

        //devWorkingList =mConvertCursorToArrayString.getDevWorkingFromViewChartItem(currentYear, "");
        pdWorkingList =mConvertCursorToArrayString.getPDWorkingFromViewChartItem(currentYear, "");
        //devTrainingList =mConvertCursorToArrayString.getDevTrainingFromViewChartItem(currentYear);
        pdTrainingList =mConvertCursorToArrayString.getPDTrainingFromViewChartItem(currentYear);
        pdYasumiList =mConvertCursorToArrayString.getPDYasumiFromViewChartItem();	
        //devYasumiList =mConvertCursorToArrayString.getDevYasumiFromViewChartItem();	
  
		ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
		//loop qua 12 tháng
		String yearMonth="";
		DateTime firstMonthCurrentYear = new DateTime(currentYear, 1, 1, 0, 0);
        DateTime firstMonthCurrentYearFix = new DateTime(currentYear, 1, 1, 0, 0);
        for(int month=0;month<12;month++){
        	firstMonthCurrentYear =firstMonthCurrentYearFix.plusMonths(month);
        	DateTimeFormatter fmt = ISODateTimeFormat.yearMonth();
        	yearMonth =fmt.print(firstMonthCurrentYear);
        	
        	//float devWork = Float.parseFloat(Utils.findValueItemFromChartItem(yearMonth, devWorkingList));
        	float pdWork = Float.parseFloat(Utils.findValueItemFromChartItem(yearMonth, pdWorkingList));
        	//float devTraining = Float.parseFloat(Utils.findValueItemFromChartItem(yearMonth, devTrainingList));
        	float pdTraining = Float.parseFloat(Utils.findValueItemFromChartItem(yearMonth, pdTrainingList));
        	//float devYasumi = Float.parseFloat(Utils.findValueItemFromChartItem(yearMonth, devYasumiList));
        	float pdYasumi = Float.parseFloat(Utils.findValueItemFromChartItem(yearMonth, pdYasumiList));

        	yVals1.add(new BarEntry(new float[] { pdWork, pdTraining , pdYasumi }, month));
        }
      
		BarDataSet set1 = new BarDataSet(yVals1, "  --Năm " + String.valueOf(currentYear));
		set1.setColors(getColors(3));
		set1.setStackLabels(new String[] { "PD", "Thử việc(PD)" ,"Nghỉ việc(PD)"});

		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		dataSets.add(set1);

		BarData data = new BarData(mMonths, dataSets);
		data.setValueFormatter(new MyValueFormatter());
		data.setValueTypeface(tf);

        return data;
    }

protected BarData getBarDataWorkingTrainingYasumi(Context ct) {
	mConvertCursorToArrayString = new ConvertCursorToArrayString(ct);
	/** Tao array gom 12 thang cua nam hien tai */
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    /** get năm xử lý của system */
    SystemConfigItemHelper config = new SystemConfigItemHelper(ct);
    currentYear = config.getYearProcessing();

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

    	yVals1.add(new BarEntry(new float[] { devWork,pdWork, devTraining +pdTraining, devYasumi + pdYasumi}, month));
    }
  
	BarDataSet set1 = new BarDataSet(yVals1, "  --Năm " + String.valueOf(currentYear));
	set1.setColors(getColors(4));
	set1.setStackLabels(new String[] { "LTV", "PD","Thử việc(LT+PD)","Nghỉ việc(LT+PD)"});

	ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
	dataSets.add(set1);

	BarData data = new BarData(mMonths, dataSets);
	data.setValueFormatter(new MyValueFormatter());
	data.setValueTypeface(tf);

    return data;
}

protected BarData getBarDataWorkingTraining(Context ct) {
	mConvertCursorToArrayString = new ConvertCursorToArrayString(ct);
    
	/** Tao array gom 12 thang cua nam hien tai */
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    /** get năm xử lý của system */
    SystemConfigItemHelper config = new SystemConfigItemHelper(ct);
    currentYear = config.getYearProcessing();

    ArrayList<ChartItem> devWorkingList = new ArrayList<ChartItem>();
    ArrayList<ChartItem> pdWorkingList =new ArrayList<ChartItem>();
    ArrayList<ChartItem> devTrainingList =new ArrayList<ChartItem>();
    ArrayList<ChartItem> pdTrainingList =new ArrayList<ChartItem>();
    //ArrayList<ChartItem> pdYasumiList =new ArrayList<ChartItem>();
    //ArrayList<ChartItem> devYasumiList =new ArrayList<ChartItem>();

    devWorkingList =mConvertCursorToArrayString.getDevWorkingFromViewChartItem(currentYear, "");
    pdWorkingList =mConvertCursorToArrayString.getPDWorkingFromViewChartItem(currentYear, "");
    devTrainingList =mConvertCursorToArrayString.getDevTrainingFromViewChartItem(currentYear);
    pdTrainingList =mConvertCursorToArrayString.getPDTrainingFromViewChartItem(currentYear);
    //pdYasumiList =mConvertCursorToArrayString.getPDYasumiFromViewChartItem();	
    //devYasumiList =mConvertCursorToArrayString.getDevYasumiFromViewChartItem();	

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
    	//float devYasumi = Float.parseFloat(Utils.findValueItemFromChartItem(yearMonth, devYasumiList));
    	//float pdYasumi = Float.parseFloat(Utils.findValueItemFromChartItem(yearMonth, pdYasumiList));

    	yVals1.add(new BarEntry(new float[] { devWork,pdWork, devTraining +pdTraining}, month));
    }
  
	BarDataSet set1 = new BarDataSet(yVals1, "  --Năm " + String.valueOf(currentYear));
	set1.setColors(getColors(3));
	set1.setStackLabels(new String[] { "LTV", "PD","Thử việc(LT+PD)"});

	ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
	dataSets.add(set1);

	BarData data = new BarData(mMonths, dataSets);
	data.setValueFormatter(new MyValueFormatter());
	data.setValueTypeface(tf);

    return data;
}

protected BarData getBarDataWorking(Context ct) {
	mConvertCursorToArrayString = new ConvertCursorToArrayString(ct);
    
	/** Tao array gom 12 thang cua nam hien tai */
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    /** get năm xử lý của system */
    SystemConfigItemHelper config = new SystemConfigItemHelper(ct);
    currentYear = config.getYearProcessing();

    ArrayList<ChartItem> devWorkingList = new ArrayList<ChartItem>();
    ArrayList<ChartItem> pdWorkingList =new ArrayList<ChartItem>();
    ArrayList<ChartItem> devTrainingList =new ArrayList<ChartItem>();
    ArrayList<ChartItem> pdTrainingList =new ArrayList<ChartItem>();
    //ArrayList<ChartItem> pdYasumiList =new ArrayList<ChartItem>();
    //ArrayList<ChartItem> devYasumiList =new ArrayList<ChartItem>();

    devWorkingList =mConvertCursorToArrayString.getDevWorkingFromViewChartItem(currentYear, "");
    pdWorkingList =mConvertCursorToArrayString.getPDWorkingFromViewChartItem(currentYear, "");
    devTrainingList =mConvertCursorToArrayString.getDevTrainingFromViewChartItem(currentYear);
    pdTrainingList =mConvertCursorToArrayString.getPDTrainingFromViewChartItem(currentYear);
    //pdYasumiList =mConvertCursorToArrayString.getPDYasumiFromViewChartItem();	
    //devYasumiList =mConvertCursorToArrayString.getDevYasumiFromViewChartItem();	

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
    	//float devYasumi = Float.parseFloat(Utils.findValueItemFromChartItem(yearMonth, devYasumiList));
    	//float pdYasumi = Float.parseFloat(Utils.findValueItemFromChartItem(yearMonth, pdYasumiList));

    	yVals1.add(new BarEntry(new float[] { devWork,pdWork}, month));
    }
  
	BarDataSet set1 = new BarDataSet(yVals1, "  --Năm " + String.valueOf(currentYear));
	set1.setColors(getColors(2));
	set1.setStackLabels(new String[] { "LTV", "PD"});

	ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
	dataSets.add(set1);

	BarData data = new BarData(mMonths, dataSets);
	data.setValueFormatter(new MyValueFormatter());
	data.setValueTypeface(tf);

    return data;
}

    private String[] mLabels = new String[] { "Company A", "Company B", "Company C", "Company D", "Company E", "Company F" };
//    private String[] mXVals = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec" };
    
    private String getLabel(int i) {
        return mLabels[i];
    }
    
    private int[] getColors(int size) {

		int stacksize = size;

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
