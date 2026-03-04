package com.sunten.hrms.security.service.impl;

import com.sunten.hrms.security.security.JwtUser;
import com.sunten.hrms.security.security.OnlineUser;
import com.sunten.hrms.security.service.OnlineUserService;
import com.sunten.hrms.security.utils.JwtTokenUtil;
import com.sunten.hrms.utils.EncryptUtils;
import com.sunten.hrms.utils.FileUtil;
import com.sunten.hrms.utils.PageUtil;
import com.sunten.hrms.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author batan
 * @since 2019-12-24
 */
@Service
public class OnlineUserServiceImpl implements OnlineUserService {

    private final RedisTemplate redisTemplate;
    private final JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;
    @Value("${jwt.token-keep-expiration}")
    private Long tokenKeepExpiration;
    @Value("${jwt.online-key}")
    private String onlineKey;
    @Value("${jwt.keep-online-key}")
    private String keepOnlineKey;

    public OnlineUserServiceImpl(RedisTemplate redisTemplate, JwtTokenUtil jwtTokenUtil) {
        this.redisTemplate = redisTemplate;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public void save(JwtUser jwtUser, String token, String refreshToken, HttpServletRequest request) {
        String job = jwtUser.getDept() + "/" + jwtUser.getJob();
        String ip = StringUtils.getIp(request);
        String browser = StringUtils.getBrowser(request);
        String address = StringUtils.getCityInfo(ip);
        OnlineUser onlineUser = null;
        try {
            onlineUser = new OnlineUser(jwtUser.getUsername(), job, browser, ip, address, EncryptUtils.desEncrypt(token), EncryptUtils.desEncrypt(refreshToken), LocalDateTime.now());
        } catch (Exception e) {
            e.printStackTrace();
        }
        redisTemplate.opsForValue().set(onlineKey + "::" + jwtUser.getUsername() + ":" + token, onlineUser);
        redisTemplate.expire(onlineKey + "::" + jwtUser.getUsername() + ":" + token, refreshExpiration, TimeUnit.MILLISECONDS);
    }

    @Override
    public Page<OnlineUser> getAll(String filter, Pageable pageable) {
        List<OnlineUser> onlineUsers = getAll(filter);
        return new PageImpl<OnlineUser>(
                PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), onlineUsers),
                pageable,
                onlineUsers.size());
    }

    @Override
    public List<OnlineUser> getAll(String filter) {
        List<String> keys = new ArrayList<>(redisTemplate.keys(onlineKey + "::" + "*"));
        Collections.reverse(keys);
        List<OnlineUser> onlineUsers = new ArrayList<>();
        for (String key : keys) {
            OnlineUser onlineUser = (OnlineUser) redisTemplate.opsForValue().get(key);
            if (StringUtils.isNotBlank(filter)) {
                if (onlineUser.toString().contains(filter)) {
                    onlineUsers.add(onlineUser);
                }
            } else {
                onlineUsers.add(onlineUser);
            }
        }
        Collections.sort(onlineUsers, (o1, o2) -> {
            return o2.getLoginTime().compareTo(o1.getLoginTime());
        });
        return onlineUsers;
    }

    @Override
    public void kickOut(String userName, String val) throws Exception {
        String key = onlineKey + "::" + userName + ":" + EncryptUtils.desDecrypt(val);
        redisTemplate.delete(key);
    }

    @Override
    public void logout(String token) {
        if (null != token && !"".equals(token)) {
            String key = onlineKey + "::" + jwtTokenUtil.getUsernameFromToken(token) + ":" + token;
            redisTemplate.delete(key);
        }
    }

    @Override
    public void updateExpireTime(String userName, String token) {
        List<String> keys = new ArrayList<>(redisTemplate.keys(onlineKey + "::" + userName + ":" + token));
        if (keys != null && keys.size() > 0) {
            redisTemplate.rename(onlineKey + "::" + userName + ":" + token, keepOnlineKey + "::" + userName + ":" + token);
            redisTemplate.expire(keepOnlineKey + "::" + userName + ":" + token, tokenKeepExpiration, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void download(List<OnlineUser> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (OnlineUser user : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", user.getUserName());
            map.put("岗位", user.getJob());
            map.put("登录IP", user.getIp());
            map.put("登录地点", user.getAddress());
            map.put("浏览器", user.getBrowser());
            map.put("登录日期", user.getLoginTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
