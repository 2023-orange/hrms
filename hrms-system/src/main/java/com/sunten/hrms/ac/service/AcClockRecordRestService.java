package com.sunten.hrms.ac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.ac.domain.AcClockRecordRest;
import com.sunten.hrms.ac.domain.AcLeaveApplication;
import com.sunten.hrms.ac.dto.AcClockRecordRestDTO;
import com.sunten.hrms.ac.dto.AcClockRecordRestQueryCriteria;
import com.sunten.hrms.ac.dto.AcLeaveApplicationQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Zouyp
 */
public interface AcClockRecordRestService  extends IService<AcClockRecordRest> {
    /**
     * 分页查询
     * @param criteria
     * @param pageable
     * @return
     */
    Map<String, Object> listAll(AcClockRecordRestQueryCriteria criteria, Pageable pageable);

    /**
     * 导出打卡记录
     * @param acClockRecordRestDTOS
     */
    void download(List<AcClockRecordRestDTO> acClockRecordRestDTOS, HttpServletResponse response) throws IOException;

    /**
     * 导出打卡记录
     * @param criteria
     * @param pageable
     * @return
     */
    List<AcClockRecordRestDTO> downloadListAll(AcClockRecordRestQueryCriteria criteria, Pageable pageable);

}
