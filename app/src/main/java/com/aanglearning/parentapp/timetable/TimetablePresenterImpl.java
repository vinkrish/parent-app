package com.aanglearning.parentapp.timetable;

import com.aanglearning.parentapp.model.Timetable;

import java.util.List;

/**
 * Created by Vinay on 13-06-2017.
 */

public class TimetablePresenterImpl implements TimetablePresenter, TimetableInteractor.OnFinishedListener {
    private TimetableView mView;
    private TimetableInteractor mInteractor;

    TimetablePresenterImpl(TimetableView view, TimetableInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void getTimetable(long sectionId) {
        if(mView != null) {
            mView.showProgress();
            mInteractor.getTimetable(sectionId, this);
        }
    }

    @Override
    public void onDestroy() {
        if(mView != null) {
            mView = null;
        }
    }

    @Override
    public void onError(String message) {
        if(mView != null) {
            mView.hideProgess();
            mView.showError(message);
        }
    }

    @Override
    public void onTimetableReceived(List<Timetable> timetableList) {
        if(mView != null) {
            mView.showTimetable(timetableList);
            mView.hideProgess();
        }
    }
}
