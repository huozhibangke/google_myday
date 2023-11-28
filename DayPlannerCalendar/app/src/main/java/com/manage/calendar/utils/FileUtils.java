package com.manage.calendar.utils;

import java.text.DecimalFormat;

public class FileUtils {
    public static String formatSize(long size) {
        try {
            if (size / (1024 * 1024 * 1024) > 0) {
                float tmpSize = (float) (size) / (float) (1024 * 1024 * 1024);
                DecimalFormat df = new DecimalFormat("#.##");
                return "" + df.format(tmpSize) + "GB";
            } else if (size / (1024 * 1024) > 0) {
                float tmpSize = (float) (size) / (float) (1024 * 1024);
                DecimalFormat df = new DecimalFormat("#.##");
                return "" + df.format(tmpSize) + "MB";
            } else if (size / 1024 > 0) {
                return "" + (size / (1024)) + "KB";
            } else {
                return "" + size + "B";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0.3GB";
    }


    public static boolean notAddOtherName(String appName) {
        return appName.contains("android")
                //Application  application
                || appName.contains("pplication")
                || appName.contains("plugin")
                || appName.contains("flyme")
                || appName.contains("vivo")
                || appName.contains("oppo")
                || appName.contains("huawei")
                || appName.contains("miui");
    }
}
