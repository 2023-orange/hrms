package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmEmployeeFamily;
import com.sunten.hrms.pm.dto.PmEmployeeFamilyDTO;
import com.sunten.hrms.pm.dto.PmEmployeeFamilyQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 家庭情况表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
public interface PmEmployeeFamilyService extends IService<PmEmployeeFamily> {

    PmEmployeeFamilyDTO insert(PmEmployeeFamily employeeFamilyNew);

    void delete(Long id);

    void delete(PmEmployeeFamily employeeFamily);

    void update(PmEmployeeFamily employeeFamilyNew);

    PmEmployeeFamilyDTO getByKey(Long id);

    List<PmEmployeeFamilyDTO> listAll(PmEmployeeFamilyQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeeFamilyQueryCriteria criteria, Pageable pageable);

    void download(List<PmEmployeeFamilyDTO> employeeFamilyDTOS, HttpServletResponse response) throws IOException;

    List<PmEmployeeFamilyDTO> batchInsert(List<PmEmployeeFamily> pmEmployeeFamilies,Long employeeId);

    List<PmEmployeeFamilyDTO> listAllByCheck(Long employeeId);

    List<PmEmployeeFamilyDTO> getChild(Long employeeId);
}
