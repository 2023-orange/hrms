package com.sunten.hrms.ac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.ac.domain.AcEmpDepts;
import com.sunten.hrms.ac.dto.AcEmpDeptsDTO;
import com.sunten.hrms.ac.dto.AcEmpDeptsQueryCriteria;
import com.sunten.hrms.pm.dto.PmEmployeeQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 考勤模块人员数据权限范围表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-12-09
 */
public interface AcEmpDeptsService extends IService<AcEmpDepts> {

    AcEmpDeptsDTO insert(AcEmpDepts empDeptsNew);

    void delete(Long id);

    void delete(AcEmpDepts empDepts);

    void update(AcEmpDepts empDeptsNew);

    AcEmpDeptsDTO getByKey(Long id);

    List<AcEmpDeptsDTO> listAll(AcEmpDeptsQueryCriteria criteria);

    Map<String, Object> listAll(AcEmpDeptsQueryCriteria criteria, Pageable pageable);

    void download(List<AcEmpDeptsDTO> empDeptsDTOS, HttpServletResponse response) throws IOException;

    void updateEmpDept(AcEmpDepts acEmpDepts);

    Map<String, Object> getRoleEmpList(PmEmployeeQueryCriteria criteria, Pageable pageable);

    Integer countRoleEmp(PmEmployeeQueryCriteria criteria);

    void removeRelationByid(Long employeeId, String dataType);

    void initEmpDept();

    void initEmpDeptChildExtend();

    Boolean checkDocPermission(Long employeeId);
    // 获取当前用户的管辖部门id集合，以考勤的角色设置为基准
    Set<Long> getJurisdictionDeptId(Long employeeId);
}
