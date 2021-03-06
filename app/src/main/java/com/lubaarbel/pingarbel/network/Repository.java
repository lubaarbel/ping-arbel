package com.lubaarbel.pingarbel.network;

import android.util.Log;

import com.lubaarbel.pingarbel.model.UserInputModel;
import com.lubaarbel.pingarbel.network.database.model.FirebaseDatabaseResponse;
import com.lubaarbel.pingarbel.network.database.FirebaseDatabaseUploadingService;
import com.lubaarbel.pingarbel.network.fcm.FirebaseNotificationSendingService;
import com.lubaarbel.pingarbel.network.fcm.model.FirebaseNotificationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository class is a model class that provide the necessary data
 * Currently, the class can save data to database (firebase database, in out case),
 * and send push notification (firebase data message, in out case)
 * **/

public class Repository {
    private static final String TAG = Repository.class.getSimpleName();

    public static final String FIREBASE_DATABASE_URL_PATH = "userInput.json";
    public static final String FIREBASE_DATABASE_BASE_URL = "https://ping-arbel.firebaseio.com/";

    public static final String FIREBASE_NOTIFICATION_PATH = "fcm/send";
    public static final String FIREBASE_NOTIFICATION_BASE_URL = "https://fcm.googleapis.com/";

    private static volatile Repository INSTANCE = null;

    private Repository() {}

    public static Repository getInstance() {
        Repository instance = INSTANCE;
        if (instance == null) {
            synchronized (Repository.class) {
                instance = INSTANCE;
                if (instance == null) {
                    instance = new Repository();
                    INSTANCE = instance;
                }
            }
        }
        return INSTANCE;
    }

    /** DataBase **/

    private Callback cloudDatabaseCallback = new Callback<FirebaseDatabaseResponse>() {
        @Override
        public void onResponse(Call<FirebaseDatabaseResponse> call,
                               Response<FirebaseDatabaseResponse> response) {
            if (response.body() != null) {
                String userInputDbId = response.body().name;
                Log.i(TAG, "firebase database onResponse id: " + userInputDbId);
                Log.i(TAG, "firebase database onResponse thread: " + Thread.currentThread().getName());
            }
        }
        @Override
        public void onFailure(Call<FirebaseDatabaseResponse> call, Throwable t) {
            Log.i(TAG, "firebase database onFailure: " + t.getMessage());
        }
    };

    public void saveEncryptedDataToDatabase(String encData) {
        FirebaseDatabaseUploadingService uploadingService = new FirebaseDatabaseUploadingService();
        uploadingService.uploadDataToFirebase(encData, cloudDatabaseCallback);
    }

    /** Push notifications **/

    private Callback cloudPushNotificationCallback = new Callback<FirebaseNotificationResponse>() {
        @Override
        public void onResponse(Call<FirebaseNotificationResponse> call, Response<FirebaseNotificationResponse> response) {
            UserInputModel.getInstance().setUserInputEncrypted(null);
        }

        @Override
        public void onFailure(Call<FirebaseNotificationResponse> call, Throwable t) {
        }
    };

    public void scheduleDataPushNotification() {
        FirebaseNotificationSendingService sendingService = new FirebaseNotificationSendingService();
        sendingService.sendPushNotificationWithDataViaFirebase(
                UserInputModel.getInstance().getUserInputEncrypted(), cloudPushNotificationCallback);
    }
}
