package com.sunten.hrms.re.service;

import com.sunten.hrms.re.domain.ReRecruitment;
import com.sunten.hrms.re.dto.ReRecruitmentDTO;
import com.sunten.hrms.re.dto.ReRecruitmentQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 招骋数据表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
public interface ReRecruitmentService extends IService<ReRecruitment> {

    ReRecruitmentDTO insert(ReRecruitment recruitmentNew);

    void delete(Long id);

    void delete(ReRecruitment recruitment);

    void update(ReRecruitment recruitmentNew);

    ReRecruitmentDTO getByKey(Long id);

    List<ReRecruitmentDTO> listAll(ReRecruitmentQueryCriteria criteria);

    Map<String, Object> listAll(ReRecruitmentQueryCriteria criteria, Pageable pageable);

    void download(List<ReRecruitmentDTO> recruitmentDTOS, HttpServletResponse response) throws IOException;

    void insertByTempInterface(Long groupId);
}
