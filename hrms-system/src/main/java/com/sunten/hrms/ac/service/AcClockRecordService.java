package com.sunten.hrms.ac.service;

import com.sunten.hrms.ac.domain.AcClockRecord;
import com.sunten.hrms.ac.dto.AcClockRecordDTO;
import com.sunten.hrms.ac.dto.AcClockRecordQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.ac.vo.TempEmployeeClockRecordVo;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 打卡记录表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-10-15
 */
public interface AcClockRecordService extends IService<AcClockRecord> {

    AcClockRecordDTO insert(AcClockRecord clockRecordNew);

    void delete(Long id);

    void delete(AcClockRecord clockRecord);

    void update(AcClockRecord clockRecordNew);

    void updateClockForOutRest(AcClockRecord clockRecord);

    AcClockRecordDTO getByKey(Long id);

    List<AcClockRecordDTO> listAll(AcClockRecordQueryCriteria criteria);

    Map<String, Object> listAll(AcClockRecordQueryCriteria criteria, Pageable pageable);

    void download(List<AcClockRecordDTO> clockRecordDTOS, HttpServletResponse response) throws IOException;

    void updateEmployeeIdMonthly();

    Boolean getFakeRecordSetting(String userName);

    void exportTempEmployeeRecordList(HttpServletResponse response) throws IOException;

}
