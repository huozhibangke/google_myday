package com.manage.calendar.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.manage.calendar.Calendar;
import com.manage.calendar.WeekView;


/**
 * 魅族周视图
 * Created by huanghaibin on 2017/11/29.
 */

public class MeizuWeekView extends WeekView {
    private Paint mTextPaint = new Paint();

    private Paint mSchemeBasicPaint = new Paint();
    private float mRadio;
    private int mPadding;
    private int circlemPadding;
    private int mSpacePadding;
    private float mSchemeBaseLine;
    private int mStrokeWidth;
    private int bk = Color.parseColor("#D63F27");

    private int lunarEssJq = Color.parseColor("#1A9550");
    private int traditionColor = Color.parseColor("#D63F27");

    private int todayPaintColor = Color.parseColor("#CCCCCC");

    private Paint mCurWeekTextPaint = new Paint();
    //农历二十四节气 画笔
    private Paint mLunarEssJqTextPaint = new Paint();

    private Paint mTraditionTextPaint = new Paint();

    private Paint mCurrentTodayPaint = new Paint();



    public MeizuWeekView(Context context) {
        super(context);

        mTextPaint.setTextSize(dipToPx(context, 8));
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);

        mSchemeBasicPaint.setAntiAlias(true);
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicPaint.setColor(0xffed5353);
        mSchemeBasicPaint.setFakeBoldText(true);
        mRadio = dipToPx(getContext(), 6);
        mPadding = dipToPx(getContext(), 4);
        mSpacePadding = dipToPx(getContext(), 1);
        circlemPadding = dipToPx(getContext(), 6);
        mStrokeWidth= dipToPx(getContext(), 2);
        Paint.FontMetrics metrics = mSchemeBasicPaint.getFontMetrics();
        mSchemeBaseLine = mRadio - metrics.descent + (metrics.bottom - metrics.top) / 2 + dipToPx(getContext(), 1);


        //周末 画笔
        mCurWeekTextPaint.setAntiAlias(true);
        mCurWeekTextPaint.setTextAlign(Paint.Align.CENTER);
        mCurWeekTextPaint.setColor(bk);
        mCurWeekTextPaint.setFakeBoldText(true);
        mCurWeekTextPaint.setTextSize(dipToPx(context, 16));
        //二十四节日 画笔
        mLunarEssJqTextPaint.setAntiAlias(true);
        mLunarEssJqTextPaint.setTextAlign(Paint.Align.CENTER);
        mLunarEssJqTextPaint.setColor(lunarEssJq);
        mLunarEssJqTextPaint.setFakeBoldText(true);
        mLunarEssJqTextPaint.setTextSize(dipToPx(context, 10));
        //农历 传统节日
        mTraditionTextPaint.setAntiAlias(true);
        mTraditionTextPaint.setTextAlign(Paint.Align.CENTER);
        mTraditionTextPaint.setColor(traditionColor);
        mTraditionTextPaint.setTextSize(dipToPx(context, 10));
        mTraditionTextPaint.setFakeBoldText(true);

        //今天 日期背景画笔
        mCurrentTodayPaint.setColor(todayPaintColor);
        mSelectedPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return true 则绘制onDrawScheme，因为这里背景色不是是互斥的
     */
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, boolean hasScheme) {
        mSelectedPaint.setStyle(Paint.Style.STROKE);
        mSelectedPaint.setColor(bk);
        mSelectedPaint.setStrokeWidth(mStrokeWidth);
        //canvas.drawRect(x + mPadding, mPadding, x + mItemWidth - mPadding, mItemHeight - mPadding, mSelectedPaint);
        canvas.drawRoundRect(x+mSpacePadding, mPadding, x + mItemWidth-mSpacePadding, mItemHeight - mPadding, dipToPx(getContext(), 4), dipToPx(getContext(), 4), mSelectedPaint);
        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x) {
        mSchemeBasicPaint.setColor(calendar.getSchemeColor());

        canvas.drawCircle(x + mItemWidth - circlemPadding - mRadio / 2, circlemPadding + mRadio, mRadio, mSchemeBasicPaint);

        canvas.drawText(calendar.getScheme(),
                x + mItemWidth - circlemPadding - mRadio / 2 - getTextWidth(calendar.getScheme()) / 2,
                circlemPadding + mSchemeBaseLine, mTextPaint);
    }

    private float getTextWidth(String text) {
        return mTextPaint.measureText(text);
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;
        int top = -mItemHeight / 6;

        boolean isInRange = isInRange(calendar);

//        if (isSelected) {
//            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
//                    mSelectTextPaint);
//            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + mItemHeight / 10, mSelectedLunarTextPaint);
//        } else if (hasScheme) {
//            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
//                    calendar.isCurrentMonth() && isInRange ? mSchemeTextPaint : mOtherMonthTextPaint);
//
//            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + mItemHeight / 10, mCurMonthLunarTextPaint);
//        } else {
//            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
//                    calendar.isCurrentDay() && isInRange ? mCurDayTextPaint :
//                            calendar.isCurrentMonth() && isInRange ? mCurMonthTextPaint : mOtherMonthTextPaint);
//            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + mItemHeight / 10,
//                    calendar.isCurrentDay() && isInRange ? mCurDayLunarTextPaint :
//                            calendar.isCurrentMonth() ? mCurMonthLunarTextPaint : mOtherMonthLunarTextPaint);
//        }

        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, mSelectTextPaint);
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + mItemHeight / 10, mSelectedLunarTextPaint);
        } else {
            if (calendar.isWeekend()) {
                //周末画笔
                canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, mCurWeekTextPaint);
            } else if (calendar.isCurrentDay()) {
                //今天
                canvas.drawRoundRect(x + mSpacePadding, mPadding, x + mItemWidth - mSpacePadding,  mItemHeight - mPadding, dipToPx(getContext(), 4), dipToPx(getContext(), 4), mCurrentTodayPaint);
                canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, mCurDayTextPaint);
            } else {
                //正常
                canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, mCurMonthTextPaint);
            }
            onDrawTextFestival(canvas, calendar, cx, top, isInRange);
        }
    }


    private void onDrawTextFestival(Canvas canvas, Calendar calendar, int cx, int top, boolean isInRange) {

        //公历节日 劳动节 母亲节  红色
        if (calendar.getGregorianFestival() != null && !calendar.getGregorianFestival().equals("")) {
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + mItemHeight / 10, mTraditionTextPaint);
        } else if (calendar.getSolarTerm() != null && !calendar.getSolarTerm().equals("")) {
            //二十四节气  绿色
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + mItemHeight / 10, mLunarEssJqTextPaint);
        } else if (calendar.getTraditionFestival() != null && !calendar.getTraditionFestival().equals("")) {
            //传统节日 红色
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + mItemHeight / 10, mTraditionTextPaint);
        } else {
            //正常 黑色
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + mItemHeight / 10,
                    calendar.isCurrentDay() && isInRange ? mCurDayLunarTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthLunarTextPaint : mOtherMonthLunarTextPaint);
        }
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
