package com.manage.calendar.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import com.manage.calendar.R;
import com.manage.calendar.bean.DrawerBean;

public class DrawerAdapter extends BaseQuickAdapter<DrawerBean, BaseViewHolder> {
    public DrawerAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, DrawerBean drawerBean) {
        AppCompatImageView icon = holder.getView(R.id.aciv_menu_icon);
        AppCompatTextView title = holder.getView(R.id.tv_menu_title);
        icon.setImageResource(drawerBean.getIcon());
        title.setText(drawerBean.getTitle());
    }
}
