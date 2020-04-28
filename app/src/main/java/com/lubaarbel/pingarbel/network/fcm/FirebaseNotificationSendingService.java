package com.lubaarbel.pingarbel.network.fcm;


import android.app.ApplicationErrorReport;
import android.util.Log;

import com.lubaarbel.pingarbel.AppHolder;
import com.lubaarbel.pingarbel.R;
import com.lubaarbel.pingarbel.network.Repository;
import com.lubaarbel.pingarbel.network.fcm.model.Data;
import com.lubaarbel.pingarbel.network.fcm.model.FirebaseNotificationRequestBody;
import com.lubaarbel.pingarbel.network.fcm.model.FirebaseNotificationResponse;
import com.lubaarbel.pingarbel.network.fcm.model.Notification;
import com.lubaarbel.pingarbel.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirebaseNotificationSendingService {
    private static final String TAG = FirebaseNotificationSendingService.class.getSimpleName();

    public void sendPushNotificationWithDataViaFirebase(String data, Callback<FirebaseNotificationResponse> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Repository.FIREBASE_NOTIFICATION_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FirebaseNotificationApiService service = retrofit.create(FirebaseNotificationApiService.class);
        FirebaseNotificationRequestBody body = generateFirebaseNotificationRequestBody(data);
        String authHeaderValue = "key=" + AppHolder.getContext().getString(R.string.firebase_fcm_server_key);
        Call<FirebaseNotificationResponse> call = service.sendPushNotificationWithData(
                body,
                authHeaderValue);
        call.enqueue(callback);
        Log.i(TAG, "send push notification with data via firebase url: " + call.request().url());
    }

    private FirebaseNotificationRequestBody generateFirebaseNotificationRequestBody(String encStr) {
        Notification notification = new Notification();
        notification.click_action = "OPEN_ACTIVITY_1";
        notification.body = "I come with DATA";
        notification.title = "New input available";

        Data data = new Data();
        data.userInput = encStr;
        data.isScheduled = "true";
        // in 15 seconds
        data.scheduledTime = Utils.formatDateString(System.currentTimeMillis() + 15*1000);

        FirebaseNotificationRequestBody body = new FirebaseNotificationRequestBody(
                notification,
                data,
                "/topics/input",
                "high");
        return body;
    }
}
