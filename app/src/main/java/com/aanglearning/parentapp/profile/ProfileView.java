package com.aanglearning.parentapp.profile;

import com.aanglearning.parentapp.model.Student;

/**
 * Created by Vinay on 23-06-2017.
 */

interface ProfileView {
    void showProgress();

    void hideProgress();

    void showError(String message);

    void setProfile(Student student);
}
