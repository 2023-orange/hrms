package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmMedicalLine;
import com.sunten.hrms.pm.dto.PmMedicalLineDTO;
import com.sunten.hrms.pm.dto.PmMedicalLineQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.pm.vo.PmMedicalAutoVo;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 体检申请子表 服务类
 * </p>
 *
 * @author xukai
 * @since 2021-04-07
 */
public interface PmMedicalLineService extends IService<PmMedicalLine> {

    PmMedicalLineDTO insert(PmMedicalLine medicalLineNew);

    void delete(Long id);

    void delete(PmMedicalLine medicalLine);

    void update(PmMedicalLine medicalLineNew);

    PmMedicalLineDTO getByKey(Long id);

    List<PmMedicalLineDTO> listAll(PmMedicalLineQueryCriteria criteria);

    Map<String, Object> listAll(PmMedicalLineQueryCriteria criteria, Pageable pageable);

    void download(List<PmMedicalLineDTO> medicalLineDTOS, HttpServletResponse response) throws IOException;

    List<PmMedicalLineDTO> listAllByMedicalId(Long medicalId);

    void batchWriteMedicalResult(List<PmMedicalLineQueryCriteria> criteria);

    void updateFromOa(PmMedicalLine medicalLine);

    List<PmMedicalLineDTO> getMedicalLinesAuto();

}
