package com.lubaarbel.pingarbel.crypto;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface ICrypto {

    byte[] encrypt(byte[] data, PublicKey publicKey) throws GeneralSecurityException;

    byte[] decrypt(byte[] data, PrivateKey privateKey) throws GeneralSecurityException;
}
