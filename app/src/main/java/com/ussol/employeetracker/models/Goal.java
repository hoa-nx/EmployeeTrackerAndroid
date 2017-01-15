package com.ussol.employeetracker.models;

import android.os.Parcel;

import com.ussol.employeetracker.utils.Utils;

/**
 * Created by HOA-NX on 2017/01/14.
 */

public class Goal extends MasterItem {

    public Long timeInMillis;
    public String favorite = null;

    public String goal_year ;
    public float goal_revenue;
    public float goal_staff_qty_year;
    public int goal_japanese_n1_year;
    public int goal_japanese_n2_year ;
    public int goal_japanese_n3_year;
    public int goal_japanese_n4_year;
    public int goal_japanese_n5_year;
    public int goal_staff_m2_year;
    public int goal_staff_m1_year;
    public int goal_staff_m0_year;
    public int goal_staff_l2_year;
    public int goal_staff_l1_year;
    public int goal_staff_tl2_year;
    public int goal_staff_tl1_year;
    public int goal_staff_s2_year;
    public int goal_staff_s1_year;
    public int goal_staff_ts1_year;
    public int goal_staff_ts2_year;

    public static final Creator<Goal> CREATOR = new Creator<Goal>() {
        public Goal createFromParcel(Parcel in) {
            return new Goal(in);
        }

        public Goal[] newArray(int size) {
            return new Goal[size];
        }
    };

    public Goal() {}

    public Goal(Parcel in) {
        super(in);

        goal_year = in.readString();
        goal_revenue  = in.readFloat();
        goal_staff_qty_year = in.readFloat();
        goal_japanese_n1_year = in.readInt();
        goal_japanese_n2_year  = in.readInt();
        goal_japanese_n3_year = in.readInt();
        goal_japanese_n4_year = in.readInt();
        goal_japanese_n5_year = in.readInt();
        goal_staff_m2_year = in.readInt();
        goal_staff_m1_year = in.readInt();
        goal_staff_m0_year = in.readInt();
        goal_staff_l2_year = in.readInt();
        goal_staff_l1_year = in.readInt();
        goal_staff_tl2_year = in.readInt();
        goal_staff_tl1_year = in.readInt();
        goal_staff_s2_year = in.readInt();
        goal_staff_s1_year = in.readInt();
        goal_staff_ts1_year = in.readInt();
        goal_staff_ts2_year = in.readInt();
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(code);
        dest.writeString(name);
        dest.writeString(ryaku);

        dest.writeString(goal_year );
        dest.writeFloat(goal_revenue );
        dest.writeFloat(goal_staff_qty_year);
        dest.writeInt(goal_japanese_n1_year );
        dest.writeInt(goal_japanese_n2_year  );
        dest.writeInt(goal_japanese_n3_year );
        dest.writeInt(goal_japanese_n4_year );
        dest.writeInt(goal_japanese_n5_year );
        dest.writeInt(goal_staff_m2_year );
        dest.writeInt(goal_staff_m1_year );
        dest.writeInt(goal_staff_m0_year );
        dest.writeInt(goal_staff_l2_year );
        dest.writeInt(goal_staff_l1_year );
        dest.writeInt(goal_staff_tl2_year );
        dest.writeInt(goal_staff_tl1_year );
        dest.writeInt(goal_staff_s2_year );
        dest.writeInt(goal_staff_s1_year );
        dest.writeInt(goal_staff_ts1_year );
        dest.writeInt(goal_staff_ts2_year );

        dest.writeInt(isdeleted);
        dest.writeString(note);
        dest.writeInt(yobi_code1);
        dest.writeInt(yobi_code2);
        dest.writeInt(yobi_code3);
        dest.writeInt(yobi_code4);
        dest.writeInt(yobi_code5);
        dest.writeString(yobi_text1);
        dest.writeString(yobi_text2);
        dest.writeString(yobi_text3);
        dest.writeString(yobi_text4);
        dest.writeString(yobi_text5);
        dest.writeString(yobi_date1);
        dest.writeString(yobi_date2);
        dest.writeString(yobi_date3);
        dest.writeString(yobi_date4);
        dest.writeString(yobi_date5);
        dest.writeFloat(yobi_real1);
        dest.writeFloat(yobi_real2);
        dest.writeFloat(yobi_real3);
        dest.writeFloat(yobi_real4);
        dest.writeFloat(yobi_real5);
        dest.writeString(up_date);
        dest.writeString(ad_date);
        dest.writeString(opid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public Goal clone()
    {
        return (Goal) Utils.clone(this);
    }
}
