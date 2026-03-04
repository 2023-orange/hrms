package com.sunten.hrms.ac.service;

import com.sunten.hrms.ac.domain.AcAttendanceRecordTemp;
import com.sunten.hrms.ac.dto.AcAttendanceRecordTempDTO;
import com.sunten.hrms.ac.dto.AcAttendanceRecordTempQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 考勤处理记录临时表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
public interface AcAttendanceRecordTempService extends IService<AcAttendanceRecordTemp> {

    AcAttendanceRecordTempDTO insert(AcAttendanceRecordTemp attendanceRecordTempNew);

    void delete(Long id);

    void delete(AcAttendanceRecordTemp attendanceRecordTemp);

    void update(AcAttendanceRecordTemp attendanceRecordTempNew);

    AcAttendanceRecordTempDTO getByKey(Long id);

    List<AcAttendanceRecordTempDTO> listAll(AcAttendanceRecordTempQueryCriteria criteria);

    Map<String, Object> listAll(AcAttendanceRecordTempQueryCriteria criteria, Pageable pageable);

    void download(List<AcAttendanceRecordTempDTO> attendanceRecordTempDTOS, HttpServletResponse response) throws IOException;

    void generateAbnormalAttendance();

    void updateLeaveEmployeeServiceImpl();
}
