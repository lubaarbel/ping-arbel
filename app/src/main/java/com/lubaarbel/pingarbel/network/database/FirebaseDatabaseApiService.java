package com.lubaarbel.pingarbel.network.database;

import com.lubaarbel.pingarbel.network.Repository;
import com.lubaarbel.pingarbel.network.database.model.FirebaseDatabaseRequestBody;
import com.lubaarbel.pingarbel.network.database.model.FirebaseDatabaseResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FirebaseDatabaseApiService {
    @Headers({"Accept: application/json",
              "Content-Type: application/json"})
    @POST(Repository.FIREBASE_DATABASE_URL_PATH)
    Call<FirebaseDatabaseResponse> uploadEncryptedUserInputToDatabase(@Body FirebaseDatabaseRequestBody body);
}
