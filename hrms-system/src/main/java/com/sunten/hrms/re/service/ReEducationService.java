package com.sunten.hrms.re.service;

import com.sunten.hrms.re.domain.ReEducation;
import com.sunten.hrms.re.dto.ReEducationDTO;
import com.sunten.hrms.re.dto.ReEducationQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 受教育经历表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
public interface ReEducationService extends IService<ReEducation> {

    ReEducationDTO insert(ReEducation educationNew);

    void delete(Long id);

    void delete(ReEducation education);

    void update(ReEducation educationNew);

    ReEducationDTO getByKey(Long id);

    List<ReEducationDTO> listAll(ReEducationQueryCriteria criteria);

    Map<String, Object> listAll(ReEducationQueryCriteria criteria, Pageable pageable);

    void download(List<ReEducationDTO> educationDTOS, HttpServletResponse response) throws IOException;

    List<ReEducationDTO> batchInsert(List<ReEducation> reEducations,Long reId);
}
