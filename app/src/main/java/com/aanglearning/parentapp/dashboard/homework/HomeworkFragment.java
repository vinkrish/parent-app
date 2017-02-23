package com.aanglearning.parentapp.dashboard.homework;

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

public class HomeworkFragment extends Fragment {

    public static HomeworkFragment newInstance() {
        HomeworkFragment fragment = new HomeworkFragment();
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
        View view = inflater.inflate(R.layout.homework_fragment, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

}
