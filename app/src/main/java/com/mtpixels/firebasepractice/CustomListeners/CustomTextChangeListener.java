package com.mtpixels.firebasepractice.CustomListeners;

import android.text.Editable;
import android.view.View;

/**
 * Created by S.M.Mubbashir.A.Z. on 4/14/2021.
 */
public interface CustomTextChangeListener {

    public void beforeTextChanged(CharSequence s, int start, int count, int after, View view);

    public void onTextChanged(CharSequence s, int start, int before, int count, View view);

    public void afterTextChanged(Editable s, View view);
}
