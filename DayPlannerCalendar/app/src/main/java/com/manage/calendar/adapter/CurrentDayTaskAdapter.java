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
import com.manage.calendar.calendar.CalendarEvent;

public class CurrentDayTaskAdapter extends BaseQuickAdapter<CalendarEvent, BaseViewHolder> {
    private Drawable expireDrawable; // 过期
    private Drawable unExpireDrawable; //没过去

    private Context context;

    public CurrentDayTaskAdapter(Context context, int layoutResId) {
        super(layoutResId);
        this.context= context;
        expireDrawable = ContextCompat.getDrawable(context, R.drawable.shape_circle_task_expire);
        unExpireDrawable = ContextCompat.getDrawable(context, R.drawable.shape_circle_task_not_expire);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CalendarEvent item) {
        AppCompatImageView mark = helper.getView(R.id.aciv_task_mark);
        AppCompatTextView title = helper.getView(R.id.actv_task_title);
        AppCompatTextView time = helper.getView(R.id.actv_task_time);
        AppCompatTextView desc = helper.getView(R.id.actv_task_desc);
        AppCompatTextView delete = helper.getView(R.id.actv_delete_task);

        if (item.isExpire()) {
            mark.setBackgroundResource(R.drawable.shape_circle_task_expire);
        } else {
            mark.setBackgroundResource(R.drawable.shape_circle_task_not_expire);
        }

        if (item.getTitle() == null || "".equals(item.getTitle()) || "null".equals(item.getTitle())){
            title.setText(context.getString(R.string.task_not_title));
        }else{
            title.setText(item.getTitle());
        }


        time.setText(item.getTime());
        if (item.getDescription() == null || "".equals(item.getDescription()) || "null".equals(item.getDescription())) {
            desc.setText(context.getString(R.string.task_not_content));
        } else {
            desc.setText(item.getDescription());

        }


    }
}
