package com.lubaarbel.pingarbel.model;


import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.lubaarbel.pingarbel.AppHolder;

public class UserInputModel {

    private static final String SHARED_PREFS_NAME = "userInputEncryptedSharedPrefs";
    private static final String SHARED_PREFS_VALUE_USER_INPUT_ENC = "userInputEncrypted";

    private static UserInputModel INSTANCE;

    private UserInputModel() {}

    public static UserInputModel getInstance() {
        UserInputModel instance = INSTANCE;
        if (instance == null) {
            synchronized (UserInputModel.class) {
                instance = INSTANCE;
                if (instance == null) {
                    instance = new UserInputModel();
                    INSTANCE = instance;
                }
            }
        }
        return INSTANCE;
    }

    private MutableLiveData<String> userInputLd = new MutableLiveData<>();
    private MutableLiveData<String> incomingUserInputEncryptedLd = new MutableLiveData<>();

    private SharedPreferences sharedpreferences = AppHolder.getContext().
            getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);

    public void postUserInputLd(String userInput) {
        this.userInputLd.postValue(userInput);
    }

    public String getUserInputFromLd() {
        return userInputLd.getValue();
    }

    public String getUserInputEncrypted() {
        return sharedpreferences.getString(SHARED_PREFS_VALUE_USER_INPUT_ENC, null);
    }

    public void serUserInputEncrypted(String userInputEncrypted) {
        // apply() wright in bg
        this.sharedpreferences.edit().putString(SHARED_PREFS_VALUE_USER_INPUT_ENC, userInputEncrypted).apply();
    }

    public String getIncomingUserInputEncryptedLd() {
        return incomingUserInputEncryptedLd.getValue();
    }

    public void postIncomingUserInputEncryptedLd(String incomingUserInputEncrypted) {
        this.incomingUserInputEncryptedLd.postValue(incomingUserInputEncrypted);
    }

    public void registerToIncomingUserInputEncryptedLd(@NonNull LifecycleOwner owner, @NonNull Observer observer) {
        incomingUserInputEncryptedLd.observe(owner, observer);
    }
}
