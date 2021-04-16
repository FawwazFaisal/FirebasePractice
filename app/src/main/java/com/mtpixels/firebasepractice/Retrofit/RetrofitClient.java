package com.mtpixels.firebasepractice.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by S.M.Mubbashir.A.Z. on 4/15/2021.
 */
public class RetrofitClient {

    public RetrofitAPI api;
    private static RetrofitClient client;

    public static RetrofitClient getClient(String baseUrl) {
        if(client==null){
            client = new RetrofitClient(baseUrl);
        }
        return client;
    }
    private RetrofitClient(String baseUrl){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        api = retrofit.create(RetrofitAPI.class);
    }
}
