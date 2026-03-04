package com.sunten.hrms.pm.rest;

import com.sunten.hrms.annotation.AnonymousAccess;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.domain.PmEmployeeTemp;
import com.sunten.hrms.pm.dto.PmEmployeeTempQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeTempService;
import com.sunten.hrms.utils.SecurityUtils;
import com.sunten.hrms.utils.ThrowableUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 人员临时表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@RestController
@Api(tags = "人员临时表")
@RequestMapping("/api/pm/employeeTemp")
public class PmEmployeeTempController {
    private static final String ENTITY_NAME = "employeeTemp";
    private final PmEmployeeTempService pmEmployeeTempService;
    private final FndUserService fndUserService;

    public PmEmployeeTempController(PmEmployeeTempService pmEmployeeTempService, FndUserService fndUserService) {
        this.pmEmployeeTempService = pmEmployeeTempService;
        this.fndUserService = fndUserService;
    }

    @Log("新增人员临时表")
    @ApiOperation("新增人员临时表")
    @PostMapping
    @AnonymousAccess
    public ResponseEntity create(@Validated @RequestBody PmEmployeeTemp employeeTemp) {
        if (!fndUserService.isActiveUser(employeeTemp.getEmployee().getId())) {
            throw new BadRequestException("不是本人，无法新增人员临时信息");
        }
        return new ResponseEntity<>(pmEmployeeTempService.insert(employeeTemp), HttpStatus.CREATED);
    }

    @Log("删除人员临时表")
    @ApiOperation("删除人员临时表")
    @DeleteMapping(value = "/{id}")
    @AnonymousAccess
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeeTempService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该人员临时表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改人员临时表")
    @ApiOperation("修改人员临时表")
    @PutMapping
    @AnonymousAccess
    public ResponseEntity update(@Validated(PmEmployeeTemp.Update.class) @RequestBody PmEmployeeTemp employeeTemp) {
        if (!fndUserService.isActiveUser(employeeTemp.getEmployee().getId())) {
            throw new BadRequestException("不是本人，无法修改人员信息");
        }
        pmEmployeeTempService.update(employeeTemp);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个人员临时表")
    @ApiOperation("获取单个人员临时表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeTemp:list','employeeTemp:check')")
    public ResponseEntity getEmployeeTemp(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeeTempService.getByEmployeeId(id), HttpStatus.OK);
    }

    @ErrorLog("获取本人临时表")
    @ApiOperation("获取本人临时表")
    @GetMapping(value = "/myself")
    @AnonymousAccess
    public ResponseEntity getMyselfEmployeeTemp() {
        //  获取令牌中的人员信息
        FndUserDTO fndUserDTO = fndUserService.getByName(SecurityUtils.getUsername());
        if (fndUserDTO.getEmployee() == null || fndUserDTO.getEmployee().getId().equals(-1L)) {
            throw new InfoCheckWarningMessException("找不到登录人员对应用户");
        }
        return new ResponseEntity<>(pmEmployeeTempService.getByEmployeeId(fndUserDTO.getEmployee().getId()), HttpStatus.OK);
    }

    @ErrorLog("查询人员临时表（分页）")
    @ApiOperation("查询人员临时表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeTemp:list')")
    public ResponseEntity getEmployeeTempPage(PmEmployeeTempQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeeTempService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询人员临时表（不分页）")
    @ApiOperation("查询人员临时表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeTemp:list')")
    public ResponseEntity getEmployeeTempNoPaging(PmEmployeeTempQueryCriteria criteria) {
        return new ResponseEntity<>(pmEmployeeTempService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出人员临时表数据")
    @ApiOperation("导出人员临时表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeTemp:list')")
    public void download(HttpServletResponse response, PmEmployeeTempQueryCriteria criteria) throws IOException {
        pmEmployeeTempService.download(pmEmployeeTempService.listAll(criteria), response);
    }

    @Log("校核被修改的人员信息")
    @ApiOperation("校核被修改的人员信息")
    @PutMapping(value = "/check")
    @PreAuthorize("@el.check('employeeTemp:check')")
    public ResponseEntity checkEmployeeTemp(@Validated @RequestBody PmEmployeeTemp employeeTemp) {
        pmEmployeeTempService.updateCheckFlag(employeeTemp);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
