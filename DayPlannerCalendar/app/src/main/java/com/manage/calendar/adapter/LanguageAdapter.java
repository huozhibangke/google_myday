package com.manage.calendar.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.manage.calendar.R;
import com.manage.calendar.bean.LanguageBean;
import com.manage.calendar.widget.language.LanguageHelper;


public class LanguageAdapter extends BaseQuickAdapter<LanguageBean, BaseViewHolder> {

    private Drawable select;
    private Drawable unSelect;
    private int type;
    public LanguageAdapter(Context context,int layoutResId) {
        super(layoutResId);
        type = LanguageHelper.getInstance().getLanguageType();
        select = ContextCompat.getDrawable(context, R.drawable.ic_local_checked);
        unSelect = ContextCompat.getDrawable(context, R.drawable.ic_local_unchecked);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, LanguageBean item) {
        AppCompatImageView icChecked=helper.getView(R.id.iv_check);
        AppCompatTextView language=helper.getView(R.id.tv_language_name);
        language.setText(item.getLanguageName());
        if (type == item.getLanguageType()) {
            icChecked.setImageDrawable(select);
        } else {
            icChecked.setImageDrawable(unSelect);
        }
    }
}
