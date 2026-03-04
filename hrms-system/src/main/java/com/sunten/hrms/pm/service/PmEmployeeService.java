package com.sunten.hrms.pm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.pm.domain.PmEmployee;
import com.sunten.hrms.pm.dto.PmEmployeeDTO;
import com.sunten.hrms.pm.dto.PmEmployeeLikeQueryCriteria;
import com.sunten.hrms.pm.dto.PmEmployeeQueryCriteria;
import com.sunten.hrms.pm.vo.PmLeaderVo;
import com.sunten.hrms.pm.vo.PmManagerVo;
import com.sunten.hrms.pm.vo.PmMsgVo;
import com.sunten.hrms.vo.ElTreeBaseVo;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 人事档案表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
public interface PmEmployeeService extends IService<PmEmployee> {

    PmEmployeeDTO insert(PmEmployee employeeNew);

    void delete(Long id);

    void delete(PmEmployee employee);

    void update(PmEmployee employeeNew);

    PmEmployeeDTO getByKey(Long id);

    PmEmployeeDTO getByKey(Long id, Set<Long> deptIdsInDataScope);

    List<PmEmployeeDTO> listAll(PmEmployeeQueryCriteria criteria);

    List<PmEmployeeDTO> baseEmployeeListByPage(PmEmployeeQueryCriteria criteria, Pageable pageable);

    List<PmEmployeeDTO> getListByDeptName(String deptname);

    List<ElTreeBaseVo> getListByNameOrCard(PmEmployeeLikeQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeeQueryCriteria criteria, Pageable pageable);

    void download(PmEmployeeQueryCriteria criteria, Pageable pageable, HttpServletResponse response) throws IOException;

    Map<String, Object> listAllByCheck(PmEmployeeQueryCriteria criteria, Pageable pageable);

    void updatePhoto(MultipartFile multipartFile, Long employeeId);

    List<PmEmployeeDTO> listAllByJobId(Long jobId);

    List<PmEmployeeDTO> listForIdNumberOrWorkCardExist(PmEmployeeQueryCriteria criteria);

    void batchUpdateCompanyAge();

    Map<String,Object> listForAttendanceSet(PmEmployeeQueryCriteria criteria, Pageable pageable);

    void updateAttendanceFlag(PmEmployee employee);

    void updateAttendanceFlagBatch(List<PmEmployee> employees);

    List<PmEmployeeDTO> listOtherEmployees(String idNumber, long id);
    // 获取当前管理岗人员
    List<PmEmployeeDTO> listLeaderEmployees();
    // 根据工牌或姓名获取人员信息
    List<PmEmployeeDTO> listEmployeesBaseInfoList(PmEmployeeQueryCriteria criteria);

    void updatePhotoPath(PmEmployee pmEmployee);

    PmMsgVo getPmMsgByUserId(Long userId);

    PmLeaderVo getCurrentManagerAndSuperior(Long userId);

    Set<String> getWorkListByRolePermission(String permission);

    List<PmManagerVo> getTeamByDeptNameAndDepartmentNameAndTeamName(Map<String, Object> map);

    List<PmManagerVo> getSuperiorByDeptNameAndDepartmentName(Map<String, Object> map);

    List<PmManagerVo> getManagerByDeptName(Map<String, Object> map);

    List<PmManagerVo> getLeaderList();

    PmEmployeeDTO getLeaderByDeptId(Long deptId);

    // 每日更新带薪年假(自动任务)
    void updatePaidAnnualLeaveEveryDay();

    void updatePaidAnnualLeaveEveryDayAfterTenYearUpdate(String workCard);

    void updateTenYearDate(PmEmployee pe);

    void updateTenYearDateSetNull(PmEmployee pe);

    Map<String,Object> getPmEmailList(Pageable pageable, PmEmployeeQueryCriteria pmEmployeeQueryCriteria);

    void savePmEmail(PmEmployee pe);

    PmLeaderVo getManagerAndSuperiorByDeptId(Long deptId);

    Map<String,Object> getPmTelList(Pageable pageable, PmEmployeeQueryCriteria pmEmployeeQueryCriteria);

    void inspectionResignedPersonnel();
}
