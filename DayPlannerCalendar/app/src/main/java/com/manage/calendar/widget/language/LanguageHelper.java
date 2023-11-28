package com.manage.calendar.widget.language;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;

import androidx.annotation.RequiresApi;


import com.manage.calendar.R;
import com.manage.calendar.widget.SPHelper;
import com.manage.calendar.Const;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LanguageHelper {
    private volatile static LanguageHelper instance;
    private Locale defaultLanguage;

    private HashMap<Integer, Locale> supportLanguage;

    private LanguageHelper() {
        defaultLanguage = getSystemLocal();
        initLangauge();
    }


    public static LanguageHelper getInstance() {
        if (instance == null) {
            synchronized (LanguageHelper.class) {
                if (instance == null) {
                    instance = new LanguageHelper();
                }
            }
        }
        return instance;
    }

    private void initLangauge() {
        supportLanguage = new HashMap<>();
        supportLanguage.put(LanguageType.LANGUAGE_FOLLOW_SYSTEM, defaultLanguage);//默认
        supportLanguage.put(LanguageType.LANGUAGE_EN, Locale.ENGLISH);  //英文
        supportLanguage.put(LanguageType.LANGUAGE_CHINESE_SIMPLIFIED, Locale.SIMPLIFIED_CHINESE); //简体
        supportLanguage.put(LanguageType.LANGUAGE_CHINESE_TRADITIONAL, Locale.TRADITIONAL_CHINESE);//繁体
        supportLanguage.put(LanguageType.LANGUAGE_THAILAND, new Locale("th", "TH"));   //泰国
        supportLanguage.put(LanguageType.LANGUAGE_VIETNAM, new Locale("vi", "VN"));//越南
        supportLanguage.put(LanguageType.LANGUAGE_JAPAN, Locale.JAPAN); //日本
        supportLanguage.put(LanguageType.LANGUAGE_SOUTH_KOREA, Locale.KOREA);  //韩国
        supportLanguage.put(LanguageType.LANGUAGE_GERMANY, Locale.GERMANY); //德国
        supportLanguage.put(LanguageType.LANGUAGE_RUSSIAN, new Locale("ru", "RU")); //俄语
        supportLanguage.put(LanguageType.LANGUAGE_FRENCH, Locale.FRENCH);  //法语
        supportLanguage.put(LanguageType.LANGUAGE_PORTUGAL, new Locale("pt", "PT"));//葡萄牙
        supportLanguage.put(LanguageType.LANGUAGE_INDIA, new Locale("te", "IN"));  //印度
        supportLanguage.put(LanguageType.LANGUAGE_INDONESIA, new Locale("in", "ID"));//印度尼西亚
        supportLanguage.put(LanguageType.LANGUAGE_SPAIN, new Locale("es", "ES")); //西班牙

    }

    public HashMap<Integer, Locale> getSupportLanguage() {
        return supportLanguage;
    }


    public int getLanguageType() {
        int languageType = SPHelper.getInstance().getInt(Const.CURRENT_LOCAL_LANGUAGE, LanguageType.LANGUAGE_FOLLOW_SYSTEM);
        for (Map.Entry<Integer, Locale> entry : supportLanguage.entrySet()) {
            if (languageType == entry.getKey()) {
                return languageType;
            }
        }
        return languageType;
    }

    private Locale getSystemLocal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Resources.getSystem().getConfiguration().getLocales().get(0);
        } else {
            defaultLanguage = Locale.getDefault();
            return defaultLanguage;
        }
    }

    public Context attachBaseContext(Context context) {
        int language = SPHelper.getInstance().getInt(Const.CURRENT_LOCAL_LANGUAGE, -1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            return createConfigurationContext(context, language);
        } else {
            return updateConfiguration(context, language);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private Context createConfigurationContext(Context context, int language) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = getLanguageLocale(language);
        LocaleList localeList = new LocaleList(locale);
        configuration.setLocales(localeList);
        return context.createConfigurationContext(configuration);
    }


    private Context updateConfiguration(Context context, int language) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = getLanguageLocale(language);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // apply locale
            configuration.setLocales(new LocaleList(locale));
        } else {
            // updateConfiguration
            configuration.locale = locale;
            DisplayMetrics dm = resources.getDisplayMetrics();
            resources.updateConfiguration(configuration, dm);
        }
        return context;
    }

    private Locale getLanguageLocale(int language) {
        if (supportLanguage.containsKey(language)) {
            return supportLanguage.get(language);
        } else {
            Locale systemLocal = getSystemLocal();
            for (Map.Entry<Integer, Locale> entry : supportLanguage.entrySet()) {
                if (entry.getValue().getLanguage().equals(systemLocal.getLanguage())) {
                    return systemLocal;
                }
            }
        }
        return Locale.ENGLISH;
    }


    public Context getNewLocalContext(Context newBase) {
        try {
            // 多语言适配
            Context context = attachBaseContext(newBase);
            // 兼容appcompat 1.2.0后切换语言失效问题
            Configuration configuration = context.getResources().getConfiguration();
            return new ContextThemeWrapper(context, R.style.ThemeDialog) {
                @Override
                public void applyOverrideConfiguration(Configuration overrideConfiguration) {
                    overrideConfiguration.setTo(configuration);
                    super.applyOverrideConfiguration(overrideConfiguration);
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newBase;
    }

    public void updateApplicationLocale(Context context, int newLanguage) {
        SPHelper.getInstance().setInt(Const.CURRENT_LOCAL_LANGUAGE, newLanguage);

        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = getLanguageLocale(newLanguage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // apply locale
            configuration.setLocales(new LocaleList(locale));
            // context.createConfigurationContext(configuration)
        } else {
            configuration.setLocale(locale);

        }
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);
    }
}
