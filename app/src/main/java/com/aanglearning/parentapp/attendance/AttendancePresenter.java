package com.aanglearning.parentapp.attendance;

/**
 * Created by Vinay on 03-03-2017.
 */

interface AttendancePresenter {
    void getStudentAbsentDays(long studentId);

    void getAttendance(long sectionId, long studentId, String attendanceDate);

    void onDestroy();
}
