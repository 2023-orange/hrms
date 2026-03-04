package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.domain.FndUsersRoles;
import com.sunten.hrms.fnd.dto.FndUsersRolesDTO;
import com.sunten.hrms.fnd.dto.FndUsersRolesQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
public interface FndUsersRolesService extends IService<FndUsersRoles> {

    FndUsersRolesDTO insert(FndUsersRoles usersRolesNew);

    void delete(Long userId, Long roleId);

    void update(FndUsersRoles usersRolesNew);

    FndUsersRolesDTO getByKey(Long userId, Long roleId);

    List<FndUsersRolesDTO> listAll(FndUsersRolesQueryCriteria criteria);

    Map<String, Object> listAll(FndUsersRolesQueryCriteria criteria, Pageable pageable);

    void download(List<FndUsersRolesDTO> usersRolesDTOS, HttpServletResponse response) throws IOException;

    Boolean checkHaveRoleBySeIdAndPermission(Long seId, String permission);

    Boolean checkHaveRoleByUserIdAndPermission(Long userId, String permission);
}
