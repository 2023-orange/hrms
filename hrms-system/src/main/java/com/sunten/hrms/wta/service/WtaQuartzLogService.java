package com.sunten.hrms.wta.service;

import com.sunten.hrms.wta.domain.WtaQuartzLog;
import com.sunten.hrms.wta.dto.WtaQuartzLogDTO;
import com.sunten.hrms.wta.dto.WtaQuartzLogQueryCriteria;
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
 * @author batan
 * @since 2019-12-23
 */
public interface WtaQuartzLogService extends IService<WtaQuartzLog> {

    WtaQuartzLogDTO insert(WtaQuartzLog quartzLogNew);

    void delete(Long id);

    void delete(WtaQuartzLog quartzLog);

    void update(WtaQuartzLog quartzLogNew);

    WtaQuartzLogDTO getByKey(Long id);

    List<WtaQuartzLogDTO> listAll(WtaQuartzLogQueryCriteria criteria);

    Map<String, Object> listAll(WtaQuartzLogQueryCriteria criteria, Pageable pageable);

    void download(List<WtaQuartzLogDTO> quartzLogDTOS, HttpServletResponse response) throws IOException;
}
