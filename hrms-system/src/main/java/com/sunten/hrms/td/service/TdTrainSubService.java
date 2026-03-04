package com.sunten.hrms.td.service;

import com.sunten.hrms.td.domain.TdTrainSub;
import com.sunten.hrms.td.dto.TdTrainSubDTO;
import com.sunten.hrms.td.dto.TdTrainSubQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 参加培训人员情况 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
public interface TdTrainSubService extends IService<TdTrainSub> {

    TdTrainSubDTO insert(TdTrainSub trainSubNew);

    void delete(Long id);

    void delete(TdTrainSub trainSub);

    void update(TdTrainSub trainSubNew);

    TdTrainSubDTO getByKey(Long id);

    List<TdTrainSubDTO> listAll(TdTrainSubQueryCriteria criteria);

    Map<String, Object> listAll(TdTrainSubQueryCriteria criteria, Pageable pageable);

    void download(List<TdTrainSubDTO> trainSubDTOS, HttpServletResponse response) throws IOException;

    List<TdTrainSubDTO> batchInsert(List<TdTrainSub> tdTrainSubs);
}
