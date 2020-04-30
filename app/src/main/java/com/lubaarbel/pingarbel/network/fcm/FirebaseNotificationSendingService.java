package com.lubaarbel.pingarbel.network.fcm;

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

    private static final String HEADER_KEY = "key=";
    private static final String BODY_TO_VALUE = "/topics/input";
    private static final String BODY_PRIORITY = "high";

    public void sendPushNotificationWithDataViaFirebase(String data, Callback<FirebaseNotificationResponse> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Repository.FIREBASE_NOTIFICATION_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FirebaseNotificationApiService service = retrofit.create(FirebaseNotificationApiService.class);
        FirebaseNotificationRequestBody body = generateFirebaseNotificationRequestBody(data);
        String authHeaderValue = HEADER_KEY + AppHolder.getContext().getString(R.string.firebase_fcm_server_key);
        Call<FirebaseNotificationResponse> call = service.sendPushNotificationWithData(
                body,
                authHeaderValue);
        call.enqueue(callback);
        Log.i(TAG, "send push notification with data via firebase url: " + call.request().url());
    }

    private FirebaseNotificationRequestBody generateFirebaseNotificationRequestBody(String encStr) {
        Notification notification = new Notification();
        notification.click_action = Utils.PUSH_NOTIFICATION_INTENT_ACTION;
        notification.body = AppHolder.getContext().getString(R.string.push_notification_request_body_title);
        notification.title = AppHolder.getContext().getString(R.string.push_notification_request_title_title);

        Data data = new Data();
        data.userInput = encStr;
        data.isScheduled = String.valueOf(true);
        // in 15 seconds
        data.scheduledTime = Utils.formatDateString(System.currentTimeMillis() + 15*1000);

        FirebaseNotificationRequestBody body = new FirebaseNotificationRequestBody(
                notification,
                data,
                BODY_TO_VALUE,
                BODY_PRIORITY);
        return body;
    }
}
