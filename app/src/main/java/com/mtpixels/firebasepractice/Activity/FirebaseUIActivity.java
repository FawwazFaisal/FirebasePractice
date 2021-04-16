package com.mtpixels.firebasepractice.Activity;

import android.os.Bundle;

import com.mtpixels.firebasepractice.App;
import com.mtpixels.firebasepractice.CustomListeners.CustomRetrofitCallback;
import com.mtpixels.firebasepractice.FCM.Data;
import com.mtpixels.firebasepractice.FCM.FcmRequest;
import com.mtpixels.firebasepractice.FCM.FcmResponse;
import com.mtpixels.firebasepractice.FCM.Notifier;
import com.mtpixels.firebasepractice.databinding.ActivityFirebaseUIBinding;

import retrofit2.Call;
import retrofit2.Response;

public class FirebaseUIActivity extends BaseActivity implements CustomRetrofitCallback {

    ActivityFirebaseUIBinding bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bd = ActivityFirebaseUIBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        super.onCreate(savedInstanceState);
        FcmRequest request = new FcmRequest();
        Data data = new Data();
        data.setMessage("Testing");
        data.setTitle("Test");
        request.setData(data);
        request.setTo(App.getUser().getFcmToken());
        Notifier.sendNotification(request,this);
    }

    @Override
    public void onResponse(Call call, Response response, String tag) {
        if(tag.equals("notification")){
            if(response.code()==200){
                if(((FcmResponse)response.body()).success == 1){

                }
            }
        }
    }

    @Override
    public void onFailure(Call call, Throwable t, String tag) {

    }
}