package com.aanglearning.parentapp.dashboard;

import com.aanglearning.parentapp.model.Groups;
import com.aanglearning.parentapp.model.MessageRecipient;

import java.util.List;

/**
 * Created by Vinay on 02-04-2017.
 */

interface GroupView {
    void showProgress();

    void hideProgress();

    void showError(String message);

    void backupGroup(Groups group);

    void setGroup(Groups group);

    void setRecentSchoolGroups(List<Groups> groups);

    void setSchoolGroups(List<Groups> groups);

    void setRecentGroups(List<Groups> groups);

    void setGroups(List<Groups> groups);

    void setMessageRecipients(List<MessageRecipient> mrList);
}
