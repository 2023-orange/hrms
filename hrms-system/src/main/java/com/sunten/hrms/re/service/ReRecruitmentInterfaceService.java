package com.sunten.hrms.re.service;

import com.sunten.hrms.re.domain.ReRecruitmentInterface;
import com.sunten.hrms.re.dto.ReRecruitmentInterfaceDTO;
import com.sunten.hrms.re.dto.ReRecruitmentInterfaceQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 招骋数据临时表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
public interface ReRecruitmentInterfaceService extends IService<ReRecruitmentInterface> {

    ReRecruitmentInterfaceDTO insert(ReRecruitmentInterface recruitmentInterfaceNew);

    void delete(Long id);

    void delete(ReRecruitmentInterface recruitmentInterface);

    void update(ReRecruitmentInterface recruitmentInterfaceNew);

    ReRecruitmentInterfaceDTO getByKey(Long id);

    List<ReRecruitmentInterfaceDTO> listAll(ReRecruitmentInterfaceQueryCriteria criteria);

    Map<String, Object> listAll(ReRecruitmentInterfaceQueryCriteria criteria, Pageable pageable);

    void download(List<ReRecruitmentInterfaceDTO> recruitmentInterfaceDTOS, HttpServletResponse response) throws IOException;
}
