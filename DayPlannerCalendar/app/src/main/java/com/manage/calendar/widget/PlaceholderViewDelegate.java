package com.manage.calendar.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.dylanc.loadingstateview.LoadingStateView;
import com.dylanc.loadingstateview.ViewType;
import com.manage.calendar.R;


public class PlaceholderViewDelegate extends LoadingStateView.ViewDelegate {

    public PlaceholderViewDelegate() {
        super(ViewType.EMPTY);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return inflater.inflate(R.layout.layout_placeholder, parent, false);
    }
}