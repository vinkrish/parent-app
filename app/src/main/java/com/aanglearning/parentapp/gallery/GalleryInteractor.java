package com.aanglearning.parentapp.gallery;

import com.aanglearning.parentapp.model.Album;
import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.model.DeletedAlbum;

import java.util.List;

/**
 * Created by Vinay on 30-10-2017.
 */

interface GalleryInteractor {
    interface OnFinishedListener {
        void onError(String message);

        void onRecentAlbumsReceived(List<Album> albumList);

        void onAlbumsReceived(List<Album> albumList);

        void onDeletedAlbumsReceived(List<DeletedAlbum> deletedAlbums);
    }

    void getAlbumsAboveId(ChildInfo childInfo, long id, GalleryInteractor.OnFinishedListener listener);

    void getAlbums(ChildInfo childInfo, GalleryInteractor.OnFinishedListener listener);

    void getRecentDeletedAlbums(ChildInfo childInfo, long id, GalleryInteractor.OnFinishedListener listener);

    void getDeletedAlbums(ChildInfo childInfo, GalleryInteractor.OnFinishedListener listener);
}
