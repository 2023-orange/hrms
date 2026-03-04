package com.sunten.hrms.td.service;

import com.sunten.hrms.td.domain.TdPlanEmployeeInterface;
import com.sunten.hrms.td.dto.TdPlanEmployeeInterfaceDTO;
import com.sunten.hrms.td.dto.TdPlanEmployeeInterfaceQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 培训参训人员接口表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2021-09-24
 */
public interface TdPlanEmployeeInterfaceService extends IService<TdPlanEmployeeInterface> {

    TdPlanEmployeeInterfaceDTO insert(TdPlanEmployeeInterface planEmployeeInterfaceNew);

    void delete(Long id);

    void delete(TdPlanEmployeeInterface planEmployeeInterface);

    void update(TdPlanEmployeeInterface planEmployeeInterfaceNew);

    TdPlanEmployeeInterfaceDTO getByKey(Long id);

    List<TdPlanEmployeeInterfaceDTO> listAll(TdPlanEmployeeInterfaceQueryCriteria criteria);

    Map<String, Object> listAll(TdPlanEmployeeInterfaceQueryCriteria criteria, Pageable pageable);

    void download(List<TdPlanEmployeeInterfaceDTO> planEmployeeInterfaceDTOS, HttpServletResponse response) throws IOException;

    List<TdPlanEmployeeInterfaceDTO> insertExcel(List<TdPlanEmployeeInterface> tdPlanEmployeeInterfaces);

    void insertMainAndSon(List<TdPlanEmployeeInterface> tdPlanEmployeeInterfaces, Long groupId, Long userId);
}
