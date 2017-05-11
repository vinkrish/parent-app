package com.aanglearning.parentapp.homework;

import com.aanglearning.parentapp.dao.HomeworkDao;
import com.aanglearning.parentapp.model.Homework;

import java.util.List;

/**
 * Created by Vinay on 03-03-2017.
 */

class HomeworkPresenterImpl implements HomeworkPresenter, HomeworkInteractor.OnFinishedListener {
    private HomeworkView mView;
    private HomeworkInteractor mInteractor;

    HomeworkPresenterImpl(HomeworkView view, HomeworkInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void getHomeworks(long sectionId, String lastDate) {
        if(mView != null) {
            mView.showProgress();
            List<Homework> homeworkList = HomeworkDao.getHomework(sectionId, lastDate);
            if(homeworkList.size() == 0) {
                mInteractor.getHomeworks(sectionId, lastDate, this);
            } else {
                mView.showHomework(homeworkList);
                mView.hideProgess();
            }
        }
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void onError(String message) {
        if(mView != null) {
            mView.hideProgess();
            mView.showError(message);
        }
    }

    @Override
    public void onHomeworkReceived(List<Homework> homeworks) {
        if(mView != null) {
            mView.showHomework(homeworks);
            mView.hideProgess();
            mView.syncHomework();
        }
    }
}
