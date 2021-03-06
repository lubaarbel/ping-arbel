package com.lubaarbel.pingarbel;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.lubaarbel.pingarbel.bg.NotificationSender;

/**
 * Helper class of lifeCycle callback to detect when the app goes to background
 * **/
public class AppLifecycleListener implements LifecycleObserver {
    private static final String TAG = AppLifecycleListener.class.getSimpleName();

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() {
        Log.i(TAG, "app onMoveToForeground");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onMoveToBackground() {
        Log.i(TAG, "app onMoveToBackground");

        NotificationSender sender = new NotificationSender();
        sender.sendNotificationWithDelayIfNeeded(15);
    }
}
