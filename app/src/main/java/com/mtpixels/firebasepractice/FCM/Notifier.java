package com.mtpixels.firebasepractice.FCM;

import com.mtpixels.firebasepractice.CustomListeners.CustomRetrofitCallback;
import com.mtpixels.firebasepractice.CustomListeners.RetrofitCallbackClient;
import com.mtpixels.firebasepractice.Retrofit.RetrofitClient;
import com.mtpixels.firebasepractice.Utility.Constants;

import retrofit2.Call;

/**
 * Created by S.M.Mubbashir.A.Z. on 4/15/2021.
 */

public class Notifier {
    public static void sendNotification(FcmRequest request, CustomRetrofitCallback callback){
        Call<FcmResponse> notificationCall = RetrofitClient.getClient(Constants.FCM_BASE_URL).api.sendNotification(request);
        RetrofitCallbackClient.enqueue(notificationCall, "notification", callback);
    }
}
