package com.aanglearning.parentapp.dashboard.attendance;

import com.aanglearning.parentapp.model.Attendance;

import java.util.List;

/**
 * Created by Vinay on 08-03-2017.
 */

public class AttendancePresenterImpl implements AttendancePresenter,
        AttendanceInteractor.OnFinishedListener {

    @Override
    public void getAttendance(long sectionId, long studentId, String lastDate) {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onAPIError(String message) {

    }

    @Override
    public void onAttendanceReceived(List<Attendance> attendanceList) {

    }
}
