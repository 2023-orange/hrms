package com.sunten.hrms.fnd.vo;

import lombok.Data;

@Data
public class RsaKeyPairVo {
    private String oldPrivateKey;
    private String oldPublicKey;
    private String newPrivateKey;
    private String newPublicKey;
}
