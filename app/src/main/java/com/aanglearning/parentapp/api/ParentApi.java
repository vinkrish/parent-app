package com.aanglearning.parentapp.api;

import com.aanglearning.parentapp.model.AppVersion;
import com.aanglearning.parentapp.model.Attendance;
import com.aanglearning.parentapp.model.Chat;
import com.aanglearning.parentapp.model.Evnt;
import com.aanglearning.parentapp.model.Groups;
import com.aanglearning.parentapp.model.Homework;
import com.aanglearning.parentapp.model.Message;
import com.aanglearning.parentapp.model.MessageRecipient;
import com.aanglearning.parentapp.model.Student;
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
    @GET("appversion/{versionId}/{appName}")
    Call<AppVersion> getAppVersion(@Path("versionId") int versionId,
                                   @Path("appName") String appName);

    @GET("student/{studentId}")
    Call<Student> getStudent(@Path("studentId") long studentId);

    @GET("app/homework/section/{sectionId}/homeworkDate/{homeworkDate}")
    Call<List<Homework>> getHomework(@Path("sectionId") long sectionId,
                                      @Path("homeworkDate") String homeworkDate);

    @GET("app/attendance/student/{studentId}")
    Call<List<Attendance>> getStudentAbsentDays(@Path("studentId") long studentId);

    @GET("app/attendance/section/{sectionId}/currentDate/{attendanceDate}")
    Call<List<Attendance>> getAttendance(@Path("sectionId") long sectionId,
                                          @Path("attendanceDate") String attendanceDate);

    //Timetable API

    @GET("app/timetable/section/{sectionId}")
    Call<List<Timetable>> getTimetable(@Path("sectionId") long sectionId);

    //Group Message API

    @POST("messagerecipient")
    Call<List<MessageRecipient>> saveMessageRecipient(@Body List<MessageRecipient> mrList);

    @GET("messagerecipient/{recipientId}")
    Call<List<MessageRecipient>> getAllMessageRecipients(@Path("recipientId") long recipientId);

    @GET("groups/{groupId}")
    Call<Groups> getGroup(@Path("groupId") long groupId);

    @GET("groups/student/{id}")
    Call<List<Groups>> getGroups(@Path("id") long id);

    @GET("message/group/{groupId}/messagesUp/{messageId}")
    Call<ArrayList<Message>> getGroupMessagesAboveId(@Path("groupId") long groupId,
                                                     @Path("messageId") long messageId);

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

    //Event API

    @GET("event/school/{schoolId}")
    Call<List<Evnt>> getEvents(@Path("schoolId") long schoolId);

}
