package com.sunten.hrms.security.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiniu.util.StringUtils;
import com.sunten.hrms.security.service.LoginService;
import com.sunten.hrms.security.utils.JwtTokenUtil;
import com.sunten.hrms.utils.EncryptUtils;
import com.sunten.hrms.utils.SendRequestUtil;
import com.sunten.hrms.utils.ThrowableUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.core.util.IOUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {
    private final static String HRMS_TOKEN_EXPIRE_DATE = "HRMS-TOKEN-EXPIRE-DATE";
    private final static String HRMS_TOKEN = "HRMS-TOKEN";
    private final static String HRMS_REFRESH_TOKEN = "HRMS-REFRESH-TOKEN";

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisTemplate redisTemplate;
    private final LoginService loginService;
    @Value("${jwt.online-key}")
    private String onlineKey;
    @Value("${jwt.keep-online-key}")
    private String keepOnlineKey;
    @Value("${jwt.access-token-type}")
    private String accessTokenType;
    @Value("${jwt.refresh-token-type}")
    private String refreshTokenType;

    @Value("${sunten.oaUri}")
    private String oaUri;
    @Value("${sunten.oauth.oa.client_type}")
    private String oaClientType;
    @Value("${sunten.oauth.oa.client_id}")
    private String oaClientId;
    @Value("${sunten.oauth.oa.client_secret}")
    private String oaClientSecret;
    @Value("${sunten.oauth.hrms.client_id}")
    private String hrmsClientId;
    @Value("${sunten.oauth.hrms.client_secret}")
    private String hrmsClientSecret;
    @Value("${sunten.hrmsVueIp}")
    private String hrmsVueIp;
    @Value("${sunten.hrmsVuePort}")
    private String hrmsVuePort;
    @Value("${sunten.mobilePlatformUrl}")
    private String mobilePlatformUrl;
    @Value("${sunten.oauth.mobileplatform.client_type}")
    private String mobilePlatformClientType;
    @Value("${sunten.oauth.mobileplatform.client_id}")
    private String mobilePlatformClientId;
    @Value("${sunten.oauth.mobileplatform.client_secret}")
    private String mobilePlatformClientSecret;


    public JwtAuthorizationTokenFilter(@Qualifier("jwtUserDetailsServiceImpl") UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil, RedisTemplate redisTemplate, LoginService loginService) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.redisTemplate = redisTemplate;
        this.loginService = loginService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        boolean canContinue = true;
        log.debug("========= 请求地址：" + request.getServletPath() + "  " + request.getMethod());
        if (request.getMethod().equals("OPTIONS")) {

        } else {
            String authToken = jwtTokenUtil.getToken(request);
            Boolean payloadType = false;
            Boolean createToken = false;
            String clientType = null;
            String type = request.getContentType();
            String payLoad = null;
            AuthUser authUser = new AuthUser();
            Map<String, Object> payLoadParamMap = new HashMap<String, Object>();
            if (!StringUtils.isNullOrEmpty(type)
                    && !type.contains("application/x-www-form-urlencoded")
                    && (type.startsWith("application/json") || type.startsWith("text/plain"))) {
                request = new ReaderReuseHttpServletRequestWrapper(request);
                Reader reader = request.getReader();
                //         读取Request Payload数据
                payLoad = IOUtils.toString(reader);
                if (StringUtils.isNullOrEmpty(payLoad) || !payLoad.contains("\"clientType\"")) {
                    payloadType = false;
                } else {
                    payloadType = false;
                    if (type.startsWith("application/json")) {
                        payloadType = true;
                        JSONObject jsonObject = JSONObject.parseObject(payLoad);
                        if (jsonObject != null) {
                            for (Map.Entry entry : jsonObject.entrySet()) {
                                payLoadParamMap.put((String) entry.getKey(), entry.getValue());
                            }
                        }
                    } else {
                        if (type.startsWith("text/plain")) {
                            payloadType = true;
                            String[] kvs = payLoad.split("&");
                            for (String kv : kvs) {
                                String[] lf = kv.split("=");
                                payLoadParamMap.put(lf[0], lf[1]);
                            }
                        }
                    }
                }
            }
            if (payloadType) {
                clientType = (String) payLoadParamMap.get("clientType");
            } else {
                clientType = request.getParameter("clientType");
            }
            if (!StringUtils.isNullOrEmpty(clientType)) {
//                if (oaClientType.equals(clientType)) {
                if (oaClientType.equals(clientType) || mobilePlatformClientType.equals(clientType)) {
                    String createTokenStr = "N";
                    if (payloadType) {
                        authUser.setSessionId((String) payLoadParamMap.get("sessionId"));
                        authUser.setUsername((String) payLoadParamMap.get("username"));
                        createTokenStr = (String) payLoadParamMap.get("createToken");
                    } else {
                        authUser.setSessionId(request.getParameter("sessionId"));
                        authUser.setUsername(request.getParameter("username"));
                        createTokenStr = request.getParameter("createToken");
                    }
                    createToken = !StringUtils.isNullOrEmpty(createTokenStr) && "Y".equals(createTokenStr);
                }
            }
            if (StringUtils.isNullOrEmpty(clientType)) {
                //非远程调用
                OnlineUser onlineUser = null;
                if (null != authToken) {
                    try {
                        onlineUser = (OnlineUser) redisTemplate.opsForValue().get(onlineKey + "::" + jwtTokenUtil.getUsernameFromToken(authToken) + ":" + authToken);
                    } catch (ExpiredJwtException e) {
                        log.error(e.getMessage());
                    }
                }
                if (request.getServletPath().equals("/auth/refreshToken")) {
                    log.debug("==== refreshToken 拦截检查 =====");
                    log.debug("      onlineUser : " + onlineUser);
                    log.debug("       authToken : " + authToken);
                    if (onlineUser != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        String refreshToken = null;
                        try {
                            refreshToken = EncryptUtils.desDecrypt(onlineUser.getRefreshKey());
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                        String refreshAuthToken = jwtTokenUtil.getRefreshToken(request);
                        log.debug("refreshAuthToken : " + refreshAuthToken);
                        if (refreshToken.equals(refreshAuthToken) && jwtTokenUtil.getTokenType(authToken).equals(accessTokenType) && jwtTokenUtil.getTokenType(refreshAuthToken).equals(refreshTokenType)) {
                            JwtUser userDetails = (JwtUser) this.userDetailsService.loadUserByUsername(onlineUser.getUserName());
                            if (jwtTokenUtil.validateToken(refreshAuthToken, userDetails)) {
                                log.debug(" refreshAuthToken 验证成功 ");
                                generateAuthentication(request, userDetails);
                            } else {
                                log.debug(" refreshAuthToken 验证失败 ");
                                //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "登录已过期");
                            }
                        } else {
                            log.debug("       登录信息无效    ");
                            //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "登录信息无效");
                        }
                    } else {
                        log.debug("       登录已过期    ");
                        //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "登录已过期");
                        //response.setStatus();
                    }
                } else {
                    Boolean remoteClintCheckPass = true;
                    if (request.getServletPath().equals("/auth/checkToken")) {
                        String clientId = request.getParameter("clientId");
                        String clientSecret = request.getParameter("clientSecret");
                        remoteClintCheckPass = oaClientId.equals(clientId) && oaClientSecret.equals(clientSecret);
                    }
                    log.debug("========= remoteClintCheckPass：" + remoteClintCheckPass);
                    if (remoteClintCheckPass) {
                        try {
                            if (onlineUser == null) {
                                if (!request.getServletPath().equals("/swagger-ui.html")) {
                                    response.setHeader("Content-Disposition", "attachment");
                                }
                                if (null != authToken) {
                                    onlineUser = (OnlineUser) redisTemplate.opsForValue().get(keepOnlineKey + "::" + jwtTokenUtil.getUsernameFromToken(authToken) + ":" + authToken);
                                }
                            }
                        } catch (ExpiredJwtException e) {
                            log.error(e.getMessage());
                        }
                        if (onlineUser != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                            // It is not compelling necessary to load the use details from the database. You could also store the information
                            // in the token and read it from it. It's up to you ;)
                            JwtUser userDetails = (JwtUser) this.userDetailsService.loadUserByUsername(onlineUser.getUserName());
                            //System.out.println("userDetails="+userDetails.getAuthorities());
                            // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
                            // the database compellingly. Again it's up to you ;)
                            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                                generateAuthentication(request, userDetails);
                            }
                        }
                    }
                }
            } else {
                OnlineUser onlineUser = null;
                if (!StringUtils.isNullOrEmpty(authToken)) {
                    if (null != authToken) {
                        try {
                            onlineUser = (OnlineUser) redisTemplate.opsForValue().get(onlineKey + "::" + jwtTokenUtil.getUsernameFromToken(authToken) + ":" + authToken);
                        } catch (ExpiredJwtException e) {
                            log.error(e.getMessage());
                        }
                    }
                    if (onlineUser != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        // It is not compelling necessary to load the use details from the database. You could also store the information
                        // in the token and read it from it. It's up to you ;)
                        JwtUser userDetails = (JwtUser) this.userDetailsService.loadUserByUsername(onlineUser.getUserName());
                        //System.out.println("userDetails="+userDetails.getAuthorities());
                        // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
                        // the database compellingly. Again it's up to you ;)
                        if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                            generateAuthentication(request, userDetails);
                        }
                    }
                }
                if (onlineUser == null) {
                    // OA远程调用
                    if (oaClientType.equals(clientType) && !StringUtils.isNullOrEmpty(authUser.getSessionId()) && !StringUtils.isNullOrEmpty(authUser.getUsername())) {
                        log.debug("=========begin================ 调用OA接口检查session是否有效合法");
                        URI tagetUri = null;
                        try {
                            tagetUri = new URIBuilder(oaUri + "/OA/checkJSidAndUserName.do?clientId=" + URLEncoder.encode(hrmsClientId, "UTF-8") + "&clientSecret=" + URLEncoder.encode(hrmsClientSecret, "UTF-8"))
                                    .build();
                        } catch (URISyntaxException e) {

                        }
                        log.debug("tagetUri:" + tagetUri);
                        String payload = JSON.toJSONString(authUser);
                        log.debug("payload:" + payload);
                        String checkJsessionIdResult = SendRequestUtil.sendPostRequest(tagetUri, payload, authUser.getSessionId());
                        log.debug("返回结果:" + checkJsessionIdResult);
                        log.debug("=========== end ============== 调用OA接口检查session是否有效合法");
                        if ("200".equals(checkJsessionIdResult)) {
                            if (createToken) {
                                // 登录
                                try {
                                    log.debug("=====begin===== 创建用户登录信息：" + checkJsessionIdResult);
                                    AuthInfo authInfo = loginService.loginM(authUser, request, false);
                                    log.debug("用户登录信息：" + authInfo);
                                    log.debug("设置cookie：IP:***" + hrmsVueIp + "***端口:***" + hrmsVuePort + "***");
                                    response.addCookie(this.generateCookie(HRMS_TOKEN_EXPIRE_DATE + "_" + hrmsVuePort, authInfo.getTokenExpireDate().toString(), hrmsVueIp, null));
                                    response.addCookie(this.generateCookie(HRMS_TOKEN + "_" + hrmsVuePort, authInfo.getToken(), hrmsVueIp, null));
                                    response.addCookie(this.generateCookie(HRMS_REFRESH_TOKEN + "_" + hrmsVuePort, authInfo.getRefreshToken(), hrmsVueIp, null));
                                    log.debug("===== end ===== 创建用户登录信息：" + checkJsessionIdResult);
                                } catch (Exception e) {
//                                e.printStackTrace();
                                    log.error(ThrowableUtil.getStackTrace(e));
                                }
                            } else {
                                // 单次调用，不需要保存登录信息
                            }
                            JwtUser userDetails = (JwtUser) this.userDetailsService.loadUserByUsername(authUser.getUsername());
                            generateAuthentication(request, userDetails);
                        }
                    }
                    // 暂时不需要帮忙建立HRMS的页面Cookies， mobilePlatform的调用逻辑
                    else {
                        String mobileToken = jwtTokenUtil.getToken(request); // mobilePlatform的token
                        if (mobilePlatformClientType.equals(clientType) && !StringUtils.isNullOrEmpty(authUser.getSessionId()) && !StringUtils.isNullOrEmpty(authUser.getUsername()) && !StringUtils.isNullOrEmpty(mobileToken)) {
                            log.debug("代发送checkJSidAndUserName前夕");
                            URI tagetUri = null;
                            try {
                                tagetUri = new URIBuilder(mobilePlatformUrl + "auth/checkJSidAndUserName.do?clientId=" + URLEncoder.encode(hrmsClientId, "UTF-8") + "&clientSecret=" + URLEncoder.encode(hrmsClientSecret, "UTF-8")
                                        + "&mobileToken=" + URLEncoder.encode(mobileToken, "UTF-8"))
                                        .build();
                            } catch (URISyntaxException e) {

                            }
                            String payload = JSON.toJSONString(authUser);
                            String checkJsessionIdResult = SendRequestUtil.sendPostRequest(tagetUri, payload, authUser.getSessionId());
                            if ("200".equals(checkJsessionIdResult)) {
                                JwtUser userDetails = (JwtUser) this.userDetailsService.loadUserByUsername(authUser.getUsername());
                                generateAuthentication(request, userDetails);
                            }
                        }
                    }
                }
            }
        }
        chain.doFilter(request, response);
    }

    private void generateAuthentication(HttpServletRequest request, JwtUser userDetails) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Cookie generateCookie(String name, String value, String domain, String path) throws UnsupportedEncodingException {
        Cookie cookie;
        if (value != null) {
            String encode = URLEncoder.encode(value, "UTF-8");
            cookie = new Cookie(name, encode);
        } else {
            cookie = new Cookie(name, null);
        }
        if (null != domain) {
            cookie.setDomain(domain);
        }
        cookie.setMaxAge(60 * 60 * 2);//单位秒
        if (null != path) {
            cookie.setPath(path);
        } else {
            cookie.setPath("/");
        }
        return cookie;
    }
}
