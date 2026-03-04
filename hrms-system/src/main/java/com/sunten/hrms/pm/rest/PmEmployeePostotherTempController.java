package com.sunten.hrms.pm.rest;

import com.sunten.hrms.fnd.service.FndUserService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.pm.domain.PmEmployeePostotherTemp;
import com.sunten.hrms.pm.dto.PmEmployeePostotherTempQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeePostotherTempService;
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
 * 工作外职务临时表 前端控制器
 * </p>
 *
 * @author xukai
 * @since 2021-11-24
 */
@RestController
@Api(tags = "工作外职务临时表")
@RequestMapping("/api/pm/employeePostotherTemp")
public class PmEmployeePostotherTempController {
    private static final String ENTITY_NAME = "employeePostotherTemp";
    private final PmEmployeePostotherTempService pmEmployeePostotherTempService;
    private final FndUserService fndUserService;

    public PmEmployeePostotherTempController(PmEmployeePostotherTempService pmEmployeePostotherTempService, FndUserService fndUserService) {
        this.pmEmployeePostotherTempService = pmEmployeePostotherTempService;
        this.fndUserService = fndUserService;
    }

    @Log("新增工作外职务临时表")
    @ApiOperation("新增工作外职务临时表")
    @PostMapping
    public ResponseEntity create(@Validated @RequestBody PmEmployeePostotherTemp employeePostotherTemp) {
        if (!fndUserService.isActiveUser(employeePostotherTemp.getEmployee().getId())) {
            throw new BadRequestException("不是本人，无法新增");
        }
        return new ResponseEntity<>(pmEmployeePostotherTempService.insert(employeePostotherTemp), HttpStatus.CREATED);
    }

    @Log("删除工作外职务临时表")
    @ApiOperation("删除工作外职务临时表")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeePostotherTempService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该工作外职务临时表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改工作外职务临时表")
    @ApiOperation("修改工作外职务临时表")
    @PutMapping
    public ResponseEntity update(@Validated(PmEmployeePostotherTemp.Update.class) @RequestBody PmEmployeePostotherTemp employeePostotherTemp) {
        if (!fndUserService.isActiveUser(employeePostotherTemp.getEmployee().getId())) {
            throw new BadRequestException("不是本人，无法修改");
        }
        pmEmployeePostotherTempService.update(employeePostotherTemp);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个工作外职务临时表")
    @ApiOperation("获取单个工作外职务临时表")
    @GetMapping(value = "/{id}")
    public ResponseEntity getEmployeePostotherTemp(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeePostotherTempService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询工作外职务临时表（分页）")
    @ApiOperation("查询工作外职务临时表（分页）")
    @GetMapping
    public ResponseEntity getEmployeePostotherTempPage(PmEmployeePostotherTempQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeePostotherTempService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询工作外职务临时表（不分页）")
    @ApiOperation("查询工作外职务临时表（不分页）")
    @GetMapping(value = "/noPaging")
    public ResponseEntity getEmployeePostotherTempNoPaging(PmEmployeePostotherTempQueryCriteria criteria) {
    return new ResponseEntity<>(pmEmployeePostotherTempService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出工作外职务临时表数据")
    @ApiOperation("导出工作外职务临时表数据")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, PmEmployeePostotherTempQueryCriteria criteria) throws IOException {
        pmEmployeePostotherTempService.download(pmEmployeePostotherTempService.listAll(criteria), response);
    }

    @Log("校核工作外职务临时表")
    @ApiOperation("校核工作外职务临时表")
    @PutMapping(value = "/check")
    @PreAuthorize("@el.check('employeePostotherTemp:check','employeeTemp:check')")
    public ResponseEntity check(@Validated @RequestBody PmEmployeePostotherTemp employeePostotherTemp) {
        pmEmployeePostotherTempService.check(employeePostotherTemp);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
