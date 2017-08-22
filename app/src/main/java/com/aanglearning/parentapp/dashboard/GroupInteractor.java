package com.aanglearning.parentapp.dashboard;

import com.aanglearning.parentapp.model.Authorization;
import com.aanglearning.parentapp.model.Groups;

import java.util.List;

/**
 * Created by Vinay on 02-04-2017.
 */

interface GroupInteractor {
    interface OnFinishedListener {
        void onError(String message);

        void onGroupReceived(Groups group);

        void onGroupsReceived(List<Groups> groupsList);
    }

    void getGroup(long groupId, GroupInteractor.OnFinishedListener listener);

    void getGroups(long userId, GroupInteractor.OnFinishedListener listener);

    void updateFcmToken(Authorization authorization);
}
