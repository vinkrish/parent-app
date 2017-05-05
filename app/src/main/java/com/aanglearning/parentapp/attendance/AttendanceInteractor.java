package com.aanglearning.parentapp.attendance;

import com.aanglearning.parentapp.model.Attendance;

import java.util.List;

/**
 * Created by Vinay on 03-03-2017.
 */

public interface AttendanceInteractor {
    interface OnFinishedListener {
        void onError(String message);

       void onAttendanceReceived(List<Attendance> attendanceList);
    }

    void getAttendance(long sectionId, String lastDate,
                       AttendanceInteractor.OnFinishedListener listener);
}
