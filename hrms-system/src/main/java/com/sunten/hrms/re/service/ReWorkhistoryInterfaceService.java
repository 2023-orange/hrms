package com.sunten.hrms.re.service;

import com.sunten.hrms.re.domain.ReWorkhistoryInterface;
import com.sunten.hrms.re.dto.ReWorkhistoryInterfaceDTO;
import com.sunten.hrms.re.dto.ReWorkhistoryInterfaceQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 工作经历临时表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
public interface ReWorkhistoryInterfaceService extends IService<ReWorkhistoryInterface> {

    ReWorkhistoryInterfaceDTO insert(ReWorkhistoryInterface workhistoryInterfaceNew);

    void delete(Long id);

    void delete(ReWorkhistoryInterface workhistoryInterface);

    void update(ReWorkhistoryInterface workhistoryInterfaceNew);

    ReWorkhistoryInterfaceDTO getByKey(Long id);

    List<ReWorkhistoryInterfaceDTO> listAll(ReWorkhistoryInterfaceQueryCriteria criteria);

    Map<String, Object> listAll(ReWorkhistoryInterfaceQueryCriteria criteria, Pageable pageable);

    void download(List<ReWorkhistoryInterfaceDTO> workhistoryInterfaceDTOS, HttpServletResponse response) throws IOException;
}
