package com.aanglearning.parentapp.homework;

import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.model.Homework;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 24-04-2017.
 */

class HomeworkAdapterr extends RecyclerView.Adapter<HomeworkAdapterr.ViewHolder>{
    private List<Homework> homeworks;

    HomeworkAdapterr(List<Homework> homeworks) {
        this.homeworks = homeworks;
    }

    List<Homework> getDataSet() {
        return homeworks;
    }

    @UiThread
    void setDataSet(List<Homework> homeworks) {
        this.homeworks = homeworks;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.homework_item, parent, false);
        return new HomeworkAdapterr.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(homeworks.get(position));
    }

    @Override
    public int getItemCount() {
        return homeworks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.subject_name) TextView subjectName;
        @BindView(R.id.hw_message) TextView hwMessage;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(final Homework homework) {
            subjectName.setText(homework.getSubjectName());
            hwMessage.setText(homework.getHomeworkMessage());
        }
    }
}
