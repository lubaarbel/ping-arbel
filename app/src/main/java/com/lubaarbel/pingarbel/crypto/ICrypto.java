package com.lubaarbel.pingarbel.crypto;

import java.security.GeneralSecurityException;
import java.security.PublicKey;

public interface ICrypto {

    byte[] encrypt(byte[] data, PublicKey publicKey) throws GeneralSecurityException;

    byte[] decrypt(byte[] data, byte[] privateKey) throws GeneralSecurityException;
}
