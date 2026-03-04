package com.sunten.hrms.tool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.tool.domain.ToolEmailInterface;
import com.sunten.hrms.tool.dto.ToolEmailInterfaceDTO;
import com.sunten.hrms.tool.dto.ToolEmailInterfaceQueryCriteria;
import com.sunten.hrms.tool.vo.ToolEmailVo;
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
 * @since 2020-10-30
 */
public interface ToolEmailInterfaceService extends IService<ToolEmailInterface> {

    ToolEmailInterfaceDTO insert(ToolEmailInterface emailInterfaceNew);

    ToolEmailInterfaceDTO insert(ToolEmailVo emailVo);

    void delete(Long id);

    void delete(ToolEmailInterface emailInterface);

    void update(ToolEmailInterface emailInterfaceNew);

    ToolEmailInterfaceDTO getByKey(Long id);

    List<ToolEmailInterfaceDTO> listAll(ToolEmailInterfaceQueryCriteria criteria);

    Map<String, Object> listAll(ToolEmailInterfaceQueryCriteria criteria, Pageable pageable);

    void download(List<ToolEmailInterfaceDTO> emailInterfaceDTOS, HttpServletResponse response) throws IOException;

    ToolEmailInterfaceDTO sendAndSave(ToolEmailInterface emailInterface, Boolean isNewEmail);

    ToolEmailInterfaceDTO sendAndSaveWithThrow(ToolEmailInterface emailInterface, Boolean isNewEmail);

    ToolEmailInterfaceDTO sendAndSaveWithThrow(ToolEmailVo emailVo);

    void sendEmail();

    void sendEmailImmediate(Long id);

    Map<String, String> checkEmailSetting();

    String getUrl();
}
