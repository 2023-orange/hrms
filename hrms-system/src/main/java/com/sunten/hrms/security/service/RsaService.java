package com.sunten.hrms.security.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

/**
 * @author batan
 * @since  2019-12-24
 */
public interface RsaService {

    Map<String, String> getTransactionKeyPair();

    String getTransactionPublicKey();

    String getTransactionPrivateKey();

    String transactionPublicEncrypt(String data) throws InvalidKeySpecException, NoSuchAlgorithmException;

    String transactionPrivateDecrypt(String data) throws InvalidKeySpecException, NoSuchAlgorithmException;

    String transactionPublicEncrypt(String data, String publicKey) throws InvalidKeySpecException, NoSuchAlgorithmException;

    String transactionPrivateDecrypt(String data, String privateKey) throws InvalidKeySpecException, NoSuchAlgorithmException;

    String transactionPrivateEncrypt(String data) throws InvalidKeySpecException, NoSuchAlgorithmException;

    String transactionPublicDecrypt(String data) throws InvalidKeySpecException, NoSuchAlgorithmException;

    String getPassPublicKey();

    String passEncrypt(String data) throws InvalidKeySpecException, NoSuchAlgorithmException;

    String passEncrypt(String data, String publicKey) throws InvalidKeySpecException, NoSuchAlgorithmException;

    String passDecrypt(String data, String privateKey) throws InvalidKeySpecException, NoSuchAlgorithmException;
}
