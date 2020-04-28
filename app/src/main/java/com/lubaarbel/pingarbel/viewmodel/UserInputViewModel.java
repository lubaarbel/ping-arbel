package com.lubaarbel.pingarbel.viewmodel;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.lubaarbel.pingarbel.action.IUserInput;
import com.lubaarbel.pingarbel.crypto.CryptoImpl;
import com.lubaarbel.pingarbel.crypto.RSAKeysGenerator;
import com.lubaarbel.pingarbel.model.SingleLiveEvent;
import com.lubaarbel.pingarbel.model.UserInputModel;

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

    private SingleLiveEvent<String> userInput = new SingleLiveEvent<>();
    private CryptoImpl crypto;

    @Override
    public void onUserEnteredInput(View view, String text) {
        userInput.setValue(text);
    }

    public void registerToObserveUserInput(LifecycleOwner owner, final Observer observer) {
        userInput.observe(owner, observer);
    }

    public void handleUserInput(String text) throws
            NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException,
            BadPaddingException, NoSuchPaddingException, SignatureException {

        UserInputModel.getInstance().postUserInputLd(text);

        crypto = new CryptoImpl();

        String encStr = encryptUserInput(text);
        String encSignStr = signAndVerifyEncryptedUserInput(encStr);

        UserInputModel.getInstance().postUserInputEncryptedLd(encSignStr);
        //Repository.getInstance().saveEncryptedDataToDatabase(encSignStr);
    }

    public void handleEncryptedDataFromNotification(String text) {
        // TODO
    }

    public String encryptUserInput(String text) throws
            NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException,
            BadPaddingException, NoSuchPaddingException, SignatureException {

        RSAKeysGenerator encKeys = new RSAKeysGenerator(); // first pair

        Log.i(TAG, "user input:: " + text);

        byte[] enc = crypto.encrypt(text.getBytes(), encKeys.getPublicKey());
        String encStr = Base64.getEncoder().encodeToString(enc);
        Log.i(TAG, "Encrypted::  " + encStr);

        return encStr;

        // TODO in push - save encKeys.getPrivateKey()
//        byte[] dec = crypto.decrypt(Base64.getDecoder().decode(encStr), encKeys.getPrivateKey());
//        Log.i(TAG, "Decrypted::  " + new String(dec));
    }

    private String signAndVerifyEncryptedUserInput(String encText) throws
            NoSuchAlgorithmException, SignatureException, InvalidKeyException {

        RSAKeysGenerator signKeys = new RSAKeysGenerator(); // second pair

        byte[] signature = crypto.sign(Base64.getDecoder().decode(encText), signKeys.getPrivateKey());
        verify(crypto, encText, signature, signKeys.getPublicKey());

        return Base64.getEncoder().encodeToString(signature);
    }

    private boolean verify(CryptoImpl crypto, String encText, byte[] signature, PublicKey publicKey) throws
            NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        return crypto.verify(Base64.getDecoder().decode(encText), signature, publicKey);
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
