package com.aanglearning.parentapp.homework;

import com.aanglearning.parentapp.model.Homework;

import java.util.List;

/**
 * Created by Vinay on 03-03-2017.
 */

interface HomeworkView {

    void showHomework(List<Homework> homeworkList);

    void showProgress();

    void hideProgess();

    void showError(String message);
}
