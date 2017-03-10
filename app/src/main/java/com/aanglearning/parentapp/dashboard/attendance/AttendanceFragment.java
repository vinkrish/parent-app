package com.aanglearning.parentapp.dashboard.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.dashboard.service.SyncAttendanceIntentService;
import com.aanglearning.parentapp.model.Attendance;

import java.util.List;

/**
 * Created by Vinay on 22-02-2017.
 */

public class AttendanceFragment extends Fragment implements AttendanceView {

    public static AttendanceFragment newInstance() {
        AttendanceFragment fragment = new AttendanceFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.attendance_fragment, container, false);

        return view;
    }

    @Override
    public void showAttendance(List<Attendance> attendanceList) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgess() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void showAPIError(String message) {

    }

    @Override
    public void syncAttendance() {
        getActivity().startService(new Intent(getContext(), SyncAttendanceIntentService.class));
    }
}
