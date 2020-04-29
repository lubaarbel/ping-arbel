package com.lubaarbel.pingarbel.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

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
    }

    // getters
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
