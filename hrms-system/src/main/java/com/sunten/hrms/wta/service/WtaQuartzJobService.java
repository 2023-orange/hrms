package com.sunten.hrms.wta.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.wta.domain.WtaQuartzJob;
import com.sunten.hrms.wta.dto.WtaQuartzJobDTO;
import com.sunten.hrms.wta.dto.WtaQuartzJobQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author batan
 * @since 2019-12-23
 */
public interface WtaQuartzJobService extends IService<WtaQuartzJob> {

    WtaQuartzJobDTO insert(WtaQuartzJob quartzJobNew);

    void delete(Long id);

    void delete(WtaQuartzJob quartzJob);

    void update(WtaQuartzJob quartzJobNew);

    WtaQuartzJobDTO getByKey(Long id);

    WtaQuartzJob getEntityByKey(Long id);

    List<WtaQuartzJobDTO> listAll(WtaQuartzJobQueryCriteria criteria);

    Map<String, Object> listAll(WtaQuartzJobQueryCriteria criteria, Pageable pageable);

    void download(List<WtaQuartzJobDTO> quartzJobDTOS, HttpServletResponse response) throws IOException;

    /**
     * 更改定时任务状态
     * @param quartzJob /
     */
    void updatePause(WtaQuartzJob quartzJob);

    /**
     * 立即执行定时任务
     * @param quartzJob /
     */
    void execution(WtaQuartzJob quartzJob);
}
