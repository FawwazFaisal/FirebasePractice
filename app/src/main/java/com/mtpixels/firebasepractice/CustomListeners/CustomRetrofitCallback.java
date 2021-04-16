package com.mtpixels.firebasepractice.CustomListeners;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by S.M.Mubbashir.A.Z. on 4/15/2021.
 */
public interface CustomRetrofitCallback {
    public void onResponse(Call call, Response response, String tag);
    public void onFailure(Call call, Throwable t, String tag);
}
