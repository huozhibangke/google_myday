package com.manage.calendar.sys;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gyf.immersionbar.ImmersionBar;

import com.manage.calendar.R;
import com.manage.calendar.adapter.LanguageAdapter;
import com.manage.calendar.bean.LanguageBean;
import com.manage.calendar.databinding.ActivitySetLanguageBinding;
import com.manage.calendar.utils.ClickDelay;
import com.manage.calendar.utils.DensityUtil;
import com.manage.calendar.widget.language.LanguageHelper;
import com.manage.calendar.widget.language.LanguageType;
import com.manage.calendar.MainActivity;
import com.manage.calendar.MyApp;
import com.manage.calendar.base.BaseActivity;
import com.manage.calendar.utils.DialogUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SetLanguageActivity extends BaseActivity<ActivitySetLanguageBinding> {

    private LanguageAdapter adapter;

    @Override
    public void initParamConfig() {
        setloadingState(false);
        setTopBarView(false);
        setHideTitleBar(false);
        super.initParamConfig();
    }

    @Override
    public ActivitySetLanguageBinding setViewBinding() {
        return ActivitySetLanguageBinding.inflate(layoutInflater);
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        initViewStatus();
        mBinding.rvLanguage.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new LanguageAdapter(mContext, R.layout.language_item_rv);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(mContext, R.drawable.shape_divider_v_10dp)));
        mBinding.rvLanguage.addItemDecoration(divider);
        mBinding.rvLanguage.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                  if(ClickDelay.isFastClick()){
                      if (view.getId() == R.id.item_language) {
                          LanguageBean bean = (LanguageBean) adapter.getData().get(position);
                          showConfirmDialog(bean);
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

    private void initViewStatus() {
        int statusBarHeight = ImmersionBar.getStatusBarHeight(activity);
        ViewGroup.LayoutParams lp = mBinding.viewStatus.getLayoutParams();
        lp.height = statusBarHeight;
        mBinding.viewStatus.setLayoutParams(lp);
    }

    @Override
    public void onInitData() {
        List<LanguageBean> list = new ArrayList<>();
        HashMap<Integer, String> hashMap = new HashMap<>();
        hashMap.put(LanguageType.LANGUAGE_FOLLOW_SYSTEM, getString(R.string.setting_follower_system));
        hashMap.put(LanguageType.LANGUAGE_EN, getString(R.string.setting_language_english));
        for (Map.Entry<Integer, String> entry : hashMap.entrySet()) {
            list.add(new LanguageBean(entry.getKey(), entry.getValue()));
        }

        adapter.setNewData(list);
    }

    private void showConfirmDialog(LanguageBean bean) {
        Dialog mDialog = DialogUtils.createDialog(this, R.layout.dialog_set_language_confirm, 0.8f, DensityUtil.dipToPx(mContext, 110f), Gravity.CENTER);
        TextView tvContent = mDialog.findViewById(R.id.tv_content);
        TextView tvCancel = mDialog.findViewById(R.id.tv_cancel);
        TextView tvConfirm = mDialog.findViewById(R.id.tv_confirm);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
               // Logx.e("languageType22222", "${bean.languageType}----${bean.languageName}");
                LanguageHelper.getInstance().updateApplicationLocale(MyApp.mApplication.getApplicationContext(), bean.getLanguageType());
                Intent intent = new Intent(activity, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(intent);
            }
        });
    }
}
