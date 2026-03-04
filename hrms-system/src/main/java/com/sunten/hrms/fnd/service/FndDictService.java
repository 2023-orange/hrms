package com.sunten.hrms.fnd.service;

import com.sunten.hrms.fnd.domain.FndDict;
import com.sunten.hrms.fnd.dto.FndDictDTO;
import com.sunten.hrms.fnd.dto.FndDictQueryCriteria;
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
 * @since 2019-12-19
 */
public interface FndDictService extends IService<FndDict> {

    FndDictDTO insert(FndDict dictNew);

    void delete(Long id);

    void update(FndDict dictNew);

    FndDictDTO getByKey(Long id);

    List<FndDictDTO> listAll(FndDictQueryCriteria criteria);

    Map<String, Object> listAll(FndDictQueryCriteria criteria, Pageable pageable);

    void download(List<FndDictDTO> dictDTOS, HttpServletResponse response) throws IOException;
}
