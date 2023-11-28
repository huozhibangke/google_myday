package com.manage.calendar.widget.empty;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.dylanc.loadingstateview.LoadingStateView;
import com.dylanc.loadingstateview.ViewType;
import com.manage.calendar.databinding.LayoutEmptyBinding;


public class EmptyView extends LoadingStateView.ViewDelegate {
    private LayoutEmptyBinding binding;

    public EmptyView() {
        super(ViewType.EMPTY);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        binding = LayoutEmptyBinding.inflate(layoutInflater);
        return binding.getRoot();
    }
    public void  setCenterEmptyImage(int resImageId) {
        binding.ivDefaultEmptyImage.setImageResource(resImageId);
    }

    public void setText(String text) {
        binding.tvDefaultEmptyText.setText(text);
    }
}
