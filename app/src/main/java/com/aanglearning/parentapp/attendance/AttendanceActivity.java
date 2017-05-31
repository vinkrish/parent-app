package com.aanglearning.parentapp.attendance;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.service.SyncAttendanceIntentService;
import com.aanglearning.parentapp.model.Attendance;
import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.util.DatePickerFragment;
import com.aanglearning.parentapp.util.DateUtil;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AttendanceActivity extends AppCompatActivity implements AttendanceView{
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.progress) ProgressBar progressBar;
    @BindView(R.id.date_tv) TextView dateView;
    @BindView(R.id.change_btn) Button changeDateBtn;
    @BindView(R.id.penultimate_date) TextView validDateView;
    @BindView(R.id.attendance_daily) RelativeLayout dailyLayout;
    @BindView(R.id.attendance_session) RelativeLayout sessionLayout;
    @BindView(R.id.attendance_period) RelativeLayout periodLayout;
    @BindView(R.id.attendance_daily_tv) TextView attDailyTv;
    @BindView(R.id.attendance_session1_tv) TextView attSession1Tv;
    @BindView(R.id.attendance_session2_tv) TextView attSession2Tv;
    @BindView(R.id.tableLayout) RelativeLayout tableLayout;

    private AttendancePresenter presenter;
    private ChildInfo childInfo;
    private TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new AttendancePresenterImpl(this, new AttendanceInteractorImpl());

        changeDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDate();
            }
        });

        setDefaultDate();

        table = new TableLayout(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        childInfo = SharedPreferenceUtil.getProfile(this);
        getAttendance();
    }

    private void setDefaultDate() {
        dateView.setText(DateUtil.getDisplayFormattedDate(SharedPreferenceUtil.getAttendanceDate(this)));
    }

    private void changeDate() {
        SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = new Date();
        try {
            date = defaultFormat.parse(SharedPreferenceUtil.getAttendanceDate(this));
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
        bundle.putInt("year", year);
        bundle.putInt("month", month);
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

            Calendar tomorrowCal = Calendar.getInstance();
            Date tomorrowDate = tomorrowCal.getTime();

            if (date.after(tomorrowDate)) {
                isValidTargetDate(true, "");
            } else {
                dateView.setText(DateUtil.getDisplayFormattedDate(dateFormat.format(date)));
                isValidTargetDate(false, dateFormat.format(date));
            }
        }
    };

    private void isValidTargetDate(boolean visibile, String date) {
        if (visibile) {
            validDateView.setVisibility(View.VISIBLE);
            validDateView.setText(getResources().getText(R.string.future_date));
        } else {
            if (validDateView != null && validDateView.getVisibility() == View.VISIBLE) {
                validDateView.setVisibility(View.GONE);
            }
            SharedPreferenceUtil.saveAttendanceDate(this, date);
            getAttendance();
        }
    }

    private void getAttendance() {
        presenter.getAttendance(childInfo.getSectionId(), childInfo.getStudentId(),
                SharedPreferenceUtil.getAttendanceDate(this));
    }

    private void checkAttendance(List<Attendance> attendanceList) {
        String attendanceType = "";
        String session0 = "-";
        String session1 = "-";
        TreeMap<Integer, String> map = new TreeMap<>();
        for (Attendance att : attendanceList) {
            switch (att.getType()) {
                case "Daily":
                    attendanceType = "Daily";
                    if (att.getStudentId() != childInfo.getStudentId()
                            && !session0.equals("Absent")) {
                        session0 = "Present";
                    } else if (att.getStudentId() == childInfo.getStudentId()
                            && att.getTypeOfLeave().equals("Absent")) {
                        session0 = "Absent";
                    }
                    break;
                case "Session":
                    attendanceType = "Session";
                    if (att.getSession() == 0
                            && att.getStudentId() != childInfo.getStudentId()
                            && !session0.equals("Absent")) {
                        session0 = "Present";
                    } else if (att.getSession() == 1
                            && att.getStudentId() != childInfo.getStudentId()
                            && !session1.equals("Absent")) {
                        session1 = "Present";
                    } else if (att.getSession() == 0
                            && att.getStudentId() == childInfo.getStudentId()
                            && att.getTypeOfLeave().equals("Absent")) {
                        session0 = "Absent";
                    } else if (att.getSession() == 1
                            && att.getStudentId() == childInfo.getStudentId()
                            && att.getTypeOfLeave().equals("Absent")) {
                        session1 = "Absent";
                    }
                    break;
                case "Period":
                    attendanceType = "Period";
                    if (att.getStudentId() != childInfo.getStudentId()
                            && !session0.equals("Absent")) {
                        map.put(att.getSession(), "Present");
                    } else if (att.getStudentId() == childInfo.getStudentId()
                            && att.getTypeOfLeave().equals("Absent")) {
                        map.put(att.getSession(), "Absent");
                    }
                    break;
                default:
                    break;
            }
        }
        if (attendanceType.equals("Daily")) {
            showDailyAttendance(session0);
        } else if (attendanceType.equals("Session")) {
            showSessionAttendance(session0, session1);
        } else if (attendanceType.equals("Period")) {
            showPeriodAttendance(map);
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgess() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {

    }

    private void resetLayout() {
        dailyLayout.setVisibility(View.GONE);
        sessionLayout.setVisibility(View.GONE);
        periodLayout.setVisibility(View.GONE);
    }

    private void showDailyAttendance(String attendance) {
        dailyLayout.setVisibility(View.VISIBLE);
        attDailyTv.setText(attendance);
    }

    private void showSessionAttendance(String session1, String session2) {
        sessionLayout.setVisibility(View.VISIBLE);
        attSession1Tv.setText(session1);
        attSession2Tv.setText(session2);
    }

    private void showPeriodAttendance(final TreeMap<Integer, String> map) {
        periodLayout.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        table.removeAllViews();
                        tableLayout.removeAllViews();
                        TableRow tableHeaderRow = generateHeaderRow(map);
                        table.addView(tableHeaderRow);
                        TableRow tableRow = generateRow(map);
                        table.addView(tableRow);
                        tableLayout.addView(table);
                    }
                });
            }
        }).start();

    }

    private TableRow generateHeaderRow(TreeMap<Integer, String> map) {
        int id = 1234567;
        TableRow tableRowForTable = new TableRow(this);
        TableRow.LayoutParams param = new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        LinearLayout verticalLayout = new LinearLayout(this);
        verticalLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout horizontalLayout = new LinearLayout(this);
        horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);

        int width =  getResources().getDisplayMetrics().widthPixels / map.size();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);

        for (Map.Entry<Integer, String> att : map.entrySet()) {
            TextView tv = new TextView(this);
            tv.setId(id++);
            tv.setLayoutParams(params);
            tv.setText(att.getKey().toString());
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(0, 10, 0, 10);
            tv.setTextSize(18);
            tv.setTextColor(ContextCompat.getColor(this, R.color.primaryTextColor));
            horizontalLayout.addView(tv);
            View verticalBorder = new View(this);
            verticalBorder.setBackgroundColor(ContextCompat.getColor(this, R.color.dividerColor));
            LinearLayout.LayoutParams vlp = new LinearLayout.LayoutParams(2, LinearLayout.LayoutParams.MATCH_PARENT);
            verticalBorder.setLayoutParams(vlp);
            verticalBorder.setId(id++);
            horizontalLayout.addView(verticalBorder);
        }

        verticalLayout.addView(horizontalLayout);
        View horizontalBorder = new View(this);
        horizontalBorder.setBackgroundColor(ContextCompat.getColor(this, R.color.dividerColor));
        LinearLayout.LayoutParams hlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
        horizontalBorder.setLayoutParams(hlp);
        verticalLayout.addView(horizontalBorder);

        tableRowForTable.addView(verticalLayout, param);

        return tableRowForTable;
    }

    private TableRow generateRow(TreeMap<Integer, String> map) {
        int id = 123456;

        TableRow tableRowForTable = new TableRow(this);
        TableRow.LayoutParams param = new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        LinearLayout verticalLayout = new LinearLayout(this);
        verticalLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout horizontalLayout = new LinearLayout(this);
        horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);

        int width =  getResources().getDisplayMetrics().widthPixels / map.size();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);

        for (Map.Entry<Integer, String> att : map.entrySet()) {
            TextView tv = new TextView(this);
            tv.setId(id++);
            tv.setLayoutParams(params);
            tv.setText(att.getValue());
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(18);
            tv.setPadding(0, 10, 0, 10);
            tv.setTextColor(ContextCompat.getColor(this, R.color.primaryTextColor));
            horizontalLayout.addView(tv);
            View verticalBorder = new View(this);
            verticalBorder.setBackgroundColor(ContextCompat.getColor(this, R.color.dividerColor));
            LinearLayout.LayoutParams vlp = new LinearLayout.LayoutParams(2, LinearLayout.LayoutParams.MATCH_PARENT);
            verticalBorder.setLayoutParams(vlp);
            verticalBorder.setId(id++);
            horizontalLayout.addView(verticalBorder);
        }

        verticalLayout.addView(horizontalLayout);
        View horizontalBorder = new View(this);
        horizontalBorder.setBackgroundColor(ContextCompat.getColor(this, R.color.dividerColor));
        LinearLayout.LayoutParams hlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
        horizontalBorder.setLayoutParams(hlp);
        verticalLayout.addView(horizontalBorder);

        tableRowForTable.addView(verticalLayout, param);

        return tableRowForTable;
    }

    @Override
    public void showAttendance(List<Attendance> attendanceList) {
        resetLayout();
        checkAttendance(attendanceList);
    }

    public void syncAttendance() {
        startService(new Intent(this, SyncAttendanceIntentService.class));
    }

}
