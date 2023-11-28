package com.manage.calendar.webview;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.manage.calendar.R;

public class DialogUtil {

    /**
     * 全局公用提示框
     *
     * @param context
     * @param view
     * @param mWidth          :屏幕宽度占比
     * @param mHeight         ：屏幕高度占比
     * @param gravity
     * @return
     */
    public static Dialog CommonDialog(Context context, View view, float mWidth, float mHeight, int gravity) {
        final Dialog dialog = new Dialog(context, R.style.common_dialog_style);
        Window dialog_window = dialog.getWindow();
        switch (gravity){
            case Gravity.BOTTOM:
                dialog_window.setWindowAnimations(R.style.main_menu_animStyle);
                break;
            case Gravity.TOP:
                dialog_window.setWindowAnimations(R.style.main_top_animStyle);
                break;
            default:
                dialog_window.setWindowAnimations(R.style.dialogWindowAnim);
                break;
        }
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
            lp.height = (int) (d.getHeight() * mHeight);
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog_window.setAttributes(lp);
        dialog.setContentView(view);
        dialog.show();
        return dialog;
    }

}
