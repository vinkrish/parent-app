package com.aanglearning.parentapp.dashboard.attendance;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.dashboard.service.SyncAttendanceIntentService;
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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 22-02-2017.
 */

public class AttendanceFragment extends Fragment implements AttendanceView {
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

    public static AttendanceFragment newInstance() {
        return new AttendanceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        childInfo = SharedPreferenceUtil.getProfile(getContext());
        getAttendance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.attendance_fragment, container, false);
        ButterKnife.bind(this, view);

        presenter = new AttendancePresenterImpl(this, new AttendanceInteractorImpl());

        changeDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDate();
            }
        });

        setDefaultDate();

        return view;
    }

    private void setDefaultDate() {
        dateView.setText(DateUtil.getDisplayFormattedDate(SharedPreferenceUtil.getAttendanceDate(getContext())));
    }

    private void changeDate() {
        SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = new Date();
        try {
            date = defaultFormat.parse(SharedPreferenceUtil.getAttendanceDate(getContext()));
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
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
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
            SharedPreferenceUtil.saveAttendanceDate(getContext(), date);
            getAttendance();
        }
    }

    private void getAttendance() {
        presenter.getAttendance(childInfo.getSectionId(), childInfo.getStudentId(),
                SharedPreferenceUtil.getAttendanceDate(getContext()));
    }

    private void checkAttendance(List<Attendance> attendanceList) {
        String attendanceType = "";
        String session1 = "";
        String session2 = "";
        for (Attendance att : attendanceList) {
            if (att.getType().equals("Daily")
                    && att.getStudentId() != childInfo.getStudentId()) {
                showDailyAttendance("Present");
                break;
            } else if (att.getType().equals("Daily")
                    && att.getTypeOfLeave().equals("Absent")) {
                showDailyAttendance("Absent");
                break;
            } else if (att.getType().equals("Session")) {
                attendanceType = "Session";
                if (att.getSession() == 0 && att.getStudentId() != childInfo.getStudentId()) {
                    session1 = "Present";
                } else if (att.getSession() == 1 && att.getStudentId() != childInfo.getStudentId()) {
                    session2 = "Present";
                } else if (att.getSession() == 0 && att.getTypeOfLeave().equals("Absent")) {
                    session1 = "Absent";
                } else if (att.getSession() == 1 && att.getTypeOfLeave().equals("Absent")) {
                    session2 = "Absent";
                }
            } else if (att.getType().equals("Period")) {
                attendanceType = "Period";
                break;
            }
        }
        if (attendanceType.equals("Session")) {
            showSessionAttendance(session1, session2);
        } else if (attendanceType.equals("Period")) {
            showPeriodAttendance(attendanceList);
        }
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
    public void showError() {

    }

    @Override
    public void showAPIError(String message) {

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

    private void showPeriodAttendance(List<Attendance> attendanceList) {

    }

    @Override
    public void showAttendance(List<Attendance> attendanceList) {
        resetLayout();
        checkAttendance(attendanceList);
    }

    @Override
    public void syncAttendance() {
        getActivity().startService(new Intent(getContext(), SyncAttendanceIntentService.class));
    }
}
