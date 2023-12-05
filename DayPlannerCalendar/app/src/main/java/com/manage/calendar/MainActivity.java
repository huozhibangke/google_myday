package com.manage.calendar;


import static autodispose2.AutoDispose.autoDisposable;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.gyf.immersionbar.ImmersionBar;

import com.manage.calendar.adapter.DrawerAdapter;
import com.manage.calendar.base.BaseActivity;
import com.manage.calendar.bean.DrawerBean;
import com.manage.calendar.bean.SelectDate;
import com.manage.calendar.calendar.CalendarEvent;
import com.manage.calendar.calendar.CalendarProviderManager;
import com.manage.calendar.databinding.ActivityMainBinding;
import com.manage.calendar.home.CurrentDayTaskActivity;
import com.manage.calendar.sys.PrivacyActivity;
import com.manage.calendar.sys.SetLanguageActivity;
import com.manage.calendar.sys.SuggestionActivity;
import com.manage.calendar.utils.ClickDelay;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private DrawerAdapter drawerAdapter;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private ActivityResultLauncher<String[]> permission;
    private int year;
    private int month;

    private int day;
    private Date otherDate;

    private Date currentDate;

    private int[] momths;
    private int check;

    @Override
    public void initParamConfig() {
        setloadingState(false);
        setTopBarView(false);
        setHideTitleBar(false);
        super.initParamConfig();
    }

    @Override
    public ActivityMainBinding setViewBinding() {
        return ActivityMainBinding.inflate(layoutInflater);
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        initViewStatus();
        drawerAdapter = new DrawerAdapter(R.layout.drawer_rv_menu_item);
        drawerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (ClickDelay.isFastClick()) {
                    Log.e("onItemClick", position + "");
                    goFunUI(adapter, view, position);
                }

            }
        });
        mBinding.menuDrawer.drawerRv.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.menuDrawer.drawerRv.setAdapter(drawerAdapter);

        mBinding.appBarMain.acivTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mBinding.drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    mBinding.drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });
