package com.lubaarbel.pingarbel.bg;

import androidx.work.Constraints;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.lubaarbel.pingarbel.AppHolder;

import java.util.concurrent.TimeUnit;

public class NotificationSender {

    public void sendNotificationWithDelay(long delayInSec) {
        Constraints constraints = new Constraints
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
