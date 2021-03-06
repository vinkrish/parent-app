package com.aanglearning.parentapp.chathome;

import com.aanglearning.parentapp.model.Chat;

import java.util.List;

/**
 * Created by Vinay on 28-04-2017.
 */

interface ChatsInteractor {
    interface OnFinishedListener {
        void onError(String message);

        void onChatsReceived(List<Chat> chats);
    }

    void getChats(long teacherId, ChatsInteractor.OnFinishedListener listener);
}
