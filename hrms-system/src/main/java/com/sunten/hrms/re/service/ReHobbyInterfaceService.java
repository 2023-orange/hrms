package com.sunten.hrms.re.service;

import com.sunten.hrms.re.domain.ReHobbyInterface;
import com.sunten.hrms.re.dto.ReHobbyInterfaceDTO;
import com.sunten.hrms.re.dto.ReHobbyInterfaceQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 技术爱好临时表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
public interface ReHobbyInterfaceService extends IService<ReHobbyInterface> {

    ReHobbyInterfaceDTO insert(ReHobbyInterface hobbyInterfaceNew);

    void delete(Long id);

    void delete(ReHobbyInterface hobbyInterface);

    void update(ReHobbyInterface hobbyInterfaceNew);

    ReHobbyInterfaceDTO getByKey(Long id);

    List<ReHobbyInterfaceDTO> listAll(ReHobbyInterfaceQueryCriteria criteria);

    Map<String, Object> listAll(ReHobbyInterfaceQueryCriteria criteria, Pageable pageable);

    void download(List<ReHobbyInterfaceDTO> hobbyInterfaceDTOS, HttpServletResponse response) throws IOException;
}
