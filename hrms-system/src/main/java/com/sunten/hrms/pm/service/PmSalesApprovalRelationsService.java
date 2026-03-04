package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmSalesApprovalRelations;
import com.sunten.hrms.pm.dto.PmSalesApprovalRelationsDTO;
import com.sunten.hrms.pm.dto.PmSalesApprovalRelationsQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 销售审批节点关系表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2022-02-17
 */
public interface PmSalesApprovalRelationsService extends IService<PmSalesApprovalRelations> {

    PmSalesApprovalRelationsDTO insert(PmSalesApprovalRelations salesApprovalRelationsNew);

    void delete(Long id);

    void delete(PmSalesApprovalRelations salesApprovalRelations);

    void update(PmSalesApprovalRelations salesApprovalRelationsNew);

    PmSalesApprovalRelationsDTO getByKey(Long id);

    List<PmSalesApprovalRelationsDTO> listAll(PmSalesApprovalRelationsQueryCriteria criteria);

    Map<String, Object> listAll(PmSalesApprovalRelationsQueryCriteria criteria, Pageable pageable);

    void download(List<PmSalesApprovalRelationsDTO> salesApprovalRelationsDTOS, HttpServletResponse response) throws IOException;
}
