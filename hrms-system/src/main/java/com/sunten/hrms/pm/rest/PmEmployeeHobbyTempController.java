package com.sunten.hrms.pm.rest;

import com.sunten.hrms.annotation.AnonymousAccess;
import com.sunten.hrms.fnd.service.FndUserService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.pm.domain.PmEmployeeHobbyTemp;
import com.sunten.hrms.pm.dto.PmEmployeeHobbyTempQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeHobbyTempService;
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
 * 技术爱好临时表 前端控制器
 * </p>
 *
 * @author xukai
 * @since 2021-11-24
 */
@RestController
@Api(tags = "技术爱好临时表")
@RequestMapping("/api/pm/employeeHobbyTemp")
public class PmEmployeeHobbyTempController {
    private static final String ENTITY_NAME = "employeeHobbyTemp";
    private final PmEmployeeHobbyTempService pmEmployeeHobbyTempService;
    private final FndUserService fndUserService;

    public PmEmployeeHobbyTempController(PmEmployeeHobbyTempService pmEmployeeHobbyTempService, FndUserService fndUserService) {
        this.pmEmployeeHobbyTempService = pmEmployeeHobbyTempService;
        this.fndUserService = fndUserService;
    }

    @Log("新增技术爱好临时表")
    @ApiOperation("新增技术爱好临时表")
    @PostMapping
    @AnonymousAccess
    public ResponseEntity create(@Validated @RequestBody PmEmployeeHobbyTemp employeeHobbyTemp) {
        if (!fndUserService.isActiveUser(employeeHobbyTemp.getEmployee().getId())) {
            throw new BadRequestException("不是本人，无法新增");
        }
        return new ResponseEntity<>(pmEmployeeHobbyTempService.insert(employeeHobbyTemp), HttpStatus.CREATED);
    }

    @Log("删除技术爱好临时表")
    @ApiOperation("删除技术爱好临时表")
    @DeleteMapping(value = "/{id}")
    @AnonymousAccess
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeeHobbyTempService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该技术爱好临时表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改技术爱好临时表")
    @ApiOperation("修改技术爱好临时表")
    @PutMapping
    @AnonymousAccess
    public ResponseEntity update(@Validated(PmEmployeeHobbyTemp.Update.class) @RequestBody PmEmployeeHobbyTemp employeeHobbyTemp) {
        if (!fndUserService.isActiveUser(employeeHobbyTemp.getEmployee().getId())) {
            throw new BadRequestException("不是本人，无法修改");
        }
        pmEmployeeHobbyTempService.update(employeeHobbyTemp);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个技术爱好临时表")
    @ApiOperation("获取单个技术爱好临时表")
    @GetMapping(value = "/{id}")
    @AnonymousAccess
    public ResponseEntity getEmployeeHobbyTemp(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeeHobbyTempService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询技术爱好临时表（分页）")
    @ApiOperation("查询技术爱好临时表（分页）")
    @GetMapping
    @AnonymousAccess
    public ResponseEntity getEmployeeHobbyTempPage(PmEmployeeHobbyTempQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeeHobbyTempService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询技术爱好临时表（不分页）")
    @ApiOperation("查询技术爱好临时表（不分页）")
    @GetMapping(value = "/noPaging")
    @AnonymousAccess
    public ResponseEntity getEmployeeHobbyTempNoPaging(PmEmployeeHobbyTempQueryCriteria criteria) {
    return new ResponseEntity<>(pmEmployeeHobbyTempService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出技术爱好临时表数据")
    @ApiOperation("导出技术爱好临时表数据")
    @GetMapping(value = "/download")
    @AnonymousAccess
    public void download(HttpServletResponse response, PmEmployeeHobbyTempQueryCriteria criteria) throws IOException {
        pmEmployeeHobbyTempService.download(pmEmployeeHobbyTempService.listAll(criteria), response);
    }

    @Log("校核技术爱好临时表")
    @ApiOperation("校核技术爱好临时表")
    @PutMapping(value = "/check")
    @PreAuthorize("@el.check('employeeHobbyTemp:check','employeeTemp:check')")
    public ResponseEntity check(@Validated @RequestBody PmEmployeeHobbyTemp employeeHobbyTemp) {
        pmEmployeeHobbyTempService.check(employeeHobbyTemp);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
