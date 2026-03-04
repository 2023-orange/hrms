package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.domain.FndJobDept;
import com.sunten.hrms.fnd.dto.FndJobDeptDTO;
import com.sunten.hrms.fnd.dto.FndJobDeptQueryCriteria;
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
 * @author batan
 * @since 2020-12-03
 */
public interface FndJobDeptService extends IService<FndJobDept> {

    FndJobDeptDTO insert(FndJobDept jobDeptNew);

    void delete(Long deptId, Long jobId);

    void delete(FndJobDept jobDept);

    void update(FndJobDept jobDeptNew);

    FndJobDeptDTO getByKey(Long deptId, Long jobId);

    List<FndJobDeptDTO> listAll(FndJobDeptQueryCriteria criteria);

    Map<String, Object> listAll(FndJobDeptQueryCriteria criteria, Pageable pageable);

    void download(List<FndJobDeptDTO> jobDeptDTOS, HttpServletResponse response) throws IOException;
}
