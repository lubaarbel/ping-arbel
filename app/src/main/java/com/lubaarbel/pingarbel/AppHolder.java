package com.lubaarbel.pingarbel;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.lifecycle.ProcessLifecycleOwner;

public class AppHolder extends Application {
    public static final String CHANNEL_ID_USER_INPUT = "CHANNEL_ID_USER_INPUT";

    private static Application sApplication;
    private static Context sApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sApplication = this;
        sApplicationContext = getApplicationContext();

        createNotificationChannel();

        ProcessLifecycleOwner.get().getLifecycle().addObserver(new AppLifecycleListener());
    }

    public static Context getContext() {
        return sApplicationContext;
    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID_USER_INPUT,
                "User Input Channel",
                NotificationManager.IMPORTANCE_DEFAULT
        );

        channel.setDescription("Notifying user on input changes");
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
    }
}
