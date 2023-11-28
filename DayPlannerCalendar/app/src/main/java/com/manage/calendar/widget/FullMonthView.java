package com.manage.calendar.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.manage.calendar.Calendar;
import com.manage.calendar.MonthView;

import java.util.List;

/**
 * 高仿魅族日历布局
 * Created by huanghaibin on 2017/11/15.
 */

public class FullMonthView extends MonthView {

    private Paint mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int defaultPaintColor = Color.parseColor("#FFFFFF");


    private int selectTextPaintColor = Color.parseColor("#51ABF0");

    private int todayPaintColor = Color.parseColor("#D63F27");

    private Paint mCurrentTodayPaint = new Paint();
    /**
     * 自定义魅族标记的圆形背景
     */
    private Paint mSchemeBasicPaint = new Paint();

    private Paint circlePaint = new Paint();
    private int circlePaintColor = Color.parseColor("#D8D8D8");

    private int taskPaintColor = Color.parseColor("#ffffff");

    private Paint taskPaint = new Paint();

    public FullMonthView(Context context) {
        super(context);
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setStrokeWidth(dipToPx(context, 0.5f));
        mRectPaint.setColor(defaultPaintColor);
        // mRectPaint.setStyle(Paint.Style.FILL);
        // mRectPaint.setStrokeWidth(dipToPx(context, 0.5f));
        // mRectPaint.setColor(defaultPaintColor);

        mSchemeBasicPaint.setAntiAlias(true);
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicPaint.setFakeBoldText(true);

        //兼容硬件加速无效的代码
        //setLayerType(View.LAYER_TYPE_SOFTWARE, mSchemeBasicPaint);
        //4.0以上硬件加速会导致无效
        //mSelectedPaint.setMaskFilter(new BlurMaskFilter(50, BlurMaskFilter.Blur.SOLID));
        mCurrentTodayPaint.setAntiAlias(true);
        mCurrentTodayPaint.setColor(todayPaintColor);
        mCurrentTodayPaint.setTextAlign(Paint.Align.CENTER);
        mCurrentTodayPaint.setStyle(Paint.Style.FILL);
        mCurrentTodayPaint.setTextSize(dipToPx(context, 14));
        mCurrentTodayPaint.setFakeBoldText(true);

        circlePaint.setAntiAlias(true);
        circlePaint.setColor(circlePaintColor);
        circlePaint.setStyle(Paint.Style.FILL);

        taskPaint.setAntiAlias(true);
        taskPaint.setColor(taskPaintColor);
        taskPaint.setTextAlign(Paint.Align.LEFT);
        taskPaint.setStyle(Paint.Style.FILL);
        taskPaint.setTextSize(dipToPx(context, 8));
//        taskPaint.setFakeBoldText(true);



    }

    /**
     * 绘制选中的日子
     *
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param y         日历Card y起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return true 则绘制onDrawScheme，因为这里背景色不是是互斥的
     */
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
//        canvas.drawRect(x, y, x + mItemWidth, y + mItemHeight, mSelectedPaint);

//        int cx = x + mItemWidth / 2;
//        int top = (y - mItemHeight / 6)-dipToPx(getContext(), 14f);
//        canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
//                mSelectTextPaint);
        return true;
    }

    /**
     * 绘制标记的事件日子
     *
     * @param canvas   canvas
     * @param calendar 日历calendar
     * @param x        日历Card x起点坐标
     * @param y        日历Card y起点坐标
     */
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        mSchemeBasicPaint.setColor(calendar.getSchemeColor());

        // Log.e("onDrawScheme", calendar.getScheme() + ":" + x + ":" + y + ":" + mItemWidth + ":" + mItemHeight + ":" + mTextBaseLine);
          //mSchemePaint.setColor(Color.parseColor("#000000"));
         //canvas.drawRect(x,y,x+mItemWidth,mItemHeight+y,mSchemePaint);

        List<Calendar.Scheme> schemes = calendar.getSchemes();
        Log.e("onDrawScheme", schemes.size() + "");
        if (schemes == null || schemes.size() == 0) {
            return;
        }

        //每一项文字起始坐标
//        int cx = x + mItemWidth / 2;
//        int top = y - mItemHeight / 6;
//        String str=String.valueOf(calendar.getDay());
//        //mCurDayTextPaint.getW


//        int space = dipToPx(getContext(), 2);
        // int indexY = y + mItemHeight - 2 * space;
