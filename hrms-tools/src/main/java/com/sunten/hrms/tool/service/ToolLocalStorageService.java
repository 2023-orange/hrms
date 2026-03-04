package com.sunten.hrms.tool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.tool.domain.ToolLocalStorage;
import com.sunten.hrms.tool.dto.ToolLocalStorageDTO;
import com.sunten.hrms.tool.dto.ToolLocalStorageQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author batan
 * @since 2019-12-25
 */
public interface ToolLocalStorageService extends IService<ToolLocalStorage> {

    ToolLocalStorageDTO insert(ToolLocalStorage localStorageNew);

    void delete(Long id);

    void delete(ToolLocalStorage localStorage);

    void update(ToolLocalStorage localStorageNew);

    void updateName(ToolLocalStorage localStorageNew);

    ToolLocalStorageDTO getByKey(Long id);

    List<ToolLocalStorageDTO> listAll(ToolLocalStorageQueryCriteria criteria);

    Map<String, Object> listAll(ToolLocalStorageQueryCriteria criteria, Pageable pageable);

    void download(List<ToolLocalStorageDTO> localStorageDTOS, HttpServletResponse response) throws IOException;

    ToolLocalStorageDTO insert(String name, MultipartFile multipartFile);

    void deleteAll(Long[] ids);
}
