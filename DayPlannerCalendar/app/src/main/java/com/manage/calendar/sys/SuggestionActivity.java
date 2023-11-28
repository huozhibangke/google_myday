package com.manage.calendar.sys;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gyf.immersionbar.ImmersionBar;

import com.manage.calendar.R;
import com.manage.calendar.databinding.ActivitySuggestionBinding;
import com.manage.calendar.utils.ClickDelay;
import com.manage.calendar.base.BaseActivity;


import java.util.Objects;

public class SuggestionActivity extends BaseActivity<ActivitySuggestionBinding> {


    public void initParamConfig() {
        setloadingState(false);
        setTopBarView(false);
        setHideTitleBar(false);
        super.initParamConfig();
    }
    @Override
    public ActivitySuggestionBinding setViewBinding() {
        return ActivitySuggestionBinding.inflate(layoutInflater);
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        initViewStatus();
        mBinding.tvCommitInfo.setOnClickListener(v -> {
            if (ClickDelay.isFastClick()) {
                submitInfo();
            }
        });
        mBinding.acivBack.setOnClickListener(v -> finish());
    }

    @Override
    public void onInitData() {

    }

    private void initViewStatus() {
        int statusBarHeight = ImmersionBar.getStatusBarHeight(activity);
        ViewGroup.LayoutParams lp = mBinding.viewStatus.getLayoutParams();
        lp.height = statusBarHeight;
        mBinding.viewStatus.setLayoutParams(lp);
    }

    private void submitInfo() {
        String content = Objects.requireNonNull(mBinding.etSuggestionContent.getText()).toString();
        String email = Objects.requireNonNull(mBinding.etAddress.getText()).toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, mContext.getString(R.string.feed_back_content), Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, mContext.getString(R.string.feed_back_email), Toast.LENGTH_LONG)
                    .show();
            return;
        }

//        BCSUserHelper.getInstance().feedBack(mContext,content,email,getString(R.string.feed_back_ok),getString(R.string.feed_back_fail));

    }
}
