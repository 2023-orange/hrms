package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmFdgz;
import com.sunten.hrms.swm.dto.SwmFdgzDTO;
import com.sunten.hrms.swm.dto.SwmFdgzQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 旧的浮动工资表 服务类
 * </p>
 *
 * @author zhoujy
 * @since 2023-04-10
 */
public interface SwmFdgzService extends IService<SwmFdgz> {

    SwmFdgzDTO insert(SwmFdgz fdgzNew);

    void delete();

    void delete(SwmFdgz fdgz);

    void update(SwmFdgz fdgzNew);

    SwmFdgzDTO getByKey();

    List<SwmFdgzDTO> listAll(SwmFdgzQueryCriteria criteria);

    Map<String, Object> listAll(SwmFdgzQueryCriteria criteria, Pageable pageable);

    void download(List<SwmFdgzDTO> fdgzDTOS, HttpServletResponse response) throws IOException;

    // 返回旧浮动工资
    List<SwmFdgzDTO> getOldFloatSalary(String workCard, String year);
}
