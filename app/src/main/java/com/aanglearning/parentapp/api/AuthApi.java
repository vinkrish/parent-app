package com.aanglearning.parentapp.api;

import com.aanglearning.parentapp.model.Authorization;
import com.aanglearning.parentapp.model.CommonResponse;
import com.aanglearning.parentapp.model.Credentials;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Vinay on 16-02-2017.
 */

public interface AuthApi {

    @Headers("content-type: application/json")
    @POST("parent/login")
    Call<Credentials> login(@Body Credentials credentials);

    @Headers("content-type: application/json")
    @POST("sms/student/{username}")
    Call<Void> newPassword(@Path("username") String username);

    @Headers("content-type: application/json")
    @POST("authorization/fcm")
    Call<Void> updateFcmToken(@Body Authorization authorization);

}
