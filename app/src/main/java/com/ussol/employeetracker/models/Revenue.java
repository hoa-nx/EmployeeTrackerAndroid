package com.ussol.employeetracker.models;

import android.os.Parcel;

import com.ussol.employeetracker.utils.Utils;

/**
 * Created by HOA-NX on 2017/01/14.
 */

public class Revenue extends MasterItem {

    public Long timeInMillis;
    public String favorite = null;

    public String customer_id ;
    public String customer_name ;
    public String pj_id;
    public String pj_name;
    public String manager ;
    public String vice_manager;
    public String pm_id;
    public String pl_id;
    public String member_group;
    public String basic_design;
    public String detail_design;
    public String pg;
    public String ut;
    public String ct;
    public String st;
    public String mainternance;
    public String start_date;
    public String end_date;
    public int sime=20;
    public String unit_price_code;
    public float unit_price;
    public String unit;
    public float rate_yen_usd;
    public float rate_yen_vnd;
    public float rate_usd_vnd;
    public float discount;
    public float monthly_mm;
    public float estimate_mm;
    public float dev_mm;
    public float mana_mm;
    public float trans_mm;
    public float other_mm1;
    public float other_mm2;
    public float monthly_revenue;
    public float monthly_cost;
    public float monthly_revenue_minus;

    public static final Creator<Revenue> CREATOR = new Creator<Revenue>() {
        public Revenue createFromParcel(Parcel in) {
            return new Revenue(in);
        }

        public Revenue[] newArray(int size) {
            return new Revenue[size];
        }
    };

    public Revenue() {}

    public Revenue(Parcel in) {
        super(in);

        customer_id = in.readString();
        customer_name  = in.readString();
        pj_id = in.readString();
        pj_name = in.readString();
        manager  = in.readString();
        vice_manager = in.readString();
        pm_id = in.readString();
        pl_id = in.readString();
        member_group = in.readString();
        basic_design = in.readString();
        detail_design = in.readString();
        pg = in.readString();
        ut = in.readString();
        ct = in.readString();
        st = in.readString();
        mainternance = in.readString();
        start_date = in.readString();
        end_date = in.readString();
        sime = in.readInt();
        unit_price_code = in.readString();
        unit_price = in.readFloat();
        unit = in.readString();
        rate_yen_usd = in.readFloat();
        rate_yen_vnd = in.readFloat();
        rate_usd_vnd = in.readFloat();
        discount = in.readFloat();
        monthly_mm = in.readFloat();
        estimate_mm = in.readFloat();
        dev_mm = in.readFloat();
        mana_mm = in.readFloat();
        trans_mm = in.readFloat();
        other_mm1 = in.readFloat();
        other_mm2 = in.readFloat();
        monthly_revenue = in.readFloat();
        monthly_cost = in.readFloat();
        monthly_revenue_minus = in.readFloat();

    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(code);
        dest.writeString(name);
        dest.writeString(ryaku);

        dest.writeString(customer_id );
        dest.writeString(customer_name);
        dest.writeString(pj_id );
        dest.writeString(pj_name);
        dest.writeString(manager);
        dest.writeString(vice_manager);
        dest.writeString(pm_id );
        dest.writeString(pl_id );
        dest.writeString(member_group );
        dest.writeString(basic_design );
        dest.writeString(detail_design );
        dest.writeString(pg );
        dest.writeString(ut );
        dest.writeString(ct );
        dest.writeString(st );
        dest.writeString(mainternance );
        dest.writeString(start_date );
        dest.writeString(end_date );
        dest.writeInt(sime);
        dest.writeString(unit_price_code);
        dest.writeFloat(unit_price);
        dest.writeString(unit);
        dest.writeFloat(rate_yen_usd );
        dest.writeFloat(rate_yen_vnd );
        dest.writeFloat(rate_usd_vnd );
        dest.writeFloat(discount );
        dest.writeFloat(monthly_mm );
        dest.writeFloat(estimate_mm );
        dest.writeFloat(dev_mm );
        dest.writeFloat(mana_mm );
        dest.writeFloat(trans_mm );
        dest.writeFloat(other_mm1 );
        dest.writeFloat(other_mm2 );
        dest.writeFloat(monthly_revenue );
        dest.writeFloat(monthly_cost );
        dest.writeFloat(monthly_revenue_minus);

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
    public Revenue clone()
    {
        return (Revenue) Utils.clone(this);
    }
}
