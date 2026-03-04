package com.sunten.hrms.td.service;

import com.sunten.hrms.td.domain.TdCredential;
import com.sunten.hrms.td.dto.TdCredentialDTO;
import com.sunten.hrms.td.dto.TdCredentialQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 培训证书表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2021-06-30
 */
public interface TdCredentialService extends IService<TdCredential> {

    TdCredentialDTO insert(TdCredential credentialNew);

    void delete(Long id);

    void delete(TdCredential credential);

    void update(TdCredential credentialNew);

    TdCredentialDTO getByKey(Long id);

    List<TdCredentialDTO> listAll(TdCredentialQueryCriteria criteria);

    Map<String, Object> listAll(TdCredentialQueryCriteria criteria, Pageable pageable);

    void download(List<TdCredentialDTO> credentialDTOS, HttpServletResponse response) throws IOException;
}
