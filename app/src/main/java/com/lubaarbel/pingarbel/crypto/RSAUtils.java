package com.lubaarbel.pingarbel.crypto;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAUtils {
    public static final String RSA = "RSA";
    public static final String TRASFORMATION_RSA_ECB_PKCS1Padding = "RSA/ECB/PKCS1Padding";
    public static final String ALGORITHM_SHA256_RSA = "SHA256withRSA";

    public static PublicKey getPublicKey(byte[] base64PublicKey){
        try{
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(base64PublicKey);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static PrivateKey getPrivateKey(byte[] base64PrivateKey){
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(base64PrivateKey);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
