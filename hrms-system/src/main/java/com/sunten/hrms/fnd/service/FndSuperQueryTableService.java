package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.domain.FndSuperQueryTable;
import com.sunten.hrms.fnd.dto.FndSuperQueryTableDTO;
import com.sunten.hrms.fnd.dto.FndSuperQueryTableQueryCriteria;
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
 * @author batan
 * @since 2022-08-12
 */
public interface FndSuperQueryTableService extends IService<FndSuperQueryTable> {

    FndSuperQueryTableDTO insert(FndSuperQueryTable superQueryTableNew);

    void delete(Long id);

    void delete(FndSuperQueryTable superQueryTable);

    void update(FndSuperQueryTable superQueryTableNew);

    FndSuperQueryTableDTO getByKey(Long id);

    List<FndSuperQueryTableDTO> listAll(FndSuperQueryTableQueryCriteria criteria);

    Map<String, Object> listAll(FndSuperQueryTableQueryCriteria criteria, Pageable pageable);

    void download(List<FndSuperQueryTableDTO> superQueryTableDTOS, HttpServletResponse response) throws IOException;
}
