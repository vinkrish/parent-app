package com.aanglearning.parentapp.dashboard;

import com.aanglearning.parentapp.model.Authorization;

/**
 * Created by Vinay on 02-04-2017.
 */

interface GroupPresenter {
    void getGroup(long groupId);

    void getGroups(long studentId);

    void getMessageRecipients(long recipientId);

    void onDestroy();
}
