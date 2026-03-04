package com.sunten.hrms.ac.service;

import com.sunten.hrms.ac.domain.AcLeaveFormTemp;
import com.sunten.hrms.ac.dto.AcLeaveFormTempDTO;
import com.sunten.hrms.ac.dto.AcLeaveFormTempQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * OA审批通过的请假条临时表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-10-20
 */
public interface AcLeaveFormTempService extends IService<AcLeaveFormTemp> {

    AcLeaveFormTempDTO insert(AcLeaveFormTemp leaveFormTempNew);

    void delete(Long id);

    void delete(AcLeaveFormTemp leaveFormTemp);

    void update(AcLeaveFormTemp leaveFormTempNew);

    AcLeaveFormTempDTO getByKey(Long id);

    List<AcLeaveFormTempDTO> listAll(AcLeaveFormTempQueryCriteria criteria);

    Map<String, Object> listAll(AcLeaveFormTempQueryCriteria criteria, Pageable pageable);

    void download(List<AcLeaveFormTempDTO> leaveFormTempDTOS, HttpServletResponse response) throws IOException;
}
