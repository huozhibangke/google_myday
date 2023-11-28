package com.manage.calendar.widget.empty;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.dylanc.loadingstateview.LoadingStateView;
import com.dylanc.loadingstateview.ViewType;
import com.manage.calendar.databinding.LayoutLoadingBinding;

public class LoadingView extends LoadingStateView.ViewDelegate{
    public LoadingView() {
        super(ViewType.LOADING);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        LayoutLoadingBinding binding = LayoutLoadingBinding.inflate(layoutInflater);
        return binding.getRoot();
    }


}
