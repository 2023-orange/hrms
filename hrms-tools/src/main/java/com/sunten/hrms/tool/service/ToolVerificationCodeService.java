package com.sunten.hrms.tool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunten.hrms.tool.domain.ToolVerificationCode;
import com.sunten.hrms.tool.dto.ToolVerificationCodeDTO;
import com.sunten.hrms.tool.dto.ToolVerificationCodeQueryCriteria;
import com.sunten.hrms.tool.vo.ToolEmailVo;
import org.springframework.data.domain.Pageable;

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
public interface ToolVerificationCodeService extends IService<ToolVerificationCode> {

    ToolVerificationCodeDTO insert(ToolVerificationCode verificationCodeNew);

    void delete(Long id);

    void delete(ToolVerificationCode verificationCode);

    void update(ToolVerificationCode verificationCodeNew);

    ToolVerificationCodeDTO getByKey(Long id);

    List<ToolVerificationCodeDTO> listAll(ToolVerificationCodeQueryCriteria criteria);

    Map<String, Object> listAll(ToolVerificationCodeQueryCriteria criteria, Pageable pageable);

    void download(List<ToolVerificationCodeDTO> verificationCodeDTOS, HttpServletResponse response) throws IOException;

    /**
     * 发送邮件验证码
     * @param code 验证码
     * @return EmailVo
     */
    ToolEmailVo sendEmail(ToolVerificationCode code);

    /**
     * 验证
     * @param code 验证码
     */
    void validated(ToolVerificationCode code);
}
