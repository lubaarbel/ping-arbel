package com.lubaarbel.pingarbel.viewmodel;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.lubaarbel.pingarbel.AppHolder;
import com.lubaarbel.pingarbel.R;
import com.lubaarbel.pingarbel.action.IUserInput;
import com.lubaarbel.pingarbel.crypto.CryptoImpl;
import com.lubaarbel.pingarbel.crypto.RSAKeysGenerator;
import com.lubaarbel.pingarbel.crypto.RSAUtils;
import com.lubaarbel.pingarbel.model.ResultsModel;
import com.lubaarbel.pingarbel.model.SingleLiveEvent;
import com.lubaarbel.pingarbel.model.UserInputModel;
import com.lubaarbel.pingarbel.utils.Utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class UserInputViewModel extends ViewModel implements IUserInput {
    private static final String TAG = UserInputViewModel.class.getSimpleName();

    private CryptoImpl crypto;
    private SingleLiveEvent<String> userInput = new SingleLiveEvent<>();
    private ResultsModel resultsModel;

    public ResultsModel getResultsModel() {
        return resultsModel;
    }

    public void initAndSaveResultsModel() {
        String initialValue = AppHolder.getContext().getString(R.string.frag_user_input_result_text_initial);
        resultsModel = new ResultsModel(initialValue, false, View.VISIBLE);
    }
//
//    public int getReAuthBtnVisibility() {
//        return resultsModel.isAuthenticated() ? View.GONE : View.VISIBLE;
//    }

    public void handleNotificationIfNeeded(Intent intent) {
        if (intent != null &&
                "OPEN_ACTIVITY_1".equals(intent.getAction()) &&
                intent.getExtras() != null) {

            String dataToDecipher = (String) intent.getExtras().get("userInput");
            UserInputModel.getInstance().postIncomingUserInputEncryptedLd(dataToDecipher);
        }
    }

    @Override
    public void onUserEnteredInput(View view, String text) {
        userInput.setValue(text);
    }

    public void registerToObserveUserInput(LifecycleOwner owner, final Observer observer) {
        userInput.observe(owner, observer);
    }

    public void handleUserInput(String text) {
        UserInputModel.getInstance().postUserInputLd(text);
        crypto = new CryptoImpl();

        try {
            String encStr = encryptUserInput(text);
            signEncryptedUserInput(encStr);
            UserInputModel.getInstance().serUserInputEncrypted(encStr);
        } catch (NoSuchAlgorithmException | IllegalBlockSizeException | InvalidKeyException |
                BadPaddingException | NoSuchPaddingException | SignatureException e) {
            e.printStackTrace();
        }
    }

    public void handleEncryptedDataFromNotification() {
        String text = UserInputModel.getInstance().getIncomingUserInputEncryptedLd();
        crypto = new CryptoImpl();
        try {
            if (verify(text)) {
                String output = decryptUserInput(text);
                resultsModel.setResultViewText(output);
            }
        } catch (IllegalBlockSizeException | InvalidKeyException | BadPaddingException |
                NoSuchAlgorithmException | NoSuchPaddingException | SignatureException e) {
            e.printStackTrace();
        }
    }

    private String encryptUserInput(String text) throws
            NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException,
            BadPaddingException, NoSuchPaddingException {

        RSAKeysGenerator encKeys = new RSAKeysGenerator(); // first pair

        Log.i(TAG, "user input:: " + text);
        byte[] enc = crypto.encrypt(text.getBytes(), encKeys.getPublicKey());
        String encStr = Base64.getEncoder().encodeToString(enc);
        Log.i(TAG, "Encrypted::  " + encStr);


        Utils.writeToFile(getFullPath(RSAUtils.ENC_PRIVATE_KEY_FILE_PATH), encKeys.getPrivateKey().getEncoded());
        return encStr;
    }

    private String getFullPath(String relativePath) {
        String filePath = AppHolder.getContext().getFilesDir().getPath() + relativePath;
        return filePath;
    }

    private String decryptUserInput(String encStr) throws
            IllegalBlockSizeException, InvalidKeyException, BadPaddingException,
            NoSuchAlgorithmException, NoSuchPaddingException {

        byte[] privateKey = Utils.readFromFile(getFullPath(RSAUtils.ENC_PRIVATE_KEY_FILE_PATH));
        byte[] dec = crypto.decrypt(Base64.getDecoder().decode(encStr), privateKey);
        String result = new String(dec);
        Log.i(TAG, "Decrypted::  " + result);

        return result;
    }

    private void signEncryptedUserInput(String encText) throws
            NoSuchAlgorithmException, SignatureException, InvalidKeyException {

        RSAKeysGenerator signKeys = new RSAKeysGenerator(); // second pair
        byte[] signature = crypto.sign(Base64.getDecoder().decode(encText), signKeys.getPrivateKey());

        Utils.writeToFile(getFullPath(RSAUtils.SIGNATURE_FILE_PATH), signature);
        Utils.writeToFile(getFullPath(RSAUtils.SIGN_PUBLIC_KEY_FILE_PATH), signKeys.getPublicKey().getEncoded());
    }

    private boolean verify(String encStr) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        byte[] signature = Utils.readFromFile(getFullPath(RSAUtils.SIGNATURE_FILE_PATH));
        byte[] encKey = Utils.readFromFile(getFullPath(RSAUtils.SIGN_PUBLIC_KEY_FILE_PATH));
        PublicKey publicKey = RSAUtils.getPublicKey(encKey);

        return crypto.verify(Base64.getDecoder().decode(encStr), signature, publicKey);
    }

//    private void testSignature() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
//        RSAKeysGenerator pair = new RSAKeysGenerator();
//        CryptoImpl crypto = new CryptoImpl();
//
//        Log.i(TAG, "Test signature for:: hello");
//        byte[] signature = crypto.sign(Base64.getDecoder().decode("hello"), pair.getPrivateKey());
//
//        boolean isCorrect = crypto.verify(Base64.getDecoder().decode("hello"), signature, pair.getPublicKey());
//        Log.i(TAG, "Signature  correct:: " + isCorrect);
//    }

}
