package com.sunten.hrms.re.service;

import com.sunten.hrms.re.domain.ReTrain;
import com.sunten.hrms.re.dto.ReTrainDTO;
import com.sunten.hrms.re.dto.ReTrainQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 培训记录表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
public interface ReTrainService extends IService<ReTrain> {

    ReTrainDTO insert(ReTrain trainNew);

    void delete(Long id);

    void delete(ReTrain train);

    void update(ReTrain trainNew);

    ReTrainDTO getByKey(Long id);

    List<ReTrainDTO> listAll(ReTrainQueryCriteria criteria);

    Map<String, Object> listAll(ReTrainQueryCriteria criteria, Pageable pageable);

    void download(List<ReTrainDTO> trainDTOS, HttpServletResponse response) throws IOException;

    List<ReTrainDTO> batchInsert(List<ReTrain> reTrains,Long reId);
}
