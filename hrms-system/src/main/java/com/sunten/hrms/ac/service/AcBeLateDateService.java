package com.sunten.hrms.ac.service;

import com.sunten.hrms.ac.domain.AcBeLateDate;
import com.sunten.hrms.ac.dto.AcBeLateDateDTO;
import com.sunten.hrms.ac.dto.AcBeLateDateQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 厂车迟到时间记录表 服务类
 * </p>
 *
 * @author xukai
 * @since 2021-07-08
 */
public interface AcBeLateDateService extends IService<AcBeLateDate> {

    AcBeLateDateDTO insert(AcBeLateDate beLateDateNew);

    void delete(Long id);

    void delete(AcBeLateDate beLateDate);

    void update(AcBeLateDate beLateDateNew);

    AcBeLateDateDTO getByKey(Long id);

    AcBeLateDateDTO getByDay(LocalDate lateDate);

    List<AcBeLateDateDTO> listAll(AcBeLateDateQueryCriteria criteria);

    Map<String, Object> listAll(AcBeLateDateQueryCriteria criteria, Pageable pageable);

    void download(List<AcBeLateDateDTO> beLateDateDTOS, HttpServletResponse response) throws IOException;
}
