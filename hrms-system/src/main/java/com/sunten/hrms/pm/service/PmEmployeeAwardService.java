package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmEmployeeAward;
import com.sunten.hrms.pm.dto.PmEmployeeAwardDTO;
import com.sunten.hrms.pm.dto.PmEmployeeAwardQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 奖罚情况表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
public interface PmEmployeeAwardService extends IService<PmEmployeeAward> {

    PmEmployeeAwardDTO insert(PmEmployeeAward employeeAwardNew);

    void delete(Long id);

    void delete(PmEmployeeAward employeeAward);

    void update(PmEmployeeAward employeeAwardNew);

    PmEmployeeAwardDTO getByKey(Long id);

    List<PmEmployeeAwardDTO> listAll(PmEmployeeAwardQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeeAwardQueryCriteria criteria, Pageable pageable);

    void download(List<PmEmployeeAwardDTO> employeeAwardDTOS, HttpServletResponse response) throws IOException;

    List<PmEmployeeAwardDTO> batchInsert(List<PmEmployeeAward> employeeAwardNews,Long employeeId);
}
