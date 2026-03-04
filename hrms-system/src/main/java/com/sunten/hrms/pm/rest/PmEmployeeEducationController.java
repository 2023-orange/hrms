package com.sunten.hrms.pm.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.pm.domain.PmEmployeeEducation;
import com.sunten.hrms.pm.dto.PmEmployeeEducationQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeEducationService;
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
 * 教育信息表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@RestController
@Api(tags = "教育信息表")
@RequestMapping("/api/pm/employeeEducation")
public class PmEmployeeEducationController {
    private static final String ENTITY_NAME = "employeeEducation";
    private final PmEmployeeEducationService pmEmployeeEducationService;

    public PmEmployeeEducationController(PmEmployeeEducationService pmEmployeeEducationService) {
        this.pmEmployeeEducationService = pmEmployeeEducationService;
    }

    @Log("新增教育信息表")
    @ApiOperation("新增教育信息表")
    @PostMapping
    @PreAuthorize("@el.check('employeeEducation:add','employee:add')")
    public ResponseEntity create(@Validated @RequestBody PmEmployeeEducation employeeEducation) {
        if (employeeEducation.getId() == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " must be already have an ID");
        }
        return new ResponseEntity<>(pmEmployeeEducationService.insert(employeeEducation), HttpStatus.CREATED);
    }

    @Log("删除教育信息表")
    @ApiOperation("删除教育信息表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeEducation:del','employee:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeeEducationService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该教育信息表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改教育信息表")
    @ApiOperation("修改教育信息表")
    @PutMapping
    @PreAuthorize("@el.check('employeeEducation:edit','employee:edit')")
    public ResponseEntity update(@Validated(PmEmployeeEducation.Update.class) @RequestBody PmEmployeeEducation employeeEducation) {
        pmEmployeeEducationService.update(employeeEducation);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个教育信息表")
    @ApiOperation("获取单个教育信息表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeEducation:list','employee:list')")
    public ResponseEntity getEmployeeEducation(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeeEducationService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询教育信息表（分页）")
    @ApiOperation("查询教育信息表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeEducation:list','employee:list')")
    public ResponseEntity getEmployeeEducationPage(PmEmployeeEducationQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeeEducationService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询教育信息表（不分页）")
    @ApiOperation("查询教育信息表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeEducation:list','employee:list')")
    public ResponseEntity getEmployeeEducationNoPaging(PmEmployeeEducationQueryCriteria criteria) {
        return new ResponseEntity<>(pmEmployeeEducationService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出教育信息表数据")
    @ApiOperation("导出教育信息表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeEducation:list','employee:list')")
    public void download(HttpServletResponse response, PmEmployeeEducationQueryCriteria criteria) throws IOException {
        pmEmployeeEducationService.download(pmEmployeeEducationService.listAll(criteria), response);
    }

    @Log("批量编辑教育信息表")
    @ApiOperation("批量编辑教育信息表")
    @PutMapping(value = "/batchSave")
    @PreAuthorize("@el.check('employeeEducation:add','employeeEducation:edit','employee:add','employee:edit')")
    public ResponseEntity batchSave(@Validated @RequestBody List<PmEmployeeEducation> employeeEducations) {
        if (employeeEducations == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot be null");
        }
        return new ResponseEntity<>(pmEmployeeEducationService.batchInsert(employeeEducations, null), HttpStatus.CREATED);
    }
}
