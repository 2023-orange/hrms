package com.sunten.hrms.security.utils;

import cn.hutool.core.util.ObjectUtil;
import com.sunten.hrms.security.security.JwtUser;
import com.sunten.hrms.utils.DateUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;

    @Value("${jwt.base64-secret}")
    private String base64Secret;

    @Value("${jwt.access-token-type}")
    private String accessTokenType;

    @Value("${jwt.refresh-token-type}")
    private String refreshTokenType;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.refresh-header}")
    private String refreshTokenHeader;

    public String getUsernameFromToken(String token) {
        if (ObjectUtil.isEmpty(token)) {
            return "";
        } else {
            final Claims claims = getAllClaimsFromToken(token);
            return claims.get("userName", String.class);
        }
    }

    public String getTokenType(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private LocalDateTime getIssuedAtDateFromToken(String token) {
        if (ObjectUtil.isEmpty(token)) {
            return LocalDateTime.now();
        } else {
            return DateUtil.asLocalDateTime(getClaimFromToken(token, Claims::getIssuedAt));
        }
    }

    public LocalDateTime getExpirationDateFromToken(String token) {
        if (ObjectUtil.isEmpty(token)) {
            return LocalDateTime.now();
        } else {
            return DateUtil.asLocalDateTime(getClaimFromToken(token, Claims::getExpiration));
        }
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        if (ObjectUtil.isEmpty(token)) {
            return null;
        } else {
            final Claims claims = getAllClaimsFromToken(token);
            return claimsResolver.apply(claims);
        }
    }

    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
//                    .setSigningKey(base64Secret)
                    .setSigningKey(this.getSecretKey())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            claims = ex.getClaims();
        }
        return claims;
    }

    private Boolean isTokenExpired(String token) {
        if (ObjectUtil.isEmpty(token)) {
            return true;
        } else {
            final LocalDateTime expiration = getExpirationDateFromToken(token);
            return expiration.isBefore(LocalDateTime.now());
        }
    }

    private Boolean isCreatedBeforeLastPasswordReset(LocalDateTime created, LocalDateTime lastPasswordReset) {
        return (lastPasswordReset != null && created.isBefore(lastPasswordReset));
    }

    private Boolean ignoreTokenExpiration(String token) {
        // here you specify tokens, for that the expiration is ignored
        return false;
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userName", userDetails.getUsername());
        return doGenerateToken(claims, accessTokenType);
    }


    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userName", userDetails.getUsername());
        return doGenerateToken(claims, refreshTokenType);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        final LocalDateTime createdDate = LocalDateTime.now();
        final LocalDateTime expirationDate = calculateExpirationDate(createdDate, subject);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(DateUtil.asDate(createdDate))
                .setExpiration(DateUtil.asDate(expirationDate))
//                .signWith(SignatureAlgorithm.HS512, base64Secret)
                .signWith(this.getSecretKey())
                .compact();
    }

    public Boolean canTokenBeRefreshed(String token, LocalDateTime lastPasswordReset) {
        final LocalDateTime created = getIssuedAtDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public String refreshToken(String token) {
        final LocalDateTime createdDate = LocalDateTime.now();
        final LocalDateTime expirationDate = calculateExpirationDate(createdDate, accessTokenType);

        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(DateUtil.asDate(createdDate));
        claims.setExpiration(DateUtil.asDate(expirationDate));

        return Jwts.builder()
                .setClaims(claims)
//                .signWith(SignatureAlgorithm.HS512, base64Secret)
                .signWith(this.getSecretKey())
                .compact();
    }

    public String getToken(HttpServletRequest request) {
        final String requestHeader = request.getHeader(tokenHeader);
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            return requestHeader.substring(7);
        }
        return null;
    }


    public String getRefreshToken(HttpServletRequest request) {
        final String requestHeader = request.getHeader(refreshTokenHeader);
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            return requestHeader.substring(7);
        }
        return null;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        JwtUser user = (JwtUser) userDetails;
        final LocalDateTime created = getIssuedAtDateFromToken(token);
//        final LocalDateTime expiration = getExpirationDateFromToken(token);
//        如果token存在，且token创建日期 > 最后修改密码的日期 则代表token有效
        return (!isTokenExpired(token)
                && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate())
        );
    }

    public int validateTokenCase(String token, UserDetails userDetails) {
        JwtUser user = (JwtUser) userDetails;
        final LocalDateTime created = getIssuedAtDateFromToken(token);
//        final LocalDateTime expiration = getExpirationDateFromToken(token);
//        如果token存在，且token创建日期 > 最后修改密码的日期 则代表token有效
        int result = 0;//验证成功
        if (isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate())) {
            result = 1;//密码已更新
        } else {
            if (isTokenExpired(token)) {
                result = 2;//token过期
            }
        }
        return result;
    }

    private LocalDateTime calculateExpirationDate(LocalDateTime createdDate, String tokenType) {
        if (tokenType.equals(refreshTokenType)) {
            return createdDate.plusSeconds(refreshExpiration / 1000);
        } else {
            return createdDate.plusSeconds(expiration / 1000);
        }
    }

    private SecretKey getSecretKey() {
        return new SecretKeySpec(Base64.decodeBase64(base64Secret), SignatureAlgorithm.HS512.getJcaName());
    }
}

