package com.aanglearning.parentapp.api;

import com.aanglearning.parentapp.model.Attendance;
import com.aanglearning.parentapp.model.Homework;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by Vinay on 23-02-2017.
 */

public interface ParentApi {

    @Headers("content-type: application/json")
    @GET("app/homework/section/{sectionId}")
    Call<List<Homework>> syncHomework(@Path("sectionId") long sectionId);

    @Headers("content-type: application/json")
    @GET("app/homework/section/{sectionId}/date/{lastDate}")
    Call<List<Homework>> syncHomework(@Path("sectionId") long sectionId,
                                    @Path("lastDate") String lastDate);

    @Headers("content-type: application/json")
    @GET("app/homework/section/{sectionId}/currentDate/{homeworkDate}")
    Call<List<Homework>> getHomework(@Path("sectionId") long sectionId,
                                      @Path("homeworkDate") String homeworkDate);

    @Headers("content-type: application/json")
    @GET("app/attendance/section/{sectionId}/student/{studentId}/date/{lastDate}")
    Call<List<Attendance>> syncAttendance(@Path("sectionId") long sectionId,
                                          @Path("studentId") long studentId,
                                          @Path("lastDate") String lastDate);

    @Headers("content-type: application/json")
    @GET("app/attendance/section/{sectionId}/student/{studentId}/date/{attendanceDate}")
    Call<List<Attendance>> getAttendance(@Path("sectionId") long sectionId,
                                          @Path("studentId") long studentId,
                                          @Path("attendanceDate") String attendanceDate);

}