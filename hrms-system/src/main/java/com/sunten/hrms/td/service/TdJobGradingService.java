package com.sunten.hrms.td.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.td.domain.TdJobGrading;
import com.sunten.hrms.td.dto.TdJobGradingDTO;
import com.sunten.hrms.td.dto.TdJobGradingQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 * <p>
 * 岗位分级表 服务类
 * </p>
 *
 * @author xukai
 * @since 2022-03-22
 */
public interface TdJobGradingService extends IService<TdJobGrading> {

    TdJobGradingDTO insert(TdJobGrading jobGradingNew);

    void delete(Long id);

    void delete(TdJobGrading jobGrading);

    void update(TdJobGrading jobGradingNew);

    TdJobGradingDTO getByKey(Long id);

    List<TdJobGradingDTO> listAll(TdJobGradingQueryCriteria criteria);

    Map<String, Object> listAll(TdJobGradingQueryCriteria criteria, Pageable pageable);

    void download(List<TdJobGradingDTO> jobGradingDTOS, HttpServletResponse response) throws IOException;

    List<String> listForProcess(TdJobGradingQueryCriteria criteria);

    List<TdJobGradingDTO> listForJob(TdJobGradingQueryCriteria criteria);

    List<TdJobGradingDTO> getCertificationJobByProcess(TdJobGradingQueryCriteria criteria);

    TdJobGradingDTO supplementJobGrading(TdJobGrading jobGrading);
}
