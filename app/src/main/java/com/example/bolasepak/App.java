package com.example.bolasepak;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

public class App extends Application {
    public static final String CHANNEL_ID = "stepServiceChanel";
    public static final String CHANNEL_2_ID = "matchNotificationChanel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }
    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Step Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );

            NotificationChannel matchChannel = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Match Notification Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
            manager.createNotificationChannel(matchChannel);
        }
    }

}
