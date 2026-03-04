package com.sunten.hrms.pm.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.pm.domain.PmEmployeeTitle;
import com.sunten.hrms.pm.dto.PmEmployeeTitleQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeTitleService;
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
 * 职称情况表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@RestController
@Api(tags = "职称情况表")
@RequestMapping("/api/pm/employeeTitle")
public class PmEmployeeTitleController {
    private static final String ENTITY_NAME = "employeeTitle";
    private final PmEmployeeTitleService pmEmployeeTitleService;

    public PmEmployeeTitleController(PmEmployeeTitleService pmEmployeeTitleService) {
        this.pmEmployeeTitleService = pmEmployeeTitleService;
    }

    @Log("新增职称情况表")
    @ApiOperation("新增职称情况表")
    @PostMapping
    @PreAuthorize("@el.check('employeeTitle:add','employee:add')")
    public ResponseEntity create(@Validated @RequestBody PmEmployeeTitle employeeTitle) {
        if (employeeTitle.getId() == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " must be already have an ID");
        }
        return new ResponseEntity<>(pmEmployeeTitleService.insert(employeeTitle), HttpStatus.CREATED);
    }

    @Log("删除职称情况表")
    @ApiOperation("删除职称情况表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeTitle:del','employee:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeeTitleService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该职称情况表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改职称情况表")
    @ApiOperation("修改职称情况表")
    @PutMapping
    @PreAuthorize("@el.check('employeeTitle:edit','employee:edit')")
    public ResponseEntity update(@Validated(PmEmployeeTitle.Update.class) @RequestBody PmEmployeeTitle employeeTitle) {
        pmEmployeeTitleService.update(employeeTitle);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个职称情况表")
    @ApiOperation("获取单个职称情况表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeTitle:list','employee:list')")
    public ResponseEntity getEmployeeTitle(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeeTitleService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询职称情况表（分页）")
    @ApiOperation("查询职称情况表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeTitle:list','employee:list')")
    public ResponseEntity getEmployeeTitlePage(PmEmployeeTitleQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeeTitleService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询职称情况表（不分页）")
    @ApiOperation("查询职称情况表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeTitle:list','employee:list')")
    public ResponseEntity getEmployeeTitleNoPaging(PmEmployeeTitleQueryCriteria criteria) {
        return new ResponseEntity<>(pmEmployeeTitleService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出职称情况表数据")
    @ApiOperation("导出职称情况表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeTitle:list','employee:list')")
    public void download(HttpServletResponse response, PmEmployeeTitleQueryCriteria criteria) throws IOException {
        pmEmployeeTitleService.download(pmEmployeeTitleService.listAll(criteria), response);
    }

    @Log("批量编辑职称情况表")
    @ApiOperation("批量编辑职称情况表")
    @PutMapping(value = "/batchSave")
    @PreAuthorize("@el.check('employeeTitle:add','employeeTitle:edit','employee:add','employee:edit')")
    public ResponseEntity batchSave(@Validated @RequestBody List<PmEmployeeTitle> employeeTitles) {
        if (employeeTitles == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot be null");
        }
        return new ResponseEntity<>(pmEmployeeTitleService.batchInsert(employeeTitles, null), HttpStatus.CREATED);
    }
}
