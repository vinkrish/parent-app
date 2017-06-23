package com.aanglearning.parentapp.attendance;

import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.model.Attendance;
import com.aanglearning.parentapp.util.DateUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinay on 28-05-2017.
 */

class SessionAttendanceAdapter extends RecyclerView.Adapter<SessionAttendanceAdapter.ViewHolder> {
    private LinkedHashMap<String, List<Attendance>> attendanceList;
    private ArrayList<List<Attendance>> list = new ArrayList<>();

    SessionAttendanceAdapter(LinkedHashMap<String, List<Attendance>> attendanceList) {
        this.attendanceList = attendanceList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_session_item, parent, false);
        return new SessionAttendanceAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(attendanceList.get((attendanceList.keySet().toArray())[ position ]));
        //holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    @UiThread
    void setDataSet(LinkedHashMap<String, List<Attendance>> attendanceList) {
        this.attendanceList = attendanceList;
        /*for (Map.Entry<String, List<Attendance>> entry : attendanceList.entrySet()) {
            String key = entry.getKey();
            List<Attendance> value = entry.getValue();
            list.add(entry.getValue());
        }*/
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.morning_attendance_status)
        TextView morningStatus;
        @BindView(R.id.afternoon_attendance_status)
        TextView afternoonStatus;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(List<Attendance> attendances) {
            date.setText(DateUtil.getDisplayFormattedDate(attendances.get(0).getDateAttendance()));
            for(Attendance attendance: attendances) {
                if(attendance.getSession() == 0) {
                    morningStatus.setVisibility(View.VISIBLE);
                } else if(attendance.getSession() == 1) {
                    afternoonStatus.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
