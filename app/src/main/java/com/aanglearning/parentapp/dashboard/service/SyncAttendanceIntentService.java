package com.aanglearning.parentapp.dashboard.service;

import android.app.IntentService;
import android.content.Intent;

import com.aanglearning.parentapp.api.ApiClient;
import com.aanglearning.parentapp.api.ParentApi;
import com.aanglearning.parentapp.dao.AttendanceDao;
import com.aanglearning.parentapp.model.Attendance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vinay on 25-02-2017.
 */

public class SyncAttendanceIntentService extends IntentService {
    public SyncAttendanceIntentService() {
        super("attendance");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        long sectionId = intent.getLongExtra("sectionId", 0);
        long studentId = intent.getLongExtra("studentId", 0);
        String lastDate = intent.getStringExtra("lastDate");

        ParentApi api = ApiClient.getAuthorizedClient().create(ParentApi.class);

        Call<List<Attendance>> subscribedCourses = api.syncAttendance(sectionId, studentId, lastDate);
        subscribedCourses.enqueue(new Callback<List<Attendance>>() {
            @Override
            public void onResponse(Call<List<Attendance>> call, Response<List<Attendance>> response) {
                if (response.isSuccessful()) {
                    AttendanceDao.insert(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Attendance>> call, Throwable t) {
            }
        });

    }
}
