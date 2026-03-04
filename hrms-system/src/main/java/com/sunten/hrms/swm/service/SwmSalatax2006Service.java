package com.sunten.hrms.swm.service;

import com.sunten.hrms.swm.domain.SwmSalatax2006;
import com.sunten.hrms.swm.dto.SwmSalatax2006DTO;
import com.sunten.hrms.swm.dto.SwmSalatax2006QueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 年底奖金所得税 服务类
 * </p>
 *
 * @author zhoujy
 * @since 2023-04-11
 */
public interface SwmSalatax2006Service extends IService<SwmSalatax2006> {

    SwmSalatax2006DTO insert(SwmSalatax2006 salatax2006New);

    void delete();

    void delete(SwmSalatax2006 salatax2006);

    void update(SwmSalatax2006 salatax2006New);

    SwmSalatax2006DTO getByKey();

    List<SwmSalatax2006DTO> listAll(SwmSalatax2006QueryCriteria criteria);

    Map<String, Object> listAll(SwmSalatax2006QueryCriteria criteria, Pageable pageable);

    // 返回2006年年底奖金所得税明细
    List<SwmSalatax2006DTO> getOldSalatax2006(String workCard);
}
