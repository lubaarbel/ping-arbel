package com.lubaarbel.pingarbel.crypto;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class RSAKeysGenerator {
    private static final String TAG = RSAKeysGenerator.class.getSimpleName();

    private PrivateKey privateKey;
    private PublicKey publicKey;

    // constructor
    public RSAKeysGenerator() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(RSAUtils.RSA);
        keyGen.initialize(1024);
        KeyPair pair = keyGen.generateKeyPair();
        publicKey = pair.getPublic();
        privateKey = pair.getPrivate();

        Log.i(TAG, "RSA/publicKey:: \n" + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        Log.i(TAG, "RSA/privateKey:: \n" + Base64.getEncoder().encodeToString(privateKey.getEncoded()));
    }

    // getters
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    // save to file
    public void saveKeysToFile(String path, byte[] key) throws IOException {
        File f = new File(path);
        f.getParentFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(f);
        fos.write(key);
        fos.flush();
        fos.close();
    }
}
