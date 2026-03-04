package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmQuarterlyAssessmentInterface;
import com.sunten.hrms.swm.dto.SwmQuarterlyAssessmentInterfaceDTO;
import com.sunten.hrms.swm.dto.SwmQuarterlyAssessmentInterfaceQueryCriteria;
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
 * @author liangjw
 * @since 2022-05-13
 */
public interface SwmQuarterlyAssessmentInterfaceService extends IService<SwmQuarterlyAssessmentInterface> {

    SwmQuarterlyAssessmentInterfaceDTO insert(SwmQuarterlyAssessmentInterface quarterlyAssessmentInterfaceNew);

    void delete(Long id);

    void delete(SwmQuarterlyAssessmentInterface quarterlyAssessmentInterface);

    void update(SwmQuarterlyAssessmentInterface quarterlyAssessmentInterfaceNew);

    SwmQuarterlyAssessmentInterfaceDTO getByKey(Long id);

    List<SwmQuarterlyAssessmentInterfaceDTO> listAll(SwmQuarterlyAssessmentInterfaceQueryCriteria criteria);

    Map<String, Object> listAll(SwmQuarterlyAssessmentInterfaceQueryCriteria criteria, Pageable pageable);

    void download(List<SwmQuarterlyAssessmentInterfaceDTO> quarterlyAssessmentInterfaceDTOS, HttpServletResponse response) throws IOException;

    void insertMainAndSon(List<SwmQuarterlyAssessmentInterface> swmQuarterlyAssessmentInterfaces, Long groupId, Boolean reImportFlag);

    List<SwmQuarterlyAssessmentInterface> insertExcel(List<SwmQuarterlyAssessmentInterface> swmQuarterlyAssessmentInterfaces, Boolean reImport);
}
