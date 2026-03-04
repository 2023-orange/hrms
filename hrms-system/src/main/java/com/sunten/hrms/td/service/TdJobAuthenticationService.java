package com.sunten.hrms.td.service;

import com.sunten.hrms.td.domain.TdJobAuthentication;
import com.sunten.hrms.td.dto.TdJobAuthenticationDTO;
import com.sunten.hrms.td.dto.TdJobAuthenticationQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.td.dto.TdJobGradingDTO;
import com.sunten.hrms.td.dto.TdJobGradingQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 上岗认证表 服务类
 * </p>
 *
 * @author xukai
 * @since 2021-06-22
 */
public interface TdJobAuthenticationService extends IService<TdJobAuthentication> {

    TdJobAuthenticationDTO insert(TdJobAuthentication jobAuthenticationNew);

    void delete(Long id);

    void delete(TdJobAuthentication jobAuthentication);

    void update(TdJobAuthentication jobAuthenticationNew);

    TdJobAuthenticationDTO getByKey(Long id);

    List<TdJobAuthenticationDTO> listAll(TdJobAuthenticationQueryCriteria criteria);

    Map<String, Object> listAll(TdJobAuthenticationQueryCriteria criteria, Pageable pageable);

    void download(List<TdJobAuthenticationDTO> jobAuthenticationDTOS, HttpServletResponse response) throws IOException;
    // 上岗认证到期提醒，到期前两个月、三个月分别提醒一次
    void JobAuthenticationRemind();

    List<TdJobAuthenticationDTO> getAuthentication(TdJobGradingQueryCriteria criteria);
}
