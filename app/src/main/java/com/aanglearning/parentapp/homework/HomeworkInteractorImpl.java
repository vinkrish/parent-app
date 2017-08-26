package com.aanglearning.parentapp.homework;

import com.aanglearning.parentapp.App;
import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.api.ApiClient;
import com.aanglearning.parentapp.api.ParentApi;
import com.aanglearning.parentapp.model.Homework;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vinay on 03-03-2017.
 */

class HomeworkInteractorImpl implements HomeworkInteractor {
    @Override
    public void getHomeworks(long sectionId, String lastDate,
                             final OnFinishedListener listener) {
        ParentApi api = ApiClient.getAuthorizedClient().create(ParentApi.class);

        Call<List<Homework>> queue = api.getHomework(sectionId, lastDate);
        queue.enqueue(new Callback<List<Homework>>() {
            @Override
            public void onResponse(Call<List<Homework>> call, Response<List<Homework>> response) {
                if(response.isSuccessful()) {
                    listener.onHomeworkReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<List<Homework>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }
}
