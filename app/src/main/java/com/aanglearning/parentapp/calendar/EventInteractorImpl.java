package com.aanglearning.parentapp.calendar;

import com.aanglearning.parentapp.App;
import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.api.ApiClient;
import com.aanglearning.parentapp.api.ParentApi;
import com.aanglearning.parentapp.model.Evnt;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vinay on 31-07-2017.
 */

class EventInteractorImpl implements EventInteractor {
    @Override
    public void getEvents(long schoolId, long classId, final OnFinishedListener listener) {
        ParentApi api = ApiClient.getAuthorizedClient().create(ParentApi.class);

        Call<List<Evnt>> queue = api.getEvents(schoolId, classId);
        queue.enqueue(new Callback<List<Evnt>>() {
            @Override
            public void onResponse(Call<List<Evnt>> call, Response<List<Evnt>> response) {
                if(response.isSuccessful()) {
                    listener.onEventsReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<List<Evnt>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }
}
