package com.lubaarbel.pingarbel.bg;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lubaarbel.pingarbel.network.Repository;

public class FcmWorker extends Worker {
    public static final String TAG = FcmWorker.class.getSimpleName();

    public FcmWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        sendNotificationRequestToFirebase();
        return Result.success();
    }

    private void sendNotificationRequestToFirebase() {
        Repository.getInstance().scheduleDataPushNotification();
    }
}
