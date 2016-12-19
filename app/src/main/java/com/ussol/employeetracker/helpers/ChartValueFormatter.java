package com.ussol.employeetracker.helpers;

import java.text.DecimalFormat;

import com.github.mikephil.charting.data.Entry;

public class ChartValueFormatter implements com.github.mikephil.charting.formatter.ValueFormatter {

    private DecimalFormat mFormat;
    
    public ChartValueFormatter() {
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
