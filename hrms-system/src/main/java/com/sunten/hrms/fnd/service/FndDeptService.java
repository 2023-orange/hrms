package com.sunten.hrms.fnd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.fnd.domain.FndDept;
import com.sunten.hrms.fnd.dto.FndDeptDTO;
import com.sunten.hrms.fnd.dto.FndDeptQueryCriteria;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.dto.PieCriteria;
import com.sunten.hrms.fnd.vo.PieVo;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
public interface FndDeptService extends IService<FndDept> {

    FndDeptDTO insert(FndDept deptNew);

    void delete(Long id);

    void update(FndDept deptNew);

    FndDeptDTO getByKey(Long id);

    List<FndDeptDTO> listAll(FndDeptQueryCriteria criteria);

    Map<String, Object> listAll(FndDeptQueryCriteria criteria, Pageable pageable);

    void download(List<FndDeptDTO> deptDTOS, HttpServletResponse response) throws IOException;

    void downloadDeptTree(List<FndDeptDTO> deptDTOS, HttpServletResponse response) throws IOException;

    Object buildTree(List<FndDeptDTO> deptDTOS, Boolean ignoreTopFlag);


    Object buildAuthorizationTree(List<FndDeptDTO> deptDTOS);

    Object buildAcTree(List<FndDeptDTO> deptDTOS);

    List<FndDept> listByPid(long pid);

    List<FndDept> listAllChildrenByPid(Long id);

    List<Long> listAllEnableChildrenIdByPid(Long id);

    Set<FndDept> listByRoleId(Long id);

    void updateBatchSort(List<FndDept> depts);

    String getNameById(Long id);

    List<PieVo> getPieVo(PieCriteria criteria);

    Set<FndDept> listByJobId(Long id);

    // 根据员工ID获取其所有权限部门
    List<FndDeptDTO> listByAuthorization(Long employeeId);
    // 获取人员所属部门的主管、经理、高级经理的所在部门集合
    Map<String,Object> getSuperiorByDept(Long employeeId);

    // 获取员工的直属上级的人事id
    Long getLeaderPmIdByColleague(Long employeeId);

    // 获取当前部门的主管、经理、高级经理的所在部门集合
    Map<String,Object> getSuperiorByDeptId(Long deptId);

    List<FndDeptDTO> getDeptListByIds(List<Long> ids);

    List<FndDeptDTO> getDeptByEmpAndAdminJob(Long employeeId);
}
