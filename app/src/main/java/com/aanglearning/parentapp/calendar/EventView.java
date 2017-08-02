package com.aanglearning.parentapp.calendar;

import com.aanglearning.parentapp.model.Evnt;

import java.util.List;

/**
 * Created by Vinay on 31-07-2017.
 */

interface EventView {
    void showProgress();

    void hideProgess();

    void showError(String message);

    void setEvents(List<Evnt> events);
}
