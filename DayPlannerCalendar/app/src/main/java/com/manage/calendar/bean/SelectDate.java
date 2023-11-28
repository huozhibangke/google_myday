package com.manage.calendar.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class SelectDate implements Parcelable {

    private int year;
    private int month;
    private int day;

    public SelectDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.year);
        dest.writeInt(this.month);
        dest.writeInt(this.day);
    }

    public void readFromParcel(Parcel source) {
        this.year = source.readInt();
        this.month = source.readInt();
        this.day = source.readInt();
    }

    protected SelectDate(Parcel in) {
        this.year = in.readInt();
        this.month = in.readInt();
        this.day = in.readInt();
    }

    public static final Creator<SelectDate> CREATOR = new Creator<SelectDate>() {
        @Override
        public SelectDate createFromParcel(Parcel source) {
            return new SelectDate(source);
        }

        @Override
        public SelectDate[] newArray(int size) {
            return new SelectDate[size];
        }
    };
}
