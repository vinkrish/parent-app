package com.aanglearning.parentapp.gallery;

import com.aanglearning.parentapp.App;
import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.api.ApiClient;
import com.aanglearning.parentapp.api.GalleryApi;
import com.aanglearning.parentapp.model.Album;
import com.aanglearning.parentapp.model.DeletedAlbum;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vinay on 30-10-2017.
 */

class GalleryInteractorImpl implements GalleryInteractor {

    @Override
    public void getAlbumsAboveId(long schoolId, long id, final OnFinishedListener listener) {
        GalleryApi api = ApiClient.getAuthorizedClient().create(GalleryApi.class);

        Call<List<Album>> queue = api.getAlbumAboveId(schoolId, id);
        queue.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                if(response.isSuccessful()) {
                    listener.onRecentAlbumsReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getAlbums(long schoolId, final OnFinishedListener listener) {
        GalleryApi api = ApiClient.getAuthorizedClient().create(GalleryApi.class);

        Call<List<Album>> queue = api.getAlbums(schoolId);
        queue.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                if(response.isSuccessful()) {
                    listener.onAlbumsReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getRecentDeletedAlbums(long schoolId, long id, final OnFinishedListener listener) {
        GalleryApi api = ApiClient.getAuthorizedClient().create(GalleryApi.class);

        Call<List<DeletedAlbum>> queue = api.getDeletedAlbumsAboveId(schoolId, id);
        queue.enqueue(new Callback<List<DeletedAlbum>>() {
            @Override
            public void onResponse(Call<List<DeletedAlbum>> call, Response<List<DeletedAlbum>> response) {
                if(response.isSuccessful()) {
                    listener.onDeletedAlbumsReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<List<DeletedAlbum>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void getDeletedAlbums(long schoolId, final OnFinishedListener listener) {
        GalleryApi api = ApiClient.getAuthorizedClient().create(GalleryApi.class);

        Call<List<DeletedAlbum>> queue = api.getDeletedAlbums(schoolId);
        queue.enqueue(new Callback<List<DeletedAlbum>>() {
            @Override
            public void onResponse(Call<List<DeletedAlbum>> call, Response<List<DeletedAlbum>> response) {
                if(response.isSuccessful()) {
                    listener.onDeletedAlbumsReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<List<DeletedAlbum>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }
}
