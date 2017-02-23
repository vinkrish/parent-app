package com.aanglearning.parentapp.dashboard.homework;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.aanglearning.parentapp.R;
import com.bignerdranch.expandablerecyclerview.ChildViewHolder;

/**
 * Created by Vinay on 23-02-2017.
 */

public class HomeworkChildViewHolder extends ChildViewHolder {
    private TextView mHomeworkTextView;

    public HomeworkChildViewHolder(@NonNull View itemView) {
        super(itemView);
        mHomeworkTextView = (TextView) itemView.findViewById(R.id.homework_textview);
    }

    public void bind(@NonNull String homework) {
        mHomeworkTextView.setText(homework);
    }
}
