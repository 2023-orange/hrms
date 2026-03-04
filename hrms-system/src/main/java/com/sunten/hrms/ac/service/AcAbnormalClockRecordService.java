package com.sunten.hrms.ac.service;

import com.sunten.hrms.ac.domain.AcAbnormalClockRecord;
import com.sunten.hrms.ac.dto.AcAbnormalClockRecordDTO;
import com.sunten.hrms.ac.dto.AcAbnormalClockRecordQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 异常日打卡记录表（出现异常时，记录该人在异常所属日的所有打卡记录） 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
public interface AcAbnormalClockRecordService extends IService<AcAbnormalClockRecord> {

    AcAbnormalClockRecordDTO insert(AcAbnormalClockRecord abnormalClockRecordNew);

    void delete(Long id);

    void delete(AcAbnormalClockRecord abnormalClockRecord);

    void update(AcAbnormalClockRecord abnormalClockRecordNew);

    AcAbnormalClockRecordDTO getByKey(Long id);

    List<AcAbnormalClockRecordDTO> listAll(AcAbnormalClockRecordQueryCriteria criteria);

    Map<String, Object> listAll(AcAbnormalClockRecordQueryCriteria criteria, Pageable pageable);

    void download(List<AcAbnormalClockRecordDTO> abnormalClockRecordDTOS, HttpServletResponse response) throws IOException;

    //    void disposeClock(List<AcAbnormalClockRecord> clockRecords);

}
