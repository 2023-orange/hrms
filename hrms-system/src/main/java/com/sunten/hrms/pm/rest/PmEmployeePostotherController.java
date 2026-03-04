package com.sunten.hrms.pm.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.pm.domain.PmEmployeePostother;
import com.sunten.hrms.pm.dto.PmEmployeePostotherQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeePostotherService;
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
import java.util.List;

/**
 * <p>
 * 工作外职务表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@RestController
@Api(tags = "工作外职务表")
@RequestMapping("/api/pm/employeePostother")
public class PmEmployeePostotherController {
    private static final String ENTITY_NAME = "employeePostother";
    private final PmEmployeePostotherService pmEmployeePostotherService;

    public PmEmployeePostotherController(PmEmployeePostotherService pmEmployeePostotherService) {
        this.pmEmployeePostotherService = pmEmployeePostotherService;
    }

    @Log("新增工作外职务表")
    @ApiOperation("新增工作外职务表")
    @PostMapping
    @PreAuthorize("@el.check('employeePostother:add','employee:add')")
    public ResponseEntity create(@Validated @RequestBody PmEmployeePostother employeePostother) {
        if (employeePostother.getId() == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " must be already have an ID");
        }
        return new ResponseEntity<>(pmEmployeePostotherService.insert(employeePostother), HttpStatus.CREATED);
    }

    @Log("删除工作外职务表")
    @ApiOperation("删除工作外职务表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeePostother:del','employee:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeePostotherService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该工作外职务表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改工作外职务表")
    @ApiOperation("修改工作外职务表")
    @PutMapping
    @PreAuthorize("@el.check('employeePostother:edit','employee:edit')")
    public ResponseEntity update(@Validated(PmEmployeePostother.Update.class) @RequestBody PmEmployeePostother employeePostother) {
        pmEmployeePostotherService.update(employeePostother);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个工作外职务表")
    @ApiOperation("获取单个工作外职务表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeePostother:list','employee:list')")
    public ResponseEntity getEmployeePostother(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeePostotherService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询工作外职务表（分页）")
    @ApiOperation("查询工作外职务表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeePostother:list','employee:list')")
    public ResponseEntity getEmployeePostotherPage(PmEmployeePostotherQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeePostotherService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询工作外职务表（不分页）")
    @ApiOperation("查询工作外职务表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeePostother:list','employee:list')")
    public ResponseEntity getEmployeePostotherNoPaging(PmEmployeePostotherQueryCriteria criteria) {
        return new ResponseEntity<>(pmEmployeePostotherService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出工作外职务表数据")
    @ApiOperation("导出工作外职务表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeePostother:list','employee:list')")
    public void download(HttpServletResponse response, PmEmployeePostotherQueryCriteria criteria) throws IOException {
        pmEmployeePostotherService.download(pmEmployeePostotherService.listAll(criteria), response);
    }

    @Log("批量编辑工作外职务表")
    @ApiOperation("批量编辑工作外职务表")
    @PutMapping(value = "/batchSave")
    @PreAuthorize("@el.check('employeePostother:add','employeePostother:edit','employee:add','employee:edit')")
    public ResponseEntity batchSave(@Validated @RequestBody List<PmEmployeePostother> employeePostothers) {
        if (employeePostothers == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot be null");
        }
        return new ResponseEntity<>(pmEmployeePostotherService.batchInsert(employeePostothers, null), HttpStatus.CREATED);
    }
}
