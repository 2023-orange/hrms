package com.sunten.hrms.pm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.pm.domain.PmEmployeeSocialrelations;
import com.sunten.hrms.pm.dto.PmEmployeeSocialrelationsDTO;
import com.sunten.hrms.pm.dto.PmEmployeeSocialrelationsQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 * <p>
 * 社会关系表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
public interface PmEmployeeSocialrelationsService extends IService<PmEmployeeSocialrelations> {

    PmEmployeeSocialrelationsDTO insert(PmEmployeeSocialrelations employeeSocialrelationsNew);

    void delete(Long id);

    void delete(PmEmployeeSocialrelations employeeSocialrelations);

    void update(PmEmployeeSocialrelations employeeSocialrelationsNew);

    PmEmployeeSocialrelationsDTO getByKey(Long id);

    List<PmEmployeeSocialrelationsDTO> listAll(PmEmployeeSocialrelationsQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeeSocialrelationsQueryCriteria criteria, Pageable pageable);

    void download(List<PmEmployeeSocialrelationsDTO> employeeSocialrelationsDTOS, HttpServletResponse response) throws IOException;

    List<PmEmployeeSocialrelationsDTO> batchInsert(List<PmEmployeeSocialrelations> pmEmployeeSocialrelations,Long employeeId);

    List<PmEmployeeSocialrelationsDTO> listAllByCheck(Long employeeId);
}
