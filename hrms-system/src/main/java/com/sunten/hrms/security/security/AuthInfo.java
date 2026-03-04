package com.sunten.hrms.security.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author batan
 * @since 2018-11-23
 * 返回token
 */
@Getter
@AllArgsConstructor
public class AuthInfo implements Serializable {

    private final String token;

    private final String refreshToken;

    private final JwtUser user;

    private final Long tokenExpireDate;

    private final Long status;
}
