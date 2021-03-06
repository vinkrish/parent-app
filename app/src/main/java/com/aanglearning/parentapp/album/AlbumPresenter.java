package com.aanglearning.parentapp.album;

/**
 * Created by Vinay on 01-11-2017.
 */

interface AlbumPresenter {
    void getAlbumUpdate(long albumId);

    void getAlbumImagesAboveId(long albumId, long id);

    void getAlbumImages(long albumId);

    void getRecentDeletedAlbumImages(long albumId, long id);

    void getDeletedAlbumImages(long albumId);

    void onDestroy();
}
