package com.aanglearning.parentapp.service;

import android.app.IntentService;
import android.content.Intent;

import com.aanglearning.parentapp.api.ApiClient;
import com.aanglearning.parentapp.api.ParentApi;
import com.aanglearning.parentapp.dao.AttendanceDao;
import com.aanglearning.parentapp.model.Attendance;
import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;

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
        ChildInfo childInfo = SharedPreferenceUtil.getProfile(getApplicationContext());

        ParentApi api = ApiClient.getAuthorizedClient().create(ParentApi.class);

        Call<List<Attendance>> subscribedCourses;
        String date = AttendanceDao.getLastAttendanceDate(childInfo.getSectionId());

        if (date.equals("")) {
            subscribedCourses = api.syncAttendance(childInfo.getSectionId());
        } else {
            subscribedCourses = api.syncAttendance(childInfo.getSectionId(), date);
        }

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
