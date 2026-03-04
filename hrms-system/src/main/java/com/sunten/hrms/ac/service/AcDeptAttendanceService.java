package com.sunten.hrms.ac.service;

import com.sunten.hrms.ac.domain.AcDeptAttendance;
import com.sunten.hrms.ac.dto.AcDeptAttendanceDTO;
import com.sunten.hrms.ac.dto.AcDeptAttendanceQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 部门考勤制度关系表 服务类
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
public interface AcDeptAttendanceService extends IService<AcDeptAttendance> {

    AcDeptAttendanceDTO insert(AcDeptAttendance deptAttendanceNew);

    void delete(Long id);

    void delete(AcDeptAttendance deptAttendance);

    void update(AcDeptAttendance deptAttendanceNew);

    AcDeptAttendanceDTO getByKey(Long id);

    List<AcDeptAttendanceDTO> listAll(AcDeptAttendanceQueryCriteria criteria);

    Map<String, Object> listAll(AcDeptAttendanceQueryCriteria criteria, Pageable pageable);

    void download(List<AcDeptAttendanceDTO> deptAttendanceDTOS, HttpServletResponse response) throws IOException;

    Object buildTree(List<AcDeptAttendanceDTO> acDeptAttendanceDTOS);

    void removeByIdAndResetOld(AcDeptAttendance deptAttendance);

    List<AcDeptAttendanceDTO> listForDeptHistory(AcDeptAttendanceQueryCriteria criteria);

}
