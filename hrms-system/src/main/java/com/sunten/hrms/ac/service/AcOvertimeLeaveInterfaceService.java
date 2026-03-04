package com.sunten.hrms.ac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.ac.domain.AcOvertimeLeaveInterface;
import com.sunten.hrms.ac.dto.AcOvertimeLeaveInterfaceDTO;
import com.sunten.hrms.ac.dto.AcOvertimeLeaveInterfaceQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * oa加班请假统计接口表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-10-26
 */
public interface AcOvertimeLeaveInterfaceService extends IService<AcOvertimeLeaveInterface> {

    AcOvertimeLeaveInterfaceDTO insert(AcOvertimeLeaveInterface overtimeLeaveInterfeNew);

    void delete(Long id);

    void delete(AcOvertimeLeaveInterface overtimeLeaveInterfe);

    void update(AcOvertimeLeaveInterface overtimeLeaveInterfeNew);

    AcOvertimeLeaveInterfaceDTO getByKey(Long id);

    List<AcOvertimeLeaveInterfaceDTO> listAll(AcOvertimeLeaveInterfaceQueryCriteria criteria);

    Map<String, Object> listAll(AcOvertimeLeaveInterfaceQueryCriteria criteria, Pageable pageable);

    void download(List<AcOvertimeLeaveInterfaceDTO> overtimeLeaveInterfeDTOS, HttpServletResponse response) throws IOException;

    List<AcOvertimeLeaveInterfaceDTO> ImportAcOvertimeLeaveInterfaces(List<AcOvertimeLeaveInterface> acOvertimeLeaveInterfaces, Boolean reImportFlag);

    void insertMainAndSon(List<AcOvertimeLeaveInterface> acOvertimeLeaveInterfaces, Long groupId, LocalDate date, Boolean reImportFlag);

    List<AcOvertimeLeaveInterfaceDTO> preCheckOverTime(List<AcOvertimeLeaveInterface> acOvertimeLeaveInterfaces);

    Boolean checkOvertimeLeaveBeforAutoUpdate(String incomePeriod);
}
