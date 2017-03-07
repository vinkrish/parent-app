package com.aanglearning.parentapp.dashboard.homework;

import com.aanglearning.parentapp.model.Homework;

import java.util.List;

/**
 * Created by Vinay on 03-03-2017.
 */

public interface HomeworkView {

    void showHomework(List<Homework> homeworkList);

    void showProgress();

    void hideProgess();

    void showError();

    void showAPIError(String message);

    void syncHomework();
}
