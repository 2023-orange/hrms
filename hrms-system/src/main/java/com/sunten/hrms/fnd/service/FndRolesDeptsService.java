package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.domain.FndRolesDepts;
import com.sunten.hrms.fnd.dto.FndRolesDeptsDTO;
import com.sunten.hrms.fnd.dto.FndRolesDeptsQueryCriteria;
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
public interface FndRolesDeptsService extends IService<FndRolesDepts> {

    FndRolesDeptsDTO insert(FndRolesDepts rolesDeptsNew);

    void delete(Long roleId, Long deptId);

    void update(FndRolesDepts rolesDeptsNew);

    FndRolesDeptsDTO getByKey(Long roleId, Long deptId);

    List<FndRolesDeptsDTO> listAll(FndRolesDeptsQueryCriteria criteria);

    Map<String, Object> listAll(FndRolesDeptsQueryCriteria criteria, Pageable pageable);

    void download(List<FndRolesDeptsDTO> rolesDeptsDTOS, HttpServletResponse response) throws IOException;
}
