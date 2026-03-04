package com.sunten.hrms.pm.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.pm.domain.PmEmployeeVocational;
import com.sunten.hrms.pm.dto.PmEmployeeVocationalQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeVocationalService;
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
 * 职业资格表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@RestController
@Api(tags = "职业资格表")
@RequestMapping("/api/pm/employeeVocational")
public class PmEmployeeVocationalController {
    private static final String ENTITY_NAME = "employeeVocational";
    private final PmEmployeeVocationalService pmEmployeeVocationalService;

    public PmEmployeeVocationalController(PmEmployeeVocationalService pmEmployeeVocationalService) {
        this.pmEmployeeVocationalService = pmEmployeeVocationalService;
    }

    @Log("新增职业资格表")
    @ApiOperation("新增职业资格表")
    @PostMapping
    @PreAuthorize("@el.check('employeeVocational:add','employee:add')")
    public ResponseEntity create(@Validated @RequestBody PmEmployeeVocational employeeVocational) {
        if (employeeVocational.getId() == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " must be already have an ID");
        }
        return new ResponseEntity<>(pmEmployeeVocationalService.insert(employeeVocational), HttpStatus.CREATED);
    }

    @Log("删除职业资格表")
    @ApiOperation("删除职业资格表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeVocational:del','employee:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeeVocationalService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该职业资格表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改职业资格表")
    @ApiOperation("修改职业资格表")
    @PutMapping
    @PreAuthorize("@el.check('employeeVocational:edit','employee:edit')")
    public ResponseEntity update(@Validated(PmEmployeeVocational.Update.class) @RequestBody PmEmployeeVocational employeeVocational) {
        pmEmployeeVocationalService.update(employeeVocational);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个职业资格表")
    @ApiOperation("获取单个职业资格表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeVocational:list','employee:list')")
    public ResponseEntity getEmployeeVocational(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeeVocationalService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询职业资格表（分页）")
    @ApiOperation("查询职业资格表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeVocational:list','employee:list')")
    public ResponseEntity getEmployeeVocationalPage(PmEmployeeVocationalQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeeVocationalService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询职业资格表（不分页）")
    @ApiOperation("查询职业资格表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeVocational:list','employee:list')")
    public ResponseEntity getEmployeeVocationalNoPaging(PmEmployeeVocationalQueryCriteria criteria) {
        return new ResponseEntity<>(pmEmployeeVocationalService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出职业资格表数据")
    @ApiOperation("导出职业资格表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeVocational:list','employee:list')")
    public void download(HttpServletResponse response, PmEmployeeVocationalQueryCriteria criteria) throws IOException {
        pmEmployeeVocationalService.download(pmEmployeeVocationalService.listAll(criteria), response);
    }

    @Log("批量编辑职业资格表")
    @ApiOperation("批量编辑职业资格表")
    @PutMapping(value = "/batchSave")
    @PreAuthorize("@el.check('employeeVocational:add','employee:add')")
    public ResponseEntity batchSave(@Validated @RequestBody List<PmEmployeeVocational> employeeVocationals) {
        if (employeeVocationals == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot be null");
        }
        return new ResponseEntity<>(pmEmployeeVocationalService.batchInsert(employeeVocationals, null), HttpStatus.CREATED);
    }
}
