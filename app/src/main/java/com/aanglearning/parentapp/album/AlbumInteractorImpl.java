package com.aanglearning.parentapp.album;

import com.aanglearning.parentapp.App;
import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.api.ApiClient;
import com.aanglearning.parentapp.api.GalleryApi;
import com.aanglearning.parentapp.dao.AlbumDao;
import com.aanglearning.parentapp.model.Album;
import com.aanglearning.parentapp.model.AlbumImage;
import com.aanglearning.parentapp.model.DeletedAlbumImage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vinay on 01-11-2017.
 */

class AlbumInteractorImpl implements AlbumInteractor {
    @Override
    public void getAlbumUpdate(long albumId, final OnFinishedListener listener) {
        GalleryApi api = ApiClient.getAuthorizedClient().create(GalleryApi.class);

        Call<Album> queue = api.getAlbum(albumId);
        queue.enqueue(new Callback<Album>() {
            @Override
            public void onResponse(Call<Album> call, Response<Album> response) {
                if(response.isSuccessful()) {
                    AlbumDao.update(response.body());
                }
            }

            @Override
            public void onFailure(Call<Album> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getAlbumImagesAboveId(long albumId, long id, final OnFinishedListener listener) {
        GalleryApi api = ApiClient.getAuthorizedClient().create(GalleryApi.class);

        Call<ArrayList<AlbumImage>> queue = api.getAlbumImagesAboveId(albumId, id);
        queue.enqueue(new Callback<ArrayList<AlbumImage>>() {
            @Override
            public void onResponse(Call<ArrayList<AlbumImage>> call, Response<ArrayList<AlbumImage>> response) {
                if(response.isSuccessful()) {
                    listener.onRecentAlbumImagesReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AlbumImage>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getAlbumImages(long albumId, final OnFinishedListener listener) {
        GalleryApi api = ApiClient.getAuthorizedClient().create(GalleryApi.class);

        Call<ArrayList<AlbumImage>> queue = api.getAlbumImages(albumId);
        queue.enqueue(new Callback<ArrayList<AlbumImage>>() {
            @Override
            public void onResponse(Call<ArrayList<AlbumImage>> call, Response<ArrayList<AlbumImage>> response) {
                if(response.isSuccessful()) {
                    listener.onAlbumImagesReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AlbumImage>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getRecentDeletedAlbumImages(long albumId, long id, final OnFinishedListener listener) {
        GalleryApi api = ApiClient.getAuthorizedClient().create(GalleryApi.class);

        Call<List<DeletedAlbumImage>> queue = api.getDeletedAlbumImagesAboveId(albumId, id);
        queue.enqueue(new Callback<List<DeletedAlbumImage>>() {
            @Override
            public void onResponse(Call<List<DeletedAlbumImage>> call, Response<List<DeletedAlbumImage>> response) {
                if(response.isSuccessful()) {
                    listener.onAlbumImagesDeleted(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<List<DeletedAlbumImage>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getDeletedAlbumImages(long albumId, final OnFinishedListener listener) {
        GalleryApi api = ApiClient.getAuthorizedClient().create(GalleryApi.class);

        Call<List<DeletedAlbumImage>> queue = api.getDeletedAlbumImages(albumId);
        queue.enqueue(new Callback<List<DeletedAlbumImage>>() {
            @Override
            public void onResponse(Call<List<DeletedAlbumImage>> call, Response<List<DeletedAlbumImage>> response) {
                if(response.isSuccessful()) {
                    listener.onAlbumImagesDeleted(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<List<DeletedAlbumImage>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }
}
