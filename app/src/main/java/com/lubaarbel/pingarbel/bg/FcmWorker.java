package com.lubaarbel.pingarbel.bg;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lubaarbel.pingarbel.model.UserInputModel;
import com.lubaarbel.pingarbel.network.fcm.FirebaseNotificationSendingService;
import com.lubaarbel.pingarbel.network.fcm.model.FirebaseNotificationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        FirebaseNotificationSendingService sendingService = new FirebaseNotificationSendingService();
        sendingService.sendPushNotificationWithDataViaFirebase(
                UserInputModel.getInstance().getUserInputEncrypted(),
                new Callback<FirebaseNotificationResponse>() {
                    @Override
                    public void onResponse(Call<FirebaseNotificationResponse> call, Response<FirebaseNotificationResponse> response) {
                        Log.i(TAG, "FirebaseNotification onResponse:: " + response.code());
                        UserInputModel.getInstance().serUserInputEncrypted(null);
                    }

                    @Override
                    public void onFailure(Call<FirebaseNotificationResponse> call, Throwable t) {
                        Log.i(TAG, "FirebaseNotification onFailure:: " + t.getMessage());
                    }
                }
        );
    }
}