//        add_schedule
        mBinding.appBarMain.addSchedule.setOnClickListener(v -> {
            if (ClickDelay.isFastClick()) {

                check = 1;
                checkPermission();

            }
        });

        permission = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                if (result.get(Manifest.permission.WRITE_CALENDAR) != null && result.get(Manifest.permission.READ_CALENDAR) != null) {
                    if (Objects.requireNonNull(result.get(Manifest.permission.WRITE_CALENDAR)).equals(true)
                            && Objects.requireNonNull(result.get(Manifest.permission.READ_CALENDAR)).equals(true)) {
                        if (check == 1) {
                            CalendarProviderManager.startCalendarForIntentToInsert(MainActivity.this, System.currentTimeMillis(),
                                    System.currentTimeMillis() + 60000, "", "", "",
                                    false);
                        } else if (check == 2) {
                            queryAllEvent();
                        }

                    } else {
                        //跳转权限设置界面
                    }
                }
            }
        });

        mBinding.appBarMain.calendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(Calendar calendar) {

            }

            @Override
            public void onCalendarSelect(Calendar calendar, boolean isClick) {
                if (isClick && ClickDelay.isFastClick()) {
                    int year = calendar.getYear();
                    int month = calendar.getMonth();
                    int day = calendar.getDay();
                    SelectDate selectDate = new SelectDate(year, month, day);
                    Intent intent = new Intent(mContext, CurrentDayTaskActivity.class);
                    intent.putExtra("selectDate", selectDate);
                    startActivity(intent);
                }

            }
        });
        mBinding.appBarMain.calendarView.setOnYearChangeListener(new CalendarView.OnYearChangeListener() {
            @Override
            public void onYearChange(int year) {

                mBinding.appBarMain.actvYear.setText(year + "");
            }

        });

        mBinding.appBarMain.calendarView.setOnMonthChangeListener(new CalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                mBinding.appBarMain.actvMonth.setText(month + "");
                mBinding.appBarMain.actvYear.setText(year + "");
            }
        });

        mBinding.appBarMain.selectYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickDelay.isFastClick()) {
                    if (mBinding.appBarMain.calendarView.isYearSelectLayoutVisible()) {
                        mBinding.appBarMain.calendarView.closeYearSelectLayout();
                    } else {
                        mBinding.appBarMain.calendarView.showYearSelectLayout(mBinding.appBarMain.calendarView.getCurYear());
                    }
                }
            }
        });

        mBinding.appBarMain.acivPreDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickDelay.isFastClick()) {
                    preDay();
                }
            }
        });

        mBinding.appBarMain.acivNextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextDay();
            }
        });
        momths = mContext.getResources().getIntArray(R.array.month);
    }

    @Override
    public void onInitData() {
        drawerAdapter.setNewData(initDarwerData());
        year = mBinding.appBarMain.calendarView.getCurYear();
        month = mBinding.appBarMain.calendarView.getCurMonth();
        mBinding.appBarMain.actvMonth.setText(month + "");
        mBinding.appBarMain.actvYear.setText(year + "");


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            check = 2;
            permission.launch(new String[]{Manifest.permission.WRITE_CALENDAR,
                    Manifest.permission.READ_CALENDAR});
        } else {
            queryAllEvent();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==12301){
            queryAllEvent();
        }
    }

    private void queryAllEvent() {
        Observable.create(new ObservableOnSubscribe<List<CalendarEvent>>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<List<CalendarEvent>> emitter) throws Throwable {
                        long calID4 = CalendarProviderManager.obtainCalendarAccountID(mContext);
                        List<CalendarEvent> allEvent = CalendarProviderManager.queryAccountAllEvent(mContext, calID4);
                        if (allEvent != null && allEvent.size() > 0) {
                            emitter.onNext(allEvent);
                        } else {
                            emitter.onNext(new ArrayList<>());
                        }

                    }
                }).map(new Function<List<CalendarEvent>, HashMap<String, List<CalendarEvent>>>() {
                    @Override
                    public HashMap<String, List<CalendarEvent>> apply(List<CalendarEvent> calendarEvents) throws Throwable {
                        HashMap<String, List<CalendarEvent>> map = new HashMap<>();
                        for (CalendarEvent calendarEvent : calendarEvents) {
                            String time = sdf.format(new Date(calendarEvent.getStart()));
                            List<CalendarEvent> tmpList = map.get(time);
                            if (tmpList == null) {
                                tmpList = new ArrayList<>();
                                tmpList.add(calendarEvent);
                                map.put(time, tmpList);
                            } else {
                                tmpList.add(calendarEvent);
                            }
                        }
                        return map;
                    }
                }).flatMap(new Function<HashMap<String, List<CalendarEvent>>, ObservableSource<Map<String, Calendar>>>() {
                    @Override
                    public ObservableSource<Map<String, Calendar>> apply(HashMap<String, List<CalendarEvent>> stringListHashMap) throws Throwable {
                        Map<String, Calendar> map = new HashMap<>();
                        for (Map.Entry<String, List<CalendarEvent>> entry : stringListHashMap.entrySet()) {
                            String time = entry.getKey();
                            String[] date = time.split("-");
                            List<CalendarEvent> list = entry.getValue();
                            Calendar calendar = getSchemeCalendar(Integer.valueOf(date[0]), Integer.valueOf(date[1]), Integer.valueOf(date[2]), 0xFFFFFF00, list);
                            map.put(calendar.toString(), calendar);
                        }
                        return Observable.just(map);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .to(autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle()))).subscribe(new Consumer<Map<String, Calendar>>() {
                    @Override
                    public void accept(Map<String, Calendar> stringCalendarMap) throws Throwable {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            stringCalendarMap.forEach(new BiConsumer<String, Calendar>() {
                                @Override
                                public void accept(String s, Calendar calendar) {
                                    Log.e("-main-", "s===>" + s + "calendar===>" + calendar.toString());
                                }
                            });
                        }
                        mBinding.appBarMain.calendarView.setSchemeDate(stringCalendarMap);
                    }
                });

    }


    private Calendar getSchemeCalendar(int year, int month, int day, int color, List<CalendarEvent> events) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme("");
        for (int i = 0; i < events.size(); i++) {
            if (i == 4) {
                break;
            }

            CalendarEvent calendarEvent = events.get(i);
            if (calendarEvent.getStart() < System.currentTimeMillis()) {
                calendar.addScheme(0xFFCDCDCD, events.get(i).getTitle());
            } else {
                calendar.addScheme(0xFF51ABF0, events.get(i).getTitle());
            }

        }
        return calendar;
    }


