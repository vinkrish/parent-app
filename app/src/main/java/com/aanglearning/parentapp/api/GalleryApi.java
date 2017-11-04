package com.aanglearning.parentapp.api;

import com.aanglearning.parentapp.model.Album;
import com.aanglearning.parentapp.model.AlbumImage;
import com.aanglearning.parentapp.model.DeletedAlbum;
import com.aanglearning.parentapp.model.DeletedAlbumImage;
import com.aanglearning.parentapp.model.DeletedSubAlbum;
import com.aanglearning.parentapp.model.DeletedSubAlbumImage;
import com.aanglearning.parentapp.model.SubAlbum;
import com.aanglearning.parentapp.model.SubAlbumImage;

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

    @GET("album/{id}/school/{schoolId}")
    Call<List<Album>> getAlbumAboveId(@Path("schoolId") long schoolId,
                                      @Path("id") long id);

    @GET("album/school/{schoolId}")
    Call<List<Album>> getAlbums(@Path("schoolId") long schoolId);

    @GET("deletedalbum/{id}/school/{schoolId}")
    Call<List<DeletedAlbum>> getDeletedAlbumsAboveId(@Path("schoolId") long schoolId,
                                                     @Path("id") long id);

    @GET("deletedalbum/school/{schoolId}")
    Call<List<DeletedAlbum>> getDeletedAlbums(@Path("schoolId") long schoolId);

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

    @GET("subalbum/{id}/album/{albumId}")
    Call<List<SubAlbum>> getSubAlbumAboveId(@Path("albumId") long albumId,
                                            @Path("id") long id);

    @GET("subalbum/album/{albumId}")
    Call<List<SubAlbum>> getSubAlbums(@Path("albumId") long albumId);

    @GET("deletedsubalbum/{id}/album/{albumId}")
    Call<List<DeletedSubAlbum>> getDeletedSubAlbumsAboveId(@Path("albumId") long albumId,
                                                           @Path("id") long id);

    @GET("deletedsubalbum/album/{albumId}")
    Call<List<DeletedSubAlbum>> getDeletedSubAlbums(@Path("albumId") long albumId);

    @GET("sai/{id}/subalbum/{subAlbumId}")
    Call<List<SubAlbumImage>> getSubAlbumImagesAboveId(@Path("subAlbumId") long subAlbumId,
                                                       @Path("id") long id);

    @GET("sai/subalbum/{subAlbumId}")
    Call<List<SubAlbumImage>> getSubAlbumImages(@Path("subAlbumId") long subAlbumId);

    @GET("deletedsai/{id}/subalbum/{subalbumId}")
    Call<List<DeletedSubAlbumImage>> getDeletedSubAlbumImagesAboveId(@Path("subalbumId") long subalbumId,
                                                                     @Path("id") long id);

    @GET("deletedsai/subalbum/{subalbumId}")
    Call<List<DeletedSubAlbumImage>> getDeletedSubAlbumImages(@Path("subalbumId") long subalbumId);

}
