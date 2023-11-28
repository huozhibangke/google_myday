package com.manage.calendar.sys;

import static autodispose2.AutoDispose.autoDisposable;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gyf.immersionbar.ImmersionBar;

import com.manage.calendar.R;
import com.manage.calendar.bean.InstallAppBean;
import com.manage.calendar.databinding.ActivityManagerAppBinding;
import com.manage.calendar.utils.ClickDelay;
import com.manage.calendar.utils.DensityUtil;
import com.manage.calendar.utils.FileUtils;
import com.manage.calendar.MyApp;
import com.manage.calendar.adapter.SoftAppsAdapter;
import com.manage.calendar.base.BaseActivity;
import com.manage.calendar.utils.DialogUtils;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AppManagerActivity extends BaseActivity<ActivityManagerAppBinding> {
    private int position = 0;
    SoftAppsAdapter softAdapter;

    private InstallAppBean unIntsallApp;

    private String appPackageName = "";

    private boolean isUninstall = false;
    private Dialog mDialog;

    private PackageManager packageManager;


    @Override
    public void initParamConfig() {
        setTopBarView(false);
        setHideTitleBar(false);
        super.initParamConfig();
    }

    @Override
    public ActivityManagerAppBinding setViewBinding() {
        return ActivityManagerAppBinding.inflate(layoutInflater);
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        initViewStatus();
        softAdapter = new SoftAppsAdapter(mContext, R.layout.item_apps);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(mContext, R.drawable.shape_divider_v_1dp)));
        mBinding.rvApps.addItemDecoration(divider);
        mBinding.rvApps.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvApps.setAdapter(softAdapter);
        softAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (ClickDelay.isFastClick()) {
                    if (view.getId() == R.id.item_root) {
                        //val entity = adapter.getItem(position) as InstallAppEntity
                        AppManagerActivity.this.position = position;
                        AppManagerActivity.this.softAdapter.setPosition(position);
                        adapter.notifyItemChanged(position);
                        selectApp();
                    }
                }
            }
        });

        mBinding.tvUninstallApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickDelay.isFastClick()) {
                    InstallAppBean installAppBean = softAdapter.getSelectItem();
                    if (installAppBean != null) {
                        shouUnintallConfirm(installAppBean.getPackageName());
                    } else {
                        Toast.makeText(mContext, getString(R.string.uninstall_app_tip), Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }
        });

        mBinding.acivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onInitData() {
        packageManager = MyApp.mApplication.getPackageManager();
        Observable<List<InstallAppBean>> observable = Observable.create(new ObservableOnSubscribe<List<InstallAppBean>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<InstallAppBean>> emitter) throws Throwable {
                // Logx.e("subscribe", Thread.currentThread() + "");
                List<InstallAppBean> list = new ArrayList<>();
                List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
                for (PackageInfo info : packageInfos) {
                    if (Objects.equals(info.packageName, MyApp.mApplication.getPackageName())) {
                        continue;
                    }
                    InstallAppBean bean = new InstallAppBean();
                    bean.setPackageName(info.packageName);
                    bean.setDrawable(info.applicationInfo.loadIcon(packageManager));
                    bean.setName(info.applicationInfo.loadLabel(packageManager).toString());
                    bean.setVersionName(info.versionName);
                    bean.setAppTime(info.firstInstallTime / 1000);
                    bean.setAppSize(new File(info.applicationInfo.sourceDir).length());
                    //Logx.e("reciver", (info.applicationInfo.flags | ApplicationInfo.FLAG_SYSTEM) + "----" + ApplicationInfo.FLAG_SYSTEM);
                    if ((ApplicationInfo.FLAG_SYSTEM & info.applicationInfo.flags) == ApplicationInfo.FLAG_SYSTEM) {
                        bean.setSystemApp(true);
                    } else {
                        bean.setSystemApp(false);
                    }

                    boolean isNeed = !FileUtils.notAddOtherName(bean.getPackageName()) && !bean.isSystemApp();
                    if (!isNeed) {
                        continue;
                    }

                    list.add(bean);

                }

                emitter.onNext(list);


            }
        });


        Observer<List<InstallAppBean>> reciver = new Observer<List<InstallAppBean>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull List<InstallAppBean> list) {
                if (list.size() > 0) {
                    showContentView();
                    softAdapter.setNewData(list);
                } else {
                    showEmptyView();
                    setEmptyContent(R.drawable.ic_favorite_empty, getString(R.string.empty_software));
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .to(autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle())))
                .subscribe(reciver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isUninstall) {
            if (!Objects.equals(appPackageName, "")) {
                if (!isAppInstalled(appPackageName)) {
                    softAdapter.remove(position);
                    //   Logx.e("onResume11111", "被卸载");
                    mBinding.tvUninstallApp.setText(getString(R.string.uninstall));
                }
                //  Logx.e("onResume22222", "被卸载");
            }
            isUninstall = false;
            appPackageName = "";
            unIntsallApp = null;
            position = 0;
        }
    }

    private void initViewStatus() {
        int statusBarHeight = ImmersionBar.getStatusBarHeight(activity);
        ViewGroup.LayoutParams lp = mBinding.viewStatus.getLayoutParams();
        lp.height = statusBarHeight;
        mBinding.viewStatus.setLayoutParams(lp);
    }

    private void selectApp() {
        InstallAppBean installAppBean = softAdapter.getSelectItem();
        if (installAppBean != null) {
            unIntsallApp = installAppBean;
            if (installAppBean.getAppSize() > 0) {
                mBinding.tvUninstallApp.setText(getString(R.string.uninstall) + "(" + FileUtils.formatSize(installAppBean.getAppSize()) + ")");

            } else {
                mBinding.tvUninstallApp.setText(getString(R.string.uninstall));
            }
        }
    }

    private void shouUnintallConfirm(String packageName) {
        mDialog = DialogUtils.createDialog(mContext, R.layout.dialog_set_language_confirm, 0.8f, DensityUtil.dipToPx(mContext, 110f), Gravity.CENTER);
        TextView tvContent = mDialog.findViewById(R.id.tv_content);
        TextView tvCancel = mDialog.findViewById(R.id.tv_cancel);
        TextView tvConfirm = mDialog.findViewById(R.id.tv_confirm);
        tvCancel.setText(getString(R.string.cancle));
        tvContent.setText(getString(R.string.uninstall_tip));
        tvConfirm.setText(getString(R.string.uninstall_now));

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickDelay.isFastClick()) {
                    mDialog.dismiss();
                }
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickDelay.isFastClick()) {
                    mDialog.dismiss();
                    uninstallApp(packageName);
                }
            }
        });
    }

    private void uninstallApp(String packageName) {
        try {
            Intent uninstall_intent = new Intent();
            uninstall_intent.setAction(Intent.ACTION_DELETE);
            uninstall_intent.setData(Uri.parse("package:" + packageName));
            startActivity(uninstall_intent);
            appPackageName = packageName;
            isUninstall = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isAppInstalled(String packageName) {
        PackageInfo packageInfo = null;
        try {
            if (packageManager == null) {
                packageManager = MyApp.mApplication.getPackageManager();
            }

            packageInfo = packageManager.getPackageInfo(packageName, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }
}
