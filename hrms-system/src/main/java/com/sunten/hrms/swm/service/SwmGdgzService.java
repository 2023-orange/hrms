package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmGdgz;
import com.sunten.hrms.swm.dto.SwmGdgzDTO;
import com.sunten.hrms.swm.dto.SwmGdgzQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 旧的固定工资表 服务类
 * </p>
 *
 * @author zhoujy
 * @since 2023-04-07
 */
public interface SwmGdgzService extends IService<SwmGdgz> {

    SwmGdgzDTO insert(SwmGdgz gdgzNew);

    void delete();

    void delete(SwmGdgz gdgz);

    void update(SwmGdgz gdgzNew);

    SwmGdgzDTO getByKey();

    List<SwmGdgzDTO> listAll(SwmGdgzQueryCriteria criteria);

    Map<String, Object> listAll(SwmGdgzQueryCriteria criteria, Pageable pageable);

    void download(List<SwmGdgzDTO> gdgzDTOS, HttpServletResponse response) throws IOException;

    // 返回旧固定工资
    List<SwmGdgzDTO> getOldFixedSalary(String workCard, String year);
}
