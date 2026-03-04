package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmEmployeeContract;
import com.sunten.hrms.pm.dto.PmEmployeeContractDTO;
import com.sunten.hrms.pm.dto.PmEmployeeContractQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 合同情况表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
public interface PmEmployeeContractService extends IService<PmEmployeeContract> {

    PmEmployeeContractDTO insert(PmEmployeeContract employeeContractNew);

    void delete(Long id);

    void delete(PmEmployeeContract employeeContract);

    void update(PmEmployeeContract employeeContractNew);

    PmEmployeeContractDTO getByKey(Long id);

    List<PmEmployeeContractDTO> listAll(PmEmployeeContractQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeeContractQueryCriteria criteria, Pageable pageable);

    void download(PmEmployeeContractQueryCriteria criteria, HttpServletResponse response) throws IOException;

    List<PmEmployeeContractDTO> batchInsert(List<PmEmployeeContract> pmEmployeeContracts,Long employeeId);

    Map<String, Object> listAllAndSignNumByPage(PmEmployeeContractQueryCriteria criteria, Pageable pageable);

    List<PmEmployeeContractDTO> listAllAndSignNum(PmEmployeeContractQueryCriteria criteria);

    List<PmEmployeeContractDTO> getAllByemployeeId(Long employeeId);

    PmEmployeeContractDTO getNewestContractByEmployeeId(Long employeeId);
}
