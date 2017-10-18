package com.aanglearning.parentapp.dashboard;

import com.aanglearning.parentapp.model.Groups;
import com.aanglearning.parentapp.model.MessageRecipient;

import java.util.List;

/**
 * Created by Vinay on 02-04-2017.
 */

interface GroupInteractor {
    interface OnFinishedListener {
        void onError(String message);

        void onGroupReceived(Groups group);

        void onRecentSchoolGroupsReceived(List<Groups> groupsList);

        void onSchoolGroupsReceived(List<Groups> groupsList);

        void onRecentGroupsReceived(List<Groups> groupsList);

        void onGroupsReceived(List<Groups> groupsList);

        void onMessageRecipientsReceived(List<MessageRecipient> mrList);
    }

    void getGroup(long groupId, GroupInteractor.OnFinishedListener listener);

    void getSchoolGroupsAboveId(long schoolId, long id, GroupInteractor.OnFinishedListener listener);

    void getschoolGroups(long schoolId, GroupInteractor.OnFinishedListener listener);

    void getGroupsAboveId(long userId, long id, GroupInteractor.OnFinishedListener listener);

    void getGroups(long userId, GroupInteractor.OnFinishedListener listener);

    void getMessageRecipients(long recipientId, GroupInteractor.OnFinishedListener listener);
}
