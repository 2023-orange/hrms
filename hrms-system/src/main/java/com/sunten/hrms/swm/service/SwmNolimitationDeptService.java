package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmNolimitationDept;
import com.sunten.hrms.swm.dto.SwmNolimitationDeptDTO;
import com.sunten.hrms.swm.dto.SwmNolimitationDeptQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-21
 */
public interface SwmNolimitationDeptService extends IService<SwmNolimitationDept> {

    SwmNolimitationDeptDTO insert(SwmNolimitationDept nolimitationDeptNew);

    void delete();

    void delete(SwmNolimitationDept nolimitationDept);

    void update(SwmNolimitationDept nolimitationDeptNew);

    SwmNolimitationDeptDTO getByKey();

    List<SwmNolimitationDeptDTO> listAll(SwmNolimitationDeptQueryCriteria criteria);

    Map<String, Object> listAll(SwmNolimitationDeptQueryCriteria criteria, Pageable pageable);

    void download(List<SwmNolimitationDeptDTO> nolimitationDeptDTOS, HttpServletResponse response) throws IOException;

    Long countDept(String deptName);

    List<SwmNolimitationDept> getSwmDept();
}
