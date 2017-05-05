package com.aanglearning.parentapp.attendance;

import com.aanglearning.parentapp.dao.AttendanceDao;
import com.aanglearning.parentapp.model.Attendance;

import java.util.List;

/**
 * Created by Vinay on 08-03-2017.
 */

class AttendancePresenterImpl implements AttendancePresenter,
        AttendanceInteractor.OnFinishedListener {
    private AttendanceView mView;
    private AttendanceInteractor mInteractor;

    AttendancePresenterImpl(AttendanceView view, AttendanceInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void getAttendance(long sectionId, long studentId, String lastDate) {
        if(mView != null) {
            mView.showProgress();
            List<Attendance> attendanceList = AttendanceDao.getAttendance(sectionId, lastDate);
            if(attendanceList.size() == 0) {
                mInteractor.getAttendance(sectionId, lastDate, this);
            } else {
                mView.showAttendance(attendanceList);
                mView.hideProgess();
            }
        }
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void onError(String message) {
        if(mView != null) {
            mView.hideProgess();
        }
    }

    @Override
    public void onAttendanceReceived(List<Attendance> attendanceList) {
        if(mView != null) {
            mView.showAttendance(attendanceList);
            mView.hideProgess();
            mView.syncAttendance();
        }
    }
}
