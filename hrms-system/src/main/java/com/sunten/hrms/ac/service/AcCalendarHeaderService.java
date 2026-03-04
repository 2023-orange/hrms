package com.sunten.hrms.ac.service;

import com.sunten.hrms.ac.domain.AcCalendarHeader;
import com.sunten.hrms.ac.dto.AcCalendarHeaderDTO;
import com.sunten.hrms.ac.dto.AcCalendarHeaderQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 日历主表 服务类
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
public interface AcCalendarHeaderService extends IService<AcCalendarHeader> {

    AcCalendarHeaderDTO insert(AcCalendarHeader calendarHeaderNew);

    void delete(Long id);

    void delete(AcCalendarHeader calendarHeader);

    void update(AcCalendarHeader calendarHeaderNew);

    AcCalendarHeaderDTO getByKey(Long id);

    List<AcCalendarHeaderDTO> listAll(AcCalendarHeaderQueryCriteria criteria);

    Map<String, Object> listAll(AcCalendarHeaderQueryCriteria criteria, Pageable pageable);

    void download(List<AcCalendarHeaderDTO> calendarHeaderDTOS, HttpServletResponse response) throws IOException;
}
