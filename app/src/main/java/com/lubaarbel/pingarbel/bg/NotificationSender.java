package com.lubaarbel.pingarbel.bg;

import android.util.Log;

import androidx.work.Constraints;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.lubaarbel.pingarbel.AppHolder;
import com.lubaarbel.pingarbel.model.UserInputModel;

import java.util.concurrent.TimeUnit;

public class NotificationSender {
    public static final String TAG = NotificationSender.class.getSimpleName();

    public void sendNotificationWithDelayIfNeeded(long delayInSec) {
        // if there is a value to send...
        String value = UserInputModel.getInstance().getUserInputEncrypted();
        if (value == null || value.isEmpty()) return;

        Log.i(TAG, "NotificationSender:: prepare WorkManager to start in 15 sec");

        Constraints constraints = new Constraints // optional
                .Builder()
                .setRequiresBatteryNotLow(true)
                .build();

        OneTimeWorkRequest request = new OneTimeWorkRequest
                .Builder(FcmWorker.class)
                .setInitialDelay(delayInSec, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(AppHolder.getContext())
                .enqueue(request);
    }
}
