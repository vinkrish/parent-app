package com.aanglearning.parentapp.attendance;

/**
 * Created by Vinay on 03-03-2017.
 */

interface AttendancePresenter {
    void getStudentAbsentDays(long studentId);

    void onDestroy();
}
