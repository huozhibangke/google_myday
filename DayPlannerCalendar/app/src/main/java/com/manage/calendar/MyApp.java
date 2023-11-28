package com.manage.calendar;

import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.multidex.BuildConfig;
import androidx.multidex.MultiDexApplication;

import com.appsflyer.AppsFlyerLib;
import com.dylanc.loadingstateview.LoadingStateView;
import com.manage.calendar.widget.empty.EmptyView;
import com.manage.calendar.widget.empty.ErrorView;
import com.manage.calendar.widget.empty.LoadingView;
import com.manage.calendar.widget.language.LanguageHelper;
import com.nobbily.profoundness.IsocyclicEngine;
import com.tencent.mmkv.MMKV;


public class MyApp extends MultiDexApplication {
    public static MyApp mApplication;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(initLanguage(base));
        IsocyclicEngine.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        initSdk();
    }

    private Context initLanguage(Context base) {
        MMKV.initialize(base);
        return LanguageHelper.getInstance().attachBaseContext(base);
    }

    private void initSdk() {

        if (!BuildConfig.APPLICATION_ID.equals(getPackageName())) {
            return;
        }

        AppsFlyerLib.getInstance().init("ewoxyEdQxYHrhU7QGHorrP", null, this);
        AppsFlyerLib.getInstance().start(this);
        AppsFlyerLib.getInstance().setDebugLog(true);

        LoadingStateView.setViewDelegatePool(pool -> {
            pool.register(
                    new LoadingView(), new EmptyView(), new ErrorView()
            );
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LanguageHelper.getInstance().attachBaseContext(this);
    }

}
