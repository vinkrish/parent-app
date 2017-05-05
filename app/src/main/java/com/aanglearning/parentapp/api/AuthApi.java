package com.aanglearning.parentapp.api;

import com.aanglearning.parentapp.model.CommonResponse;
import com.aanglearning.parentapp.model.Credentials;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Vinay on 16-02-2017.
 */

public interface AuthApi {

    @Headers("content-type: application/json")
    @POST("parent/login")
    Call<Credentials> login(@Body Credentials credentials);

    @Headers("content-type: application/json")
    @POST("parent/newPassword")
    Call<CommonResponse> newPassword(@Body String updatedPassword);

}
