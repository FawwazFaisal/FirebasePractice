package com.mtpixels.firebasepractice.CustomListeners;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

/**
 * Created by S.M.Mubbashir.A.Z. on 4/14/2021.
 */
public class CustomTextChangeClient implements TextWatcher {

    View mView;
    CustomTextChangeListener mCallbackConsumer;

    public static void addListener(View view, CustomTextChangeListener listener){
        CustomTextChangeClient client = new CustomTextChangeClient();
        client.mView = view;
        client.mCallbackConsumer = listener;
        if(client.mView instanceof TextInputLayout){
            ((TextInputLayout) client.mView).getEditText().addTextChangedListener(client);
        }else {
            ((EditText)client.mView).addTextChangedListener(client);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        mCallbackConsumer.beforeTextChanged(s,start,count,after,mView);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mCallbackConsumer.onTextChanged(s,start,before,count,mView);
    }

    @Override
    public void afterTextChanged(Editable s) {
        mCallbackConsumer.afterTextChanged(s,mView);
    }
}
