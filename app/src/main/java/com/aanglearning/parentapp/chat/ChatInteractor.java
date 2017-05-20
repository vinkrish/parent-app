package com.aanglearning.parentapp.chat;

import com.aanglearning.parentapp.model.Message;

import java.util.List;

/**
 * Created by Vinay on 28-04-2017.
 */

public interface ChatInteractor {
    interface OnFinishedListener {
        void onError(String message);

        void onMessageSaved(Message message);

        void onMessageReceived(List<Message> messages);

        void onFollowupMessagesReceived(List<Message> messages);
    }

    void saveMessage(Message message, ChatInteractor.OnFinishedListener listener);

    void getMessages(String senderRole, long senderId, String recipientRole, long recipeintId,
                     ChatInteractor.OnFinishedListener listener);

    void getFollowupMessages(String senderRole, long senderId, String recipientRole, long recipeintId,
                             long messageId, ChatInteractor.OnFinishedListener listener);
}
