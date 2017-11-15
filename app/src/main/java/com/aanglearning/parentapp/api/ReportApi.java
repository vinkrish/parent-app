package com.aanglearning.parentapp.api;

import com.aanglearning.parentapp.reportcard.Activity;
import com.aanglearning.parentapp.reportcard.ActivityScore;
import com.aanglearning.parentapp.reportcard.Exam;
import com.aanglearning.parentapp.reportcard.ExamSubject;
import com.aanglearning.parentapp.reportcard.Mark;
import com.aanglearning.parentapp.reportcard.SubActivity;
import com.aanglearning.parentapp.reportcard.SubActivityScore;

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

    @GET("examsubject/exam/{examId}")
    Call<ArrayList<ExamSubject>> getExamSubjects(@Path("examId") long examId);

    @GET("mark/exam/{examId}/subject/{subjectId}/section/{sectionId}/student/{studentId}")
    Call<ArrayList<Mark>> getMark(@Path("examId") long examId,
                                   @Path("subjectId") long subjectId,
                                   @Path("sectionId") long sectionId,
                                   @Path("studentId") long studentId);

    @GET("activity/section/{sectionId}/exam/{examId}/subject/{subjectId}")
    Call<ArrayList<Activity>> getActivities(@Path("sectionId") long sectionId,
                                            @Path("examId") long examId,
                                            @Path("subjectId") long subjectId);

    @GET("activityscore/activity/{activityId}/student/{studentId}")
    Call<ArrayList<ActivityScore>> getActivityScore(@Path("activityId") long activityId,
                                                     @Path("studentId") long studentId);

    @GET("subactivity/activity/{activityId}")
    Call<ArrayList<SubActivity>> getSubActivities(@Path("activityId") long activityId);

    @GET("subactivityscore/subactivity/{subActivityId}/student/{studentId}")
    Call<ArrayList<SubActivityScore>> getSubActivityScore(@Path("subActivityId") long subActivityId,
                                                           @Path("studentId") long studentId);

}
