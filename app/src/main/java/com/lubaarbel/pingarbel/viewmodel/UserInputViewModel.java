package com.lubaarbel.pingarbel.viewmodel;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.lubaarbel.pingarbel.AppHolder;
import com.lubaarbel.pingarbel.R;
import com.lubaarbel.pingarbel.action.IUserInput;
import com.lubaarbel.pingarbel.crypto.CryptoHandler;
import com.lubaarbel.pingarbel.model.ResultsModel;
import com.lubaarbel.pingarbel.model.SingleLiveEvent;
import com.lubaarbel.pingarbel.model.UserInputModel;
import com.lubaarbel.pingarbel.utils.SettingsPrefs;
import com.lubaarbel.pingarbel.utils.Utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Shared ViewModel for activity and both fragments
 * Handles business logic on the above mentioned UI classes
 * **/
public class UserInputViewModel extends ViewModel implements IUserInput {
    private static final String TAG = UserInputViewModel.class.getSimpleName();

    private MutableLiveData<Boolean> cryptoStatesEncryptingLd = new MutableLiveData<>();
    private MutableLiveData<Boolean> cryptoStatesSigningLd = new MutableLiveData<>();
    private MutableLiveData<Boolean> cryptoStatesVerifyingLd = new MutableLiveData<>();
    private MutableLiveData<Boolean> cryptoStatesDecryptingLd = new MutableLiveData<>();

    private SingleLiveEvent<String> userInput = new SingleLiveEvent<>();
    private ResultsModel resultsModel;
    private boolean shouldNavigateStraightToResults;

    public void registerToObserveUserInput(LifecycleOwner owner, final Observer observer) {
        userInput.observe(owner, observer);
    }

    public void registerToCryptoStatesEncryptingLd(@NonNull LifecycleOwner owner, @NonNull Observer observer) {
        cryptoStatesEncryptingLd.observe(owner, observer);
    }

    public void registerToCryptoStatesSigningLd(@NonNull LifecycleOwner owner, @NonNull Observer observer) {
        cryptoStatesSigningLd.observe(owner, observer);
    }

    public void registerToCryptoStatesVerifyingLd(@NonNull LifecycleOwner owner, @NonNull Observer observer) {
        cryptoStatesVerifyingLd.observe(owner, observer);
    }

    public void registerToCryptoStatesDecryptingLd(@NonNull LifecycleOwner owner, @NonNull Observer observer) {
        cryptoStatesDecryptingLd.observe(owner, observer);
    }

    public boolean isShouldBioAuthenticate() {
        return SettingsPrefs.getInstance().getBooleanValueFromSharedPrefs(
                SettingsPrefs.SHARED_PREFS_VALUE_SHOULD_BIO_AUTHENTICATE);
    }

    public void setShouldNavigateStraightToResults(boolean value) {
        shouldNavigateStraightToResults = value;
    }
    public boolean isShouldNavigateStraightToResults() {
        return shouldNavigateStraightToResults;
    }

    public ResultsModel getResultsModel() {
        return resultsModel;
    }

    public void initAndSaveResultsModel() {
        String initialValue = AppHolder.getContext().getString(R.string.frag_user_input_result_text_initial);
        resultsModel = new ResultsModel(initialValue,
                isShouldBioAuthenticate() ? View.VISIBLE : View.GONE);
    }

    public void handleNotificationIfNeeded(Intent intent) {
        if (intent != null &&
                Utils.PUSH_NOTIFICATION_INTENT_ACTION.equals(intent.getAction()) &&
                intent.getExtras() != null) {

            shouldNavigateStraightToResults = true;
            String dataToDecipher = (String) intent.getExtras().get(Utils.PUSH_NOTIFICATION_INTENT_DATA_KEY);
            UserInputModel.getInstance().setIncomingUserInputEncryptedLd(dataToDecipher);
        }
    }

    @Override
    public void onUserEnteredInput(View view, String text) {
        userInput.setValue(text);
    }

    @Override
    public void onRadioButtonClicked(View view) {
        SettingsPrefs.getInstance().putBooleanValueToSharedPrefs(
                SettingsPrefs.SHARED_PREFS_VALUE_SHOULD_BIO_AUTHENTICATE, ((Switch) view).isChecked());
    }

    /** Crypto staff **/
    // Done on worker thread using Executors.newSingleThreadExecutor()
    public void handleUserInput(String text) { // way out
        Log.i(TAG, "Encrypt UserInput on thread:: " + Thread.currentThread().getName());
        UserInputModel.getInstance().postUserInputLd(text);

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Log.i(TAG, "Encrypt UserInput on thread:: " + Thread.currentThread().getName());
            CryptoHandler cryptoHandler = new CryptoHandler();
            try {
                String encStr = cryptoHandler.encryptUserInput(text);
                new Handler(Looper.getMainLooper()).post(() ->
                        cryptoStatesEncryptingLd.postValue(true)); // update ui
                cryptoHandler.signEncryptedUserInput(encStr);
                new Handler(Looper.getMainLooper()).post(() ->
                        cryptoStatesSigningLd.postValue(true)); // update ui
                UserInputModel.getInstance().setUserInputEncrypted(encStr);
            } catch (NoSuchAlgorithmException | IllegalBlockSizeException | InvalidKeyException |
                    BadPaddingException | NoSuchPaddingException | SignatureException e) {
                e.printStackTrace();
            }
        });
    }

    public void handleEncryptedDataFromNotification() { // return
        Log.i(TAG, "Decrypt UserInput on thread:: " + Thread.currentThread().getName());

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Log.i(TAG, "Decrypt UserInput on thread:: " + Thread.currentThread().getName());
            String text = UserInputModel.getInstance().getIncomingUserInputEncryptedLd();
            CryptoHandler cryptoHandler = new CryptoHandler();

            try {
                if (cryptoHandler.verify(text)) {
                    new Handler(Looper.getMainLooper()).post(() ->
                            cryptoStatesVerifyingLd.postValue(true)); // update ui
                    String output = cryptoHandler.decryptUserInput(text);
                    new Handler(Looper.getMainLooper()).post(() ->
                            cryptoStatesDecryptingLd.postValue(true)); // update ui
                    resultsModel.setResultViewText(output);
                    UserInputModel.getInstance().postIncomingUserInputEncryptedLd(null);
                }
            } catch (NoSuchAlgorithmException | IllegalBlockSizeException | InvalidKeyException |
                    BadPaddingException | NoSuchPaddingException | SignatureException e) {
                e.printStackTrace();
            }
        });
    }
}
