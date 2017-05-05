package com.aanglearning.parentapp.attendance;

import com.aanglearning.parentapp.model.Attendance;

import java.util.List;

/**
 * Created by Vinay on 08-03-2017.
 */

public interface AttendanceView {
    void showProgress();

    void hideProgess();

    void showError(String message);

    void showAttendance(List<Attendance> attendanceList);

    void syncAttendance();
}
