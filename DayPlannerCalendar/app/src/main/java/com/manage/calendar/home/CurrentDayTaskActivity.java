package com.manage.calendar.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.dylanc.loadingstateview.LoadingStateView;
import com.gyf.immersionbar.ImmersionBar;
import com.manage.calendar.R;
import com.manage.calendar.adapter.CurrentDayTaskAdapter;
import com.manage.calendar.base.BaseActivity;
import com.manage.calendar.bean.SelectDate;
import com.manage.calendar.calendar.CalendarEvent;
import com.manage.calendar.calendar.CalendarProviderManager;
import com.manage.calendar.databinding.ActivityCurrentDayTaskBinding;
import com.manage.calendar.utils.ClickDelay;
import com.manage.calendar.widget.PlaceholderViewDelegate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CurrentDayTaskActivity extends BaseActivity<ActivityCurrentDayTaskBinding> implements View.OnClickListener {
    //当前日期
    private Date currentDate;

    //切换过后的日期
    private Date otherDate;
    private SelectDate selectDate;
    private int[] momths;
    private String[] weeks;
    private SimpleDateFormat taskSdf = new SimpleDateFormat("HH:mm");
    private CurrentDayTaskAdapter currentDayTaskAdapter;

    private boolean isfirst = true;


    private int year;
    private int month;
    private int day;
    private LoadingStateView loadingStateView;

    private ActivityResultLauncher<String[]> permission;

    @Override
    public void initParamConfig() {
        setloadingState(false);
        setTopBarView(false);
        setHideTitleBar(false);
        super.initParamConfig();

    }

    @Override
    public ActivityCurrentDayTaskBinding setViewBinding() {
        return ActivityCurrentDayTaskBinding.inflate(layoutInflater);
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        initViewStatus();
        loadingStateView = new LoadingStateView(mBinding.rvTask);
        loadingStateView.register(new PlaceholderViewDelegate());
        loadingStateView.showContentView();
        mBinding.ivPreDate.setOnClickListener(this);
        mBinding.ivNextDate.setOnClickListener(this);

        currentDayTaskAdapter = new CurrentDayTaskAdapter(mContext, R.layout.item_current_day_task);
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(mContext, R.drawable.shape_divider_v_10dp)));
        mBinding.rvTask.addItemDecoration(divider);
        mBinding.rvTask.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvTask.setAdapter(currentDayTaskAdapter);
        currentDayTaskAdapter.addChildClickViewIds(R.id.actv_delete_task);
        currentDayTaskAdapter.addChildClickViewIds(R.id.item_root);
        currentDayTaskAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (ClickDelay.isFastClick()) {
                    if (view.getId() == R.id.actv_delete_task) {
                        //val entity = adapter.getItem(position) as InstallAppEntity
                        CalendarEvent calendarEvent = currentDayTaskAdapter.getData().get(position);
                        CalendarProviderManager.deleteCalendarEvent(mContext, calendarEvent.getId());
                        currentDayTaskAdapter.remove(position);
//                        currentDayTaskAdapter.notifyItemChanged(position);
                    }
                }
            }
        });

        currentDayTaskAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CalendarEvent calendarEvent = currentDayTaskAdapter.getData().get(position);
                CalendarProviderManager.startCalendarForIntentToEdit(mContext, calendarEvent.getId(), calendarEvent.getStart(), calendarEvent.getEnd());
            }
        });

        mBinding.acivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mBinding.addSchedule.setOnClickListener(v -> {
            if (ClickDelay.isFastClick()) {
                checkPermission();
            }
        });


        permission = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                if (result.get(Manifest.permission.WRITE_CALENDAR) != null && result.get(Manifest.permission.READ_CALENDAR) != null) {
                    if (Objects.requireNonNull(result.get(Manifest.permission.WRITE_CALENDAR)).equals(true)
                            && Objects.requireNonNull(result.get(Manifest.permission.READ_CALENDAR)).equals(true)) {
                        CalendarProviderManager.startCalendarForIntentToInsert(mContext, System.currentTimeMillis(),
                                System.currentTimeMillis() + 60000, "", "", "",
                                false);
                    } else {
                        //跳转权限设置界面
                    }
                }
            }
        });
    }


    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
