package com.manage.calendar.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import com.manage.calendar.R;
import com.manage.calendar.bean.InstallAppBean;
import com.manage.calendar.utils.FileUtils;

public class SoftAppsAdapter extends BaseQuickAdapter<InstallAppBean, BaseViewHolder> {
    private Context context;
    private int mPosition = -1;

    public SoftAppsAdapter(Context context, int layoutResId) {
        super(layoutResId);
        this.context = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, InstallAppBean item) {
        ImageView ivAppImage = helper.getView(R.id.iv_app_image);
        TextView tvAppName = helper.getView(R.id.tv_app_name);
        TextView tvAppVersion = helper.getView(R.id.tv_app_version);
        ImageView ivCheck = helper.getView(R.id.cb_select_memory);
        TextView tvAppSize = helper.getView(R.id.tv_app_size);

        ivAppImage.setImageDrawable(item.getDrawable());
        tvAppName.setText(item.getName());
        tvAppVersion.setText(String.format(context.getResources().getString(R.string.soft_version), item.getVersionName()));
        tvAppSize.setText(FileUtils.formatSize(item.getAppSize()));
        if (item.isCheck()) {
            ivCheck.setImageResource(R.drawable.ic_local_checked);
        } else {
            ivCheck.setImageResource(R.drawable.ic_local_unchecked);
        }
    }


    public void setPosition(int position) {
        if (mPosition != position && mPosition != -1) {
            getData().get(mPosition).setCheck(false);
            notifyItemChanged(mPosition);
            mPosition = position;
            getData().get(mPosition).setCheck(true);
            notifyItemChanged(position);
        } else if (mPosition == -1) {
            mPosition = position;
            getData().get(mPosition).setCheck(true);
            notifyItemChanged(position);
        }
//        else {
//            data[mPosition].check = false
//            notifyItemChanged(position)
//            mPosition = -1
//        }
    }




    public InstallAppBean getSelectItem() {
        if (mPosition != -1) {
            return getData().get(mPosition);
        }
        return null;
    }
}
