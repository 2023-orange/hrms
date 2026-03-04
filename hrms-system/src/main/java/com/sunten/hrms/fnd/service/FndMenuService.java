package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.domain.FndMenu;
import com.sunten.hrms.fnd.dto.FndMenuDTO;
import com.sunten.hrms.fnd.dto.FndMenuQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.fnd.dto.FndRoleSmallDTO;
import org.springframework.data.domain.Pageable;
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
public interface FndMenuService extends IService<FndMenu> {

    FndMenuDTO insert(FndMenu menuNew);

    void delete(Long id);

    void update(FndMenu menuNew);

    FndMenuDTO getByKey(Long id);

    List<FndMenuDTO> listAll(FndMenuQueryCriteria criteria);

    Map<String, Object> listAll(FndMenuQueryCriteria criteria, Pageable pageable);

    void download(List<FndMenuDTO> menuDTOS, HttpServletResponse response) throws IOException;

    void delete(Set<FndMenu> menus);

    Set<FndMenu> listMenus(List<FndMenu> menuList, Set<FndMenu> menuSet);

    Object getMenuTree(List<FndMenu> menus);

    List<FndMenu> listByPid(long pid);

    Map<String, Object> buildTree(List<FndMenuDTO> menuDTOS);

    List<FndMenuDTO> listByRoles(List<FndRoleSmallDTO> roles);

    Object buildMenus(List<FndMenuDTO> menuDTOS);

    FndMenu getOne(Long id);
}
