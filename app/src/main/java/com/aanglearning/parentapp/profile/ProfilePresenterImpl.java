package com.aanglearning.parentapp.profile;

import com.aanglearning.parentapp.model.Student;

/**
 * Created by Vinay on 23-06-2017.
 */

class ProfilePresenterImpl implements ProfilePresenter, ProfileInteractor.OnFinishedListener {
    private ProfileView mView;
    private ProfileInteractor mInteractor;

    ProfilePresenterImpl(ProfileView view, ProfileInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void getStudent(long studentId) {
        if (mView != null) {
            mView.showProgress();
            mInteractor.getStudent(studentId, this);
        }
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void onError(String message) {
        if (mView != null) {
            mView.hideProgress();
            mView.showError(message);
        }
    }

    @Override
    public void onStudentReceived(Student student) {
        if (mView != null) {
            mView.setProfile(student);
            mView.hideProgress();
        }
    }
}
