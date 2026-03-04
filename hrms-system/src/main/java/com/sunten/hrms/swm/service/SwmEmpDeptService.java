package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmEmpDept;
import com.sunten.hrms.swm.dto.SwmEmpDeptDTO;
import com.sunten.hrms.swm.dto.SwmEmpDeptQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 薪酬人员管理范围 服务类
 * </p>
 *
 * @author liangjw
 * @since 2021-02-24
 */
public interface SwmEmpDeptService extends IService<SwmEmpDept> {

    SwmEmpDeptDTO insert(SwmEmpDept empDeptNew);

    void delete(Long id);

    void delete(SwmEmpDept empDept);

    void update(SwmEmpDept empDeptNew);

    SwmEmpDeptDTO getByKey(Long id);

    List<SwmEmpDeptDTO> listAll(SwmEmpDeptQueryCriteria criteria);

    Map<String, Object> listAll(SwmEmpDeptQueryCriteria criteria, Pageable pageable);

    void download(List<SwmEmpDeptDTO> empDeptDTOS, HttpServletResponse response) throws IOException;

    void updateByEnabled(SwmEmpDept swmEmpDept);

    List<String> getDeptCodeList(SwmEmpDeptQueryCriteria criteria);

    void updateEmpDepts(SwmEmpDept swmEmpDept);

    void updateEnabledBySwmEmployee(SwmEmpDept swmEmpDept);
}
