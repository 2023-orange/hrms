package com.sunten.hrms.pm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.pm.domain.PmMedicalTuboo;
import com.sunten.hrms.pm.dto.PmMedicalTubooDTO;
import com.sunten.hrms.pm.dto.PmMedicalTubooQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoujy
 * @since 2022-11-30
 */
public interface PmMedicalTubooService extends IService<PmMedicalTuboo> {

    PmMedicalTubooDTO insert(PmMedicalTuboo pmMedicalTubooNew);

    void delete(Long id);

    void delete(PmMedicalTuboo pmMedicalTuboo);

    void update(PmMedicalTuboo pmMedicalTubooNew);

    PmMedicalTubooDTO getByKey(Long id);

    List<PmMedicalTubooDTO> listAll(PmMedicalTubooQueryCriteria criteria);

    Map<String, Object> listAll(PmMedicalTubooQueryCriteria criteria, Pageable pageable);

    void download(List<PmMedicalTubooDTO> pmMedicalTubooDTOS, HttpServletResponse response) throws IOException;

    List<PmMedicalTubooDTO> getPmMedicalTubooSub(String workCard);
}
