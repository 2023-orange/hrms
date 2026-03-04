package com.sunten.hrms.pm.rest;

import com.sunten.hrms.fnd.service.FndUserService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.pm.domain.PmEmployeeSocialrelationsTemp;
import com.sunten.hrms.pm.dto.PmEmployeeSocialrelationsTempQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeSocialrelationsTempService;
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
 * 社会关系临时表 前端控制器
 * </p>
 *
 * @author xukai
 * @since 2021-11-24
 */
@RestController
@Api(tags = "社会关系临时表")
@RequestMapping("/api/pm/employeeSocialrelationsTemp")
public class PmEmployeeSocialrelationsTempController {
    private static final String ENTITY_NAME = "employeeSocialrelationsTemp";
    private final PmEmployeeSocialrelationsTempService pmEmployeeSocialrelationsTempService;
    private final FndUserService fndUserService;

    public PmEmployeeSocialrelationsTempController(PmEmployeeSocialrelationsTempService pmEmployeeSocialrelationsTempService, FndUserService fndUserService) {
        this.pmEmployeeSocialrelationsTempService = pmEmployeeSocialrelationsTempService;
        this.fndUserService = fndUserService;
    }

    @Log("新增社会关系临时表")
    @ApiOperation("新增社会关系临时表")
    @PostMapping
    public ResponseEntity create(@Validated @RequestBody PmEmployeeSocialrelationsTemp employeeSocialrelationsTemp) {
        if (!fndUserService.isActiveUser(employeeSocialrelationsTemp.getEmployee().getId())) {
            throw new BadRequestException("不是本人，无法新增");
        }
        return new ResponseEntity<>(pmEmployeeSocialrelationsTempService.insert(employeeSocialrelationsTemp), HttpStatus.CREATED);
    }

    @Log("删除社会关系临时表")
    @ApiOperation("删除社会关系临时表")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeeSocialrelationsTempService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该社会关系临时表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改社会关系临时表")
    @ApiOperation("修改社会关系临时表")
    @PutMapping
    public ResponseEntity update(@Validated(PmEmployeeSocialrelationsTemp.Update.class) @RequestBody PmEmployeeSocialrelationsTemp employeeSocialrelationsTemp) {
        if (!fndUserService.isActiveUser(employeeSocialrelationsTemp.getEmployee().getId())) {
            throw new BadRequestException("不是本人，无法修改");
        }
        pmEmployeeSocialrelationsTempService.update(employeeSocialrelationsTemp);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个社会关系临时表")
    @ApiOperation("获取单个社会关系临时表")
    @GetMapping(value = "/{id}")
    public ResponseEntity getEmployeeSocialrelationsTemp(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeeSocialrelationsTempService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询社会关系临时表（分页）")
    @ApiOperation("查询社会关系临时表（分页）")
    @GetMapping
    public ResponseEntity getEmployeeSocialrelationsTempPage(PmEmployeeSocialrelationsTempQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeeSocialrelationsTempService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询社会关系临时表（不分页）")
    @ApiOperation("查询社会关系临时表（不分页）")
    @GetMapping(value = "/noPaging")
    public ResponseEntity getEmployeeSocialrelationsTempNoPaging(PmEmployeeSocialrelationsTempQueryCriteria criteria) {
    return new ResponseEntity<>(pmEmployeeSocialrelationsTempService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出社会关系临时表数据")
    @ApiOperation("导出社会关系临时表数据")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, PmEmployeeSocialrelationsTempQueryCriteria criteria) throws IOException {
        pmEmployeeSocialrelationsTempService.download(pmEmployeeSocialrelationsTempService.listAll(criteria), response);
    }

    @Log("校核社会关系临时表")
    @ApiOperation("校核社会关系临时表")
    @PutMapping(value = "/check")
    @PreAuthorize("@el.check('employeeSocialrelationsTemp:check','employeeTemp:check')")
    public ResponseEntity check(@Validated @RequestBody PmEmployeeSocialrelationsTemp employeeSocialrelationsTemp) {
        pmEmployeeSocialrelationsTempService.check(employeeSocialrelationsTemp);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
