package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.domain.FndRole;
import com.sunten.hrms.fnd.dto.FndRoleDTO;
import com.sunten.hrms.fnd.dto.FndRoleQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.fnd.dto.FndRoleSmallDTO;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
public interface FndRoleService extends IService<FndRole> {

    FndRoleDTO insert(FndRole roleNew);

    void delete(Long id);

    void update(FndRole roleNew);

    FndRoleDTO getByKey(Long id);

    List<FndRoleDTO> listAll(FndRoleQueryCriteria criteria);

    Map<String, Object> listAll(FndRoleQueryCriteria criteria, Pageable pageable);

    void download(List<FndRoleDTO> roleDTOS, HttpServletResponse response) throws IOException;

    List<FndRoleSmallDTO> listByUserId(Long id);

    List<FndRole> listByUserIdComp(Long id);

    Integer getByRoles(Set<FndRole> roles);

    void updateMenu(FndRole resources);

    void untiedMenu(Long id);

    Object listAll(Pageable pageable);

}
