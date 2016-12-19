package com.ussol.employeetracker.helpers;

public enum eTableUse {
	   FROM_USER_TABLE(1),
	   FROM_VIEW(2);
	   private int value;
	   private eTableUse(int value) {
	      this.value = value;
	   }
	   public int getValue() {
	      return value;
	   }
}
