package com.sunten.hrms.pm.service;

import com.sunten.hrms.pm.domain.PmEmployeeRehire;
import com.sunten.hrms.pm.dto.PmEmployeeRehireDTO;
import com.sunten.hrms.pm.dto.PmEmployeeRehireQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 返聘协议表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
public interface PmEmployeeRehireService extends IService<PmEmployeeRehire> {

    PmEmployeeRehireDTO insert(PmEmployeeRehire employeeRehireNew);

    void delete(Long id);

    void delete(PmEmployeeRehire employeeRehire);

    void update(PmEmployeeRehire employeeRehireNew);

    PmEmployeeRehireDTO getByKey(Long id);

    List<PmEmployeeRehireDTO> listAll(PmEmployeeRehireQueryCriteria criteria);

    Map<String, Object> listAll(PmEmployeeRehireQueryCriteria criteria, Pageable pageable);

    void download(List<PmEmployeeRehireDTO> employeeRehireDTOS, HttpServletResponse response) throws IOException;
}
