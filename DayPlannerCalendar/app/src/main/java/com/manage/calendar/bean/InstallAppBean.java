package com.manage.calendar.bean;

import android.graphics.drawable.Drawable;

public class InstallAppBean {

    private String packageName;
    private Drawable drawable;
    private String name;
    private boolean systemApp;
    private boolean check;
    private String versionName;
    private long appTime;
    private long appSize;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSystemApp() {
        return systemApp;
    }

    public void setSystemApp(boolean systemApp) {
        this.systemApp = systemApp;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public long getAppTime() {
        return appTime;
    }

    public void setAppTime(long appTime) {
        this.appTime = appTime;
    }

    public long getAppSize() {
        return appSize;
    }

    public void setAppSize(long appSize) {
        this.appSize = appSize;
    }


    @Override
    public String toString() {
        return "InstallAppBean{" +
                "packageName='" + packageName + '\'' +
                ", drawable=" + drawable +
                ", name='" + name + '\'' +
                ", systemApp=" + systemApp +
                ", check=" + check +
                ", versionName='" + versionName + '\'' +
                ", appTime=" + appTime +
                ", appSize=" + appSize +
                '}';
    }
}
