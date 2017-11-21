package com.aanglearning.parentapp.reportcard;

import com.aanglearning.parentapp.App;
import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.api.ApiClient;
import com.aanglearning.parentapp.api.ReportApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vinay on 21-11-2017.
 */

public class ReportInteractorImpl implements ReportInteractor {
    @Override
    public void getExams(long classId, final OnFinishedListener listener) {
        ReportApi api = ApiClient.getAuthorizedClient().create(ReportApi.class);

        Call<ArrayList<Exam>> queue = api.getExams(classId);
        queue.enqueue(new Callback<ArrayList<Exam>>() {
            @Override
            public void onResponse(Call<ArrayList<Exam>> call, Response<ArrayList<Exam>> response) {
                if(response.isSuccessful()) {
                    listener.onExamReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Exam>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getExamScore(long examId, long studentId, final OnFinishedListener listener) {
        ReportApi api = ApiClient.getAuthorizedClient().create(ReportApi.class);

        Call<ArrayList<StudentScore>> queue = api.getExamScore(examId, studentId);
        queue.enqueue(new Callback<ArrayList<StudentScore>>() {
            @Override
            public void onResponse(Call<ArrayList<StudentScore>> call, Response<ArrayList<StudentScore>> response) {
                if(response.isSuccessful()) {
                    listener.onExamScoreReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }
            @Override
            public void onFailure(Call<ArrayList<StudentScore>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getActivityScore(long sectionId, long examId, long subjectId, long studentId, final OnFinishedListener listener) {
        ReportApi api = ApiClient.getAuthorizedClient().create(ReportApi.class);

        Call<ArrayList<StudentScore>> queue = api.getActivityScore(sectionId, examId, subjectId, studentId);
        queue.enqueue(new Callback<ArrayList<StudentScore>>() {
            @Override
            public void onResponse(Call<ArrayList<StudentScore>> call, Response<ArrayList<StudentScore>> response) {
                if(response.isSuccessful()) {
                    listener.onActivityScoreReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }
            @Override
            public void onFailure(Call<ArrayList<StudentScore>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }
}
