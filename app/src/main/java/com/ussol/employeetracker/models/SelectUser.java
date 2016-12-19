package com.ussol.employeetracker.models;
import android.graphics.Bitmap;

public class SelectUser {
    String name;

    public Bitmap getThumb() {
        return thumb;
    }

    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }

    Bitmap thumb;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    String phone;
    
	boolean checkedBox = false;
    public boolean getCheckedBox() {
        return checkedBox;
    }

    public void setCheckedBox(boolean checkedBox) {
        this.checkedBox = checkedBox;
    }

    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    String training_date;

    public String getTraining_date() {
        return training_date;
    }

    public void setTraining_date(String training_date) {
        this.training_date = training_date;
    }
    
    String in_date;

    public String getIn_date() {
        return in_date;
    }

    public void setIn_date(String in_date) {
        this.in_date = in_date;
    }
    
    String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    String birthday;

    public String getBirthDay() {
        return birthday;
    }

    public void setBirthDay(String birthday) {
        this.birthday = birthday;
    }
    
    String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
    Boolean linked;
    public Boolean getLinked() {
        return linked;
    }

    public void setLinked(Boolean link) {
        this.linked = link;
    }
    
    String google_id;

    public String getGoogleId() {
        return google_id;
    }

    public void setGoogleId(String google_id) {
        this.google_id = google_id;
    }

}
