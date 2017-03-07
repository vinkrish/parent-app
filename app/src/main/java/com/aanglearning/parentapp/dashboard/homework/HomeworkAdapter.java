package com.aanglearning.parentapp.dashboard.homework;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.model.Homework;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import java.util.List;

/**
 * Created by Vinay on 23-02-2017.
 */

class HomeworkAdapter extends ExpandableRecyclerAdapter<HomeworkViewObj, String, HomeworkViewHolder, HomeworkChildViewHolder> {
    private LayoutInflater mInflater;
    private List<HomeworkViewObj> mHomeworkList;

    /**
     * Primary constructor. Sets up {@link #mParentList} and {@link #mFlatItemList}.
     * <p>
     * Any changes to {@link #mParentList} should be made on the original instance, and notified via
     * {@link #notifyParentInserted(int)}
     * {@link #notifyParentRemoved(int)}
     * {@link #notifyParentChanged(int)}
     * {@link #notifyParentRangeInserted(int, int)}
     * {@link #notifyChildInserted(int, int)}
     * {@link #notifyChildRemoved(int, int)}
     * {@link #notifyChildChanged(int, int)}
     * methods and not the notify methods of RecyclerView.Adapter.
     *
     * @param parentList List of all parents to be displayed in the RecyclerView that this
     *                   adapter is linked to
     */
    HomeworkAdapter(Context context, @NonNull List<HomeworkViewObj> parentList) {
        super(parentList);
        mInflater = LayoutInflater.from(context);
        mHomeworkList = parentList;
    }

    @UiThread
    void replaceData(List<HomeworkViewObj> homeworkViewObjs) {
        mHomeworkList = homeworkViewObjs;
        notifyDataSetChanged();
        //notifyParentDataSetChanged(true);
    }

    @UiThread
    @NonNull
    @Override
    public HomeworkViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View homeworkParentView = mInflater.inflate(R.layout.homework_view, parentViewGroup, false);;
        return new HomeworkViewHolder(homeworkParentView);
    }

    @UiThread
    @NonNull
    @Override
    public HomeworkChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View homeworkChildView = mInflater.inflate(R.layout.homework_view_child, childViewGroup, false);;
        return new HomeworkChildViewHolder(homeworkChildView);
    }

    @Override
    public void onBindParentViewHolder(@NonNull HomeworkViewHolder parentViewHolder,
                                       int parentPosition, @NonNull HomeworkViewObj parent) {
        parentViewHolder.bind(parent);
    }

    @Override
    public void onBindChildViewHolder(@NonNull HomeworkChildViewHolder childViewHolder,
                                      int parentPosition, int childPosition, @NonNull String child) {
        childViewHolder.bind(child);
    }

}
