package com.aanglearning.parentapp.dashboard;

import com.aanglearning.parentapp.model.Attendance;
import com.aanglearning.parentapp.model.Credentials;
import com.aanglearning.parentapp.model.Homework;

import java.util.List;

/**
 * Created by Vinay on 24-02-2017.
 */

public class DashboardPresenterImpl implements DashboardPresenter, DashboardInteractor.OnFinishedListener {
    private DashboardView dashboardView;
    private DashboardInteractor interactor;

    public DashboardPresenterImpl(DashboardView dashboardView, DashboardInteractor interactor) {
        this.dashboardView = dashboardView;
        this.interactor = interactor;
    }

    @Override
    public void onError() {

    }

    @Override
    public void onAPIError(String message) {

    }

}
