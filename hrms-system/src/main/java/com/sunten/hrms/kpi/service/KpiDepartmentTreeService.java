package com.sunten.hrms.kpi.service;

import com.sunten.hrms.kpi.domain.KpiDepartmentTree;
import com.sunten.hrms.kpi.dto.KpiDepartmentTreeDTO;
import com.sunten.hrms.kpi.dto.KpiDepartmentTreeQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.kpi.vo.KpiDepartmentTreeVo;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * KPI部门树表 服务类
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-27
 */
public interface KpiDepartmentTreeService extends IService<KpiDepartmentTree> {

    KpiDepartmentTreeDTO insert(KpiDepartmentTree departmentTreeNew);

    void delete(Long id);

    void delete(KpiDepartmentTree departmentTree);

    void update(KpiDepartmentTree departmentTreeNew);

    KpiDepartmentTreeDTO getByKey(Long id);

    List<KpiDepartmentTreeDTO> listAll(KpiDepartmentTreeQueryCriteria criteria);

    Map<String, Object> listAll(KpiDepartmentTreeQueryCriteria criteria, Pageable pageable);

    void download(List<KpiDepartmentTreeDTO> departmentTreeDTOS, HttpServletResponse response) throws IOException;

    Object buildTree(List<KpiDepartmentTreeDTO> kpiDepartmentTreeDTOS, Boolean ignoreTopFlag);

    String getNameByDeptId(Long id,String year);

    void updateByKpiTree(KpiDepartmentTree kpiDepartmentTree);
}
