package com.aanglearning.parentapp.dashboard.homework;

import com.aanglearning.parentapp.api.APIError;
import com.aanglearning.parentapp.api.ApiClient;
import com.aanglearning.parentapp.api.ErrorUtils;
import com.aanglearning.parentapp.api.ParentApi;
import com.aanglearning.parentapp.model.Homework;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vinay on 03-03-2017.
 */

public class HomeworkInteractorImpl implements HomeworkInteractor {
    @Override
    public void getHomeworks(String authToken, long sectionId, String lastDate,
                             final OnFinishedListener listener) {
        ParentApi api = ApiClient.getAuthorizedClient(authToken).create(ParentApi.class);

        Call<List<Homework>> subscribedCourses = api.getHomework(sectionId, lastDate);
        subscribedCourses.enqueue(new Callback<List<Homework>>() {
            @Override
            public void onResponse(Call<List<Homework>> call, Response<List<Homework>> response) {
                if(response.isSuccessful()) {
                    listener.onHomeworkReceived(response.body());
                } else {
                    //APIError error = ErrorUtils.parseError(response);
                    //listener.onAPIError(error.getMessage());
                    listener.onError();
                }
            }

            @Override
            public void onFailure(Call<List<Homework>> call, Throwable t) {
                listener.onError();
            }
        });
    }
}
