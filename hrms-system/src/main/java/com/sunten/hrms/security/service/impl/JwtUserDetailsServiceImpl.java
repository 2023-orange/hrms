package com.sunten.hrms.security.service.impl;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dto.FndDeptSmallDTO;
import com.sunten.hrms.fnd.dto.FndJobSmallDTO;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.mapper.PmEmployeeMapper;
import com.sunten.hrms.security.security.JwtUser;
import com.sunten.hrms.security.service.JwtPermissionService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author batan
 * @since 2018-11-22
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    private final FndUserService fndUserService;

    private final JwtPermissionService permissionService;

    private final PmEmployeeMapper pmEmployeeMapper;

    public JwtUserDetailsServiceImpl(FndUserService fndUserService, JwtPermissionService jwtPermissionService, PmEmployeeMapper pmEmployeeMapper) {
        this.fndUserService = fndUserService;
        this.permissionService = jwtPermissionService;
        this.pmEmployeeMapper = pmEmployeeMapper;
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        FndUserDTO user = fndUserService.getByName(username);
        if (user == null) {
            throw new InfoCheckWarningMessException("账号不存在");
        } else {
            return createJwtUser(user);
        }
    }

    public UserDetails createJwtUser(FndUserDTO user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getDescription(),
                user.getSalt(),
                user.getPassword(),
                user.getAvatar(),
                user.getEmail(),
                user.getPhone(),
                permissionService.mapToGrantedAuthorities(user),
                user.getEnabledFlag(),
                user.getCreateTime(),
                user.getLastPasswordResetTime(),
                Optional.ofNullable(user.getDept()).map(FndDeptSmallDTO::getDeptName).orElse(null),
                Optional.ofNullable(user.getJob()).map(FndJobSmallDTO::getJobName).orElse(null),
                pmEmployeeMapper.toEntity(user.getEmployee()),
                Optional.ofNullable(user.getDept()).map(FndDeptSmallDTO::getExtDeptName).orElse(null),
                Optional.ofNullable(user.getDept()).map(FndDeptSmallDTO::getExtDepartmentName).orElse(null),
                Optional.ofNullable(user.getDept()).map(FndDeptSmallDTO::getExtTeamName).orElse(null)
        );
    }
}
