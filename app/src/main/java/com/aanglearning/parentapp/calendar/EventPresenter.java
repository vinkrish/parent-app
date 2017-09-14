package com.aanglearning.parentapp.calendar;

/**
 * Created by Vinay on 31-07-2017.
 */

interface EventPresenter {
    void getEvents(long schoolId, long classId);

    void onDestroy();
}
