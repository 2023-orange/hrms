package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.domain.FndDynamicQueryGroupDetail;
import com.sunten.hrms.fnd.dto.FndDynamicQueryGroupDetailDTO;
import com.sunten.hrms.fnd.dto.FndDynamicQueryGroupDetailQueryCriteria;
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
 * @since 2022-07-29
 */
public interface FndDynamicQueryGroupDetailService extends IService<FndDynamicQueryGroupDetail> {

    FndDynamicQueryGroupDetailDTO insert(FndDynamicQueryGroupDetail dynamicQueryGroupDetailNew);

    void delete(Long id);

    void deleteByGroupId(Long groupId);

    void delete(FndDynamicQueryGroupDetail dynamicQueryGroupDetail);

    void update(FndDynamicQueryGroupDetail dynamicQueryGroupDetailNew);

    FndDynamicQueryGroupDetailDTO getByKey(Long id);

    List<FndDynamicQueryGroupDetailDTO> listAll(FndDynamicQueryGroupDetailQueryCriteria criteria);

    Map<String, Object> listAll(FndDynamicQueryGroupDetailQueryCriteria criteria, Pageable pageable);

    void download(List<FndDynamicQueryGroupDetailDTO> dynamicQueryGroupDetailDTOS, HttpServletResponse response) throws IOException;
}
