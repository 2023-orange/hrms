package com.sunten.hrms.ac.service;

import com.sunten.hrms.ac.domain.AcCalendarHeaderAndYear;
import com.sunten.hrms.ac.domain.AcCalendarLine;
import com.sunten.hrms.ac.dto.AcCalendarLineDTO;
import com.sunten.hrms.ac.dto.AcCalendarLineQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 日历详细表 服务类
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
public interface AcCalendarLineService extends IService<AcCalendarLine> {

    List<AcCalendarLineDTO> insert(AcCalendarHeaderAndYear acCalendarHeaderAndYear);

    void delete(Long id);

    void delete(AcCalendarLine calendarLine);

    void update(AcCalendarLine calendarLineNew);

    AcCalendarLineDTO getByKey(Long id);

    List<AcCalendarLineDTO> listAll(AcCalendarLineQueryCriteria criteria);

    Map<String, Object> listAll(AcCalendarLineQueryCriteria criteria, Pageable pageable);

    void download(List<AcCalendarLineDTO> calendarLineDTOS, HttpServletResponse response) throws IOException;

    List<AcCalendarLine> generateAcCalendarLines(LocalDate now, Long acCalendarHeaderId);

    void autoGenerateCalendarLines();

    void insertCollection(List<AcCalendarLine> acCalendarLines);
}
