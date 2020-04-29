package com.lubaarbel.pingarbel.bg;

import androidx.work.Constraints;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.lubaarbel.pingarbel.AppHolder;
import com.lubaarbel.pingarbel.model.UserInputModel;

import java.util.concurrent.TimeUnit;

public class NotificationSender {

    public void sendNotificationWithDelayIfNeeded(long delayInSec) {
        // if there is a value to send...
        String value = UserInputModel.getInstance().getUserInputEncrypted();
        if (value == null || value.isEmpty()) return;

        // schedule to send value
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
