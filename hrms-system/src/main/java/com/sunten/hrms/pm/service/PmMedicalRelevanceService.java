package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmMedicalRelevance;
import com.sunten.hrms.pm.dto.PmMedicalRelevanceDTO;
import com.sunten.hrms.pm.dto.PmMedicalRelevanceQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 体检项目关联表 服务类
 * </p>
 *
 * @author xukai
 * @since 2021-04-19
 */
public interface PmMedicalRelevanceService extends IService<PmMedicalRelevance> {

    PmMedicalRelevanceDTO insert(PmMedicalRelevance medicalRelevanceNew);

    void delete(Long id);

    void delete(PmMedicalRelevance medicalRelevance);

    void update(PmMedicalRelevance medicalRelevanceNew);

    PmMedicalRelevanceDTO getByKey(Long id);

    List<PmMedicalRelevanceDTO> listAll(PmMedicalRelevanceQueryCriteria criteria);

    Map<String, Object> listAll(PmMedicalRelevanceQueryCriteria criteria, Pageable pageable);

    void download(List<PmMedicalRelevanceDTO> medicalRelevanceDTOS, HttpServletResponse response) throws IOException;

    // 批量编辑
    void batchEdit(List<PmMedicalRelevance> pmMedicalRelevances,Long MedicalJobId,Long updateBy);
}
