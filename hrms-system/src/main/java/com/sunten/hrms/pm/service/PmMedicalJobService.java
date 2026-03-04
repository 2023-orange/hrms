package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmMedicalJob;
import com.sunten.hrms.pm.dto.PmMedicalJobDTO;
import com.sunten.hrms.pm.dto.PmMedicalJobQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 岗位体检表 服务类
 * </p>
 *
 * @author xukai
 * @since 2021-04-19
 */
public interface PmMedicalJobService extends IService<PmMedicalJob> {

    PmMedicalJobDTO insert(PmMedicalJob medicalJobNew);

    void delete(Long id);

    void delete(PmMedicalJob medicalJob);

    void update(PmMedicalJob medicalJobNew);

    PmMedicalJobDTO getByKey(Long id);

    List<PmMedicalJobDTO> listAll(PmMedicalJobQueryCriteria criteria);

    Map<String, Object> listAll(PmMedicalJobQueryCriteria criteria, Pageable pageable);

    void download(List<PmMedicalJobDTO> medicalJobDTOS, HttpServletResponse response) throws IOException;

    PmMedicalJobDTO getProjectListByKey(Long id);

    PmMedicalJobDTO getByJobId(Long jobId);
}
