package com.sunten.hrms.ac.service;

import com.sunten.hrms.ac.domain.AcRegimeRelation;
import com.sunten.hrms.ac.dto.AcRegimeRelationDTO;
import com.sunten.hrms.ac.dto.AcRegimeRelationQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 考勤制度排班时间关系表 服务类
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
public interface AcRegimeRelationService extends IService<AcRegimeRelation> {

    AcRegimeRelationDTO insert(AcRegimeRelation regimeRelationNew);

    void delete(Long id);

    void delete(AcRegimeRelation regimeRelation);

    void update(AcRegimeRelation regimeRelationNew);

    AcRegimeRelationDTO getByKey(Long id);

    List<AcRegimeRelationDTO> listAll(AcRegimeRelationQueryCriteria criteria);

    Map<String, Object> listAll(AcRegimeRelationQueryCriteria criteria, Pageable pageable);

    void download(List<AcRegimeRelationDTO> regimeRelationDTOS, HttpServletResponse response) throws IOException;

    void deleteByRegimeIdAndRegimeTimeId(AcRegimeRelation regimeRelation);
}
