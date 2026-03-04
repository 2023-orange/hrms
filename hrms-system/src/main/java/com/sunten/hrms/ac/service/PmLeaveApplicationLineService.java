package com.sunten.hrms.ac.service;

import com.sunten.hrms.ac.domain.PmLeaveApplicationLine;
import com.sunten.hrms.ac.dto.PmLeaveApplicationLineDTO;
import com.sunten.hrms.ac.dto.PmLeaveApplicationLineQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 请假申请信息子表 服务类
 * </p>
 *
 * @author zouyp
 * @since 2023-06-12
 */
public interface PmLeaveApplicationLineService extends IService<PmLeaveApplicationLine> {

    PmLeaveApplicationLineDTO insert(PmLeaveApplicationLine pmLeaveApplicationLineNew);

    void delete();

    void delete(PmLeaveApplicationLine pmLeaveApplicationLine);

    void update(PmLeaveApplicationLine pmLeaveApplicationLineNew);

    PmLeaveApplicationLineDTO getByKey();

    List<PmLeaveApplicationLineDTO> listAll(PmLeaveApplicationLineQueryCriteria criteria);

    Map<String, Object> listAll(PmLeaveApplicationLineQueryCriteria criteria, Pageable pageable);

    void download(List<PmLeaveApplicationLineDTO> pmLeaveApplicationLineDTOS, HttpServletResponse response) throws IOException;
}
