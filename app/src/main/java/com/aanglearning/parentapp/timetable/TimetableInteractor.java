package com.aanglearning.parentapp.timetable;

import com.aanglearning.parentapp.model.Timetable;

import java.util.List;

/**
 * Created by Vinay on 13-06-2017.
 */

interface TimetableInteractor {
    interface OnFinishedListener {
        void onError(String message);

        void onTimetableReceived(List<Timetable> timetableList);
    }

    void getTimetable(long sectionId, TimetableInteractor.OnFinishedListener listener);
}
