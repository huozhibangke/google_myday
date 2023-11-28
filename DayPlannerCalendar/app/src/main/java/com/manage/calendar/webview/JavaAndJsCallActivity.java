package com.manage.calendar.webview;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.manage.calendar.R;

public class JavaAndJsCallActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_and_js_call);

        initWebView();

        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login("android");
            }
        });

    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    public void initWebView() {
        //可以加载网页和自定义浏览器
        webView = new WebView(this);

        //设置支持JS
        webView.getSettings().setJavaScriptEnabled(true);

        //设置不调用系统浏览器，使用自定义浏览器
        webView.setWebViewClient(new WebViewClient());

        //加载网络或本地网页
//        webView.loadUrl("https://www.bilibili.com/video/BV1SW411L7qM?p=3");
        webView.loadUrl("file:///android_asset/JavaAndJsCallActivity.html");

        //添加JavascriptInterface后JS可通过Android字段调用JavaAndJsCallInterface类中的任何方法
        webView.addJavascriptInterface(new JavaAndJsCallInterface(), "Android");

        //TODO 1、先初始化webView，此处先不要加载webView
//        setContentView(webView);
    }

    private void login(final String name) {

        webView.post(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    webView.evaluateJavascript("javascript:javaCallJs(" + " ' "+ name + " ' " + ")", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
                            //JS返回结果
                            Log.e("TAG", "onReceiveValue: " + s);
                        }
                    });
                } else {
                    webView.loadUrl("javascript:javaCallJs(" + " ' "+ name + " ' " + ")");
                }
            }
        });

        //TODO 2、调用完函数后再加载webView（否则会出现HTML中函数未定义的错误）
        setContentView(webView);
    }

    /**
     * JS要调用的Java中的类.
     */
    class JavaAndJsCallInterface {

        @JavascriptInterface
        public void toast() {
            Toast.makeText(JavaAndJsCallActivity.this, "我是Android代码，我被调用了！", Toast.LENGTH_LONG).show();
        }

    }
}
