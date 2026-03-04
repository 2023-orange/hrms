package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.domain.FndDynamicQueryOperatorGroup;
import com.sunten.hrms.fnd.dto.FndDynamicQueryOperatorGroupDTO;
import com.sunten.hrms.fnd.dto.FndDynamicQueryOperatorGroupQueryCriteria;
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
public interface FndDynamicQueryOperatorGroupService extends IService<FndDynamicQueryOperatorGroup> {

    FndDynamicQueryOperatorGroupDTO insert(FndDynamicQueryOperatorGroup dynamicQueryOperatorGroupNew);

    void delete(Long id);

    void delete(FndDynamicQueryOperatorGroup dynamicQueryOperatorGroup);

    void update(FndDynamicQueryOperatorGroup dynamicQueryOperatorGroupNew);

    FndDynamicQueryOperatorGroupDTO getByKey(Long id);

    List<FndDynamicQueryOperatorGroupDTO> listAll(FndDynamicQueryOperatorGroupQueryCriteria criteria);

    Map<String, Object> listAll(FndDynamicQueryOperatorGroupQueryCriteria criteria, Pageable pageable);

    void download(List<FndDynamicQueryOperatorGroupDTO> dynamicQueryOperatorGroupDTOS, HttpServletResponse response) throws IOException;
}
