package com.manage.calendar.utils;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.StyleRes;

import com.manage.calendar.R;


public class DialogUtils {

    public static Dialog createDialog(Context context, int dialogLayoutRes, float mWidth, float mHeight, int gravity) {
        final Dialog dialog = new Dialog(context, R.style.dialog_base_style);
        View view = LayoutInflater.from(context).inflate(dialogLayoutRes, null);
        Window dialog_window = dialog.getWindow();
        dialog_window.setGravity(gravity);
        dialog_window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog_window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        if (mWidth > 0) {
            lp.width = (int) (d.getWidth() * mWidth);
        }
        if (mHeight > 0) {
            lp.height = (int) (mHeight);
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog_window.setAttributes(lp);
        dialog.setContentView(view);
        dialog.show();
        return dialog;
    }


    public static Dialog createDialog(Context context, @StyleRes int style, int dialogLayoutRes, float mHeight, int gravity) {
        final Dialog dialog = new Dialog(context, style);
        View view = LayoutInflater.from(context).inflate(dialogLayoutRes, null);
        Window dialog_window = dialog.getWindow();
        dialog_window.setGravity(gravity);
        dialog_window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = m.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager.LayoutParams lp = dialog_window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = (int) (metrics.widthPixels * 0.8f);
        //设置窗口高度为包裹内容
        lp.height = (int) mHeight;

        dialog.setCanceledOnTouchOutside(false);
        dialog_window.setAttributes(lp);
        dialog.setContentView(view);
        dialog.show();
        return dialog;
    }
}
