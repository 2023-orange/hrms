package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmEmployeeAwardInterface;
import com.sunten.hrms.pm.dto.PmEmployeeAwardInterfaceDTO;
import com.sunten.hrms.pm.dto.PmEmployeeAwardInterfaceQueryCriteria;
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
 * @author xk
 * @since 2021-09-23
 */
public interface PmEmployeeAwardInterfaceService extends IService<PmEmployeeAwardInterface> {

    PmEmployeeAwardInterfaceDTO insert(PmEmployeeAwardInterface employeeAwardInterfaceNew);

    void delete(Long id);

    void delete(PmEmployeeAwardInterface employeeAwardInterface);

    void update(PmEmployeeAwardInterface employeeAwardInterfaceNew);

    PmEmployeeAwardInterfaceDTO getByKey(Long id);

    List<PmEmployeeAwardInterfaceDTO> listAll(PmEmployeeAwardInterfaceQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeeAwardInterfaceQueryCriteria criteria, Pageable pageable);

    void download(List<PmEmployeeAwardInterfaceDTO> employeeAwardInterfaceDTOS, HttpServletResponse response) throws IOException;

    List<PmEmployeeAwardInterfaceDTO> importAwardByExcel(List<PmEmployeeAwardInterface> awardInterfaces, Boolean reImportFlag);

    void insertMainAndSon(List<PmEmployeeAwardInterface> pmEmployeeAwardInterfaces, Long groupId, Boolean reImportFlag);
}
