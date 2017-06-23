package com.aanglearning.parentapp.profile;

import com.aanglearning.parentapp.App;
import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.api.ApiClient;
import com.aanglearning.parentapp.api.ParentApi;
import com.aanglearning.parentapp.model.Student;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vinay on 23-06-2017.
 */

public class ProfileInteractorImpl implements ProfileInteractor {
    @Override
    public void getStudent(long studentId, final OnFinishedListener listener) {
        ParentApi api = ApiClient.getAuthorizedClient().create(ParentApi.class);

        Call<Student> classList = api.getStudent(studentId);
        classList.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if(response.isSuccessful()) {
                    listener.onStudentReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }
}
