package com.sunten.hrms.ac.service;

import com.sunten.hrms.ac.domain.AcAbnormalAttendanceRecord;
import com.sunten.hrms.ac.dto.AcAbnormalAttendanceRecordDTO;
import com.sunten.hrms.ac.dto.AcAbnormalAttendanceRecordQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 异常考勤执行记录 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
public interface AcAbnormalAttendanceRecordService extends IService<AcAbnormalAttendanceRecord> {

    AcAbnormalAttendanceRecordDTO insert(AcAbnormalAttendanceRecord abnormalAttendanceRecordNew);

    AcAbnormalAttendanceRecordDTO insertByNewTransation(AcAbnormalAttendanceRecord acAbnormalAttendanceRecord);


    void delete(Long id);

    void delete(AcAbnormalAttendanceRecord abnormalAttendanceRecord);

    void update(AcAbnormalAttendanceRecord abnormalAttendanceRecordNew);

    void updateByNewTransation(AcAbnormalAttendanceRecord acAbnormalAttendanceRecord);

    AcAbnormalAttendanceRecordDTO getByKey(Long id);

    List<AcAbnormalAttendanceRecordDTO> listAll(AcAbnormalAttendanceRecordQueryCriteria criteria);

    Map<String, Object> listAll(AcAbnormalAttendanceRecordQueryCriteria criteria, Pageable pageable);

    void download(List<AcAbnormalAttendanceRecordDTO> abnormalAttendanceRecordDTOS, HttpServletResponse response) throws IOException;
}
