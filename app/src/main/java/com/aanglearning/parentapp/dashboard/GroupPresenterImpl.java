package com.aanglearning.parentapp.dashboard;

import com.aanglearning.parentapp.model.Authorization;
import com.aanglearning.parentapp.model.Groups;

import java.util.List;

/**
 * Created by Vinay on 02-04-2017.
 */

class GroupPresenterImpl implements GroupPresenter, GroupInteractor.OnFinishedListener {
    private GroupView mView;
    private GroupInteractor mInteractor;

    GroupPresenterImpl(GroupView view, GroupInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void getGroups(long userId) {
        if (mView != null) {
            mView.showProgress();
            mInteractor.getGroups(userId, this);
        }
    }

    @Override
    public void updateFcmToken(Authorization authorization) {
        mInteractor.updateFcmToken(authorization);
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void onError(String message) {
        if (mView != null) {
            mView.hideProgress();
            mView.showError(message);
        }
    }

    @Override
    public void onGroupsReceived(List<Groups> groupsList) {
        if (mView != null) {
            mView.setGroups(groupsList);
            mView.hideProgress();
        }
    }

}