//        int sw = dipToPx(getContext(), mItemWidth / 10);
//        int sh = dipToPx(getContext(), 4);
//        for (Calendar.Scheme scheme : schemes) {
//            mSchemePaint.setColor(scheme.getShcemeColor());
//            canvas.drawRect(x + mItemWidth - sw - 2 * space, indexY - sh, x + mItemWidth - 2 * space, indexY, mSchemePaint);
//            indexY = indexY - space - sh;
//        }
        int space = dipToPx(getContext(), 2);
        int indexY = (int) (mTextBaseLine + y - dipToPx(getContext(), 20f));
        int sh = dipToPx(getContext(), 12);
        Paint.FontMetrics fontMetrics=taskPaint.getFontMetrics();
        float textBaseline = sh / 2 - fontMetrics.descent + (fontMetrics.bottom - fontMetrics.top) / 2;
        int textPadding = dipToPx(getContext(), 2);

        if (schemes.size() > 3) {
            int mRadio = dipToPx(getContext(), 2);
            int circleX = x + (mItemWidth / 3) + mRadio;
            int circleSpace = dipToPx(getContext(), 3);
            for (int i = 0; i < schemes.size(); i++) {
                if (i == 3) {
                    canvas.drawCircle(circleX, indexY+space, mRadio, circlePaint);
                    canvas.drawCircle(circleX+(mRadio*2)+circleSpace, indexY+space, mRadio, circlePaint);
                    canvas.drawCircle(circleX+(mRadio*4)+circleSpace*2, indexY+space, mRadio, circlePaint);
                    break;
                }
                Calendar.Scheme  scheme=  schemes.get(i);
                mSchemePaint.setColor(scheme.getShcemeColor());
                canvas.drawRect(x + 2 * space, indexY, x + mItemWidth - 2 * space, indexY+sh, mSchemePaint);
                if(scheme.getScheme().length()>5){
                    String str= scheme.getScheme().substring(0,4);
                    canvas.drawText(str,x + 2 * space+textPadding, indexY+textBaseline, taskPaint);
                }else{
                    canvas.drawText(scheme.getScheme(),x + 2 * space+textPadding, indexY+textBaseline, taskPaint);
                }

                indexY = indexY + space + sh;
              //  indexTextH = indexTextH + space +sh;


            }
        } else {
            for (Calendar.Scheme scheme : schemes) {
                mSchemePaint.setColor(scheme.getShcemeColor());
                canvas.drawRect(x + 2 * space, indexY + sh, x + mItemWidth - 2 * space, indexY, mSchemePaint);
                if(scheme.getScheme().length()>5){
                    String str= scheme.getScheme().substring(0,4);
                    canvas.drawText(str,x + 2 * space+textPadding, indexY+textBaseline, taskPaint);
                }else{
                    canvas.drawText(scheme.getScheme(),x + 2 * space+textPadding, indexY+textBaseline, taskPaint);
                }
                indexY = indexY + space + sh;
            }
        }

    }

    /**
     * 绘制文本
     *
     * @param canvas     canvas
     * @param calendar   日历calendar
     * @param x          日历Card x起点坐标
     * @param y          日历Card y起点坐标
     * @param hasScheme  是否是标记的日期
     * @param isSelected 是否选中
     */
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        canvas.drawRect(x, y, x + mItemWidth, y + mItemHeight, mRectPaint);

        int cx = x + mItemWidth / 2;
        int top = (y - mItemHeight / 6) - dipToPx(getContext(), 14f);
        boolean isInRange = isInRange(calendar);
        //canvas.drawText(String.valueOf(calendar.getDay()), cx, top, calendar.isCurrentDay() ? mCurDayTextPaint : calendar.isCurrentMonth() && isInRange ? mCurMonthTextPaint : mOtherMonthTextPaint);


        // Log.e("onDrawText", calendar.getDay()+":"+x + ":" + y + ":" + mItemWidth + ":" + mItemHeight + ":" + cx + ":" + top+ ":" + mTextBaseLine);
        // boolean isInRange = isInRange(calendar);

//        if (isSelected) {
//            mSelectTextPaint.setColor(selectTextPaintColor);
//            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
//                    mSelectTextPaint);
//        } else if (hasScheme) {
//            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
//                    calendar.isCurrentMonth() && isInRange ? mSchemeTextPaint : mOtherMonthTextPaint);
//        } else if(calendar.isCurrentDay()){
//            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, mCurrentTodayPaint);
//        }else{
//            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, calendar.isCurrentDay() ? mCurDayTextPaint : calendar.isCurrentMonth() && isInRange ? mCurMonthTextPaint : mOtherMonthTextPaint);
//        }

        if (isSelected) {
            mSelectTextPaint.setColor(selectTextPaintColor);
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    mSelectTextPaint);
        } else {
            if (calendar.isCurrentDay()) {
                canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, mCurrentTodayPaint);
            } else {
                canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, calendar.isCurrentDay() ? mCurDayTextPaint : calendar.isCurrentMonth() && isInRange ? mCurMonthTextPaint : mOtherMonthTextPaint);
            }
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
