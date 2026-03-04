package com.sunten.hrms.re.service;

import com.sunten.hrms.re.domain.ReEducationInterface;
import com.sunten.hrms.re.dto.ReEducationInterfaceDTO;
import com.sunten.hrms.re.dto.ReEducationInterfaceQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 受教育经历临时表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
public interface ReEducationInterfaceService extends IService<ReEducationInterface> {

    ReEducationInterfaceDTO insert(ReEducationInterface educationInterfaceNew);

    void delete(Long id);

    void delete(ReEducationInterface educationInterface);

    void update(ReEducationInterface educationInterfaceNew);

    ReEducationInterfaceDTO getByKey(Long id);

    List<ReEducationInterfaceDTO> listAll(ReEducationInterfaceQueryCriteria criteria);

    Map<String, Object> listAll(ReEducationInterfaceQueryCriteria criteria, Pageable pageable);

    void download(List<ReEducationInterfaceDTO> educationInterfaceDTOS, HttpServletResponse response) throws IOException;
}
