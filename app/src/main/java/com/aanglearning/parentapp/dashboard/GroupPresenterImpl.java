package com.aanglearning.parentapp.dashboard;

import com.aanglearning.parentapp.model.Authorization;
import com.aanglearning.parentapp.model.Groups;
import com.aanglearning.parentapp.model.MessageRecipient;

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
    public void getGroup(long groupId) {
        if (mView != null) {
            mView.showProgress();
            mInteractor.getGroup(groupId, this);
        }
    }

    @Override
    public void getGroups(long userId) {
        if (mView != null) {
            mView.showProgress();
            mInteractor.getGroups(userId, this);
        }
    }

    @Override
    public void getMessageRecipients(long recipientId) {
        if (mView != null) {
            mView.showProgress();
            mInteractor.getMessageRecipients(recipientId, this);
        }
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
    public void onGroupReceived(Groups group) {
        if (mView != null) {
            mView.backupGroup(group);
            mView.hideProgress();
            mView.setGroup(group);
        }
    }

    @Override
    public void onGroupsReceived(List<Groups> groupsList) {
        if (mView != null) {
            mView.setGroups(groupsList);
            mView.hideProgress();
        }
    }

    @Override
    public void onMessageRecipientsReceived(List<MessageRecipient> mrList) {
        if (mView != null) {
            mView.setMessageRecipients(mrList);
        }
    }

}
