package com.sunten.hrms.re.service;

import com.sunten.hrms.re.domain.ReTitleInterface;
import com.sunten.hrms.re.dto.ReTitleInterfaceDTO;
import com.sunten.hrms.re.dto.ReTitleInterfaceQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 职称情况临时表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
public interface ReTitleInterfaceService extends IService<ReTitleInterface> {

    ReTitleInterfaceDTO insert(ReTitleInterface titleInterfaceNew);

    void delete(Long id);

    void delete(ReTitleInterface titleInterface);

    void update(ReTitleInterface titleInterfaceNew);

    ReTitleInterfaceDTO getByKey(Long id);

    List<ReTitleInterfaceDTO> listAll(ReTitleInterfaceQueryCriteria criteria);

    Map<String, Object> listAll(ReTitleInterfaceQueryCriteria criteria, Pageable pageable);

    void download(List<ReTitleInterfaceDTO> titleInterfaceDTOS, HttpServletResponse response) throws IOException;
}
