package com.sunten.hrms.pm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.pm.domain.PmEmployeeEntry;
import com.sunten.hrms.pm.dto.PmEmployeeEntryDTO;
import com.sunten.hrms.pm.dto.PmEmployeeEntryQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 * <p>
 * 入职情况表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
public interface PmEmployeeEntryService extends IService<PmEmployeeEntry> {

    PmEmployeeEntryDTO insert(PmEmployeeEntry employeeEntryNew);

    void delete(Long id);

    void delete(PmEmployeeEntry employeeEntry);

    void update(PmEmployeeEntry employeeEntryNew);

    PmEmployeeEntryDTO getByKey(Long id);

    List<PmEmployeeEntryDTO> listAll(PmEmployeeEntryQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeeEntryQueryCriteria criteria, Pageable pageable);

    void download(PmEmployeeEntryQueryCriteria criteria, Pageable pageable, HttpServletResponse response) throws IOException;

    List<PmEmployeeEntryDTO> batchInsert(List<PmEmployeeEntry> pmEmployeeEntries,Long employeeId);

    Map<String, Object> listAllByProbationCriteriaPage(PmEmployeeEntryQueryCriteria criteria, Pageable pageable);

    void updateAssessFlag(PmEmployeeEntry employeeEntry);
}
