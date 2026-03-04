package com.sunten.hrms.fnd.rest;

import com.sunten.hrms.ac.dao.AcAttendanceRecordHistoryDao;
import com.sunten.hrms.ac.domain.AcAbnormalClockRecord;
import com.sunten.hrms.ac.domain.AcAttendanceRecordHistory;
import com.sunten.hrms.ac.domain.AcClockRecord;
import com.sunten.hrms.ac.dto.AcAttendanceRecordHistoryQueryCriteria;
import com.sunten.hrms.ac.service.AcAttendanceRecordHistoryService;
import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndAgencyService;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.fnd.vo.FndAgencyVo;
import com.sunten.hrms.security.service.JwtPermissionService;
import com.sunten.hrms.utils.DateUtil;
import com.sunten.hrms.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 代办事务
 * </p>
 *
 * @author liangjw
 * @since 2020-11-02
 */
@RestController
@Api(tags = "代办")
@RequestMapping("/api/fnd/agency")
public class FndAgencyController {
    private final FndAgencyService fndAgencyService;
    public FndAgencyController(FndAgencyService fndAgencyService) {
        this.fndAgencyService = fndAgencyService;
    }

    @ErrorLog("查询代办列表（个人、管理）")
    @ApiOperation("查询代办列表（个人、管理）")
    @GetMapping(value = "/getByAdminAndPerson")
    public ResponseEntity getByAdminAndPerson() {
        List<FndAgencyVo> fndAgencyVos = fndAgencyService.getList();
        List<FndAgencyVo> menuVos = (List<FndAgencyVo>) fndAgencyService.buildTree(fndAgencyVos).get("content");
        return new ResponseEntity<>(menuVos, HttpStatus.OK);
    }




}
