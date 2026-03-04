package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmPermissionLeaveApproval;
import com.sunten.hrms.pm.dto.PmPermissionLeaveApprovalDTO;
import com.sunten.hrms.pm.dto.PmPermissionLeaveApprovalQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.pm.vo.PmItPermissionList;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 离职申请与IT权限关联表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2021-05-10
 */
public interface PmPermissionLeaveApprovalService extends IService<PmPermissionLeaveApproval> {

    PmPermissionLeaveApprovalDTO insert(PmPermissionLeaveApproval permissionLeaveApprovalNew);

    void delete(Long id);

    void delete(PmPermissionLeaveApproval permissionLeaveApproval);

    void update(PmPermissionLeaveApproval permissionLeaveApprovalNew);

    PmPermissionLeaveApprovalDTO getByKey(Long id);

    List<PmPermissionLeaveApprovalDTO> listAll(PmPermissionLeaveApprovalQueryCriteria criteria);

    Map<String, Object> listAll(PmPermissionLeaveApprovalQueryCriteria criteria, Pageable pageable);

    void download(List<PmPermissionLeaveApprovalDTO> permissionLeaveApprovalDTOS, HttpServletResponse response) throws IOException;

    PmItPermissionList getPermissionList(PmPermissionLeaveApprovalQueryCriteria criteria);

}