//    public  <T> void requestNet(Lifecycle lifecycle, Observable<T> observable, Observer<T> callback){
//        observable.onErrorResumeNext(new HttpResponseFunc<T>())
//                .retryWhen(new RetryWithDelay())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .as(autoDisposable(AndroidLifecycleScopeProvider.from(lifecycle)))
//                .subscribe(callback);
//    }

    private void goFunUI(BaseQuickAdapter adapter, View view, int position) {
        List<DrawerBean> drawerBean = drawerAdapter.getData();
        DrawerBean drawerBean1 = drawerBean.get(position);
        mBinding.drawerLayout.closeDrawer(Gravity.LEFT);
        switch (drawerBean1.getType()) {
            case 1:
                Intent intent = new Intent(mContext, SetLanguageActivity.class);
                startActivity(intent);
                break;
            case 2:
                try {
                    Intent size = new Intent(Settings.ACTION_DISPLAY_SETTINGS);
                    startActivity(size);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                Intent su = new Intent(mContext, SuggestionActivity.class);
                startActivity(su);

                break;
            case 4:
                Intent privacy = new Intent(mContext, PrivacyActivity.class);
                startActivity(privacy);
                break;
        }


    }

    private List<DrawerBean> initDarwerData() {
        List<DrawerBean> list = new ArrayList<>();
//        list.add(new DrawerBean(R.drawable.ic_menu_manager_activies, getString(R.string.menu_manager_activies), 1));
//        list.add(new DrawerBean(R.drawable.ic_menu_schedule_tip, getString(R.string.menu_schedule_tip), 2));
        list.add(new DrawerBean(R.drawable.ic_menu_language, getString(R.string.menu_language), 1));
        list.add(new DrawerBean(R.drawable.ic_menu_font, getString(R.string.menu_font), 2));
        list.add(new DrawerBean(R.drawable.ic_menu_su_fd, getString(R.string.menu_feedback), 3));
        list.add(new DrawerBean(R.drawable.ic_menu_privacy, getString(R.string.menu_privacy), 4));
        return list;
    }

    private void initViewStatus() {
        int statusBarHeight = ImmersionBar.getStatusBarHeight(activity);
        ViewGroup.LayoutParams lp = mBinding.appBarMain.viewStatus.getLayoutParams();
        lp.height = statusBarHeight;
        mBinding.appBarMain.viewStatus.setLayoutParams(lp);

        ViewGroup.LayoutParams lp1 = mBinding.menuDrawer.viewStatus.getLayoutParams();
        lp1.height = statusBarHeight;
        mBinding.menuDrawer.viewStatus.setLayoutParams(lp1);

//        mBinding.appBarMain.llTitle.setBackgroundColor(
//            ContextCompat.getColor(
//                mContext,
//                R.color.white
//            )
//        )

        // mBinding.drawerLayout.setScrimColor(Color.TRANSPARENT)
    }

    private void preDay() {

        preAndNext(1, -1, false);
    }

    private void nextDay() {

        preAndNext(1, -1, true);
    }

    private void preAndNext(int nextDayCount, int preDayCount, boolean next) {
        Log.e("preAndNext", nextDayCount + "--" + preDayCount);
        year = mBinding.appBarMain.calendarView.getCurYear();
        month = mBinding.appBarMain.calendarView.getCurMonth();
        day = mBinding.appBarMain.calendarView.getCurDay();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(year, month, day);
        currentDate = cal.getTime();
        if (otherDate != null && currentDate.compareTo(otherDate) != 0) {
            cal.setTime(otherDate);
        } else {
            cal.setTime(currentDate);
        }

        if (next) {
            cal.add(java.util.Calendar.MONTH, nextDayCount);
        } else {
            cal.add(java.util.Calendar.MONTH, preDayCount);
        }

        otherDate = cal.getTime();

        if (otherDate != null) {
            int cuurentYear = cal.get(java.util.Calendar.YEAR);
            int cuurentMonth = cal.get(java.util.Calendar.MONTH);
            int cuurentDay = cal.get(java.util.Calendar.DATE);
//            Log.e("cuurentMonth",cuurentMonth+"");
//            int ss = momths[cuurentMonth-1];
//            Log.e("actvMonth",ss+"");
//            mBinding.appBarMain.actvMonth.setText(ss + "");
//            mBinding.appBarMain.actvYear.setText(cuurentYear + "");
            mBinding.appBarMain.calendarView.scrollToCalendar(cuurentYear, cuurentMonth, cuurentDay, true);

        }

    }
}