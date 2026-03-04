package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.domain.FndDynamicQueryGroup;
import com.sunten.hrms.fnd.dto.FndDynamicQueryGroupDTO;
import com.sunten.hrms.fnd.dto.FndDynamicQueryGroupQueryCriteria;
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
 * @author batan
 * @since 2022-07-26
 */
public interface FndDynamicQueryGroupService extends IService<FndDynamicQueryGroup> {

    FndDynamicQueryGroupDTO insert(FndDynamicQueryGroup dynamicQueryGroupNew);

    void delete(Long id);

    void delete(FndDynamicQueryGroup dynamicQueryGroup);

    void update(FndDynamicQueryGroup dynamicQueryGroupNew);

    FndDynamicQueryGroupDTO getByKey(Long id);

    List<FndDynamicQueryGroupDTO> listAll(FndDynamicQueryGroupQueryCriteria criteria);

    Map<String, Object> listAll(FndDynamicQueryGroupQueryCriteria criteria, Pageable pageable);

    void download(List<FndDynamicQueryGroupDTO> dynamicQueryGroupDTOS, HttpServletResponse response) throws IOException;
}
