package com.mtpixels.firebasepractice.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.mtpixels.firebasepractice.R;
import com.mtpixels.firebasepractice.Utility.ScreenCustomization;

public class BaseActivity extends AppCompatActivity {

    FirebaseAuth.AuthStateListener listener;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenCustomization.setFullScreen(this);
        fixTopPadding();
        setAuthListener();
    }

    private void setAuthListener() {
        listener = firebaseAuth -> {
            if (auth.getCurrentUser() == null) {
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                auth.removeAuthStateListener(listener);
            }
        };
    }

    void fixTopPadding() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar == null) {
            return;
        }
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) toolbar.getLayoutParams();
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            int statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            params.height = params.height + statusBarHeight;
            toolbar.setLayoutParams(params);
            toolbar.setPadding(toolbar.getPaddingLeft(), toolbar.getPaddingTop() + statusBarHeight, toolbar.getPaddingRight(), toolbar.getPaddingBottom());
        }
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        auth.addAuthStateListener(listener);
    }
}