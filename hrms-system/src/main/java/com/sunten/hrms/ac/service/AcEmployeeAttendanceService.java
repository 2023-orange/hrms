package com.sunten.hrms.ac.service;

import com.sunten.hrms.ac.domain.AcEmployeeAttendance;
import com.sunten.hrms.ac.domain.AcEmployeeAttendanceCollect;
import com.sunten.hrms.ac.dto.AcEmployeeAttendanceDTO;
import com.sunten.hrms.ac.dto.AcEmployeeAttendanceQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.ac.vo.AcEmployeeVo;
import com.sunten.hrms.ac.vo.DateVo;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 排班员工考勤制度关系表 服务类
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
public interface AcEmployeeAttendanceService extends IService<AcEmployeeAttendance> {

    AcEmployeeAttendanceDTO insert(AcEmployeeAttendance employeeAttendanceNew);

    void delete(Long id);

    void delete(AcEmployeeAttendance employeeAttendance);

    void update(AcEmployeeAttendance employeeAttendanceNew);

    AcEmployeeAttendanceDTO getByKey(Long id);

    List<AcEmployeeAttendanceDTO> listAll(AcEmployeeAttendanceQueryCriteria criteria);

    Map<String, Object> listAll(AcEmployeeAttendanceQueryCriteria criteria, Pageable pageable);

    void download(List<AcEmployeeAttendanceDTO> employeeAttendanceDTOS, HttpServletResponse response) throws IOException;

    List<AcEmployeeAttendanceDTO> batchSave(AcEmployeeAttendanceCollect acEmployeeAttendanceCollect);

    List<AcEmployeeVo> listAllByCalendarLine(AcEmployeeAttendanceQueryCriteria acEmployeeAttendanceQueryCriteria);

    Map<String, Object> listAllByCalendarLineByPage(AcEmployeeAttendanceQueryCriteria acEmployeeAttendanceQueryCriteria, Pageable pageable);

    void autoSendEmailForNoEmployeeAttendace();

    void downloadAttendance(List<AcEmployeeVo> employeeDTOS, Boolean showType, HttpServletResponse response) throws IOException;
}
