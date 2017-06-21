package com.aanglearning.parentapp.messagegroup;

import com.aanglearning.parentapp.model.Message;

import java.util.ArrayList;

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
    public void getMessages(long groupId) {
        if(mView != null) {
            mView.showProgress();
            mInteractor.getMessages(groupId, this);
        }
    }

    @Override
    public void getFollowupMessages(long groupId, long messageId) {
        if(mView != null) {
            mView.showProgress();
            mInteractor.getFollowupMessages(groupId, messageId, this);
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
    public void onMessageReceived(ArrayList<Message> messages) {
        if(mView != null) {
            mView.showMessages(messages);
            mView.hideProgress();
        }
    }

    @Override
    public void onFollowupMessagesReceived(ArrayList<Message> messages) {
        if(mView != null) {
            mView.showFollowupMessages(messages);
            mView.hideProgress();
        }
    }
}