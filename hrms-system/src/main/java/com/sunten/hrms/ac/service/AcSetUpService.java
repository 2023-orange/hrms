package com.sunten.hrms.ac.service;

import com.sunten.hrms.ac.domain.AcSetUp;
import com.sunten.hrms.ac.dto.AcSetUpDTO;
import com.sunten.hrms.ac.dto.AcSetUpQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 考勤异常允许设置表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2020-10-23
 */
public interface AcSetUpService extends IService<AcSetUp> {

    AcSetUpDTO insert(AcSetUp setUpNew);

    void delete(Long id);

    void delete(AcSetUp setUp);

    void update(AcSetUp setUpNew);

    AcSetUpDTO getByKey(Long id);

    List<AcSetUpDTO> listAll(AcSetUpQueryCriteria criteria);

    Map<String, Object> listAll(AcSetUpQueryCriteria criteria, Pageable pageable);

    void download(List<AcSetUpDTO> setUpDTOS, HttpServletResponse response) throws IOException;

    AcSetUpDTO getByTop();
}
