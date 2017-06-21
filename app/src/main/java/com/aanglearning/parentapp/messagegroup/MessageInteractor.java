package com.aanglearning.parentapp.messagegroup;

import com.aanglearning.parentapp.model.Message;

import java.util.ArrayList;

/**
 * Created by Vinay on 07-04-2017.
 */

interface MessageInteractor {
    interface OnFinishedListener {
        void onError(String message);

        void onMessageReceived(ArrayList<Message> messages);

        void onFollowupMessagesReceived(ArrayList<Message> messages);
    }

    void getMessages(long groupId, MessageInteractor.OnFinishedListener listener);

    void getFollowupMessages(long groupId, long messageId, MessageInteractor.OnFinishedListener listener);
}