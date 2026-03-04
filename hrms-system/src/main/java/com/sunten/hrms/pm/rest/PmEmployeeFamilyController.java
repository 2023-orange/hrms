package com.sunten.hrms.pm.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.pm.domain.PmEmployeeFamily;
import com.sunten.hrms.pm.dto.PmEmployeeFamilyQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeFamilyService;
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
 * 家庭情况表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@RestController
@Api(tags = "家庭情况表")
@RequestMapping("/api/pm/employeeFamily")
public class PmEmployeeFamilyController {
    private static final String ENTITY_NAME = "employeeFamily";
    private final PmEmployeeFamilyService pmEmployeeFamilyService;

    public PmEmployeeFamilyController(PmEmployeeFamilyService pmEmployeeFamilyService) {
        this.pmEmployeeFamilyService = pmEmployeeFamilyService;
    }

    @Log("新增家庭情况表")
    @ApiOperation("新增家庭情况表")
    @PostMapping
    @PreAuthorize("@el.check('employeeFamily:add','employee:add')")
    public ResponseEntity create(@Validated @RequestBody PmEmployeeFamily employeeFamily) {
        if (employeeFamily.getId() == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " must be already have an ID");
        }
        return new ResponseEntity<>(pmEmployeeFamilyService.insert(employeeFamily), HttpStatus.CREATED);
    }

    @Log("删除家庭情况表")
    @ApiOperation("删除家庭情况表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeFamily:del','employee:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeeFamilyService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该家庭情况表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改家庭情况表")
    @ApiOperation("修改家庭情况表")
    @PutMapping
    @PreAuthorize("@el.check('employeeFamily:edit','employee:edit')")
    public ResponseEntity update(@Validated(PmEmployeeFamily.Update.class) @RequestBody PmEmployeeFamily employeeFamily) {
        pmEmployeeFamilyService.update(employeeFamily);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个家庭情况表")
    @ApiOperation("获取单个家庭情况表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeFamily:list','employee:list')")
    public ResponseEntity getEmployeeFamily(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeeFamilyService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询家庭情况表（分页）")
    @ApiOperation("查询家庭情况表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeFamily:list','employee:list')")
    public ResponseEntity getEmployeeFamilyPage(PmEmployeeFamilyQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeeFamilyService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询家庭情况表（不分页）")
    @ApiOperation("查询家庭情况表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeFamily:list','employee:list')")
    public ResponseEntity getEmployeeFamilyNoPaging(PmEmployeeFamilyQueryCriteria criteria) {
        return new ResponseEntity<>(pmEmployeeFamilyService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出家庭情况表数据")
    @ApiOperation("导出家庭情况表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeFamily:list','employee:list')")
    public void download(HttpServletResponse response, PmEmployeeFamilyQueryCriteria criteria) throws IOException {
        pmEmployeeFamilyService.download(pmEmployeeFamilyService.listAll(criteria), response);
    }

    @Log("批量编辑家庭情况表")
    @ApiOperation("批量编辑家庭情况表")
    @PutMapping(value = "/batchSave")
    @PreAuthorize("@el.check('employeeFamily:add','employee:add')")
    public ResponseEntity batchSave(@Validated @RequestBody List<PmEmployeeFamily> employeeFamilys) {
        if (employeeFamilys == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot be null");
        }
        return new ResponseEntity<>(pmEmployeeFamilyService.batchInsert(employeeFamilys, null), HttpStatus.CREATED);
    }

    @Log("获取子女列表")
    @ApiOperation("获取子女列表")
    @GetMapping(value = "/getChild/{employeeId}")
    public ResponseEntity getChild(@PathVariable Long employeeId) {
        return new ResponseEntity<>(pmEmployeeFamilyService.getChild(employeeId), HttpStatus.OK);
    }
}
