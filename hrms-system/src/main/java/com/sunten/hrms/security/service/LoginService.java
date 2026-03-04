package com.sunten.hrms.security.service;

import com.sunten.hrms.security.security.AuthInfo;
import com.sunten.hrms.security.security.AuthUser;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface LoginService {
    AuthInfo loginM(AuthUser authUser, HttpServletRequest request, Boolean checkPasswordFlag) throws InvalidKeySpecException, NoSuchAlgorithmException;
}
