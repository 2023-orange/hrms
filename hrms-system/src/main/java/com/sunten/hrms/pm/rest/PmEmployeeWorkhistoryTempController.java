package com.sunten.hrms.pm.rest;

import com.sunten.hrms.fnd.service.FndUserService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.pm.domain.PmEmployeeWorkhistoryTemp;
import com.sunten.hrms.pm.dto.PmEmployeeWorkhistoryTempQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeWorkhistoryTempService;
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
 * 工作经历临时表 前端控制器
 * </p>
 *
 * @author xukai
 * @since 2021-11-24
 */
@RestController
@Api(tags = "工作经历临时表")
@RequestMapping("/api/pm/employeeWorkhistoryTemp")
public class PmEmployeeWorkhistoryTempController {
    private static final String ENTITY_NAME = "employeeWorkhistoryTemp";
    private final PmEmployeeWorkhistoryTempService pmEmployeeWorkhistoryTempService;
    private final FndUserService fndUserService;

    public PmEmployeeWorkhistoryTempController(PmEmployeeWorkhistoryTempService pmEmployeeWorkhistoryTempService, FndUserService fndUserService) {
        this.pmEmployeeWorkhistoryTempService = pmEmployeeWorkhistoryTempService;
        this.fndUserService = fndUserService;
    }

    @Log("新增工作经历临时表")
    @ApiOperation("新增工作经历临时表")
    @PostMapping
    public ResponseEntity create(@Validated @RequestBody PmEmployeeWorkhistoryTemp employeeWorkhistoryTemp) {
        if (!fndUserService.isActiveUser(employeeWorkhistoryTemp.getEmployee().getId())) {
            throw new BadRequestException("不是本人，无法新增");
        }
        return new ResponseEntity<>(pmEmployeeWorkhistoryTempService.insert(employeeWorkhistoryTemp), HttpStatus.CREATED);
    }

    @Log("删除工作经历临时表")
    @ApiOperation("删除工作经历临时表")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeeWorkhistoryTempService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该工作经历临时表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改工作经历临时表")
    @ApiOperation("修改工作经历临时表")
    @PutMapping
    public ResponseEntity update(@Validated(PmEmployeeWorkhistoryTemp.Update.class) @RequestBody PmEmployeeWorkhistoryTemp employeeWorkhistoryTemp) {
        if (!fndUserService.isActiveUser(employeeWorkhistoryTemp.getEmployee().getId())) {
            throw new BadRequestException("不是本人，无法修改");
        }
        pmEmployeeWorkhistoryTempService.update(employeeWorkhistoryTemp);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个工作经历临时表")
    @ApiOperation("获取单个工作经历临时表")
    @GetMapping(value = "/{id}")
    public ResponseEntity getEmployeeWorkhistoryTemp(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeeWorkhistoryTempService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询工作经历临时表（分页）")
    @ApiOperation("查询工作经历临时表（分页）")
    @GetMapping
    public ResponseEntity getEmployeeWorkhistoryTempPage(PmEmployeeWorkhistoryTempQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeeWorkhistoryTempService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询工作经历临时表（不分页）")
    @ApiOperation("查询工作经历临时表（不分页）")
    @GetMapping(value = "/noPaging")
    public ResponseEntity getEmployeeWorkhistoryTempNoPaging(PmEmployeeWorkhistoryTempQueryCriteria criteria) {
    return new ResponseEntity<>(pmEmployeeWorkhistoryTempService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出工作经历临时表数据")
    @ApiOperation("导出工作经历临时表数据")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, PmEmployeeWorkhistoryTempQueryCriteria criteria) throws IOException {
        pmEmployeeWorkhistoryTempService.download(pmEmployeeWorkhistoryTempService.listAll(criteria), response);
    }

    @Log("校核工作经历临时表")
    @ApiOperation("校核工作经历临时表")
    @PutMapping(value = "/check")
    @PreAuthorize("@el.check('employeeSocialrelationsTemp:check','employeeTemp:check')")
    public ResponseEntity check(@Validated @RequestBody PmEmployeeWorkhistoryTemp employeeWorkhistoryTemp) {
        pmEmployeeWorkhistoryTempService.check(employeeWorkhistoryTemp);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
