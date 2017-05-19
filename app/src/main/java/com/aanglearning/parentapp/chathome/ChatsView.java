package com.aanglearning.parentapp.chathome;

import com.aanglearning.parentapp.model.Chat;

import java.util.List;

/**
 * Created by Vinay on 28-04-2017.
 */

interface ChatsView {
    void showProgress();

    void hideProgess();

    void showError(String message);

    void setChats(List<Chat> chats);
}
