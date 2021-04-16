package com.mtpixels.firebasepractice.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mtpixels.firebasepractice.App;
import com.mtpixels.firebasepractice.CustomListeners.CustomTextChangeClient;
import com.mtpixels.firebasepractice.CustomListeners.CustomTextChangeListener;
import com.mtpixels.firebasepractice.Models.Users;
import com.mtpixels.firebasepractice.databinding.ActivityLoginBinding;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends BaseActivity implements View.OnClickListener, CustomTextChangeListener {

    static String verificationId = "";
    ActivityLoginBinding bd;
    PhoneAuthProvider.ForceResendingToken token;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth.AuthStateListener listener;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks phoneAuthCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            bd.OTP.getEditText().setText(phoneAuthCredential.getSmsCode());
            phoneLogin(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            token = forceResendingToken;
            verificationId = s;
            bd.OTP.setVisibility(View.VISIBLE);
            bd.loginBtn.setText("Register");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bd = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        super.onCreate(savedInstanceState);
        setListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        listener = firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() != null
                    && firebaseAuth.getCurrentUser().isEmailVerified()
                    && !firebaseAuth.getCurrentUser().getEmail().isEmpty()) {
                firestore.collection("users").document(auth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            App.setUser(task.getResult().toObject(Users.class));
                            initFCM();
                        }
                    }
                });
            }
        };
        auth.addAuthStateListener(listener);
    }

//    //sync
//    private boolean existsInDb(String email) {
//        final boolean[] isValid = {false};
//        Executors.newSingleThreadExecutor().execute(() -> {
//            try {
//                DocumentSnapshot snapshot = Tasks.await(firestore.collection("users").document(email).get());
//                if (snapshot != null && snapshot.exists()) {
//                    isValid[0] = true;
//                    App.setUser(snapshot.toObject(Users.class));
//                }
//            } catch (Exception e) {
//                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        return isValid[0];
//    }

    public void setListener() {
        bd.loginBtn.setOnClickListener(this);
        bd.register.setOnClickListener(this);
        CustomTextChangeClient.addListener(bd.email, this);
        CustomTextChangeClient.addListener(bd.firstName, this);
        CustomTextChangeClient.addListener(bd.lastName, this);
        CustomTextChangeClient.addListener(bd.phoneNo, this);
    }

    public void login() {
        auth.signInWithEmailAndPassword(bd.email.getEditText().getText().toString(), bd.password.getEditText().getText().toString()).addOnCompleteListener(authTask -> {
            if (authTask.isSuccessful() && auth.getCurrentUser().isEmailVerified()) {
                firestore.collection("users").document(bd.email.getEditText().getText().toString()).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        App.setUser(task.getResult().toObject(Users.class));
                        initFCM();
                    }
                });
            } else {
                Toast.makeText(LoginActivity.this, "There was a problem with completing the task", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initFCM() {
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                firestore.collection("users").document(App.getUser().getEmail()).update("fcmToken", task.getResult()).addOnCompleteListener(task1 -> {
                    App.getUser().setFcmToken(task.getResult());
                    startActivity(new Intent(LoginActivity.this, FirebaseUIActivity.class));
                });
            }
        });
    }

    public void phoneLogin(PhoneAuthCredential phoneAuthCredential) {
        if (phoneAuthCredential == null) {
            phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, bd.OTP.getEditText().getText().toString());
        }
        auth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                auth.signOut();
                createAccount();
            } else {
                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createAccount() {
        auth.createUserWithEmailAndPassword(bd.email.getEditText().getText().toString(), bd.password.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    addToDB();
                } else {
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addToDB() {
        firestore.collection("users").document(bd.email.getEditText().getText().toString()).set(App.getUser()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                auth.signOut();
                                Toast.makeText(LoginActivity.this, "Verification Link Sent", Toast.LENGTH_SHORT).show();
                                hideRegistrationUI();
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void verifyPhone() {
        PhoneAuthOptions.Builder optionsBuilder = PhoneAuthOptions.newBuilder();
        optionsBuilder.setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setPhoneNumber("+92" + bd.phoneNo.getEditText().getText().toString())
                .setCallbacks(phoneAuthCallbacks)
                .setActivity(this);

        if (token != null) {
            optionsBuilder.setForceResendingToken(token);
        }
        PhoneAuthOptions options = optionsBuilder.build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void showRegistrationUI() {
        bd.firstName.setVisibility(View.VISIBLE);
        bd.lastName.setVisibility(View.VISIBLE);
        bd.phoneNo.setVisibility(View.VISIBLE);
        bd.loginBtn.setText("Verify");
        bd.register.setText("Login");
    }

    private void hideRegistrationUI() {
        bd.firstName.setVisibility(View.GONE);
        bd.lastName.setVisibility(View.GONE);
        bd.phoneNo.setVisibility(View.GONE);
        bd.OTP.setVisibility(View.GONE);
        bd.loginBtn.setText("Login");
        bd.register.setText("Get registered now");
    }

    @Override
    public void onClick(View v) {
        auth.removeAuthStateListener(listener);
        if (v.getId() == bd.loginBtn.getId()) {
            if (((Button) v).getText().toString().toLowerCase().equals("login")) {
                login();
            } else if (((Button) v).getText().toString().toLowerCase().equals("verify")) {
                verifyPhone();
            } else {
                phoneLogin(null);
            }
        } else if (v.getId() == bd.register.getId()) {
            if (bd.register.getText().toString().equals("Login")) {
                hideRegistrationUI();
            } else {
                showRegistrationUI();
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after, View view) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count, View view) {

    }

    @Override
    public void afterTextChanged(Editable s, View view) {
        if (view.getTag().toString().equals("email")) {
            App.getUser().setEmail(s.toString());
        } else if (view.getTag().toString().equals("firstName")) {
            App.getUser().setName(s.toString());
        } else if (view.getTag().toString().equals("lastName")) {
            App.getUser().setLastName(s.toString());
        } else if (view.getTag().toString().equals("phoneNo")) {
            App.getUser().setPhoneNo(s.toString());
        }
    }
}