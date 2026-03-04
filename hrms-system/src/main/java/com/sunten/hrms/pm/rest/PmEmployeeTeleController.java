package com.sunten.hrms.pm.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.pm.domain.PmEmployeeTele;
import com.sunten.hrms.pm.dto.PmEmployeeTeleQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeTeleService;
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
 * 办公电话表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@RestController
@Api(tags = "办公电话表")
@RequestMapping("/api/pm/employeeTele")
public class PmEmployeeTeleController {
    private static final String ENTITY_NAME = "employeeTele";
    private final PmEmployeeTeleService pmEmployeeTeleService;

    public PmEmployeeTeleController(PmEmployeeTeleService pmEmployeeTeleService) {
        this.pmEmployeeTeleService = pmEmployeeTeleService;
    }

    @Log("新增办公电话表")
    @ApiOperation("新增办公电话表")
    @PostMapping
    @PreAuthorize("@el.check('employeeTele:add','employee:add')")
    public ResponseEntity create(@Validated @RequestBody PmEmployeeTele employeeTele) {
        if (employeeTele.getId() == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " must be already have an ID");
        }
        return new ResponseEntity<>(pmEmployeeTeleService.insert(employeeTele), HttpStatus.CREATED);
    }

    @Log("删除办公电话表")
    @ApiOperation("删除办公电话表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeTele:del','employee:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeeTeleService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该办公电话表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改办公电话表")
    @ApiOperation("修改办公电话表")
    @PutMapping
    @PreAuthorize("@el.check('employeeTele:edit','employee:edit')")
    public ResponseEntity update(@Validated(PmEmployeeTele.Update.class) @RequestBody PmEmployeeTele employeeTele) {
        pmEmployeeTeleService.update(employeeTele);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个办公电话表")
    @ApiOperation("获取单个办公电话表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeTele:list','employee:list')")
    public ResponseEntity getEmployeeTele(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeeTeleService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询办公电话表（分页）")
    @ApiOperation("查询办公电话表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeTele:list','employee:list')")
    public ResponseEntity getEmployeeTelePage(PmEmployeeTeleQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeeTeleService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询办公电话表（不分页）")
    @ApiOperation("查询办公电话表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeTele:list','employee:list')")
    public ResponseEntity getEmployeeTeleNoPaging(PmEmployeeTeleQueryCriteria criteria) {
        return new ResponseEntity<>(pmEmployeeTeleService.listAll(criteria), HttpStatus.OK);
    }


    @ErrorLog("导出办公电话表数据")
    @ApiOperation("导出办公电话表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeTele:list','employee:list')")
    public void download(HttpServletResponse response, PmEmployeeTeleQueryCriteria criteria) throws IOException {
        pmEmployeeTeleService.download(pmEmployeeTeleService.listAll(criteria), response);
    }

    @Log("批量编辑办公电话表")
    @ApiOperation("批量编辑办公电话表")
    @PutMapping(value = "/batchSave")
    @PreAuthorize("@el.check('employeeTele:add','employeeTele:edit','employee:add','employee:edit')")
    public ResponseEntity batchSave(@Validated @RequestBody List<PmEmployeeTele> employeeTeles) {
        if (employeeTeles == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot be null");
        }
        return new ResponseEntity<>(pmEmployeeTeleService.batchInsert(employeeTeles, null), HttpStatus.CREATED);
    }
}
