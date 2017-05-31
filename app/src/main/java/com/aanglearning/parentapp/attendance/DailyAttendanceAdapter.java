package com.aanglearning.parentapp.attendance;

import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.model.Attendance;
import com.aanglearning.parentapp.model.Chat;
import com.aanglearning.parentapp.util.DateUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 28-05-2017.
 */

class DailyAttendanceAdapter extends RecyclerView.Adapter<DailyAttendanceAdapter.ViewHolder> {
    private List<Attendance> attendances;

    DailyAttendanceAdapter(List<Attendance> attendances) {
        this.attendances = attendances;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_daily_item, parent, false);
        return new DailyAttendanceAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(attendances.get(position));
    }

    @Override
    public int getItemCount() {
        return attendances.size();
    }

    List<Attendance> getDataSet() {
        return attendances;
    }

    @UiThread
    void setDataSet(List<Attendance> attendances) {
        this.attendances = attendances;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.date)
        TextView date;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(final Attendance attendance) {
            date.setText(DateUtil.getDisplayFormattedDate(attendance.getDateAttendance()));
        }
    }
}
