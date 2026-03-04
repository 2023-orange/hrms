package com.sunten.hrms.re.service;

import com.sunten.hrms.re.domain.ReFamilyInterface;
import com.sunten.hrms.re.dto.ReFamilyInterfaceDTO;
import com.sunten.hrms.re.dto.ReFamilyInterfaceQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 家庭情况临时表 服务类
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
public interface ReFamilyInterfaceService extends IService<ReFamilyInterface> {

    ReFamilyInterfaceDTO insert(ReFamilyInterface familyInterfaceNew);

    void delete(Long id);

    void delete(ReFamilyInterface familyInterface);

    void update(ReFamilyInterface familyInterfaceNew);

    ReFamilyInterfaceDTO getByKey(Long id);

    List<ReFamilyInterfaceDTO> listAll(ReFamilyInterfaceQueryCriteria criteria);

    Map<String, Object> listAll(ReFamilyInterfaceQueryCriteria criteria, Pageable pageable);

    void download(List<ReFamilyInterfaceDTO> familyInterfaceDTOS, HttpServletResponse response) throws IOException;
}
