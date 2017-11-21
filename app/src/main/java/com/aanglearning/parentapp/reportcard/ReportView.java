package com.aanglearning.parentapp.reportcard;

import java.util.List;

/**
 * Created by Vinay on 21-11-2017.
 */

public interface ReportView {
    void showProgress();

    void hideProgress();

    void showError(String message);

    void showExam(List<Exam> exams);

    void showExamScore(List<StudentScore> examScores);

    void showActivityScore(List<StudentScore> activityScores);
}