//                     ActivityCompat.requestPermissions(this,
//                             new String[]{android.Manifest.permission.WRITE_CALENDAR,
//                                     Manifest.permission.READ_CALENDAR}, 1);
            permission.launch(new String[]{Manifest.permission.WRITE_CALENDAR,
                    Manifest.permission.READ_CALENDAR});
        } else {
            CalendarProviderManager.startCalendarForIntentToInsert(this, System.currentTimeMillis(),
                    System.currentTimeMillis() + 60000, null, null, null,
                    false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isfirst) {
            queryEvent(year, month, day);
        }

        isfirst = false;
    }

    @Override
    public void onInitData() {
        if (getIntent() != null) {
            selectDate = getIntent().getParcelableExtra("selectDate");
        }
        year = selectDate.getYear();
        month = selectDate.getMonth();
        day = selectDate.getDay();
        Log.e("selectDate", selectDate.getYear() + "---" + selectDate.getMonth() + "----" + selectDate.getDay());
        queryEvent(year, month, day);
    }


    private void queryEvent(int year, int month, int day) {

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(year, month - 1, day, 0, 0);

        long startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(year, month - 1, day, 24, 0);
        long endMillis = endTime.getTimeInMillis();

        momths = mContext.getResources().getIntArray(R.array.month);
        weeks = mContext.getResources().getStringArray(R.array.week);

        int ss = momths[month - 1];
        int way = beginTime.get(Calendar.DAY_OF_WEEK);
        mBinding.actvYear.setText(ss + "/" + day + "(" + weeks[way - 1] + ")");

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//
//        Date data1 = new Date(startMillis);
//        Date end = new Date(endMillis);
//
//        String time1 = sdf.format(data1);
//        String endtime = sdf.format(end);
//        String endtime111 = sdf.format(new Date(1681905420000l));
//
//        Log.e("startMillis", startMillis + "");
//        Log.e("endMillis", endMillis + "");
//        Log.e("time1", time1);
//        Log.e("endtime", endtime);
//        Log.e("endtime111", endtime111);
        // 查询事件
        long calID4 = CalendarProviderManager.obtainCalendarAccountID(this);
        List<CalendarEvent> events4 = CalendarProviderManager.queryAccountEvent(this, calID4, startMillis, endMillis);
        StringBuilder stringBuilder4 = new StringBuilder();
        if (null != events4) {
            for (CalendarEvent event : events4) {
                if (event.getStart() < System.currentTimeMillis()) {
                    event.setExpire(true);
                }
                String time = taskSdf.format(new Date(event.getStart()));
                event.setTime(time);
                Log.e("time", time);
                //stringBuilder4.append(events4.toString()).append("\n");
            }


            currentDayTaskAdapter.setNewData(events4);
            Log.e("events4", stringBuilder4.toString());
            //tvEvent.setText(stringBuilder4.toString());
            // Toast.makeText(this, "查询成功", Toast.LENGTH_SHORT).show();
            if (events4.size() > 0) {
                loadingStateView.showContentView();
            } else {
                loadingStateView.showEmptyView();
            }

        } else {
            loadingStateView.showEmptyView();
            // Toast.makeText(this, "查询失败", Toast.LENGTH_SHORT).show();
        }
    }


    private void preAndNext(int nextDayCount, int preDayCount, boolean next) {
        Log.e("preAndNext", nextDayCount + "--" + preDayCount);
        year = selectDate.getYear();
        month = selectDate.getMonth();
        day = selectDate.getDay();
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        currentDate = cal.getTime();
        if (otherDate != null && currentDate.compareTo(otherDate) != 0) {
            cal.setTime(otherDate);
        } else {
            cal.setTime(currentDate);
        }

        if (next) {
            cal.add(Calendar.DATE, nextDayCount);
        } else {
            cal.add(Calendar.DATE, preDayCount);
        }

        otherDate = cal.getTime();

        if (otherDate != null) {
            //initPageData(otherDate);
//            actv_year

            int cuurentYear = cal.get(Calendar.YEAR);
            int cuurentMonth = cal.get(Calendar.MONTH) + 1;
            int cuurentDay = cal.get(Calendar.DATE);

            int ss = momths[cuurentMonth - 1];
            int way = cal.get(Calendar.DAY_OF_WEEK);
            mBinding.actvYear.setText(ss + "/" + cuurentDay + "(" + weeks[way - 1] + ")");


            // Log.e("selectDate", selectDate.getYear() + "---" + selectDate.getMonth() + "----" + selectDate.getDay());
            queryEvent(cuurentYear, cuurentMonth, cuurentDay);
        }

    }

    @Override
    public void onClick(View v) {
        if (ClickDelay.isFastClick()) {
            switch (v.getId()) {
                case R.id.iv_pre_date:
                    preDay();
                    break;
                case R.id.iv_next_date:
                    nextDay();
                    break;
            }
        }
    }

    private void preDay() {

        preAndNext(1, -1, false);
    }

    private void nextDay() {

        preAndNext(1, -1, true);
    }

    private void initViewStatus() {
        int statusBarHeight = ImmersionBar.getStatusBarHeight(activity);
        ViewGroup.LayoutParams lp = mBinding.viewStatus.getLayoutParams();
        lp.height = statusBarHeight;
        mBinding.viewStatus.setLayoutParams(lp);
    }
}
