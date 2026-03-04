package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmItPermissions;
import com.sunten.hrms.pm.dto.PmItPermissionsDTO;
import com.sunten.hrms.pm.dto.PmItPermissionsQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.pm.vo.PmItPermissionList;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * it权限清单 服务类
 * </p>
 *
 * @author liangjw
 * @since 2021-05-10
 */
public interface PmItPermissionsService extends IService<PmItPermissions> {

    PmItPermissionsDTO insert(PmItPermissions itPermissionsNew);

    void delete(Long id);

    void delete(PmItPermissions itPermissions);

    void update(PmItPermissions itPermissionsNew);

    PmItPermissionsDTO getByKey(Long id);

    List<PmItPermissionsDTO> listAll(PmItPermissionsQueryCriteria criteria);

    Map<String, Object> listAll(PmItPermissionsQueryCriteria criteria, Pageable pageable);

    void download(List<PmItPermissionsDTO> itPermissionsDTOS, HttpServletResponse response) throws IOException;

    PmItPermissionList getPermissionListByBelong();
}
