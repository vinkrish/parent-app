package com.aanglearning.parentapp.gallery;

import com.aanglearning.parentapp.dao.DeletedAlbumDao;
import com.aanglearning.parentapp.model.Album;
import com.aanglearning.parentapp.model.DeletedAlbum;

import java.util.List;

/**
 * Created by Vinay on 30-10-2017.
 */

class GalleryPresenterImpl implements GalleryPresenter, GalleryInteractor.OnFinishedListener {
    private GalleryView mView;
    private GalleryInteractor mInteractor;

    GalleryPresenterImpl(GalleryView view, GalleryInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void getAlbumsAboveId(long schoolId, long id) {
        if (mView != null) {
            mInteractor.getAlbumsAboveId(schoolId, id, this);
        }
    }

    @Override
    public void getAlbums(long schoolId) {
        if (mView != null) {
            mView.showProgress();
            mInteractor.getAlbums(schoolId, this);
        }
    }

    @Override
    public void getRecentDeletedAlbums(long schoolId, long id) {
        if (mView != null) {
            mInteractor.getRecentDeletedAlbums(schoolId, id, this);
        }
    }

    @Override
    public void getDeletedAlbums(long schoolId) {
        if (mView != null) {
            mInteractor.getDeletedAlbums(schoolId, this);
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
    public void onRecentAlbumsReceived(List<Album> albumList) {
        if (mView != null) {
            mView.setRecentAlbums(albumList);
            mView.hideProgress();
        }
    }

    @Override
    public void onAlbumsReceived(List<Album> groupsList) {
        if (mView != null) {
            mView.setAlbums(groupsList);
            mView.hideProgress();
        }
    }

    @Override
    public void onDeletedAlbumsReceived(List<DeletedAlbum> deletedAlbums) {
        if (mView != null) {
            DeletedAlbumDao.insertDeletedAlbums(deletedAlbums);
        }
    }
}
