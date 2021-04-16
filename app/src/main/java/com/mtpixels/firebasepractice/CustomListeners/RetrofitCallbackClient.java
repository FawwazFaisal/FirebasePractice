package com.mtpixels.firebasepractice.CustomListeners;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by S.M.Mubbashir.A.Z. on 4/15/2021.
 */
public class RetrofitCallbackClient implements Callback {
    CustomRetrofitCallback callback;
    String tag;

    private RetrofitCallbackClient(String tag, CustomRetrofitCallback callback){
        this.callback = callback;
        this.tag = tag;
    }

    public static void enqueue(Call call, String tag, CustomRetrofitCallback callback){
        RetrofitCallbackClient callbackClient = new RetrofitCallbackClient(tag, callback);
        call.enqueue(callbackClient);
    }

    @Override
    public void onResponse(Call call, Response response) {
        callback.onResponse(call, response, tag);
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        callback.onFailure(call, t, tag);
    }
}
