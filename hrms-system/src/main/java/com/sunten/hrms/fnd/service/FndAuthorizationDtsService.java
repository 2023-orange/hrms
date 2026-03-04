package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.domain.FndAuthorizationDts;
import com.sunten.hrms.fnd.dto.FndAuthorizationDtsDTO;
import com.sunten.hrms.fnd.dto.FndAuthorizationDtsQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
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
 * @since 2021-02-02
 */
public interface FndAuthorizationDtsService extends IService<FndAuthorizationDts> {

    FndAuthorizationDtsDTO insert(FndAuthorizationDts authorizationDtsNew);

    void delete(Long id);

    void delete(FndAuthorizationDts authorizationDts);

    void update(FndAuthorizationDts authorizationDtsNew);

    FndAuthorizationDtsDTO getByKey(Long id);

    List<FndAuthorizationDtsDTO> listAll(FndAuthorizationDtsQueryCriteria criteria);

    Map<String, Object> listAll(FndAuthorizationDtsQueryCriteria criteria, Pageable pageable);

    void download(List<FndAuthorizationDtsDTO> authorizationDtsDTOS, HttpServletResponse response) throws IOException;

    // 根据人员id获取被授权部门id集合
    List<Long> getAuthorizationDeptsByToEmployee(Long toEmployeeId);

    void batchUpdate(FndAuthorizationDts authorizationDts);
}
