package com.aanglearning.parentapp.reportcard;

/**
 * Created by Vinay on 21-11-2017.
 */

public interface ReportPresenter {
    void getExams(long classId);

    void getExamScore(long examId, long studentId);

    void getActivityScore(long sectionId, long examId, long subjectId, long studentId);

    void onDestroy();
}
