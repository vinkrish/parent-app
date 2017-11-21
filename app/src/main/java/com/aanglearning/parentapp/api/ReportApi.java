package com.aanglearning.parentapp.api;

import com.aanglearning.parentapp.reportcard.Exam;
import com.aanglearning.parentapp.reportcard.StudentScore;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Vinay on 15-11-2017.
 */

public interface ReportApi {

    @GET("exam/class/{classId}")
    Call<ArrayList<Exam>> getExams(@Path("classId") long classId);

    @GET("mark/exam/{examId}/student/{studentId}")
    Call<ArrayList<StudentScore>> getExamScore(@Path("examId") long examId,
                                          @Path("studentId") long studentId);

    @GET("activityscore/section/{sectionId}/exam/{examId}/subject/{subjectId}/student/{studentId}")
    Call<ArrayList<StudentScore>> getActivityScore(@Path("sectionId") long sectionId,
                                                   @Path("examId") long examId,
                                                   @Path("subjectId") long subjectId,
                                                   @Path("studentId") long studentId);

}
