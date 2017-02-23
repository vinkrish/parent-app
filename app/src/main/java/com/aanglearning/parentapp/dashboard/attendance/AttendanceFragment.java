package com.aanglearning.parentapp.dashboard.attendance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aanglearning.parentapp.R;

import butterknife.ButterKnife;

/**
 * Created by Vinay on 22-02-2017.
 */

public class AttendanceFragment extends Fragment {

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
        ButterKnife.bind(this, view);

        return view;
    }

}
