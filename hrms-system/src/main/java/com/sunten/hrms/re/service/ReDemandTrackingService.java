package com.sunten.hrms.re.service;

import com.sunten.hrms.re.domain.ReDemandTracking;
import com.sunten.hrms.re.dto.ReDemandTrackingDTO;
import com.sunten.hrms.re.dto.ReDemandTrackingQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 用人需求招聘过程记录 服务类
 * </p>
 *
 * @author liangjw
 * @since 2022-01-18
 */
public interface ReDemandTrackingService extends IService<ReDemandTracking> {

    List<ReDemandTrackingDTO> insert(ReDemandTracking demandTrackingNew);

    void delete(Long id);

    void delete(ReDemandTracking demandTracking);

    List<ReDemandTrackingDTO>  update(ReDemandTracking demandTrackingNew);

    ReDemandTrackingDTO getByKey(Long id);

    List<ReDemandTrackingDTO> listAll(ReDemandTrackingQueryCriteria criteria);

    Map<String, Object> listAll(ReDemandTrackingQueryCriteria criteria, Pageable pageable);

    void download(List<ReDemandTrackingDTO> demandTrackingDTOS, HttpServletResponse response) throws IOException;
}
