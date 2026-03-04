package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.domain.FndDynamicQueryOperatorGroupDetail;
import com.sunten.hrms.fnd.dto.FndDynamicQueryOperatorGroupDetailDTO;
import com.sunten.hrms.fnd.dto.FndDynamicQueryOperatorGroupDetailQueryCriteria;
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
public interface FndDynamicQueryOperatorGroupDetailService extends IService<FndDynamicQueryOperatorGroupDetail> {

    FndDynamicQueryOperatorGroupDetailDTO insert(FndDynamicQueryOperatorGroupDetail dynamicQueryOperatorGroupDetailNew);

    void delete(Long id);

    void delete(FndDynamicQueryOperatorGroupDetail dynamicQueryOperatorGroupDetail);

    void update(FndDynamicQueryOperatorGroupDetail dynamicQueryOperatorGroupDetailNew);

    FndDynamicQueryOperatorGroupDetailDTO getByKey(Long id);

    List<FndDynamicQueryOperatorGroupDetailDTO> listAll(FndDynamicQueryOperatorGroupDetailQueryCriteria criteria);

    Map<String, Object> listAll(FndDynamicQueryOperatorGroupDetailQueryCriteria criteria, Pageable pageable);

    void download(List<FndDynamicQueryOperatorGroupDetailDTO> dynamicQueryOperatorGroupDetailDTOS, HttpServletResponse response) throws IOException;
}
