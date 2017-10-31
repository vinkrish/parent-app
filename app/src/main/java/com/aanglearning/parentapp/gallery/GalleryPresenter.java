package com.aanglearning.parentapp.gallery;

import com.aanglearning.parentapp.model.Album;
import com.aanglearning.parentapp.model.DeletedAlbum;

/**
 * Created by Vinay on 30-10-2017.
 */

interface GalleryPresenter {
    void getAlbumsAboveId(long schoolId, long id);

    void getAlbums(long schoolId);

    void getRecentDeletedAlbums(long schoolId, long id);

    void getDeletedAlbums(long schoolId);

    void onDestroy();
}
