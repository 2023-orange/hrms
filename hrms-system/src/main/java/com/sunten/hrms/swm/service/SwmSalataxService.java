package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmSalatax;
import com.sunten.hrms.swm.dto.SwmSalataxDTO;
import com.sunten.hrms.swm.dto.SwmSalataxQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 旧的所得税表 服务类
 * </p>
 *
 * @author zhoujy
 * @since 2023-04-10
 */
public interface SwmSalataxService extends IService<SwmSalatax> {

    SwmSalataxDTO insert(SwmSalatax salataxNew);

    void delete();

    void delete(SwmSalatax salatax);

    void update(SwmSalatax salataxNew);

    SwmSalataxDTO getByKey();

    List<SwmSalataxDTO> listAll(SwmSalataxQueryCriteria criteria);

    Map<String, Object> listAll(SwmSalataxQueryCriteria criteria, Pageable pageable);

    void download(List<SwmSalataxDTO> salataxDTOS, HttpServletResponse response) throws IOException;

    // 返回旧的个人所得税
    List<SwmSalataxDTO> getOldSalatax(String workCard, String year);
}
