package com.aanglearning.parentapp.homework;

import com.aanglearning.parentapp.model.Homework;

import java.util.List;

/**
 * Created by Vinay on 03-03-2017.
 */

public interface HomeworkInteractor {
    interface OnFinishedListener {
        void onError(String message);

        void onHomeworkReceived(List<Homework> homeworks);
    }

    void getHomeworks(long sectionId, String lastDate,
                      HomeworkInteractor.OnFinishedListener listener);
}
