package com.aanglearning.parentapp.dashboard;

import com.aanglearning.parentapp.model.Attendance;
import com.aanglearning.parentapp.model.Homework;

import java.util.List;

/**
 * Created by Vinay on 24-02-2017.
 */

public interface DashboardView {
    void showProgress();

    void hideProgess();

    void showError();

    void showAPIError(String message);
}
