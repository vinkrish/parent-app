package com.aanglearning.parentapp.messagegroup;

import com.aanglearning.parentapp.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinay on 07-04-2017.
 */

interface MessageInteractor {
    interface OnFinishedListener {
        void onError(String message);

        void onRecentMessagesReceived(List<Message> messages);

        void onMessageReceived(List<Message> messages);

        void onFollowupMessagesReceived(ArrayList<Message> messages);
    }

    void getRecentMessages(long groupId, long messageId, MessageInteractor.OnFinishedListener listener);

    void getMessages(long groupId, MessageInteractor.OnFinishedListener listener);

    void getFollowupMessages(long groupId, long messageId, MessageInteractor.OnFinishedListener listener);
}
