package com.sunten.hrms.ac.service;

import com.sunten.hrms.ac.domain.AcOvertimeApplicationLine;
import com.sunten.hrms.ac.dto.AcOvertimeApplicationLineDTO;
import com.sunten.hrms.ac.dto.AcOvertimeApplicationLineQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zouyp
 * @since 2023-10-16
 */
public interface AcOvertimeApplicationLineService extends IService<AcOvertimeApplicationLine> {

    AcOvertimeApplicationLineDTO insert(AcOvertimeApplicationLine overtimeApplicationLineNew);

    void delete(Integer id);

    void delete(AcOvertimeApplicationLine overtimeApplicationLine);

    void update(AcOvertimeApplicationLine overtimeApplicationLineNew);

    AcOvertimeApplicationLineDTO getByKey(Integer id);

    List<AcOvertimeApplicationLineDTO> listAll(AcOvertimeApplicationLineQueryCriteria criteria);

    Map<String, Object> listAll(AcOvertimeApplicationLineQueryCriteria criteria, Pageable pageable);

    void download(List<AcOvertimeApplicationLineDTO> overtimeApplicationLineDTOS, HttpServletResponse response) throws IOException;
}
