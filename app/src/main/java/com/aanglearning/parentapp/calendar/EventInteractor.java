package com.aanglearning.parentapp.calendar;

import com.aanglearning.parentapp.model.Evnt;

import java.util.List;

/**
 * Created by Vinay on 31-07-2017.
 */

interface EventInteractor {
    interface OnFinishedListener {
        void onError(String message);

        void onEventsReceived(List<Evnt> eventsList);
    }

    void getEvents(long schoolId, long classId, EventInteractor.OnFinishedListener listener);
}
