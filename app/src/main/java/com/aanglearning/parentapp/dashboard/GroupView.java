package com.aanglearning.parentapp.dashboard;

import com.aanglearning.parentapp.model.Groups;

import java.util.List;

/**
 * Created by Vinay on 02-04-2017.
 */

interface GroupView {
    void showProgress();

    void hideProgress();

    void showError(String message);

    void setGroups(List<Groups> groups);
}
