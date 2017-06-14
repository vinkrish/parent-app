package com.aanglearning.parentapp.timetable;

/**
 * Created by Vinay on 13-06-2017.
 */

interface TimetablePresenter {
    void getTimetable(long sectionId);

    void onDestroy();
}
