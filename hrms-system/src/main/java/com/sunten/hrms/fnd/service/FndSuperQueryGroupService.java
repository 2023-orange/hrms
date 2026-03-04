package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.domain.FndSuperQueryGroup;
import com.sunten.hrms.fnd.dto.FndSuperQueryGroupDTO;
import com.sunten.hrms.fnd.dto.FndSuperQueryGroupQueryCriteria;
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
public interface FndSuperQueryGroupService extends IService<FndSuperQueryGroup> {

    FndSuperQueryGroupDTO insert(FndSuperQueryGroup superQueryGroupNew);

    void delete(Long id);

    void delete(FndSuperQueryGroup superQueryGroup);

    void update(FndSuperQueryGroup superQueryGroupNew);

    FndSuperQueryGroupDTO getByKey(Long id);

    List<FndSuperQueryGroup> listAllExpand(FndSuperQueryGroupQueryCriteria criteria);

    List<FndSuperQueryGroupDTO> listAll(FndSuperQueryGroupQueryCriteria criteria);

    Map<String, Object> listAll(FndSuperQueryGroupQueryCriteria criteria, Pageable pageable);

    void download(List<FndSuperQueryGroupDTO> superQueryGroupDTOS, HttpServletResponse response) throws IOException;
}
