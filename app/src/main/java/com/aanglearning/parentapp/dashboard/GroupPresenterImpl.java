package com.aanglearning.parentapp.dashboard;

import com.aanglearning.parentapp.dao.DeletedGroupDao;
import com.aanglearning.parentapp.model.DeletedGroup;
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
    public void getSchoolGroupsAboveId(long schoolId, long id) {
        if (mView != null) {
            mInteractor.getSchoolGroupsAboveId(schoolId, id, this);
        }
    }

    @Override
    public void getSchoolGroups(long schoolId) {
        if (mView != null) {
            mView.showProgress();
            mInteractor.getschoolGroups(schoolId, this);
        }
    }

    @Override
    public void getGroupsAboveId(long studentId, long id) {
        if (mView != null) {
            mInteractor.getGroupsAboveId(studentId, id, this);
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
    public void getRecentDeletedGroups(long schoolId, long id) {
        if (mView != null) {
            mInteractor.getRecentDeletedGroups(schoolId, id, this);
        }
    }

    @Override
    public void getDeletedGroups(long schoolId) {
        if (mView != null) {
            mInteractor.getDeletedGroups(schoolId, this);
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
    public void onRecentSchoolGroupsReceived(List<Groups> groupsList) {
        if (mView != null) {
            mView.setRecentSchoolGroups(groupsList);
            mView.hideProgress();
        }
    }

    @Override
    public void onSchoolGroupsReceived(List<Groups> groupsList) {
        if (mView != null) {
            mView.setSchoolGroups(groupsList);
            mView.hideProgress();
        }
    }

    @Override
    public void onRecentGroupsReceived(List<Groups> groupsList) {
        if (mView != null) {
            mView.setRecentGroups(groupsList);
            mView.hideProgress();
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
    public void onDeletedGroupsReceived(List<DeletedGroup> deletedGroups) {
        if (mView != null) {
            DeletedGroupDao.insertDeletedGroups(deletedGroups);
        }
    }

    @Override
    public void onMessageRecipientsReceived(List<MessageRecipient> mrList) {
        if (mView != null) {
            mView.setMessageRecipients(mrList);
        }
    }

}
