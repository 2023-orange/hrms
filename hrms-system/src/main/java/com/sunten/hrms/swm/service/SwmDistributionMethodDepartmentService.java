package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmDistributionMethodDepartment;
import com.sunten.hrms.swm.dto.SwmDistributionMethodDepartmentDTO;
import com.sunten.hrms.swm.dto.SwmDistributionMethodDepartmentQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 分配方式部门科室关联表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
public interface SwmDistributionMethodDepartmentService extends IService<SwmDistributionMethodDepartment> {

    SwmDistributionMethodDepartmentDTO insert(SwmDistributionMethodDepartment distributionMethodDepartmentNew);

    void delete(Long id);

    void delete(SwmDistributionMethodDepartment distributionMethodDepartment);

    void update(SwmDistributionMethodDepartment distributionMethodDepartmentNew);

    SwmDistributionMethodDepartmentDTO getByKey(Long id);

    List<SwmDistributionMethodDepartmentDTO> listAll(SwmDistributionMethodDepartmentQueryCriteria criteria);

    Map<String, Object> listAll(SwmDistributionMethodDepartmentQueryCriteria criteria, Pageable pageable);

    void download(List<SwmDistributionMethodDepartmentDTO> distributionMethodDepartmentDTOS, HttpServletResponse response) throws IOException;

}
