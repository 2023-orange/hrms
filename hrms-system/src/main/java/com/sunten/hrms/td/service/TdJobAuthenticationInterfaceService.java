package com.sunten.hrms.td.service;

import com.sunten.hrms.td.domain.TdJobAuthenticationInterface;
import com.sunten.hrms.td.dto.TdJobAuthenticationInterfaceDTO;
import com.sunten.hrms.td.dto.TdJobAuthenticationInterfaceQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 上岗认证接口表 服务类
 * </p>
 *
 * @author xukai
 * @since 2021-10-11
 */
public interface TdJobAuthenticationInterfaceService extends IService<TdJobAuthenticationInterface> {

    TdJobAuthenticationInterfaceDTO insert(TdJobAuthenticationInterface jobAuthenticationInterfaceNew);

    void delete(Long id);

    void delete(TdJobAuthenticationInterface jobAuthenticationInterface);

    void update(TdJobAuthenticationInterface jobAuthenticationInterfaceNew);

    TdJobAuthenticationInterfaceDTO getByKey(Long id);

    List<TdJobAuthenticationInterfaceDTO> listAll(TdJobAuthenticationInterfaceQueryCriteria criteria);

    Map<String, Object> listAll(TdJobAuthenticationInterfaceQueryCriteria criteria, Pageable pageable);

    void download(List<TdJobAuthenticationInterfaceDTO> jobAuthenticationInterfaceDTOS, HttpServletResponse response) throws IOException;

    List<TdJobAuthenticationInterface> importAuthenticationByExcel(List<TdJobAuthenticationInterface> authenticationInterfaces);
}
