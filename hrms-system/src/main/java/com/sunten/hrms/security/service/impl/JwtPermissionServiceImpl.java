package com.sunten.hrms.security.service.impl;

import com.sunten.hrms.fnd.domain.FndMenu;
import com.sunten.hrms.fnd.domain.FndRole;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndRoleService;
import com.sunten.hrms.security.service.JwtPermissionService;
import com.sunten.hrms.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@CacheConfig(cacheNames = "role")
public class JwtPermissionServiceImpl implements JwtPermissionService {

    private final FndRoleService fndRoleService;

    public JwtPermissionServiceImpl(FndRoleService fndRoleService) {
        this.fndRoleService = fndRoleService;
    }


    /**
     * key的名称如有修改，请同步修改 UserServiceImpl 中的 update 方法
     * @param user 用户信息
     * @return Collection
     */
    @Override
    @Cacheable(key = "'loadPermissionByUser:' + #p0.id")
    public Collection<GrantedAuthority> mapToGrantedAuthorities(FndUserDTO user) {
        log.debug("--------------------loadPermissionByUser:" + user.getId() + "---------------------");
        Set<FndRole> roles = new HashSet<>(fndRoleService.listByUserIdComp(user.getId()));
        Set<String> permissions = roles.stream().filter(role -> StringUtils.isNotBlank(role.getPermission())).map(FndRole::getPermission).collect(Collectors.toSet());
        permissions.addAll(
                roles.stream().flatMap(role -> role.getMenus().stream())
                        .filter(menu -> StringUtils.isNotBlank(menu.getPermission()))
                        .map(FndMenu::getPermission).collect(Collectors.toSet())
        );
        permissions.add("refreshToken:run");
        permissions.add("checkToken:run");
        permissions.add("dict:list");
        permissions.add("dynamicQuery:list");
        permissions.add("superQueryGroup:list");
        permissions.add("dept:list");
        permissions.add("job:list");
        permissions.add("attachedDocument:list");
        permissions.add("attachedDocument:add");
        permissions.add("attachedDocument:del");
        permissions.add("storage:list"); // 主要用于文件模板导出
        return permissions.stream().map(permission -> new SimpleGrantedAuthority(permission))
                .collect(Collectors.toList());
    }
}
