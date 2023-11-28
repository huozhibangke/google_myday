package com.manage.calendar.widget.empty;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.dylanc.loadingstateview.LoadingStateView;
import com.dylanc.loadingstateview.ViewType;

import com.manage.calendar.databinding.LayoutEmptyErrorBinding;
import com.manage.calendar.utils.ClickDelay;


public class ErrorView extends LoadingStateView.ViewDelegate {
    public ErrorView() {
        super(ViewType.ERROR);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        LayoutEmptyErrorBinding binding = LayoutEmptyErrorBinding.inflate(layoutInflater);
        binding.btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickDelay.isFastClick()) {
                    if (getOnReloadListener() != null) {
                        getOnReloadListener().onReload();
                    }

                }
            }
        });
        return binding.getRoot();
    }
}
