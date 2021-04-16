package com.mtpixels.firebasepractice.Retrofit;

import com.mtpixels.firebasepractice.FCM.FcmRequest;
import com.mtpixels.firebasepractice.FCM.FcmResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by S.M.Mubbashir.A.Z. on 4/15/2021.
 */
public interface RetrofitAPI {

    @Headers(
            {
                    "Content-Type:appication/json",
                    "authorization-key:AAAADMb3ftc:APA91bEhJrgPLHuo6cEy2Uksw7l3LTHcNwCoqsDhJHc9Ir2y8WLLJaN0qPyPk5-PlQNsW8nm9IVtgPlFf65_cuG-TJx-KGIE6pMT-yM6zpd_GOAHZmtNa8FX7M4pqss51fy32PQphA2l"
            }
    )
    @POST("fcm/send")
    Call<FcmResponse> sendNotification(@Body FcmRequest request);
}
