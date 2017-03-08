package com.aanglearning.parentapp.dashboard.homework;

import com.aanglearning.parentapp.model.Credentials;

/**
 * Created by Vinay on 03-03-2017.
 */

public interface HomeworkPresenter {
    void getHomeworks(String authToken, long sectionId, String lastDate);

    void onDestroy();
}
