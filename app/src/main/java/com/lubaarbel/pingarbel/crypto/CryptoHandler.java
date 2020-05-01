package com.lubaarbel.pingarbel.crypto;

import android.util.Log;

import com.lubaarbel.pingarbel.AppHolder;
import com.lubaarbel.pingarbel.utils.Utils;
import com.lubaarbel.pingarbel.view.MainActivity;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class CryptoHandler {
    private static final String TAG = CryptoHandler.class.getSimpleName();

    private CryptoImpl crypto;

    public CryptoHandler() {
        this.crypto = new CryptoImpl();
    }

    public String encryptUserInput(String text) throws
            NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException,
            BadPaddingException, NoSuchPaddingException {

        RSAKeysGenerator encKeys = new RSAKeysGenerator(); // first pair

        Log.i(TAG, "user input:: " + text);
        Log.i(TAG, "user input bytes size:: " + text.getBytes().length);
        byte[] enc = crypto.encrypt(text.getBytes(), encKeys.getPublicKey());
        String encStr = Base64.getEncoder().encodeToString(enc);
        Log.i(TAG, "Encrypted::  " + encStr);

        Utils.writeToFile(getFullPath(RSAUtils.ENC_PRIVATE_KEY_FILE_PATH), encKeys.getPrivateKey().getEncoded());
        return encStr;
    }

    private String getFullPath(String relativePath) {
        return AppHolder.getContext().getFilesDir().getPath() + relativePath;
    }

    public String decryptUserInput(String encStr) throws
            IllegalBlockSizeException, InvalidKeyException, BadPaddingException,
            NoSuchAlgorithmException, NoSuchPaddingException {

        byte[] privateKey = Utils.readFromFile(getFullPath(RSAUtils.ENC_PRIVATE_KEY_FILE_PATH));
        byte[] dec = crypto.decrypt(Base64.getDecoder().decode(encStr), privateKey);
        String result = new String(dec);
        Log.i(TAG, "Decrypted::  " + result);

        return result;
    }

    public void signEncryptedUserInput(String encText) throws
            NoSuchAlgorithmException, SignatureException, InvalidKeyException {

        RSAKeysGenerator signKeys = new RSAKeysGenerator(); // second pair
        byte[] signature = crypto.sign(Base64.getDecoder().decode(encText), signKeys.getPrivateKey());

        Utils.writeToFile(getFullPath(RSAUtils.SIGNATURE_FILE_PATH), signature);
        Utils.writeToFile(getFullPath(RSAUtils.SIGN_PUBLIC_KEY_FILE_PATH), signKeys.getPublicKey().getEncoded());
    }

    public boolean verify(String encStr) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
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
