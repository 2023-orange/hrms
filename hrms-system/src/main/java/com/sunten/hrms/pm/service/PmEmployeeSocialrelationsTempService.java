package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmEmployeeSocialrelationsTemp;
import com.sunten.hrms.pm.dto.PmEmployeeSocialrelationsTempDTO;
import com.sunten.hrms.pm.dto.PmEmployeeSocialrelationsTempQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 社会关系临时表 服务类
 * </p>
 *
 * @author xukai
 * @since 2021-11-24
 */
public interface PmEmployeeSocialrelationsTempService extends IService<PmEmployeeSocialrelationsTemp> {

    PmEmployeeSocialrelationsTempDTO insert(PmEmployeeSocialrelationsTemp employeeSocialrelationsTempNew);

    void delete(Long id);

    void delete(PmEmployeeSocialrelationsTemp employeeSocialrelationsTemp);

    void update(PmEmployeeSocialrelationsTemp employeeSocialrelationsTempNew);

    PmEmployeeSocialrelationsTempDTO getByKey(Long id);

    List<PmEmployeeSocialrelationsTempDTO> listAll(PmEmployeeSocialrelationsTempQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeeSocialrelationsTempQueryCriteria criteria, Pageable pageable);

    void download(List<PmEmployeeSocialrelationsTempDTO> employeeSocialrelationsTempDTOS, HttpServletResponse response) throws IOException;

    void check(PmEmployeeSocialrelationsTemp employeeSocialrelationsTemp);
}
