package com.sunten.hrms.ac.service;

import com.sunten.hrms.ac.domain.AcLeaveApplicationLine;
import com.sunten.hrms.ac.dto.AcHrLeaveSubDTO;
import com.sunten.hrms.ac.dto.AcHrLeaveSubQueryCriteria;
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
 * @since 2023-05-30
 */
public interface AcHrLeaveSubService extends IService<AcLeaveApplicationLine> {

    AcHrLeaveSubDTO insert(AcLeaveApplicationLine hrLeaveSubNew);

    void delete(Integer id);

    void delete(AcLeaveApplicationLine hrLeaveSub);

    void update(AcLeaveApplicationLine hrLeaveSubNew);

    AcHrLeaveSubDTO getByKey(Integer id);

    List<AcHrLeaveSubDTO> listAll(AcHrLeaveSubQueryCriteria criteria);

    Map<String, Object> listAll(AcHrLeaveSubQueryCriteria criteria, Pageable pageable);

    void download(List<AcHrLeaveSubDTO> hrLeaveSubDTOS, HttpServletResponse response) throws IOException;
}
