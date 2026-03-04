package com.sunten.hrms.td.service;

import com.sunten.hrms.td.domain.TdTrainEmployeeJurisdiction;
import com.sunten.hrms.td.dto.TdTrainEmployeeJurisdictionDTO;
import com.sunten.hrms.td.dto.TdTrainEmployeeJurisdictionQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 培训员权限表 服务类
 * </p>
 *
 * @author xukai
 * @since 2021-06-23
 */
public interface TdTrainEmployeeJurisdictionService extends IService<TdTrainEmployeeJurisdiction> {

    TdTrainEmployeeJurisdictionDTO insert(TdTrainEmployeeJurisdiction trainEmployeeJurisdictionNew);

    void delete(Long id);

    void delete(TdTrainEmployeeJurisdiction trainEmployeeJurisdiction);

    void update(TdTrainEmployeeJurisdiction trainEmployeeJurisdictionNew);

    TdTrainEmployeeJurisdictionDTO getByKey(Long id);

    List<TdTrainEmployeeJurisdictionDTO> listAll(TdTrainEmployeeJurisdictionQueryCriteria criteria);

    Map<String, Object> listAll(TdTrainEmployeeJurisdictionQueryCriteria criteria, Pageable pageable);

    void download(List<TdTrainEmployeeJurisdictionDTO> trainEmployeeJurisdictionDTOS, HttpServletResponse response) throws IOException;

    void batchUpdate(TdTrainEmployeeJurisdiction trainEmployeeJurisdictionNew);

    List<Long> getDeptsByEmployeeeId(Long employeeId);
    // 删除该员工的所有管辖部门
    void deleteByEmployee(Long employeeId);
}
