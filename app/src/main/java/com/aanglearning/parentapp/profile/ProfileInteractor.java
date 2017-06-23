package com.aanglearning.parentapp.profile;

import com.aanglearning.parentapp.model.Student;

/**
 * Created by Vinay on 23-06-2017.
 */

interface ProfileInteractor {
    interface OnFinishedListener {
        void onError(String message);

        void onStudentReceived(Student student);
    }

    void getStudent(long studentId, ProfileInteractor.OnFinishedListener listener);
}
