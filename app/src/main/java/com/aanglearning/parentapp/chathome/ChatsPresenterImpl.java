package com.aanglearning.parentapp.chathome;

import com.aanglearning.parentapp.model.Chat;

import java.util.List;

/**
 * Created by Vinay on 28-04-2017.
 */

class ChatsPresenterImpl implements ChatsPresenter, ChatsInteractor.OnFinishedListener {
    private ChatsView mView;
    private ChatsInteractor mInteractor;

    ChatsPresenterImpl(ChatsView view, ChatsInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void getChats(long teacherId) {
        mView.showProgress();
        mInteractor.getChats(teacherId, this);
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
    public void onChatsReceived(List<Chat> chats) {
        if (mView != null) {
            mView.setChats(chats);
            mView.hideProgress();
        }
    }
}
