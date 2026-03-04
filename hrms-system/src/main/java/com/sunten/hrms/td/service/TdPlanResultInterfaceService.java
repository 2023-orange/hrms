package com.sunten.hrms.td.service;

import com.sunten.hrms.td.domain.TdPlanResultInterface;
import com.sunten.hrms.td.dto.TdPlanResultInterfaceDTO;
import com.sunten.hrms.td.dto.TdPlanResultInterfaceQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 培训结果接口表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2021-06-17
 */
public interface TdPlanResultInterfaceService extends IService<TdPlanResultInterface> {

    TdPlanResultInterfaceDTO insert(TdPlanResultInterface planResultInterfaceNew);

    void delete(Long id);

    void delete(TdPlanResultInterface planResultInterface);

    void update(TdPlanResultInterface planResultInterfaceNew);

    TdPlanResultInterfaceDTO getByKey(Long id);

    List<TdPlanResultInterfaceDTO> listAll(TdPlanResultInterfaceQueryCriteria criteria);

    Map<String, Object> listAll(TdPlanResultInterfaceQueryCriteria criteria, Pageable pageable);

    void download(List<TdPlanResultInterfaceDTO> planResultInterfaceDTOS, HttpServletResponse response) throws IOException;

    List<TdPlanResultInterfaceDTO> insertExcel(List<TdPlanResultInterface> tdPlanResultInterfaces);

    void insertMainAndSon(List<TdPlanResultInterface> tdPlanInterfaces,Long groupId,Long userId);
}
