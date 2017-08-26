package com.aanglearning.parentapp.attendance;

import com.aanglearning.parentapp.App;
import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.api.ApiClient;
import com.aanglearning.parentapp.api.ParentApi;
import com.aanglearning.parentapp.model.Attendance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vinay on 03-03-2017.
 */

class AttendanceInteractorImpl implements AttendanceInteractor {

    @Override
    public void getStudentAbsentDays(long studentId, final OnFinishedListener listener) {
        ParentApi api = ApiClient.getAuthorizedClient().create(ParentApi.class);

        Call<List<Attendance>> queue = api.getStudentAbsentDays(studentId);
        queue.enqueue(new Callback<List<Attendance>>() {
            @Override
            public void onResponse(Call<List<Attendance>> call, Response<List<Attendance>> response) {
                if(response.isSuccessful()) {
                    listener.onAttendanceReceived(response.body());
                } else {
                    //APIError error = ErrorUtils.parseError(response);
                    //listener.onAPIError(error.getMessage());
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<List<Attendance>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

}
