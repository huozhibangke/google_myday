package com.manage.calendar.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseFragment<T extends ViewBinding> extends Fragment {

    public T mBinding;
    public Context mContext;
    public Activity activity;

    public View rootView;

    private boolean needEvent = false;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
        this.activity = requireActivity();
        initParamConfig();
        if (needEvent) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
    }

    public void initParamConfig() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            parent.removeView(rootView);
        }
        if (mBinding != null) {
            ViewGroup root = (ViewGroup) mBinding.getRoot().getParent();
            if (root != null) {
                root.removeView(root.getRootView());
            }
        } else {
            mBinding = setViewBinding();
            rootView = mBinding.getRoot();
        }

        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onInitView();
        onInitData();
    }

    public abstract T setViewBinding();


    public abstract void onInitView();

    abstract void onInitData();


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (needEvent) {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
        }
        mBinding = null;
    }



    public void setNeedEvent(boolean needEvent) {
        this.needEvent = needEvent;
    }
}
