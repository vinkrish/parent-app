package com.aanglearning.parentapp.api;

import com.aanglearning.parentapp.model.Attendance;
import com.aanglearning.parentapp.model.Chat;
import com.aanglearning.parentapp.model.Groups;
import com.aanglearning.parentapp.model.Homework;
import com.aanglearning.parentapp.model.Message;
import com.aanglearning.parentapp.model.Timetable;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    @GET("app/attendance/student/{studentId}")
    Call<List<Attendance>> getStudentAbsentDays(@Path("studentId") long studentId);

    @GET("app/attendance/section/{sectionId}/currentDate/{attendanceDate}")
    Call<List<Attendance>> getAttendance(@Path("sectionId") long sectionId,
                                          @Path("attendanceDate") String attendanceDate);

    //Timetable API

    @GET("app/timetable/section/{sectionId}")
    Call<List<Timetable>> getTimetable(@Path("sectionId") long sectionId);

    //Group Message API
    @GET("groups/student/{id}")
    Call<List<Groups>> getGroups(@Path("id") long id);

    @GET("message/group/{groupId}")
    Call<ArrayList<Message>> getGroupMessages(@Path("groupId") long groupId);

    @GET("message/group/{groupId}/message/{messageId}")
    Call<ArrayList<Message>> getGroupMessagesFromId(@Path("groupId") long groupId,
                                                    @Path("messageId") long messageId);

    //Chat API
    @GET("chat/parent/{id}")
    Call<List<Chat>> getChats(@Path("id") long id);

    @POST("message")
    Call<Message> saveMessage(@Body Message message);

    @GET("message/{senderRole}/{senderId}/{recipientRole}/{recipientId}/messagesUp/{messageId}")
    Call<ArrayList<Message>> getChatMessagesAboveId(@Path("senderRole") String senderRole,
                                                    @Path("senderId") long senderId,
                                                    @Path("recipientRole") String recipientRole,
                                                    @Path("recipientId") long recipientId,
                                                    @Path("messageId") long messageId);

    @GET("message/{senderRole}/{senderId}/{recipientRole}/{recipientId}")
    Call<ArrayList<Message>> getChatMessages(@Path("senderRole") String senderRole,
                                             @Path("senderId") long senderId,
                                             @Path("recipientRole") String recipientRole,
                                             @Path("recipientId") long recipientId);

    @GET("message/{senderRole}/{senderId}/{recipientRole}/{recipientId}/message/{messageId}")
    Call<ArrayList<Message>> getChatMessagesFromId(@Path("senderRole") String senderRole,
                                                   @Path("senderId") long senderId,
                                                   @Path("recipientRole") String recipientRole,
                                                   @Path("recipientId") long recipientId,
                                                   @Path("messageId") long messageId);

}
