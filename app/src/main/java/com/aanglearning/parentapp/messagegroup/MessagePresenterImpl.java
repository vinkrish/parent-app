package com.aanglearning.parentapp.messagegroup;

import com.aanglearning.parentapp.dao.DeletedMessageDao;
import com.aanglearning.parentapp.model.DeletedMessage;
import com.aanglearning.parentapp.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 07-04-2017.
 */

class MessagePresenterImpl implements MessagePresenter, MessageInteractor.OnFinishedListener {
    private MessageView mView;
    private MessageInteractor mInteractor;

    MessagePresenterImpl(MessageView view, MessageInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void getRecentMessages(long groupId, long messageId) {
        mInteractor.getRecentMessages(groupId, messageId, this);
    }

    @Override
    public void getMessages(long groupId) {
        mView.showProgress();
        mInteractor.getMessages(groupId, this);
    }

    @Override
    public void getRecentDeletedMessages(long groupId, long id) {
        mInteractor.getRecentDeletedMessages(groupId, id, this);
    }

    @Override
    public void getDeletedMessages(long groupId) {
        mInteractor.getDeletedMessages(groupId, this);
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
    public void onRecentMessagesReceived(List<Message> messages) {
        if (mView != null) {
            mView.showRecentMessages(messages);
            mView.hideProgress();
        }
    }

    @Override
    public void onMessageReceived(List<Message> messages) {
        if (mView != null) {
            mView.showMessages(messages);
            mView.hideProgress();
        }
    }

    @Override
    public void onDeletedMessagesReceived(List<DeletedMessage> messages) {
        if (mView != null) {
            DeletedMessageDao.insertDeletedMessages(messages);
        }
    }

}
