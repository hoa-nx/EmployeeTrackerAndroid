package com.ussol.employeetracker.models;

public class ChartItem {
	public String KeyItem;
	public String ValueItem;
	public String LabelItem;
	
	public ChartItem(String key, String value) {
		KeyItem = key;
        ValueItem = value;
    }
	
	public ChartItem(String key, String value, String label) {
		KeyItem = key;
        ValueItem = value;
        LabelItem = label;
    }
	
}
