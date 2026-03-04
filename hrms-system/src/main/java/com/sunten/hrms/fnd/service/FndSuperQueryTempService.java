package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.domain.FndSuperQueryTemp;
import com.sunten.hrms.fnd.dto.FndSuperQueryTempDTO;
import com.sunten.hrms.fnd.dto.FndSuperQueryTempQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 超级查询数据临时表 服务类
 * </p>
 *
 * @author liangjw
 * @since 2021-08-19
 */
public interface FndSuperQueryTempService extends IService<FndSuperQueryTemp> {

//    FndSuperQueryTempDTO insert(FndSuperQueryTemp superQueryTempNew);

//    void delete(Long id);

//    void delete(FndSuperQueryTemp superQueryTemp);

//    void update(FndSuperQueryTemp superQueryTempNew);

//    FndSuperQueryTempDTO getByKey(Long id);

    List<FndSuperQueryTempDTO> listAll(FndSuperQueryTempQueryCriteria criteria);

    Map<String, Object> listAll(FndSuperQueryTempQueryCriteria criteria, Pageable pageable);

    void download(List<FndSuperQueryTempDTO> superQueryTempDTOS, HttpServletResponse response) throws IOException;

    void insertSuperQueryOriginal(FndSuperQueryTempQueryCriteria criteria);

    void insertSuperQueryUnpivot(FndSuperQueryTempQueryCriteria criteria);

    void insertSuperQueryCross(FndSuperQueryTempQueryCriteria criteria);
}
