package com.lubaarbel.pingarbel.network.fcm;

import com.lubaarbel.pingarbel.network.Repository;
import com.lubaarbel.pingarbel.network.fcm.model.FirebaseNotificationRequestBody;
import com.lubaarbel.pingarbel.network.fcm.model.FirebaseNotificationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FirebaseNotificationApiService {
    @Headers({"Accept: application/json",
              "Content-Type: application/json"})
    @POST(Repository.FIREBASE_NOTIFICATION_PATH)
    Call<FirebaseNotificationResponse> sendPushNotificationWithData(
            @Body FirebaseNotificationRequestBody body,
            @Header("Authorization") String serverKey);

}
