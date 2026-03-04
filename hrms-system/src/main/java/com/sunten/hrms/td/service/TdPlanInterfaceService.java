package com.sunten.hrms.td.service;

import com.sunten.hrms.td.domain.TdPlanInterface;
import com.sunten.hrms.td.dto.TdPlanInterfaceDTO;
import com.sunten.hrms.td.dto.TdPlanInterfaceQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 培训计划接口表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2021-05-23
 */
public interface TdPlanInterfaceService extends IService<TdPlanInterface> {

    TdPlanInterfaceDTO insert(TdPlanInterface planInterfaceNew);

    void delete(Long id);

    void delete(TdPlanInterface planInterface);

    void update(TdPlanInterface planInterfaceNew);

    TdPlanInterfaceDTO getByKey(Long id);

    List<TdPlanInterfaceDTO> listAll(TdPlanInterfaceQueryCriteria criteria);

    Map<String, Object> listAll(TdPlanInterfaceQueryCriteria criteria, Pageable pageable);

    void download(List<TdPlanInterfaceDTO> planInterfaceDTOS, HttpServletResponse response) throws IOException;

    List<TdPlanInterfaceDTO> insertExcel(List<TdPlanInterface> tdPlanInterfaces);

    void insertMainAndSon(List<TdPlanInterface> tdPlanInterfaces,Long groupId,Long userId);
}
