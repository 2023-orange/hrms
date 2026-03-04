package com.sunten.hrms.ac.service;

import com.sunten.hrms.ac.domain.AcExceptionDispose;
import com.sunten.hrms.ac.dto.AcExceptionDisposeDTO;
import com.sunten.hrms.ac.dto.AcExceptionDisposeQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 考勤异常及处理表 服务类
 * </p>
 *
 * @author ljw
 * @since 2020-09-17
 */
public interface AcExceptionDisposeService extends IService<AcExceptionDispose> {

    AcExceptionDisposeDTO insert(AcExceptionDispose exceptionDisposeNew);

    void delete(Long id);

    void delete(AcExceptionDispose exceptionDispose);

    void update(AcExceptionDispose exceptionDisposeNew);

    AcExceptionDisposeDTO getByKey(Long id);

    List<AcExceptionDisposeDTO> listAll(AcExceptionDisposeQueryCriteria criteria);

    Map<String, Object> listAll(AcExceptionDisposeQueryCriteria criteria, Pageable pageable);

    void download(List<AcExceptionDisposeDTO> exceptionDisposeDTOS, HttpServletResponse response) throws IOException;
}
