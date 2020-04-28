package com.lubaarbel.pingarbel.model;


import androidx.lifecycle.MutableLiveData;

public class UserInputModel {

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
    private MutableLiveData<String> userInputEncryptedLd = new MutableLiveData<>();

    public void postUserInputLd(String userInput) {
        this.userInputLd.postValue(userInput);
    }

    public String getUserInputFromLd() {
        return userInputLd.getValue();
    }

    public String getUserInputEncryptedLd() {
        return userInputEncryptedLd.getValue();
    }

    public void postUserInputEncryptedLd(String userInputEncrypted) {
        this.userInputEncryptedLd.postValue(userInputEncrypted);
    }
}
