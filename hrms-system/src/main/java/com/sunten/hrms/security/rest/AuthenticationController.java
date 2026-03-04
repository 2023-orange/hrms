package com.sunten.hrms.security.rest;

import cn.hutool.core.util.IdUtil;
import com.sunten.hrms.annotation.AnonymousAccess;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.service.RedisService;
import com.sunten.hrms.security.security.AuthInfo;
import com.sunten.hrms.security.security.AuthUser;
import com.sunten.hrms.security.security.ImgResult;
import com.sunten.hrms.security.security.JwtUser;
import com.sunten.hrms.security.service.LoginService;
import com.sunten.hrms.security.service.OnlineUserService;
import com.sunten.hrms.security.service.RsaService;
import com.sunten.hrms.security.utils.JwtTokenUtil;
import com.sunten.hrms.utils.DateUtil;
import com.sunten.hrms.utils.EncryptUtils;
import com.sunten.hrms.utils.SecurityUtils;
import com.wf.captcha.ArithmeticCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author batan
 * @since 2018-11-23
 * 授权、根据token获取用户详细信息
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@Api(tags = "系统：系统授权接口")
public class AuthenticationController {

    private final JwtTokenUtil jwtTokenUtil;
    private final RedisService redisService;
    private final UserDetailsService userDetailsService;
    private final OnlineUserService onlineUserService;
    private final RsaService rsaService;
    private final LoginService loginService;
    @Value("${jwt.code-key}")
    private String codeKey;
    @Value("${sunten.oauth.oa.client_id}")
    private String oaClientId;
    @Value("${sunten.oauth.oa.client_secret}")
    private String oaClientSecret;
    @Value("${sunten.oauth.hrms.client_id}")
    private String hrmsClientId;
    @Value("${sunten.oauth.hrms.client_secret}")
    private String hrmsClientSecret;
    @Value("${sunten.oaUri}")
    private String oaUri;

    public AuthenticationController(JwtTokenUtil jwtTokenUtil, RedisService redisService, @Qualifier("jwtUserDetailsServiceImpl") UserDetailsService userDetailsService, OnlineUserService onlineUserService, RsaService rsaService, LoginService loginService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.redisService = redisService;
        this.userDetailsService = userDetailsService;
        this.onlineUserService = onlineUserService;
        this.rsaService = rsaService;
        this.loginService = loginService;
    }

    @Log("用户登录")
    @ApiOperation("登录授权")
    @AnonymousAccess
    @PostMapping(value = "/login")
    public ResponseEntity login(@Validated @RequestBody AuthUser authUser, HttpServletRequest request) throws InvalidKeySpecException, NoSuchAlgorithmException {
        // 查询验证码
//        String code = redisService.getCodeVal(authUser.getUuid());
//        // 清除验证码
//        redisService.delete(authUser.getUuid());
//        if (StringUtils.isBlank(code)) {
//            throw new InfoCheckWarningMessException("验证码已过期");
//        }
//        if (StringUtils.isBlank(authUser.getCode()) || !authUser.getCode().equalsIgnoreCase(code)) {
//            throw new InfoCheckWarningMessException("验证码错误");
//        }
        String pass = rsaService.transactionPrivateDecrypt(authUser.getPassword());
        authUser.setPassword(pass);

        AuthInfo result = loginService.loginM(authUser, request, true);
        return ResponseEntity.ok(result);
    }


