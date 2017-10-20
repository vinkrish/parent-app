package com.aanglearning.parentapp.dashboard;

/**
 * Created by Vinay on 02-04-2017.
 */

interface GroupPresenter {
    void getGroup(long groupId);

    void getSchoolGroupsAboveId(long schoolId, long id);

    void getSchoolGroups(long schoolId);

    void getGroupsAboveId(long studentId, long id);

    void getGroups(long studentId);

    void getMessageRecipients(long recipientId);

    void getRecentDeletedGroups(long schoolId, long id);

    void getDeletedGroups(long schoolId);

    void onDestroy();
}
