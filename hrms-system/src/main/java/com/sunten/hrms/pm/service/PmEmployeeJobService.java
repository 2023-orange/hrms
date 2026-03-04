package com.sunten.hrms.pm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.pm.domain.PmEmployeeJob;
import com.sunten.hrms.pm.dto.PmEmployeeJobDTO;
import com.sunten.hrms.pm.dto.PmEmployeeJobQueryCriteria;
import com.sunten.hrms.pm.vo.PmJobEmployeeVo;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门科室岗位关系表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
public interface PmEmployeeJobService extends IService<PmEmployeeJob> {

    PmEmployeeJobDTO insert(PmEmployeeJob employeeJobNew);

    void delete(Long id);

    void delete(PmEmployeeJob employeeJob);

    void update(PmEmployeeJob employeeJobNew);

    PmEmployeeJobDTO getByKey(Long id);

    List<PmEmployeeJobDTO> listAll(PmEmployeeJobQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeeJobQueryCriteria criteria, Pageable pageable);

    void download(List<PmEmployeeJobDTO> employeeJobDTOS, HttpServletResponse response) throws IOException;

    Long getMaxGroupId();

    List<PmEmployeeJobDTO> listByEmpIdAndEnabledFlagWithExtend(Long employeeId, boolean enabledFlag);

    void setMianJob(Long id);

    List<PmEmployeeJobDTO> listAllEntityDeptIds(PmEmployeeJobQueryCriteria criteria);

    List<PmJobEmployeeVo> listJobEmployee(Long jobId, boolean enabledFlag);

    void downloadByJob(List<PmJobEmployeeVo> pmJobEmployeeVos, HttpServletResponse response) throws IOException;

    Boolean checkManager(Long userId);

    PmEmployeeJob getManagerOrSupervisor(Long userId);

//    List<PmJobEmployeeVo> listJobEmployeeByName(String jobName);
}
