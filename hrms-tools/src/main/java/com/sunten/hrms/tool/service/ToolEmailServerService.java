package com.sunten.hrms.tool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.tool.domain.ToolEmailServer;
import com.sunten.hrms.tool.dto.ToolEmailServerDTO;
import com.sunten.hrms.tool.dto.ToolEmailServerQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author batan
 * @since 2020-11-02
 */
public interface ToolEmailServerService extends IService<ToolEmailServer> {

    ToolEmailServerDTO insert(ToolEmailServer emailServerNew);

    void delete(Long id);

    void delete(ToolEmailServer emailServer);

    void update(ToolEmailServer emailServerNew);

    ToolEmailServerDTO getByKey(Long id);

    List<ToolEmailServerDTO> listAll(ToolEmailServerQueryCriteria criteria);

    Map<String, Object> listAll(ToolEmailServerQueryCriteria criteria, Pageable pageable);

    void download(List<ToolEmailServerDTO> emailServerDTOS, HttpServletResponse response) throws IOException;
}
