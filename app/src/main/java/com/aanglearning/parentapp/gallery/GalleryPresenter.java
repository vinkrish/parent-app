package com.aanglearning.parentapp.gallery;

import com.aanglearning.parentapp.model.Album;
import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.model.DeletedAlbum;

/**
 * Created by Vinay on 30-10-2017.
 */

interface GalleryPresenter {
    void getAlbumsAboveId(ChildInfo childInfo, long id);

    void getAlbums(ChildInfo childInfo);

    void getRecentDeletedAlbums(ChildInfo childInfo, long id);

    void getDeletedAlbums(ChildInfo childInfo);

    void onDestroy();
}
