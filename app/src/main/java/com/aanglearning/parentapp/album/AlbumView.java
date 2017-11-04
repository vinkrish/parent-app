package com.aanglearning.parentapp.album;

import com.aanglearning.parentapp.model.AlbumImage;

import java.util.ArrayList;
/**
 * Created by Vinay on 01-11-2017.
 */

interface AlbumView {
    void showProgress();

    void hideProgress();

    void showError(String message);

    void setRecentAlbumImages(ArrayList<AlbumImage> albumImages);

    void setAlbumImages(ArrayList<AlbumImage> albumImages);

    void onDeletedAlbumImageSync();

}
