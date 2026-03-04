package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmMedicalProject;
import com.sunten.hrms.pm.dto.PmMedicalProjectDTO;
import com.sunten.hrms.pm.dto.PmMedicalProjectQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 体检项目表 服务类
 * </p>
 *
 * @author xukai
 * @since 2021-04-19
 */
public interface PmMedicalProjectService extends IService<PmMedicalProject> {

    PmMedicalProjectDTO insert(PmMedicalProject medicalProjectNew);

    void delete(Long id);

    void delete(PmMedicalProject medicalProject);

    void update(PmMedicalProject medicalProjectNew);

    PmMedicalProjectDTO getByKey(Long id);

    List<PmMedicalProjectDTO> listAll(PmMedicalProjectQueryCriteria criteria);

    Map<String, Object> listAll(PmMedicalProjectQueryCriteria criteria, Pageable pageable);

    void download(List<PmMedicalProjectDTO> medicalProjectDTOS, HttpServletResponse response) throws IOException;
}
