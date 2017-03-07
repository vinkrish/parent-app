package com.aanglearning.parentapp.dashboard.attendance;

import com.aanglearning.parentapp.model.Attendance;
import com.aanglearning.parentapp.model.Credentials;

import java.util.List;

/**
 * Created by Vinay on 03-03-2017.
 */

public interface AttendanceInteractor {
    interface OnFinishedListener {
        void onError();

        void onAPIError(String message);

       void onAttendanceReceived(List<Attendance> attendanceList);
    }

    void getAttendance(long sectionId, long studentId, String lastDate,
                       AttendanceInteractor.OnFinishedListener listener);
}
