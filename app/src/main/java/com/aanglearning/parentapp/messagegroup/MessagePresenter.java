package com.aanglearning.parentapp.messagegroup;

import com.aanglearning.parentapp.model.Message;

/**
 * Created by Vinay on 07-04-2017.
 */

interface MessagePresenter {
    void getMessages(long groupId);

    void getFollowupMessages(long groupId, long messageId);

    void onDestroy();
}
