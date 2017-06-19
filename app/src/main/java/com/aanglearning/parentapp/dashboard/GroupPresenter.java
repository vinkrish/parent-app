package com.aanglearning.parentapp.dashboard;

import com.aanglearning.parentapp.model.Authorization;

/**
 * Created by Vinay on 02-04-2017.
 */

interface GroupPresenter {
    void getGroups(long studentId);

    void updateFcmToken(Authorization authorization);

    void onDestroy();
}
