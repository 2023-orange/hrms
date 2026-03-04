package com.sunten.hrms.tool.rest;

import com.sunten.hrms.tool.domain.ToolVerificationCode;
import com.sunten.hrms.tool.dto.ToolEmailInterfaceDTO;
import com.sunten.hrms.tool.service.ToolEmailInterfaceService;
import com.sunten.hrms.tool.service.ToolVerificationCodeService;
import com.sunten.hrms.tool.vo.ToolEmailVo;
import com.sunten.hrms.utils.GlobalConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author batan
 * @since 2019-12-25
 */
@RestController
@Api(tags = "工具：验证码管理")
@RequestMapping("/api/tool/verificationCode")
public class ToolVerificationCodeController {
    private static final String ENTITY_NAME = "verificationCode";
    private final ToolVerificationCodeService toolVerificationCodeService;
    private final ToolEmailInterfaceService toolEmailInterfaceService;

    @Value("${sunten.system-name}")
    private String systemName;

    public ToolVerificationCodeController(ToolVerificationCodeService toolVerificationCodeService, ToolEmailInterfaceService toolEmailInterfaceService) {
        this.toolVerificationCodeService = toolVerificationCodeService;
        this.toolEmailInterfaceService = toolEmailInterfaceService;
    }

    @PostMapping(value = "/resetEmail")
    @ApiOperation("重置邮箱，发送验证码")
    public ResponseEntity resetEmail(@RequestBody ToolVerificationCode code) throws Exception {
        code.setScenes(GlobalConstant.RESET_MAIL);
        ToolEmailVo emailVo = toolVerificationCodeService.sendEmail(code);
        emailVo.setSubject("重置邮箱验证码 - " + systemName);
        toolEmailInterfaceService.sendAndSaveWithThrow(emailVo);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/email/resetPass")
    @ApiOperation("重置密码，发送验证码")
    public ResponseEntity resetPass(@RequestParam String email) throws Exception {
        ToolVerificationCode code = new ToolVerificationCode();
        code.setType("email");
        code.setValue(email);
        code.setScenes(GlobalConstant.RESET_MAIL);
        ToolEmailVo emailVo = toolVerificationCodeService.sendEmail(code);
        emailVo.setSubject("重置密码验证码 - " + systemName);
        toolEmailInterfaceService.sendAndSaveWithThrow(emailVo);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/validated")
    @ApiOperation("验证码验证")
    public ResponseEntity validated(ToolVerificationCode code) {
        toolVerificationCodeService.validated(code);
        return new ResponseEntity(HttpStatus.OK);
    }
}
