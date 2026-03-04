package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.domain.FndRolesMenus;
import com.sunten.hrms.fnd.dto.FndRolesMenusDTO;
import com.sunten.hrms.fnd.dto.FndRolesMenusQueryCriteria;
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
public interface FndRolesMenusService extends IService<FndRolesMenus> {

    FndRolesMenusDTO insert(FndRolesMenus rolesMenusNew);

    void delete(Long menuId, Long roleId);

    void update(FndRolesMenus rolesMenusNew);

    FndRolesMenusDTO getByKey(Long menuId, Long roleId);

    List<FndRolesMenusDTO> listAll(FndRolesMenusQueryCriteria criteria);

    Map<String, Object> listAll(FndRolesMenusQueryCriteria criteria, Pageable pageable);

    void download(List<FndRolesMenusDTO> rolesMenusDTOS, HttpServletResponse response) throws IOException;
}
