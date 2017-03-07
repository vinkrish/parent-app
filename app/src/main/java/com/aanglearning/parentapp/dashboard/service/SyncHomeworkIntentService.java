package com.aanglearning.parentapp.dashboard.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.aanglearning.parentapp.api.ApiClient;
import com.aanglearning.parentapp.api.ParentApi;
import com.aanglearning.parentapp.dao.HomeworkDao;
import com.aanglearning.parentapp.model.Homework;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vinay on 25-02-2017.
 */

public class SyncHomeworkIntentService extends IntentService {
    public SyncHomeworkIntentService() {
        super("homework");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        long sectionId = intent.getLongExtra("sectionId", 0);
        String lastDate = intent.getStringExtra("lastDate");

        ParentApi api = ApiClient.getAuthorizedClient().create(ParentApi.class);

        Call<List<Homework>> subscribedCourses;
        if(lastDate.equals("")) {
            subscribedCourses = api.syncHomework(sectionId);
        } else {
            subscribedCourses = api.syncHomework(sectionId, lastDate);
        }

        subscribedCourses.enqueue(new Callback<List<Homework>>() {
            @Override
            public void onResponse(Call<List<Homework>> call, Response<List<Homework>> response) {
                if (response.isSuccessful()) {
                    HomeworkDao.insert(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Homework>> call, Throwable t) {
            }
        });
    }
}
