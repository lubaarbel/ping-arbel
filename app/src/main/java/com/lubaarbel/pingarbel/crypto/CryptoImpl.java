package com.lubaarbel.pingarbel.crypto;


import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class CryptoImpl implements ICrypto, ISign {
    private static final String TAG = CryptoImpl.class.getSimpleName();

    @Override
    public byte[] encrypt(byte[] data, PublicKey publicKey) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(RSAUtils.TRASFORMATION_RSA_ECB_PKCS1Padding);
        cipher.init(Cipher.ENCRYPT_MODE, RSAUtils.getPublicKey(publicKey.getEncoded()));
        return cipher.doFinal(data);
    }

    @Override
    public byte[] decrypt(byte[] data, byte[] privateKey) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(RSAUtils.TRASFORMATION_RSA_ECB_PKCS1Padding);
        cipher.init(Cipher.DECRYPT_MODE, RSAUtils.getPrivateKey(privateKey));
        return cipher.doFinal(data);
    }

    @Override
    public byte[] sign(byte[] data, PrivateKey privateKey) throws NoSuchAlgorithmException,
            InvalidKeyException, SignatureException {

        Signature signature = Signature.getInstance(RSAUtils.ALGORITHM_SHA256_RSA);
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    @Override
    public boolean verify(byte[] data, byte[] signature, PublicKey publicKey) throws NoSuchAlgorithmException,
            InvalidKeyException, SignatureException {

        Signature publicSignature = Signature.getInstance(RSAUtils.ALGORITHM_SHA256_RSA);
        publicSignature.initVerify(publicKey);
        publicSignature.update(data);
        return publicSignature.verify(signature);
    }

}
