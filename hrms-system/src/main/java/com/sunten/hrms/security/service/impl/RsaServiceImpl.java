package com.sunten.hrms.security.service.impl;

import com.sunten.hrms.security.service.RsaService;
import com.sunten.hrms.utils.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

/**
 * @author batan
 * @since 2019-12-24
 */
@Service
@CacheConfig(cacheNames = "security")
public class RsaServiceImpl implements RsaService {
    @Value("${sunten.rsa.pass-public-key}")
    private String passPublicKey;

    @Autowired
    private RsaServiceImpl instance;

    @Override
    @Cacheable(key = "'transactionKeyPair'")
    public Map<String, String> getTransactionKeyPair() {
        Map<String, String> keyMap = RSAUtil.createKeys(1024);
        return keyMap;
    }

    @Override
    public String getTransactionPublicKey() {
        Map<String, String> keyMap = instance.getTransactionKeyPair();
        return keyMap.get("publicKey");
    }

    @Override
    public String getTransactionPrivateKey() {
        Map<String, String> keyMap = instance.getTransactionKeyPair();
        return keyMap.get("privateKey");
    }

    @Override
    public String transactionPublicEncrypt(String data) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return this.transactionPublicEncrypt(data, this.getTransactionPublicKey());
    }

    @Override
    public String transactionPrivateDecrypt(String data) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return this.transactionPrivateDecrypt(data, this.getTransactionPrivateKey());
    }

    @Override
    public String transactionPublicEncrypt(String data, String publicKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return RSAUtil.publicEncrypt(data, RSAUtil.getPublicKey(publicKey), "RSA/ECB/PKCS1Padding");
    }

    @Override
    public String transactionPrivateDecrypt(String data, String privateKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return RSAUtil.privateDecrypt(data, RSAUtil.getPrivateKey(privateKey), "RSA/ECB/PKCS1Padding");
    }

    @Override
    public String transactionPrivateEncrypt(String data) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return RSAUtil.privateEncrypt(data, RSAUtil.getPrivateKey(this.getTransactionPrivateKey()), "RSA/ECB/PKCS1Padding");
    }

    @Override
    public String transactionPublicDecrypt(String data) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return RSAUtil.publicDecrypt(data, RSAUtil.getPublicKey(this.getTransactionPublicKey()), "RSA/ECB/PKCS1Padding");
    }

    @Override
    @Cacheable(key = "'passPublicKey'")
    public String getPassPublicKey() {
        return passPublicKey;
    }

    @Override
    public String passEncrypt(String data) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return this.passEncrypt(data, instance.getPassPublicKey());
    }

    @Override
    public String passEncrypt(String data, String publicKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return RSAUtil.publicEncrypt(data, RSAUtil.getPublicKey(publicKey), null);
    }

    @Override
    public String passDecrypt(String data, String privateKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return RSAUtil.privateDecrypt(data, RSAUtil.getPrivateKey(privateKey), null).replace("\0", "");
    }


}
