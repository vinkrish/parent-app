package com.aanglearning.parentapp.api;

import com.aanglearning.parentapp.App;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Vinay on 16-02-2017.
 */

public class ApiClient {
    //private static final String BASE_URL = "http://192.168.1.3:8080/guldu/webapi/";
    private static final String BASE_URL = "http://Custom-env.mkitxvjm8m.us-west-2.elasticbeanstalk.com/webapi/";
    private static Retrofit authRetrofit = null;
    private static Retrofit retrofit = null;

    public static Retrofit getAuthorizedClient() {
        //HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        //logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("Authorization",
                                        "Bearer " + SharedPreferenceUtil.getUser(App.getInstance()).getAuthToken())
                                .header("Content-Type", "application/json");
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                })
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        if (authRetrofit == null) {
            authRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return authRetrofit;
    }

    public static Retrofit getClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
