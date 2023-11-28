package com.manage.calendar.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.ColorLong;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewbinding.ViewBinding;

import com.dylanc.loadingstateview.LoadingStateView;
import com.dylanc.loadingstateview.OnReloadListener;
import com.dylanc.loadingstateview.ViewType;
import com.gyf.immersionbar.ImmersionBar;

import com.manage.calendar.R;
import com.manage.calendar.databinding.ActivityBaseBinding;
import com.manage.calendar.databinding.CommentTitleBarBinding;
import com.manage.calendar.utils.DensityUtil;
import com.manage.calendar.widget.empty.EmptyView;
import com.manage.calendar.widget.language.LanguageHelper;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity implements OnReloadListener {


    public T mBinding;
    public Context mContext;
    public Activity activity;

    public ImmersionBar mImmersionBar;

    public boolean statusBarDarkFont = true;

    public LayoutInflater layoutInflater;

    private ActivityBaseBinding baseBinding;


    private CommentTitleBarBinding titleBarBinding;

    private int statusViewColor = R.color.white;

    private int titleBarColor = R.color.white;

    private boolean topBarView = true;

    private View topView;

    private boolean hideTitleBar = true;

    private boolean loadingState = true;

    private LoadingStateView loadingView;

    private boolean needEvent = false;

    private LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
    );

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.getInstance().getNewLocalContext(newBase));
        //super.attachBaseContext(newBase);
        mContext = this;
        activity = this;
        initParamConfig();
        if (needEvent) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
    }

    public void initParamConfig() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        layoutInflater = getLayoutInflater();
        setWindowBackGround();
        initStatusBar();
        setLayoutContent();
        onInitView(savedInstanceState);
        onInitData();
    }

    private void setLayoutContent() {

        baseBinding = ActivityBaseBinding.inflate(layoutInflater);
        mBinding = setViewBinding();
        setStatusBarView(statusViewColor);
        initTitleBar();
        initPageStatus();

    }


    public abstract T setViewBinding();

    public abstract void onInitView(Bundle savedInstanceState);

    public abstract void onInitData();

    @Override
    public void onReload() {

    }

    private void initStatusBar() {
        if (mImmersionBar == null) {
            mImmersionBar = ImmersionBar.with(this);
            mImmersionBar.transparentStatusBar();
            mImmersionBar.statusBarDarkFont(statusBarDarkFont);
            mImmersionBar.init();
        }
    }


    private void setStatusBarView(@ColorLong int color) {
        if (color == 0) return;
        if (topBarView) {
            topView = new View(this);
            int statusBarHeight = ImmersionBar.getStatusBarHeight(activity);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.height = statusBarHeight;
            topView.setLayoutParams(params);
            topView.setBackgroundColor(ContextCompat.getColor(this, color));
            baseBinding.getRoot().addView(topView);
        }
    }


    private void initTitleBar() {
        if (hideTitleBar) {
            titleBarBinding = CommentTitleBarBinding.inflate(layoutInflater);
            titleBarBinding.getRoot().setBackgroundColor(ContextCompat.getColor(mContext, titleBarColor));
            titleBarBinding.llcBack.setOnClickListener(v -> {
                onClickLeftImage();
            });

            titleBarBinding.llcRight.setOnClickListener(v -> {
                onClickRight();
            });

            baseBinding.getRoot().addView(
                    titleBarBinding.getRoot(), new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            DensityUtil.dipToPx(mContext, 48f)
                    )
            );
        }
    }


    public void initPageStatus() {

        if (loadingState) {
            loadingView = new LoadingStateView(mBinding.getRoot(), this);
            baseBinding.getRoot().addView(loadingView.getDecorView(), layoutParams);
            setContentView(
                    baseBinding.getRoot(),
                    layoutParams
            );
        } else {
            baseBinding.getRoot().addView(mBinding.getRoot(), new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            setContentView(baseBinding.getRoot(), layoutParams);
        }
    }


    public void setStatusBarColor(@ColorLong int color) {
        if (topBarView) {
            topView.setBackgroundColor(ContextCompat.getColor(this, color));
        }
    }

    public void setCenterMainTitle(String title) {
        if (titleBarBinding != null) {
            titleBarBinding.actvCenterTitle.setText(title);
        }

    }

    public void setCenterMainTitle(String title, @ColorLong int color) {
        if (titleBarBinding != null) {
            titleBarBinding.actvCenterTitle.setText(title);
            titleBarBinding.actvCenterTitle.setTextColor(ContextCompat.getColor(this, color));
        }
    }

    public void setHideTitleBar(Boolean hideTitleBar) {
        this.hideTitleBar = hideTitleBar;
    }

    public void setTopBarView(Boolean topBarView) {
        this.topBarView = topBarView;
    }

    public void setStatusViewColor(@ColorLong int color) {
        statusViewColor = color;
    }


    public void setTitleBarBackgroundColor(@ColorLong int color) {
        titleBarColor = color;
    }

    public void setLeftImage(@DrawableRes int image) {
        if (titleBarBinding != null) {
            titleBarBinding.llcBack.setVisibility(View.VISIBLE);
            titleBarBinding.acivBack.setImageResource(image);
        }
    }

    public void hideLeftImage() {
        titleBarBinding.llcBack.setVisibility(View.GONE);
    }

    public void setRightText(String rihgtText) {
        if (titleBarBinding != null) {
            titleBarBinding.llcRight.setVisibility(View.VISIBLE);
            titleBarBinding.actvRightText.setVisibility(View.VISIBLE);
            titleBarBinding.actvRightText.setText(rihgtText);
        }
    }

    public void setRightImage(@DrawableRes int icon) {
        if (titleBarBinding != null) {
            titleBarBinding.llcRight.setVisibility(View.VISIBLE);
            titleBarBinding.acivRightIv.setVisibility(View.VISIBLE);
            titleBarBinding.acivRightIv.setImageResource(icon);
        }

    }

    public void setRightBoth(String rihgtText, @DrawableRes int icon) {
        if (titleBarBinding != null) {
            titleBarBinding.llcRight.setVisibility(View.VISIBLE);
            titleBarBinding.actvRightText.setVisibility(View.VISIBLE);
            titleBarBinding.acivRightIv.setVisibility(View.VISIBLE);
            titleBarBinding.actvRightText.setText(rihgtText);
            titleBarBinding.acivRightIv.setImageResource(icon);
        }
    }

    public void hideRightBoth() {
        titleBarBinding.llcRight.setVisibility(View.GONE);
    }

    public void setloadingState(Boolean loadingState) {
        this.loadingState = loadingState;
    }

    public boolean getloadingState() {
        return loadingState;
    }


    public void showLoadingView() {
        if (loadingState) {
            loadingView.showLoadingView();
        }
    }

    public void showContentView() {
        if (loadingState) {
            loadingView.showContentView();
        }
    }


    public void showErrorView() {
        if (loadingState) {
            loadingView.showErrorView();
        }
    }

    public void showEmptyView() {
        if (loadingState) {
            loadingView.showEmptyView();
        }
    }

    public void showNetErrorView() {
        if (loadingState) {
            loadingView.showErrorView();
        }
    }

    public void setEmptyContent(@DrawableRes int resImageId, String Content) {
        loadingView.updateViewDelegate(ViewType.EMPTY, viewDelegate -> {
            if (viewDelegate instanceof EmptyView) {
                EmptyView emptyView = (EmptyView) viewDelegate;
                emptyView.setCenterEmptyImage(resImageId);
                emptyView.setText(Content);
            }
        });

    }

    public void onClickLeftImage() {
        finish();
    }

    public void onClickRight() {

    }

    public void setNeedEvent(boolean needEvent) {
        this.needEvent = needEvent;
    }

    public void setWindowBackGround() {
        this.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.color.color_eeeeee));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (needEvent) {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
        }
    }
}
