package com.aanglearning.parentapp.timetable;

import com.aanglearning.parentapp.model.Timetable;

import java.util.List;

/**
 * Created by Vinay on 13-06-2017.
 */

public interface TimetableView {
    void showProgress();

    void hideProgess();

    void showError(String message);

    void showTimetable(List<Timetable> timetableList);
}
