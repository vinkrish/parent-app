package com.aanglearning.parentapp.reportcard;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.util.DividerItemDecoration;
import com.aanglearning.parentapp.util.NetworkUtil;
import com.aanglearning.parentapp.util.RecyclerItemClickListener;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportActivity extends AppCompatActivity implements ReportView,
        AdapterView.OnItemSelectedListener {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.progress) ProgressBar progressBar;
    @BindView(R.id.spinner_exam) Spinner examSpinner;
    @BindView(R.id.score_view) RecyclerView scoreView;
    @BindView(R.id.no_score) LinearLayout noScoreLayout;
    @BindView(R.id.act_score_view) RecyclerView actScoreView;
    @BindView(R.id.no_act_score) LinearLayout noActScoreLayout;
    @BindView(R.id.subject_name) TextView subjectSelected;
    @BindView(R.id.score_layout) LinearLayout scoreLayout;
    @BindView(R.id.activity_layout) LinearLayout activityLayout;

    private ChildInfo childInfo;
    private ReportPresenter presenter;
    private int oldSelectedPosition = -1;
    private ScoreAdapter scoreAdapter;
    private ActivityScoreAdapter activityScoreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        childInfo = SharedPreferenceUtil.getProfile(this);

        getSupportActionBar().setTitle(childInfo.getName()+ " - Result");

        presenter = new ReportPresenterImpl(this, new ReportInteractorImpl());

        setupRecyclerView();

        if(NetworkUtil.isNetworkAvailable(getApplicationContext())) {
            presenter.getExams(childInfo.getClassId());
        }
    }

    private void setupRecyclerView() {
        scoreView.setLayoutManager(new LinearLayoutManager(this));
        scoreView.setNestedScrollingEnabled(false);
        scoreView.setItemAnimator(new DefaultItemAnimator());
        scoreView.addItemDecoration(new DividerItemDecoration(this));

        scoreAdapter = new ScoreAdapter(getApplicationContext(), new ArrayList<StudentScore>(0));
        scoreView.setAdapter(scoreAdapter);

        actScoreView.setLayoutManager(new LinearLayoutManager(this));
        actScoreView.setNestedScrollingEnabled(false);
        actScoreView.setItemAnimator(new DefaultItemAnimator());
        actScoreView.addItemDecoration(new DividerItemDecoration(this));

        activityScoreAdapter = new ActivityScoreAdapter(new ArrayList<StudentScore>(0));
        actScoreView.setAdapter(activityScoreAdapter);

        scoreView.addOnItemTouchListener(new RecyclerItemClickListener(this, scoreView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (oldSelectedPosition != position) {
                    scoreAdapter.selectedItemChanged(oldSelectedPosition, new StudentScore());
                    oldSelectedPosition = position;
                    StudentScore studentScore = scoreAdapter.getDataSet().get(position);
                    subjectSelected.setText(String.format(Locale.ENGLISH, "%s's Activities", studentScore.getSchName()));
                    scoreAdapter.selectedItemChanged(position, studentScore);
                    presenter.getActivityScore(childInfo.getSectionId(),
                            ((Exam) examSpinner.getSelectedItem()).getId(),
                            scoreAdapter.getDataSet().get(position).getSchId(),
                            childInfo.getStudentId() );
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    private void showSnackbar(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        showSnackbar(message);
    }

    @Override
    public void showExam(List<Exam> exams) {
        ArrayAdapter<Exam> adapter = new
                ArrayAdapter<>(this, android.R.layout.simple_spinner_item, exams);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        examSpinner.setAdapter(adapter);
        examSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void showExamScore(List<StudentScore> examScores) {
        scoreLayout.setVisibility(View.VISIBLE);
        if(examScores.size() > 0) {
            noScoreLayout.setVisibility(View.GONE);
            scoreAdapter.setDataSet(examScores);
        } else {
            noScoreLayout.setVisibility(View.VISIBLE);
            scoreAdapter.setDataSet(new ArrayList<StudentScore>(0));
        }
    }

    @Override
    public void showActivityScore(List<StudentScore> activityScores) {
        activityLayout.setVisibility(View.VISIBLE);
        if(activityScores.size() > 0) {
            noActScoreLayout.setVisibility(View.GONE);
            activityScoreAdapter.setDataSet(activityScores);
        } else {
            noActScoreLayout.setVisibility(View.VISIBLE);
            activityScoreAdapter.setDataSet(new ArrayList<StudentScore>(0));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        switch (parent.getId()) {
            case R.id.spinner_exam:
                if(NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                    presenter.getExamScore(((Exam) examSpinner.getSelectedItem()).getId(), childInfo.getStudentId());
                    oldSelectedPosition = -1;
                    subjectSelected.setText("");

                    scoreLayout.setVisibility(View.GONE);
                    noActScoreLayout.setVisibility(View.GONE);
                    activityScoreAdapter.setDataSet(new ArrayList<StudentScore>(0));
                    activityLayout.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
