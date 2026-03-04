package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.domain.FndDictDetail;
import com.sunten.hrms.fnd.dto.FndDictDetailDTO;
import com.sunten.hrms.fnd.dto.FndDictDetailQueryCriteria;
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
 * @since 2019-12-19
 */
public interface FndDictDetailService extends IService<FndDictDetail> {

    FndDictDetailDTO insert(FndDictDetail dictDetailNew);

    void delete(Long id);

    void update(FndDictDetail dictDetailNew);

    FndDictDetailDTO getByKey(Long id);

    List<FndDictDetailDTO> listAll(FndDictDetailQueryCriteria criteria);

    Map<String, Object> listAll(FndDictDetailQueryCriteria criteria, Pageable pageable);

    void download(List<FndDictDetailDTO> dictDetailDTOS, HttpServletResponse response) throws IOException;
}
