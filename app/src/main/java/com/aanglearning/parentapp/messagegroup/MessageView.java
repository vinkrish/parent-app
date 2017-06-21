package com.aanglearning.parentapp.messagegroup;

import com.aanglearning.parentapp.model.Message;

import java.util.ArrayList;

/**
 * Created by Vinay on 07-04-2017.
 */

interface MessageView {
    void showProgress();

    void hideProgress();

    void showError(String message);

    void onMessageSaved(Message message);

    void showMessages(ArrayList<Message> messages);

    void showFollowupMessages(ArrayList<Message> messages);
}