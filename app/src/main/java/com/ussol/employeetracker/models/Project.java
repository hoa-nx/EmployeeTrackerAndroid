package com.ussol.employeetracker.models;

import android.os.Parcel;

import com.ussol.employeetracker.utils.Utils;

/**
 * Created by HOA-NX on 2017/01/14.
 */

public class Project extends Master {

    public Long timeInMillis;
    public String favorite = null;
    public int sime=20;
    public String start_date ;
    public String end_date ;
    public String contract_date;
    public float total_mm=0;
    public float estimate_mm=0;
    public float dev_mm=0;
    public float man_mm=0;
    public float trans_mm=0;
    public String manager;
    public String pl;
    public String pm;
    public float staff_qty;

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    public Project() {}

    public Project(Parcel in) {
        super(in);

        mkbn= MasterConstants.MASTER_MKBN_PROJECT;

        sime= in.readInt();
        start_date = in.readString();
        end_date  = in.readString();
        contract_date = in.readString();
        total_mm = in.readFloat();
        estimate_mm=in.readFloat();
        dev_mm=in.readFloat();
        man_mm=in.readFloat();
        trans_mm=in.readFloat();
        manager  = in.readString();
        pl  = in.readString();
        pm  = in.readString();
        staff_qty  = in.readFloat();

    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(MasterConstants.MASTER_MKBN_COMPANY);
        dest.writeInt(code);
        dest.writeString(name);
        dest.writeString(ryaku);
        dest.writeString(create_date);

        dest.writeInt(sime);
        dest.writeString(start_date);
        dest.writeString(end_date );
        dest.writeString(contract_date);
        dest.writeFloat(total_mm );
        dest.writeFloat(estimate_mm);
        dest.writeFloat(dev_mm);
        dest.writeFloat(man_mm);
        dest.writeFloat(trans_mm);
        dest.writeString(manager);
        dest.writeString(pl);
        dest.writeString(pm);
        dest.writeFloat(staff_qty);

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
    public Project clone()
    {
        return (Project) Utils.clone(this);
    }
}
