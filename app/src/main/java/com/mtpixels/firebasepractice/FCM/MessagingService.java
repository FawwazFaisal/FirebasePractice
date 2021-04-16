package com.mtpixels.firebasepractice.FCM;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mtpixels.firebasepractice.Activity.FirebaseUIActivity;
import com.mtpixels.firebasepractice.App;
import com.mtpixels.firebasepractice.R;

import static com.mtpixels.firebasepractice.Utility.Constants.FCM_NOTIFICATION_CHANNEL;

/**
 * Created by S.M.Mubbashir.A.Z. on 4/15/2021.
 */

public class MessagingService extends FirebaseMessagingService {

    private String title;
    private String message;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        FirebaseFirestore.getInstance().collection("users").document(App.getUser().getEmail()).update("fcmToken", s).addOnSuccessListener(aVoid -> App.getUser().setFcmToken(s));
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        title = remoteMessage.getData().get("title");
        message = remoteMessage.getData().get("message");
        createNotification();
    }

    private void createNotification() {
        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(FCM_NOTIFICATION_CHANNEL, "MVVM", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("MVVM");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, FCM_NOTIFICATION_CHANNEL);

        Intent intent = new Intent(this, FirebaseUIActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setAutoCancel(true)
                .setColor(Color.RED)
                .setOnlyAlertOnce(true)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message)
                        .setBigContentTitle(title)
                        .setSummaryText("Details"))
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_baseline_person_pin_24)
                .setShowWhen(true)
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle(title)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentText(message);


        manager.notify(1, notificationBuilder.build());
    }
}