    @ErrorLog("刷新令牌")
    @PreAuthorize("@el.check('refreshToken:run')")
    @PostMapping(value = "/refreshToken")
    public ResponseEntity refreshToken(HttpServletRequest request) {
        String oldToken = jwtTokenUtil.getToken(request);
        if (null != oldToken && !"".equals(oldToken)) {
            final JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(oldToken));
            // 生成令牌
            final String token = jwtTokenUtil.generateToken(jwtUser);
            final String refreshToken = jwtTokenUtil.generateRefreshToken(jwtUser);
            Long tokenExpireDate = DateUtil.asLong(jwtTokenUtil.getExpirationDateFromToken(token));
            // 保存在线信息
            onlineUserService.updateExpireTime(jwtUser.getUsername(), oldToken);
            onlineUserService.save(jwtUser, token, refreshToken, request);
            // 返回 token
            return ResponseEntity.ok(new AuthInfo(token, refreshToken, jwtUser, tokenExpireDate, 200L));
        } else {
            throw new BadRequestException("用户信息错误，无法刷新令牌");
        }
    }


    @ApiOperation("获取公钥")
    @AnonymousAccess
    @PostMapping(value = "/transactionPublicKey")
    public ResponseEntity getTransactionPublicKey() {
        return ResponseEntity.ok(rsaService.getTransactionPublicKey());
    }

    @ApiOperation("获取用户信息")
    @GetMapping(value = "/info")
    public ResponseEntity getUserInfo() {
        JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(SecurityUtils.getUsername());
        return ResponseEntity.ok(jwtUser);
    }

    @ApiOperation("获取验证码")
    @AnonymousAccess
    @GetMapping(value = "/code")
    public ImgResult getCode() {
        // 算术类型 https://gitee.com/whvse/EasyCaptcha
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(111, 36);
        // 几位数运算，默认是两位
        captcha.setLen(2);
        // 获取运算的结果：5
        String result = captcha.text();
        String uuid = codeKey + IdUtil.simpleUUID();
        redisService.saveCode(uuid, result);
        return new ImgResult(captcha.toBase64(), uuid);
    }

    @ApiOperation("退出登录")
    @AnonymousAccess
    @DeleteMapping(value = "/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        onlineUserService.logout(jwtTokenUtil.getToken(request));
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation("用户密码验证")
    @AnonymousAccess
    @GetMapping(value = "/verifyUser")
    public ResponseEntity verifyUser(String username, String password) throws Exception {
        String pass = EncryptUtils.desEcbNoPaddingDecrypt(password);
        final JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(username);

        if (!jwtUser.getPassword().equals(rsaService.passEncrypt(jwtUser.getSalt() + pass))) {
            throw new InfoCheckWarningMessException("密码错误");
        }

        if (!jwtUser.isEnabled()) {
            throw new InfoCheckWarningMessException("账号已停用，请联系管理员");
        }

        Map<String, String> result = new HashMap<>();
        result.put("status", "200");
        result.put("message", "验证通过");
        return ResponseEntity.ok(result);
    }

    @ApiOperation("令牌验证")
    @PreAuthorize("@el.check('checkToken:run')")
    @GetMapping(value = "/checkToken")
    public ResponseEntity checkToken() {
        JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(SecurityUtils.getUsername());
        Map<String, String> result = new HashMap<>();
        result.put("status", "200");
        result.put("message", "验证通过");
        result.put("username", jwtUser.getUsername());
        return ResponseEntity.ok(result);
    }


//    @ApiOperation("远程登录")
//    @AnonymousAccess
//    @GetMapping(value = "/remoteLogin")
//    public ResponseEntity remoteLogin(@Validated @RequestBody AuthUser authUser, String receiveClientId, HttpServletRequest request) throws Exception {
//        System.out.println("receiveClientId:" + receiveClientId);
//        System.out.println("oaClientId" + oaClientId);
//        if (!oaClientId.equals(receiveClientId)) {
//            throw new InfoCheckWarningMessException("OA客户端不受信");
//        }
//        System.out.println("username:" + authUser.getUsername());
//        System.out.println("JSESSIONID:" + authUser.getSessionId() );
//        System.out.println(hrmsClientId);
//        System.out.println(hrmsClientSecret);
//        String payload = JSON.toJSONString(authUser);
//        System.out.println(payload);
//        URI tagetUri = new URIBuilder(oaUri + "/OA/checkJSidAndUserName.do?clientId=" + hrmsClientId  + "&clientSecret=" + hrmsClientSecret)
//                .build();
//        System.out.println(oaUri + "/OA/checkJSidAndUserName.do?clientId=" + hrmsClientId  + "&clientSecret=" + hrmsClientSecret);
//        String checkJsessionIdResult = SendRequestUtil.sendPostRequest(tagetUri, payload, authUser.getSessionId());
//
//
////        String checkJsessionIdResult = SendRequestUtil.sendGetRequest(tagetUri, null, authUser.getOaSid());
//        System.out.println("checkJsessionIdResult:" + checkJsessionIdResult );
////        String pass = EncryptUtils.desEcbNoPaddingDecrypt(authUser.getPassword());
////        authUser.setPassword(pass);
//        if ("200".equals(checkJsessionIdResult)) {
//            // 验证成功，登录
//            AuthInfo result = loginM(authUser, request, false);
//            return new ResponseEntity<>(result, HttpStatus.OK);
//        } else {
//            if ("405".equals(checkJsessionIdResult)) {
//                throw new InfoCheckWarningMessException("HRMS客户端不受信");
//            } else {
//                throw new InfoCheckWarningMessException("HRMS登录失败");
//            }
//        }
//    }

}
