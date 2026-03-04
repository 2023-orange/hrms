package com.sunten.hrms.security.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.service.RedisService;
import com.sunten.hrms.security.security.AuthInfo;
import com.sunten.hrms.security.security.AuthUser;
import com.sunten.hrms.security.security.JwtUser;
import com.sunten.hrms.security.service.LoginService;
import com.sunten.hrms.security.service.OnlineUserService;
import com.sunten.hrms.security.service.RsaService;
import com.sunten.hrms.security.utils.JwtTokenUtil;
import com.sunten.hrms.utils.DateUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LoginServiceImpl implements LoginService {
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisService redisService;
    private final UserDetailsService userDetailsService;
    private final OnlineUserService onlineUserService;
    private final RsaService rsaService;

    public LoginServiceImpl(JwtTokenUtil jwtTokenUtil, RedisService redisService, @Qualifier("jwtUserDetailsServiceImpl") UserDetailsService userDetailsService, OnlineUserService onlineUserService, RsaService rsaService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.redisService = redisService;
        this.userDetailsService = userDetailsService;
        this.onlineUserService = onlineUserService;
        this.rsaService = rsaService;
    }

    @Override
    public AuthInfo loginM(AuthUser authUser, HttpServletRequest request, Boolean checkPasswordFlag) throws InvalidKeySpecException, NoSuchAlgorithmException {
        final JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(authUser.getUsername());

        if (checkPasswordFlag) {// OA模拟HR登录时不需要密码验证, 其它情况需要验证
            if (!jwtUser.getPassword().equals(rsaService.passEncrypt(jwtUser.getSalt() + authUser.getPassword()))) {
                throw new InfoCheckWarningMessException("密码错误");
            }
        }
        if (!jwtUser.isEnabled()) {
            throw new InfoCheckWarningMessException("账号已停用，请联系管理员");
        }
        // 生成令牌
        final String token = jwtTokenUtil.generateToken(jwtUser);
        final String refreshToken = jwtTokenUtil.generateRefreshToken(jwtUser);
        Long tokenExpirdDate = DateUtil.asLong(jwtTokenUtil.getExpirationDateFromToken(token));
        // 保存在线信息
        onlineUserService.save(jwtUser, token, refreshToken, request);
        // 返回 AuthInfo
        return new AuthInfo(token, refreshToken, jwtUser, tokenExpirdDate, 200L);
    }
}
