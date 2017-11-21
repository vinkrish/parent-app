package com.aanglearning.parentapp.reportcard;

import java.util.List;

/**
 * Created by Vinay on 21-11-2017.
 */

public interface ReportInteractor {

    interface OnFinishedListener {
        void onError(String message);

        void onExamReceived(List<Exam> exams);

        void onExamScoreReceived(List<StudentScore> examScores);

        void onActivityScoreReceived(List<StudentScore> activityScores);
    }

    void getExams(long classId, ReportInteractor.OnFinishedListener listener);

    void getExamScore(long examId, long studentId, ReportInteractor.OnFinishedListener listener);

    void getActivityScore(long sectionId, long examId, long subjectId, long studentId,
                          ReportInteractor.OnFinishedListener listener);
}
