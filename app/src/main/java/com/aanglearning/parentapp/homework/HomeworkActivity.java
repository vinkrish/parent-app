package com.aanglearning.parentapp.homework;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aanglearning.parentapp.BaseActivity;
import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.dao.HomeworkDao;
import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.model.Homework;
import com.aanglearning.parentapp.util.DatePickerFragment;
import com.aanglearning.parentapp.util.DateUtil;
import com.aanglearning.parentapp.util.NetworkUtil;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;

import org.joda.time.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeworkActivity extends BaseActivity implements HomeworkView {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.date_tv) TextView dateView;
    @BindView(R.id.change_btn) Button changeDateBtn;
    @BindView(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.noHomework) LinearLayout noHomework;

    private HomeworkAdapter adapter;
    private HomeworkPresenter presenter;
    private ChildInfo childInfo;
    private String homeworkDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        childInfo = SharedPreferenceUtil.getProfile(this);

        presenter = new HomeworkPresenterImpl(this, new HomeworkInteractorImpl());

        adapter = new HomeworkAdapter(this, new ArrayList<HomeworkViewObj>(0));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        changeDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDate();
            }
        });

        setDefaultDate();

        setNavigationItem(2);

        if(NetworkUtil.isNetworkAvailable(this)) {
            getHomework();
        } else {
            showOfflineData();
        }

        refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimaryDark)
        );

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHomework();
            }
        });
    }

    private void showOfflineData() {
        List<Homework> homeworks = HomeworkDao.getHomework(childInfo.getSectionId(), homeworkDate);
        if(homeworks.size() == 0) {
            noHomework.setVisibility(View.VISIBLE);
        } else {
            noHomework.setVisibility(View.INVISIBLE);
            showHomework(homeworks);
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void getHomework() {
        recyclerView.setAdapter(null);
        presenter.getHomeworks(childInfo.getSectionId(), homeworkDate);
    }

    private void setDefaultDate() {
        homeworkDate = new LocalDate().toString();
        dateView.setText(DateUtil.getDisplayFormattedDate(homeworkDate));
    }

    private void changeDate() {
        SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = new Date();
        try {
            date = defaultFormat.parse(homeworkDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerFragment newFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("year",year);
        bundle.putInt("month",month);
        bundle.putInt("day", day);
        newFragment.setCallBack(ondate);
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            Calendar cal = Calendar.getInstance();
            cal.set(year, monthOfYear, dayOfMonth);
            Date date = cal.getTime();

            dateView.setText(DateUtil.getDisplayFormattedDate(dateFormat.format(date)));
            homeworkDate = dateFormat.format(date);
            getHomework();
        }
    };

    @Override
    public void showHomework(List<Homework> homeworkList) {
        if(homeworkList.size() == 0) {
            noHomework.setVisibility(View.VISIBLE);
        } else {
            noHomework.setVisibility(View.INVISIBLE);
            List<HomeworkViewObj> objects = new ArrayList<>();
            for(Homework homework: homeworkList) {
                HomeworkViewObj obj = new HomeworkViewObj(homework.getSubjectName(),
                        Collections.singletonList("- " + homework.getHomeworkMessage()));
                objects.add(obj);
            }
            adapter = new HomeworkAdapter(this, objects);
            recyclerView.setAdapter(adapter);
            backupHomework(homeworkList);
        }

        refreshLayout.setRefreshing(false);
    }

    private void backupHomework(final List<Homework> homeworks) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HomeworkDao.delete(childInfo.getSectionId(), homeworkDate);
                HomeworkDao.insert(homeworks);
            }
        }).start();
    }

    @Override
    public void showProgress() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgess() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String message) {
        showSnackbar(message);
        showOfflineData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
