package com.aanglearning.parentapp.calendar;

import com.aanglearning.parentapp.model.Evnt;

import java.util.List;

/**
 * Created by Vinay on 31-07-2017.
 */

class EventPresenterImpl implements EventPresenter, EventInteractor.OnFinishedListener {
    private EventView mView;
    private EventInteractor mInteractor;

    EventPresenterImpl(EventView view, EventInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void getEvents(long schoolId, long classId) {
        mView.showProgress();
        mInteractor.getEvents(schoolId, classId, this);
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void onError(String message) {
        if (mView != null) {
            mView.hideProgess();
            mView.showError(message);
        }
    }

    @Override
    public void onEventsReceived(List<Evnt> eventsList) {
        if (mView != null) {
            mView.setEvents(eventsList);
            mView.hideProgess();
        }
    }
}
