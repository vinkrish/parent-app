package com.aanglearning.parentapp.homework;

/**
 * Created by Vinay on 03-03-2017.
 */

interface HomeworkPresenter {
    void getHomeworks(long sectionId, String lastDate);

    void onDestroy();
}
