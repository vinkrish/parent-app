package com.aanglearning.parentapp.reportcard;

import java.util.List;

/**
 * Created by Vinay on 21-11-2017.
 */

public class ReportPresenterImpl implements ReportPresenter, ReportInteractor.OnFinishedListener {
    private ReportView mView;
    private ReportInteractor mInteractor;

    ReportPresenterImpl(ReportView reportView, ReportInteractor reportInteractor) {
        mView = reportView;
        mInteractor = reportInteractor;
    }

    @Override
    public void getExams(long classId) {
        if (mView != null) {
            mView.showProgress();
            mInteractor.getExams(classId, this);
        }
    }

    @Override
    public void getExamScore(long examId, long studentId) {
        if (mView != null) {
            mView.showProgress();
            mInteractor.getExamScore(examId, studentId, this);
        }
    }

    @Override
    public void getActivityScore(long sectionId, long examId, long subjectId, long studentId) {
        if (mView != null) {
            mView.showProgress();
            mInteractor.getActivityScore(sectionId, examId, subjectId, studentId, this);
        }
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void onError(String message) {
        if (mView != null) {
            mView.hideProgress();
            mView.showError(message);
        }
    }

    @Override
    public void onExamReceived(List<Exam> exams) {
        if (mView != null) {
            mView.showExam(exams);
            mView.hideProgress();
        }
    }

    @Override
    public void onExamScoreReceived(List<StudentScore> examScores) {
        if (mView != null) {
            mView.showExamScore(examScores);
            mView.hideProgress();
        }
    }

    @Override
    public void onActivityScoreReceived(List<StudentScore> activityScores) {
        if (mView != null) {
            mView.showActivityScore(activityScores);
            mView.hideProgress();
        }
    }
}
