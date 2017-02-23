package com.aanglearning.parentapp.dashboard.homework;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aanglearning.parentapp.R;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 22-02-2017.
 */

public class HomeworkFragment extends Fragment {
    @BindView(R.id.recyclerview) RecyclerView recyclerView;

    private HomeworkAdapter adapter;

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

        HomeworkView h1 = new HomeworkView("English", Arrays.asList("Do the fucking homework"));
        HomeworkView h2 = new HomeworkView("Kannada", Arrays.asList("Amikand homework madappa"));
        adapter = new HomeworkAdapter(getActivity(), Arrays.asList(h1, h2));

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

}
