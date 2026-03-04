package com.sunten.hrms.utils;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

import static org.junit.Assert.*;

public class RSAUtilTest {

    @Test
    public void createKeys()throws Exception {
//        Map<String, String> keyMap = RSAUtil.createKeys(1024);
//        String  publicKey = keyMap.get("publicKey");
//        String  privateKey = keyMap.get("privateKey");
        String  publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC3uDxdTEwXbtEUAOCSnHvyfYU/KnWzi7cq3bU5CjsesKHVqjO9FqTyHFL5Vwuc+BSP9HMRluvQb0XT/830+y/WHTPRoF4RWN+VfKCwCdS9b8bJQRaDl10iS9E6ObvwBASK21PvN0FG/9wx7C0l4o6YQ9vC8/aUUQqYGfg+DiykHwIDAQAB";
        String  privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALe4PF1MTBdu0RQA4JKce/J9hT8qdbOLtyrdtTkKOx6wodWqM70WpPIcUvlXC5z4FI/0cxGW69BvRdP/zfT7L9YdM9GgXhFY35V8oLAJ1L1vxslBFoOXXSJL0To5u/AEBIrbU+83QUb/3DHsLSXijphD28Lz9pRRCpgZ+D4OLKQfAgMBAAECgYArqJZ/vV/fua/pxwOXE6TIzVda2WY7EpqmjyU/ttSxyFvNALNm3fi4qE53fl3+IV4Rj4/AwFKtp6O6a3GwI/kLyt7qKc5Q3IGJfpmE88ME/pBKHvJsdUgtPpXpoXtgefFYy+XwN6tZ7nW6/g347AloVJuqLapx372/+K4ctD+7wQJBAPZgAYWb5Vq+rQmWlXtD3nSMxV/RVPUBamAN7j0bBcn5NCzBTIGkRPSDw5Jq0vVT3vSiacTh1YQafNEiZQYWFrECQQC+5Z1EeCiX0GV4zxEql7JX8l5dh6pyjBHBskOMPoUUopXySKoMDLEkK/4eLFSrPkSzVFN5gB+l683804YUzbvPAkBM9gCDtAcZKabz784SC0laLv8Yx1M6lY6dIrzg6agNR4M818UGWkIP/3kAK85qRCDJWlKf5cvE0GFdEtlr5UqBAkBuPEAd6tleGZyPL9v04Za+TJqLni0iappSZTO2h9/ns5+tQqLXxHiCr9jV6bmXDaU0fWyazA76jHnuFuPlnYxnAkEAvbmOWvuCIFGbr2aqx6LwTV0NWIjO4ONZxY7jz8PCxxfEYemUITjnKllbX3yvlzndSYgmgGiXXkoIPnRdf0O/nw==";
        System.out.println("公钥: \n\r" + publicKey);
        System.out.println("私钥： \n\r" + privateKey);

        System.out.println("公钥加密——私钥解密");
        String str = "站在大明门前守卫的禁卫军，事先没有接到\n" +
                "有关的命令，但看到大批盛装的官员来临，也就\n" +
                "以为确系举行大典，因而未加询问。进大明门即\n" +
                "为皇城。文武百官看到端门午门之前气氛平静，\n" +
                "城楼上下也无朝会的迹象，既无几案，站队点名\n" +
                "的御史和御前侍卫“大汉将军”也不见踪影，不免\n" +
                "心中揣测，互相询问：所谓午朝是否讹传？";
        str = "test123456";
        System.out.println("\r明文：\r\n" + str);
        System.out.println("\r明文大小：\r\n" + str.getBytes().length);
        //公钥加密
        String encodedData = RSAUtil.publicEncrypt(str, RSAUtil.getPublicKey(publicKey),null);

        System.out.println("密文："+encodedData.length()+"\r\n" + encodedData);
        //私钥解密
        String decodedData = RSAUtil.privateDecrypt(encodedData, RSAUtil.getPrivateKey(privateKey),null);

        decodedData = decodedData.replace("\0","");

        System.out.println("解密后文字: "+ decodedData.getBytes().length+"\r\n" + decodedData);
    }

    @Test
    public void getPublicKey() throws InvalidKeySpecException, NoSuchAlgorithmException, UnsupportedEncodingException {
        String  publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCTiAs2Ixs5nQDYprqASIZUpqLBlnfi8nCvaN1bRoH7Fb7VjC8Zr7y4YZiUi-3gVx_3FfTjj_FygxsnaXjqwc5BvJH-igTdAhImn8bspKqY8NTX7d7Aw6ovc5sA1yx_0BcpHu1CLGkO28IrHZtzW2mn_ijkNIGM-cqlpw8ZX-KIIQIDAQAB";
        byte[] bytes = Base64.decodeBase64(publicKey);
        System.out.println(new String(bytes,"UTF-8"));
        
        RSAPublicKey pk = RSAUtil.getPublicKey(publicKey);
        System.out.println(pk.getAlgorithm());
        System.out.println(pk.getEncoded());
        System.out.println(pk.getFormat());
        System.out.println(pk.toString());
        System.out.println(pk.getModulus());
    }

    @Test
    public void getPrivateKey() {
    }

    @Test
    public void publicEncrypt() {
    }

    @Test
    public void privateDecrypt() {
    }

    @Test
    public void privateEncrypt() {
    }

    @Test
    public void publicDecrypt() {
    }
}