package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmMedicalLineRelevance;
import com.sunten.hrms.pm.dto.PmMedicalLineRelevanceDTO;
import com.sunten.hrms.pm.dto.PmMedicalLineRelevanceQueryCriteria;
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
 * @author xukai
 * @since 2021-04-20
 */
public interface PmMedicalLineRelevanceService extends IService<PmMedicalLineRelevance> {

    PmMedicalLineRelevanceDTO insert(PmMedicalLineRelevance medicalLineRelevanceNew);

    void delete(Long id);

    void delete(PmMedicalLineRelevance medicalLineRelevance);

    void update(PmMedicalLineRelevance medicalLineRelevanceNew);

    PmMedicalLineRelevanceDTO getByKey(Long id);

    List<PmMedicalLineRelevanceDTO> listAll(PmMedicalLineRelevanceQueryCriteria criteria);

    Map<String, Object> listAll(PmMedicalLineRelevanceQueryCriteria criteria, Pageable pageable);

    void download(List<PmMedicalLineRelevanceDTO> medicalLineRelevanceDTOS, HttpServletResponse response) throws IOException;
    // 批量新增
    void batchInsert(List<PmMedicalLineRelevance> relevances,Long createBy,Long medicalLineId);
    // 批量编辑（可能新增、删除）
    void batchEdit(List<PmMedicalLineRelevance> relevances,Long updateBy,Long medicalLineId);
}
