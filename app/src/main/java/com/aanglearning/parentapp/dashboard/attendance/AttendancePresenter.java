package com.aanglearning.parentapp.dashboard.attendance;

/**
 * Created by Vinay on 03-03-2017.
 */

public interface AttendancePresenter {

    void getAttendance(long sectionId, long studentId, String lastDate);

    void onDestroy();
}
