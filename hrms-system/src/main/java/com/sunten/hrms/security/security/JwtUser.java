package com.sunten.hrms.security.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sunten.hrms.pm.domain.PmEmployee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author batan
 * @since 2018-11-23
 */
@Getter
@AllArgsConstructor
public class JwtUser implements UserDetails {

    @JsonIgnore
    private final Long id;

    private final String username;

    private final String description;

    @JsonIgnore
    private final String salt;

    @JsonIgnore
    private final String password;

    private final String avatar;

    private final String email;

    private final String phone;

    @JsonIgnore
    private final Collection<GrantedAuthority> authorities;

    private final boolean enabledFlag;

    private LocalDateTime createTime;

    @JsonIgnore
    private final LocalDateTime lastPasswordResetDate;

    private final String dept;

    private final String job;

    private final PmEmployee employee;

    private final String extDeptName;

    private final String extDepartmentName;

    private final String extTeamName;

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    public String getSalt() {
        return salt;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isEnabled() {
        return enabledFlag;
    }

    public Collection getRoles() {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }
}
