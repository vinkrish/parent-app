package com.aanglearning.parentapp.dashboard.attendance;

import com.aanglearning.parentapp.model.Credentials;

/**
 * Created by Vinay on 03-03-2017.
 */

public interface AttendancePresenter {

    void getAttendance(String authToken, long sectionId, long studentId, String lastDate);

    void onDestroy();
}
