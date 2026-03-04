package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.domain.FndAuthorization;
import com.sunten.hrms.fnd.dto.FndAuthorizationDTO;
import com.sunten.hrms.fnd.dto.FndAuthorizationQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xukai
 * @since 2021-01-29
 */
public interface FndAuthorizationService extends IService<FndAuthorization> {

    FndAuthorizationDTO insert(FndAuthorization authorizationNew);

    void delete(Long id);

    void delete(FndAuthorization authorization);

    void update(FndAuthorization authorizationNew);

    FndAuthorizationDTO getByKey(Long id);

    List<FndAuthorizationDTO> listAll(FndAuthorizationQueryCriteria criteria);

    Map<String, Object> listAll(FndAuthorizationQueryCriteria criteria, Pageable pageable);

    void download(List<FndAuthorizationDTO> authorizationDTOS, HttpServletResponse response) throws IOException;

    List<FndAuthorizationDTO> listAllByCriteriaWithChild(@Param(value = "criteria") FndAuthorizationQueryCriteria criteria);
}
