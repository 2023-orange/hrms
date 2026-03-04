package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmLeaveApproval;
import com.sunten.hrms.pm.dto.PmEmployeeDTO;
import com.sunten.hrms.pm.dto.PmEmployeeQueryCriteria;
import com.sunten.hrms.pm.dto.PmLeaveApprovalDTO;
import com.sunten.hrms.pm.dto.PmLeaveApprovalQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.pm.vo.SalesAreaEtcVo;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * <p>
 * 离职审批表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2021-05-07
 */
public interface PmLeaveApprovalService extends IService<PmLeaveApproval> {

    PmLeaveApprovalDTO insert(PmLeaveApproval leaveApprovalNew);

    void delete(Long id);

    void delete(PmLeaveApproval leaveApproval);

    void update(PmLeaveApproval leaveApprovalNew);

    PmLeaveApprovalDTO getByKey(Long id);

    List<PmLeaveApprovalDTO> listAll(PmLeaveApprovalQueryCriteria criteria);

    Map<String, Object> listAll(PmLeaveApprovalQueryCriteria criteria, Pageable pageable);

    void download(List<PmLeaveApprovalDTO> leaveApprovalDTOS, HttpServletResponse response) throws IOException;

    void disabledById(Long id);

    PmLeaveApprovalDTO getByOaOrder(String oaOrder);

    SalesAreaEtcVo getSalesAreaEtcByDeptId(Long deptId);

    /**
     *  @author liangjw
     *  @since 2022/2/17 8:40
     *  根据销售关系获取所有销售人员信息
     */
    List<PmEmployeeDTO> getPmsBySalesRelations(PmEmployeeQueryCriteria pmEmployeeQueryCriteria);

    Boolean checkIsExistLeaveInApproval(Long employeeId);

    List<PmLeaveApproval> listALLByStatus(String approvalStatus);
}
