package com.sunten.hrms.security.service;

import com.sunten.hrms.security.security.JwtUser;
import com.sunten.hrms.security.security.OnlineUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author batan
 * @since 2019-12-24
 */
public interface OnlineUserService {

    void save(JwtUser jwtUser, String token, String refreshToken, HttpServletRequest request);

    Page<OnlineUser> getAll(String filter, Pageable pageable);

    List<OnlineUser> getAll(String filter);

    void kickOut(String userName, String val) throws Exception;

    void logout(String token);

    void updateExpireTime(String userName, String token);

    void download(List<OnlineUser> all, HttpServletResponse response) throws IOException;
}
