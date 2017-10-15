package com.aanglearning.parentapp.messagegroup;

import com.aanglearning.parentapp.model.Message;

/**
 * Created by Vinay on 07-04-2017.
 */

interface MessagePresenter {
    void getRecentMessages(long groupId, long messageId);

    void getMessages(long groupId);

    void getRecentDeletedMessages(long groupId, long id);

    void getDeletedMessages(long groupId);

    void onDestroy();
}
