package com.sunten.hrms.td.service;

import com.sunten.hrms.td.domain.TdPlanImplementDept;
import com.sunten.hrms.td.dto.TdPlanImplementDeptDTO;
import com.sunten.hrms.td.dto.TdPlanImplementDeptQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 培训实施参与部门扩展表（用于后期统计使用） 服务类
 * </p>
 *
 * @author liangjw
 * @since 2021-06-21
 */
public interface TdPlanImplementDeptService extends IService<TdPlanImplementDept> {

    TdPlanImplementDeptDTO insert(TdPlanImplementDept planImplementDeptNew);

    void delete(Long id);

    void delete(TdPlanImplementDept planImplementDept);

    void update(TdPlanImplementDept planImplementDeptNew);

    void deleteByEnabled(TdPlanImplementDept planImplementDept);

    TdPlanImplementDeptDTO getByKey(Long id);

    List<TdPlanImplementDeptDTO> listAll(TdPlanImplementDeptQueryCriteria criteria);

    Map<String, Object> listAll(TdPlanImplementDeptQueryCriteria criteria, Pageable pageable);

    void download(List<TdPlanImplementDeptDTO> planImplementDeptDTOS, HttpServletResponse response) throws IOException;

    void insertByImplement(TdPlanImplementDept planImplementDept);
}
