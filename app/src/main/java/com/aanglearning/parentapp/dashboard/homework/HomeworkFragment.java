package com.aanglearning.parentapp.dashboard.homework;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.dashboard.service.SyncHomeworkIntentService;
import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.model.Homework;
import com.aanglearning.parentapp.util.DatePickerFragment;
import com.aanglearning.parentapp.util.DateUtil;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;

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

/**
 * Created by Vinay on 22-02-2017.
 */

public class HomeworkFragment extends Fragment implements HomeworkView {
    @BindView(R.id.progress) ProgressBar progressBar;
    @BindView(R.id.date_tv) TextView dateView;
    @BindView(R.id.change_btn) Button changeDateBtn;
    @BindView(R.id.penultimate_date) TextView validDateView;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;

    private HomeworkAdapter adapter;
    private HomeworkPresenter presenter;
    private ChildInfo childInfo;

    public static HomeworkFragment newInstance() {
        return new HomeworkFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new HomeworkAdapter(getActivity(), new ArrayList<HomeworkViewObj>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homework_fragment, container, false);
        ButterKnife.bind(this, view);

        presenter = new HomeworkPresenterImpl(this, new HomeworkInteractorImpl());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        changeDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDate();
            }
        });

        setDefaultDate();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        childInfo = SharedPreferenceUtil.getProfile(getContext());
        getHomework();
    }

    private void getHomework() {
        presenter.getHomeworks(childInfo.getSectionId(),
                SharedPreferenceUtil.getHomeworkDate(getContext()));
    }

    private void setDefaultDate() {
        dateView.setText(DateUtil.getDisplayFormattedDate(SharedPreferenceUtil.getHomeworkDate(getContext())));
    }

    private void changeDate() {
        SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = new Date();
        try {
            date = defaultFormat.parse(SharedPreferenceUtil.getHomeworkDate(getContext()));
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
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            SimpleDateFormat displayFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            Calendar cal = Calendar.getInstance();
            cal.set(year, monthOfYear, dayOfMonth);
            Date date = cal.getTime();

            Calendar tomorrowCal = Calendar.getInstance();
            //tomorrowCal.add(Calendar.DATE, 1);
            Date tomorrowDate = tomorrowCal.getTime();

            if(date.after(tomorrowDate)) {
                isValidTargetDate(true, "");
            } else {
                dateView.setText(DateUtil.getDisplayFormattedDate(dateFormat.format(date)));
                isValidTargetDate(false, dateFormat.format(date));
            }
        }
    };

    private void isValidTargetDate(boolean visibile, String date){
        if(visibile){
            validDateView.setVisibility(View.VISIBLE);
            validDateView.setText(getResources().getText(R.string.future_date));
        } else {
            if (validDateView != null && validDateView.getVisibility() == View.VISIBLE) {
                validDateView.setVisibility(View.GONE);
            }
            SharedPreferenceUtil.saveHomeworkDate(getContext(), date);
            getHomework();
        }
    }

    @Override
    public void showHomework(List<Homework> homeworkList) {
        List<HomeworkViewObj> objects = new ArrayList<>();
        for(Homework homework: homeworkList) {
            HomeworkViewObj obj = new HomeworkViewObj(homework.getSubjectName(),
                    Collections.singletonList("- " + homework.getHomeworkMessage()));
            objects.add(obj);
        }
        adapter = new HomeworkAdapter(getActivity(), objects);
        recyclerView.setAdapter(adapter);
        //adapter.replaceData(objects);
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

    @Override
    public void syncHomework() {
        getActivity().startService(new Intent(getContext(), SyncHomeworkIntentService.class));
    }
}
