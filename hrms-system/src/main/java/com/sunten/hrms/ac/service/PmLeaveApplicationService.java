package com.sunten.hrms.ac.service;

import com.sunten.hrms.ac.domain.PmLeaveApplication;
import com.sunten.hrms.ac.dto.PmLeaveApplicationDTO;
import com.sunten.hrms.ac.dto.PmLeaveApplicationQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 请假申请信息表 服务类
 * </p>
 *
 * @author zouyp
 * @since 2023-06-12
 */
public interface PmLeaveApplicationService extends IService<PmLeaveApplication> {

    PmLeaveApplicationDTO insert(PmLeaveApplication pmLeaveApplicationNew);

    void delete(Integer id);

    void delete(PmLeaveApplication pmLeaveApplication);

    void update(PmLeaveApplication pmLeaveApplicationNew);

    PmLeaveApplicationDTO getByKey(Integer id);

    List<PmLeaveApplicationDTO> listAll(PmLeaveApplicationQueryCriteria criteria);

    Map<String, Object> listAll(PmLeaveApplicationQueryCriteria criteria, Pageable pageable);

    void download(List<PmLeaveApplicationDTO> pmLeaveApplicationDTOS, HttpServletResponse response) throws IOException;
}
