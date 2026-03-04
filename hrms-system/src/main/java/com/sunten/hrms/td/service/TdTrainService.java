package com.sunten.hrms.td.service;

import com.sunten.hrms.td.domain.TdTrain;
import com.sunten.hrms.td.dto.TdTrainDTO;
import com.sunten.hrms.td.dto.TdTrainQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 培训情况表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
public interface TdTrainService extends IService<TdTrain> {

    TdTrainDTO insert(TdTrain trainNew);

    void delete(Long id);

    void delete(TdTrain train);

    void update(TdTrain trainNew);

    TdTrainDTO getByKey(Long id);

    List<TdTrainDTO> listAll(TdTrainQueryCriteria criteria);

    Map<String, Object> listAll(TdTrainQueryCriteria criteria, Pageable pageable);

    void download(List<TdTrainDTO> trainDTOS, HttpServletResponse response) throws IOException;
}
