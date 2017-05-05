package com.aanglearning.parentapp.api;

import com.aanglearning.parentapp.model.Attendance;
import com.aanglearning.parentapp.model.Homework;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Vinay on 23-02-2017.
 */

public interface ParentApi {

    @GET("app/homework/section/{sectionId}")
    Call<List<Homework>> syncHomework(@Path("sectionId") long sectionId);

    @GET("app/homework/section/{sectionId}/date/{lastDate}")
    Call<List<Homework>> syncHomework(@Path("sectionId") long sectionId,
                                    @Path("lastDate") String lastDate);

    @GET("app/homework/section/{sectionId}/homeworkDate/{homeworkDate}")
    Call<List<Homework>> getHomework(@Path("sectionId") long sectionId,
                                      @Path("homeworkDate") String homeworkDate);

    @GET("app/attendance/section/{sectionId}")
    Call<List<Attendance>> syncAttendance(@Path("sectionId") long sectionId);

    @GET("app/attendance/section/{sectionId}/date/{lastDate}")
    Call<List<Attendance>> syncAttendance(@Path("sectionId") long sectionId,
                                          @Path("lastDate") String lastDate);

    @GET("app/attendance/section/{sectionId}/currentDate/{attendanceDate}")
    Call<List<Attendance>> getAttendance(@Path("sectionId") long sectionId,
                                          @Path("attendanceDate") String attendanceDate);

}
