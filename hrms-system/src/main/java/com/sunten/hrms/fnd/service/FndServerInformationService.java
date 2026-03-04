package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.domain.FndServerInformation;
import com.sunten.hrms.fnd.dto.FndServerInformationDTO;
import com.sunten.hrms.fnd.dto.FndServerInformationQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 服务器信息表 服务类
 * </p>
 *
 * @author batan
 * @since 2024-06-06
 */
public interface FndServerInformationService extends IService<FndServerInformation> {

    FndServerInformationDTO insert(FndServerInformation serverInformationNew);

    void delete(Long id);

    void delete(FndServerInformation serverInformation);

    void update(FndServerInformation serverInformationNew);

    FndServerInformationDTO getByKey(Long id);

    List<FndServerInformationDTO> listAll(FndServerInformationQueryCriteria criteria);

    Map<String, Object> listAll(FndServerInformationQueryCriteria criteria, Pageable pageable);

    void download(List<FndServerInformationDTO> serverInformationDTOS, HttpServletResponse response) throws IOException;
}
