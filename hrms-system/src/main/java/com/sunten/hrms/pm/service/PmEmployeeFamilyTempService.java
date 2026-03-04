package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmEmployeeFamilyTemp;
import com.sunten.hrms.pm.dto.PmEmployeeFamilyTempDTO;
import com.sunten.hrms.pm.dto.PmEmployeeFamilyTempQueryCriteria;
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
 * @author xukai
 * @since 2020-08-25
 */
public interface PmEmployeeFamilyTempService extends IService<PmEmployeeFamilyTemp> {

    PmEmployeeFamilyTempDTO insert(PmEmployeeFamilyTemp employeeFamilyTempNew);

    void delete(Long id);

    void delete(PmEmployeeFamilyTemp employeeFamilyTemp);

    void update(PmEmployeeFamilyTemp employeeFamilyTempNew);

    PmEmployeeFamilyTempDTO getByKey(Long id);

    List<PmEmployeeFamilyTempDTO> listAll(PmEmployeeFamilyTempQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeeFamilyTempQueryCriteria criteria, Pageable pageable);

    void download(List<PmEmployeeFamilyTempDTO> employeeFamilyTempDTOS, HttpServletResponse response) throws IOException;

    void check(PmEmployeeFamilyTemp pmEmployeeFamilyTemp);
}
