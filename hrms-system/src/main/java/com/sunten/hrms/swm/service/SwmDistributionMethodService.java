package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmDistributionMethod;
import com.sunten.hrms.swm.dto.SwmDistributionMethodDTO;
import com.sunten.hrms.swm.dto.SwmDistributionMethodQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 分配方式 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
public interface SwmDistributionMethodService extends IService<SwmDistributionMethod> {

    SwmDistributionMethodDTO insertSwmDistributionMethod(SwmDistributionMethod distributionMethodNew);

    void delete(Long id);

    void delete(SwmDistributionMethod distributionMethod);

    void update(SwmDistributionMethod distributionMethodNew);

    SwmDistributionMethodDTO getByKey(Long id);

    List<SwmDistributionMethodDTO> listAll(SwmDistributionMethodQueryCriteria criteria);

    Map<String, Object> listAll(SwmDistributionMethodQueryCriteria criteria, Pageable pageable);

    void download(List<SwmDistributionMethodDTO> distributionMethodDTOS, HttpServletResponse response) throws IOException;

    void disabled(SwmDistributionMethod distributionMethod);
}
