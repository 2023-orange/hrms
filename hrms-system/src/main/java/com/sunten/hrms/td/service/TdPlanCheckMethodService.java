package com.sunten.hrms.td.service;

import com.sunten.hrms.td.domain.TdPlanCheckMethod;
import com.sunten.hrms.td.dto.TdPlanCheckMethodDTO;
import com.sunten.hrms.td.dto.TdPlanCheckMethodQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 培训效果评价方式表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2022-03-08
 */
public interface TdPlanCheckMethodService extends IService<TdPlanCheckMethod> {

    TdPlanCheckMethodDTO insert(TdPlanCheckMethod planCheckMethodNew);

    void delete(Long id);

    void delete(TdPlanCheckMethod planCheckMethod);

    void update(TdPlanCheckMethod planCheckMethodNew);

    TdPlanCheckMethodDTO getByKey(Long id);

    List<TdPlanCheckMethodDTO> listAll(TdPlanCheckMethodQueryCriteria criteria);

    Map<String, Object> listAll(TdPlanCheckMethodQueryCriteria criteria, Pageable pageable);

    void download(List<TdPlanCheckMethodDTO> planCheckMethodDTOS, HttpServletResponse response) throws IOException;

    void deleteByMethodAndEnabledFlag(String checkMethod, Long planImplementId, Long updateBy);

    void updateEvaluationResultsByMethodAndPlanImplementId(TdPlanCheckMethod tdPlanCheckMethod);
}
