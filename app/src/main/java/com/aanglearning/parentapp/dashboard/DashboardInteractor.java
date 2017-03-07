package com.aanglearning.parentapp.dashboard;

import com.aanglearning.parentapp.model.Attendance;
import com.aanglearning.parentapp.model.Credentials;
import com.aanglearning.parentapp.model.Homework;

import java.util.List;

/**
 * Created by Vinay on 23-02-2017.
 */

interface DashboardInteractor {
    interface OnFinishedListener {
        void onError();

        void onAPIError(String message);
    }

}
