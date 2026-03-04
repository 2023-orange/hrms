package com.sunten.hrms.ac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.ac.domain.AcOvertimeLeave;
import com.sunten.hrms.ac.dto.AcOvertimeLeaveDTO;
import com.sunten.hrms.ac.dto.AcOvertimeLeaveQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * oa加班请假统计 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-10-26
 */
public interface AcOvertimeLeaveService extends IService<AcOvertimeLeave> {

    AcOvertimeLeaveDTO insert(AcOvertimeLeave overtimeLeaveNew);

    void delete(Long id);

    void delete(AcOvertimeLeave overtimeLeave);

    void update(AcOvertimeLeave overtimeLeaveNew);

    AcOvertimeLeaveDTO getByKey(Long id);

    List<AcOvertimeLeaveDTO> listAll(AcOvertimeLeaveQueryCriteria criteria);

    Map<String, Object> listAll(AcOvertimeLeaveQueryCriteria criteria, Pageable pageable);

    void download(List<AcOvertimeLeaveDTO> overtimeLeaveDTOS, HttpServletResponse response) throws IOException;

    void downloadSum(List<AcOvertimeLeaveDTO> overtimeLeaveDTOS, HttpServletResponse response) throws IOException;

    Integer countDataByMonth(LocalDate month);

    void deleteAllByMonth(LocalDate month);

    void insertByInterface(Long operationId);

    void autoCreateLastMonth();

    void createBeforeinterface(LocalDate date);

    void updateBeforeinterface(LocalDate date);

    Map<String, Object> sumAcOvertimeLeavePage(Pageable pageable, AcOvertimeLeaveQueryCriteria acOvertimeLeaveQueryCriteria);

    List<AcOvertimeLeaveDTO> sumAcOvertimeLeave(AcOvertimeLeaveQueryCriteria acOvertimeLeaveQueryCriteria);

    void updateLastMonthWorkingHours();

    void autoCreateLastMonthWithoutCheck();

}
