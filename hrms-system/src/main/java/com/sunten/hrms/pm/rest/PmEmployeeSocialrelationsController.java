package com.sunten.hrms.pm.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.pm.domain.PmEmployeeSocialrelations;
import com.sunten.hrms.pm.dto.PmEmployeeSocialrelationsQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeSocialrelationsService;
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
 * 社会关系表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@RestController
@Api(tags = "社会关系表")
@RequestMapping("/api/pm/employeeSocialrelations")
public class PmEmployeeSocialrelationsController {
    private static final String ENTITY_NAME = "employeeSocialrelations";
    private final PmEmployeeSocialrelationsService pmEmployeeSocialrelationsService;

    public PmEmployeeSocialrelationsController(PmEmployeeSocialrelationsService pmEmployeeSocialrelationsService) {
        this.pmEmployeeSocialrelationsService = pmEmployeeSocialrelationsService;
    }

    @Log("新增社会关系表")
    @ApiOperation("新增社会关系表")
    @PostMapping
    @PreAuthorize("@el.check('employeeSocialrelations:add','employee:add')")
    public ResponseEntity create(@Validated @RequestBody PmEmployeeSocialrelations employeeSocialrelations) {
        if (employeeSocialrelations.getId() == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " must be already have an ID");
        }
        return new ResponseEntity<>(pmEmployeeSocialrelationsService.insert(employeeSocialrelations), HttpStatus.CREATED);
    }

    @Log("删除社会关系表")
    @ApiOperation("删除社会关系表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeSocialrelations:del','employee:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeeSocialrelationsService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该社会关系表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改社会关系表")
    @ApiOperation("修改社会关系表")
    @PutMapping
    @PreAuthorize("@el.check('employeeSocialrelations:edit','employee:edit')")
    public ResponseEntity update(@Validated(PmEmployeeSocialrelations.Update.class) @RequestBody PmEmployeeSocialrelations employeeSocialrelations) {
        pmEmployeeSocialrelationsService.update(employeeSocialrelations);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个社会关系表")
    @ApiOperation("获取单个社会关系表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeSocialrelations:list','employee:list')")
    public ResponseEntity getEmployeeSocialrelations(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeeSocialrelationsService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询社会关系表（分页）")
    @ApiOperation("查询社会关系表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeSocialrelations:list','employee:list')")
    public ResponseEntity getEmployeeSocialrelationsPage(PmEmployeeSocialrelationsQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeeSocialrelationsService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询社会关系表（不分页）")
    @ApiOperation("查询社会关系表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeSocialrelations:list','employee:list')")
    public ResponseEntity getEmployeeSocialrelationsNoPaging(PmEmployeeSocialrelationsQueryCriteria criteria) {
        return new ResponseEntity<>(pmEmployeeSocialrelationsService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出社会关系表数据")
    @ApiOperation("导出社会关系表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeSocialrelations:list','employee:list')")
    public void download(HttpServletResponse response, PmEmployeeSocialrelationsQueryCriteria criteria) throws IOException {
        pmEmployeeSocialrelationsService.download(pmEmployeeSocialrelationsService.listAll(criteria), response);
    }

    @Log("批量编辑社会关系表")
    @ApiOperation("批量编辑社会关系表")
    @PutMapping(value = "/batchSave")
    @PreAuthorize("@el.check('employeeSocialrelations:add','employeeSocialrelations:edit','employee:add','employee:edit')")
    public ResponseEntity batchSave(@Validated @RequestBody List<PmEmployeeSocialrelations> employeeSocialrelations) {
        if (employeeSocialrelations == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot be null");
        }
        return new ResponseEntity<>(pmEmployeeSocialrelationsService.batchInsert(employeeSocialrelations, null), HttpStatus.CREATED);
    }
}
