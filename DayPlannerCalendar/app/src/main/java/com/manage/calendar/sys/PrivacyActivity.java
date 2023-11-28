package com.manage.calendar.sys;

import android.os.Bundle;


import com.manage.calendar.BuildConfig;
import com.manage.calendar.R;
import com.manage.calendar.base.BaseActivity;
import com.manage.calendar.databinding.ActivityPrivacyBinding;


public class PrivacyActivity extends BaseActivity<ActivityPrivacyBinding> {
    @Override
    public ActivityPrivacyBinding setViewBinding() {
        return ActivityPrivacyBinding.inflate(layoutInflater);
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        setCenterMainTitle(getString(R.string.privacy_policy));
    }

    @Override
    public void onInitData() {
        mBinding.web.loadUrl(BuildConfig.agreementPrivacy);
    }
}
