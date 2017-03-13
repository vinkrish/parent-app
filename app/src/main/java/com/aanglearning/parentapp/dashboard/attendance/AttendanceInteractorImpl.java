package com.aanglearning.parentapp.dashboard.attendance;

import android.util.Log;

import com.aanglearning.parentapp.api.APIError;
import com.aanglearning.parentapp.api.ApiClient;
import com.aanglearning.parentapp.api.ErrorUtils;
import com.aanglearning.parentapp.api.ParentApi;
import com.aanglearning.parentapp.model.Attendance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vinay on 03-03-2017.
 */

public class AttendanceInteractorImpl implements AttendanceInteractor {
    @Override
    public void getAttendance(long sectionId, String lastDate,
                              final OnFinishedListener listener) {
        ParentApi api = ApiClient.getAuthorizedClient().create(ParentApi.class);

        Call<List<Attendance>> subscribedCourses = api.getAttendance(sectionId, lastDate);
        subscribedCourses.enqueue(new Callback<List<Attendance>>() {
            @Override
            public void onResponse(Call<List<Attendance>> call, Response<List<Attendance>> response) {
                if(response.isSuccessful()) {
                    listener.onAttendanceReceived(response.body());
                } else {
                    //APIError error = ErrorUtils.parseError(response);
                    //listener.onAPIError(error.getMessage());
                    listener.onError();
                }
            }

            @Override
            public void onFailure(Call<List<Attendance>> call, Throwable t) {
                listener.onError();
            }
        });
    }
}
