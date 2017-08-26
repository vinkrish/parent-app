package com.aanglearning.parentapp.timetable;

import com.aanglearning.parentapp.App;
import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.api.ApiClient;
import com.aanglearning.parentapp.api.ParentApi;
import com.aanglearning.parentapp.model.Timetable;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vinay on 13-06-2017.
 */

class TimetableInteractorImpl implements TimetableInteractor {

    @Override
    public void getTimetable(long sectionId, final OnFinishedListener listener) {
        ParentApi api = ApiClient.getAuthorizedClient().create(ParentApi.class);

        Call<List<Timetable>> queue = api.getTimetable(sectionId);
        queue.enqueue(new Callback<List<Timetable>>() {
            @Override
            public void onResponse(Call<List<Timetable>> call, Response<List<Timetable>> response) {
                if(response.isSuccessful()) {
                    listener.onTimetableReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<List<Timetable>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }
}
