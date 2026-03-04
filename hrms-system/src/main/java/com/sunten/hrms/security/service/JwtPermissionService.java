package com.sunten.hrms.security.service;

import com.sunten.hrms.fnd.dto.FndUserDTO;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author batan
 * @since  2019-12-24
 */
public interface JwtPermissionService {
    Collection<GrantedAuthority> mapToGrantedAuthorities(FndUserDTO user);
}
