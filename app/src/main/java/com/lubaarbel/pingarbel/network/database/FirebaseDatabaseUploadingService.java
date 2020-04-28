package com.lubaarbel.pingarbel.network.database;

import android.util.Log;

import com.lubaarbel.pingarbel.network.Repository;
import com.lubaarbel.pingarbel.network.database.model.FirebaseDatabaseRequestBody;
import com.lubaarbel.pingarbel.network.database.model.FirebaseDatabaseResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirebaseDatabaseUploadingService {
    private static final String TAG = FirebaseDatabaseUploadingService.class.getSimpleName();

    public void uploadDataToFirebase(String data, Callback<FirebaseDatabaseResponse> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Repository.FIREBASE_DATABASE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FirebaseDatabaseApiService service = retrofit.create(FirebaseDatabaseApiService.class);
        FirebaseDatabaseRequestBody body = new FirebaseDatabaseRequestBody();
        body.encryptedData = data;
        Call<FirebaseDatabaseResponse> call = service.uploadEncryptedUserInputToDatabase(body);
        call.enqueue(callback);

        Log.i(TAG, "upload data to firebase database url: " + call.request().url());
    }
}
