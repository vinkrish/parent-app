package com.aanglearning.parentapp.api;

import com.aanglearning.parentapp.model.Album;
import com.aanglearning.parentapp.model.AlbumImage;
import com.aanglearning.parentapp.model.DeletedAlbum;
import com.aanglearning.parentapp.model.DeletedAlbumImage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Vinay on 29-10-2017.
 */

public interface GalleryApi {

    @GET("album/{albumId}")
    Call<Album> getAlbum(@Path("albumId") long albumId);

    @GET("album/student/{schoolId}/{classId}/{sectionId}/{id}")
    Call<List<Album>> getAlbumAboveId(@Path("schoolId") long schoolId,
                                      @Path("classId") long classId,
                                      @Path("sectionId") long sectionId,
                                      @Path("id") long id);

    @GET("album/student/{schoolId}/{classId}/{sectionId}")
    Call<List<Album>> getAlbums(@Path("schoolId") long schoolId,
                                @Path("classId") long classId,
                                @Path("sectionId") long sectionId);

    @GET("deletedalbum/student/{schoolId}/{classId}/{sectionId}/{id}")
    Call<List<DeletedAlbum>> getDeletedAlbumsAboveId(@Path("schoolId") long schoolId,
                                                     @Path("classId") long classId,
                                                     @Path("sectionId") long sectionId,
                                                     @Path("id") long id);

    @GET("deletedalbum/student/{schoolId}/{classId}/{sectionId}")
    Call<List<DeletedAlbum>> getDeletedAlbums(@Path("schoolId") long schoolId,
                                              @Path("classId") long classId,
                                              @Path("sectionId") long sectionId);

    @GET("ai/{id}/album/{albumId}")
    Call<ArrayList<AlbumImage>> getAlbumImagesAboveId(@Path("albumId") long albumId,
                                                      @Path("id") long id);

    @GET("ai/album/{albumId}")
    Call<ArrayList<AlbumImage>> getAlbumImages(@Path("albumId") long albumId);

    @GET("deletedai/{id}/album/{albumId}")
    Call<List<DeletedAlbumImage>> getDeletedAlbumImagesAboveId(@Path("albumId") long albumId,
                                                               @Path("id") long id);

    @GET("deletedai/album/{albumId}")
    Call<List<DeletedAlbumImage>> getDeletedAlbumImages(@Path("albumId") long albumId);

}
