package com.ussol.employeetracker.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.ussol.employeetracker.utils.Utils;

/**
 * Created by HOA-NX on 2017/01/14.
 */

public class Company extends Master {

    public Long timeInMillis;
    public String favorite = null;
    public long  shihon=0;
    public String tax_code ;
    public String address ;
    public String hometel ;
    public String mobile ;
    public String fax ;
    public String voucher_address ;
    public String website ;

    public static final Parcelable.Creator<Company> CREATOR = new Parcelable.Creator<Company>() {
        public Company createFromParcel(Parcel in) {
            return new Company(in);
        }

        public Company[] newArray(int size) {
            return new Company[size];
        }
    };

    public Company() {}

    public Company(Parcel in) {
        super(in);

        mkbn= MasterConstants.MASTER_MKBN_COMPANY;

        shihon= in.readLong();
        tax_code = in.readString();
        address  = in.readString();
        hometel  = in.readString();
        mobile  = in.readString();
        fax  = in.readString();
        voucher_address  = in.readString();
        website  = in.readString();

    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(MasterConstants.MASTER_MKBN_COMPANY);
        dest.writeInt(code);
        dest.writeString(name);
        dest.writeString(ryaku);
        dest.writeString(create_date);

        dest.writeLong(shihon);
        dest.writeString(tax_code);
        dest.writeString(address);
        dest.writeString(hometel);
        dest.writeString(mobile);
        dest.writeString(fax);
        dest.writeString(voucher_address);
        dest.writeString(website);

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
    public Company clone()
    {
        return (Company) Utils.clone(this);
    }
}
