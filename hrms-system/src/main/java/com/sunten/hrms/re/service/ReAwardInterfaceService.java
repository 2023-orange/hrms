package com.sunten.hrms.re.service;

import com.sunten.hrms.re.domain.ReAwardInterface;
import com.sunten.hrms.re.dto.ReAwardInterfaceDTO;
import com.sunten.hrms.re.dto.ReAwardInterfaceQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 奖罚情况临时表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
public interface ReAwardInterfaceService extends IService<ReAwardInterface> {

    ReAwardInterfaceDTO insert(ReAwardInterface awardInterfaceNew);

    void delete(Long id);

    void delete(ReAwardInterface awardInterface);

    void update(ReAwardInterface awardInterfaceNew);

    ReAwardInterfaceDTO getByKey(Long id);

    List<ReAwardInterfaceDTO> listAll(ReAwardInterfaceQueryCriteria criteria);

    Map<String, Object> listAll(ReAwardInterfaceQueryCriteria criteria, Pageable pageable);

    void download(List<ReAwardInterfaceDTO> awardInterfaceDTOS, HttpServletResponse response) throws IOException;
}
