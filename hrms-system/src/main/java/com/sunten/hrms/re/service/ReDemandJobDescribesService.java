package com.sunten.hrms.re.service;

import com.sunten.hrms.re.domain.ReDemandJobDescribes;
import com.sunten.hrms.re.dto.ReDemandJobDescribesDTO;
import com.sunten.hrms.re.dto.ReDemandJobDescribesQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 岗位说明书表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2021-04-23
 */
public interface ReDemandJobDescribesService extends IService<ReDemandJobDescribes> {

    ReDemandJobDescribesDTO insert(ReDemandJobDescribes demandJobDescribesNew);

    void delete(Long id);

    void delete(ReDemandJobDescribes demandJobDescribes);

    void update(ReDemandJobDescribes demandJobDescribesNew);

    ReDemandJobDescribesDTO getByKey(Long id);

    List<ReDemandJobDescribesDTO> listAll(ReDemandJobDescribesQueryCriteria criteria);

    Map<String, Object> listAll(ReDemandJobDescribesQueryCriteria criteria, Pageable pageable);

    void download(List<ReDemandJobDescribesDTO> demandJobDescribesDTOS, HttpServletResponse response) throws IOException;

    void updateColumnByValue(ReDemandJobDescribes demandJobDescribes);
}
