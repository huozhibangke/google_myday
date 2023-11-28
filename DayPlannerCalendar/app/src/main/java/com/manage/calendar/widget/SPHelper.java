package com.manage.calendar.widget;

import com.tencent.mmkv.MMKV;

import java.util.Set;

public class SPHelper {

    private volatile static SPHelper instance;

    private MMKV kv;

    private SPHelper() {
        kv = MMKV.defaultMMKV();
    }

    public static SPHelper getInstance() {
        if (instance == null) {
            synchronized (SPHelper.class) {
                if (instance == null) {
                    instance = new SPHelper();
                }
            }
        }
        return instance;
    }

    public void setBoolean(String key, boolean value) {
        kv.encode(key, value);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return kv.decodeBool(key, defValue);
    }

    public void setString(String key, String value) {
        kv.encode(key, value);
    }

    public String getString(String key, String defValue) {
        return kv.decodeString(key, defValue);
    }

    public void setInt(String key, int value) {
        kv.encode(key, value);
    }

    public int getInt(String key, int defValue) {
        return kv.decodeInt(key, defValue);
    }

    public void setDouble(String key, double value) {
        kv.encode(key, value);
    }

    public Double getDouble(String key, double defValue) {
        return kv.decodeDouble(key, defValue);
    }

    public void setFloat(String key, float value) {
        kv.encode(key, value);
    }

    public Float getFloat(String key, float defValue) {
        return kv.decodeFloat(key, defValue);
    }

    public void setLong(String key, long value) {
        kv.encode(key, value);
    }

    public Long getLong(String key, long defValue) {
        return kv.decodeLong(key, defValue);
    }

    public void setList(String key, Set<String> value) {
        kv.encode(key, value);
    }

    public Set<String> getList(String key, Set<String> defValue) {
        return kv.decodeStringSet(key, defValue);
    }

    public void remove(String key) {
        kv.removeValueForKey(key);
    }


    public void clearData() {
        kv.clearAll();
    }
}
