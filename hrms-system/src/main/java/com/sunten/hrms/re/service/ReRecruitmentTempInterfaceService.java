package com.sunten.hrms.re.service;

import com.sunten.hrms.re.domain.ReRecruitmentTempInterface;
import com.sunten.hrms.re.dto.ReRecruitmentTempInterfaceDTO;
import com.sunten.hrms.re.dto.ReRecruitmentTempInterfaceQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 应聘全部数据导入的临时表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2021-09-08
 */
public interface ReRecruitmentTempInterfaceService extends IService<ReRecruitmentTempInterface> {

    ReRecruitmentTempInterfaceDTO insert(ReRecruitmentTempInterface recruitmentTempInterfaceNew);

    void delete(Long id);

    void delete(ReRecruitmentTempInterface recruitmentTempInterface);

    void update(ReRecruitmentTempInterface recruitmentTempInterfaceNew);

    ReRecruitmentTempInterfaceDTO getByKey(Long id);

    List<ReRecruitmentTempInterfaceDTO> listAll(ReRecruitmentTempInterfaceQueryCriteria criteria);

    Map<String, Object> listAll(ReRecruitmentTempInterfaceQueryCriteria criteria, Pageable pageable);

    void download(List<ReRecruitmentTempInterfaceDTO> recruitmentTempInterfaceDTOS, HttpServletResponse response) throws IOException;

    List<ReRecruitmentTempInterface> importExcelData(List<ReRecruitmentTempInterface> reRecruitmentTempInterfaces);

    void insertMainAndSon(List<ReRecruitmentTempInterface> reRecruitmentTempInterfaces, Long groupId);
}
