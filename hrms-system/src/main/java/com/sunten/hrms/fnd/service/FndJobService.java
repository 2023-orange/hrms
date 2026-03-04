package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.domain.FndJob;
import com.sunten.hrms.fnd.dto.FndJobDTO;
import com.sunten.hrms.fnd.dto.FndJobQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
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
 * @since 2019-12-19
 */
public interface FndJobService extends IService<FndJob> {

    FndJobDTO insert(FndJob jobNew);

    void delete(Long id);

    void update(FndJob jobNew);

    FndJobDTO getByKey(Long id);

    List<FndJobDTO> listAll(FndJobQueryCriteria criteria);

    Map<String, Object> listAll(FndJobQueryCriteria criteria, Pageable pageable);

    Map<String, Object> listAllByPage(FndJobQueryCriteria criteria, Pageable pageable);

    void download(List<FndJobDTO> jobDTOS, HttpServletResponse response) throws IOException;

    void updateBatchSortJob(List<FndJob> jobs);

    List<FndJobDTO> listAllByDeptAndLeader(Long deptId);

    List<FndJobDTO> listByAdminJob(FndJobQueryCriteria criteria);

    void sendAddJobEmail();

    /**
     * 获取认证岗位List
     * @param jobName
     * @return
     */
    List<HashMap<String, Object>> loadAllCertificationJobList();
}
