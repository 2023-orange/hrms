package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.domain.FndSuperQueryTableColumn;
import com.sunten.hrms.fnd.dto.FndSuperQueryTableColumnDTO;
import com.sunten.hrms.fnd.dto.FndSuperQueryTableColumnQueryCriteria;
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
public interface FndSuperQueryTableColumnService extends IService<FndSuperQueryTableColumn> {

    FndSuperQueryTableColumnDTO insert(FndSuperQueryTableColumn superQueryTableColumnNew);

    void delete(Long id);

    void delete(FndSuperQueryTableColumn superQueryTableColumn);

    void update(FndSuperQueryTableColumn superQueryTableColumnNew);

    FndSuperQueryTableColumnDTO getByKey(Long id);

    List<FndSuperQueryTableColumnDTO> listAll(FndSuperQueryTableColumnQueryCriteria criteria);

    Map<String, Object> listAll(FndSuperQueryTableColumnQueryCriteria criteria, Pageable pageable);

    void download(List<FndSuperQueryTableColumnDTO> superQueryTableColumnDTOS, HttpServletResponse response) throws IOException;
}
