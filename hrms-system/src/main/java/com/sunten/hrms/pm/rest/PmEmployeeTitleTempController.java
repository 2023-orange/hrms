package com.sunten.hrms.pm.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.pm.domain.PmEmployeeTitleTemp;
import com.sunten.hrms.pm.dto.PmEmployeeTitleTempQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeTitleTempService;
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

/**
 * <p>
 * 职称情况临时表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@RestController
@Api(tags = "职称情况临时表")
@RequestMapping("/api/pm/employeeTitleTemp")
public class PmEmployeeTitleTempController {
    private static final String ENTITY_NAME = "employeeTitleTemp";
    private final PmEmployeeTitleTempService pmEmployeeTitleTempService;

    public PmEmployeeTitleTempController(PmEmployeeTitleTempService pmEmployeeTitleTempService) {
        this.pmEmployeeTitleTempService = pmEmployeeTitleTempService;
    }

    @Log("新增职称情况临时表")
    @ApiOperation("新增职称情况临时表")
    @PostMapping
    public ResponseEntity create(@Validated @RequestBody PmEmployeeTitleTemp employeeTitleTemp) {
        if (employeeTitleTemp.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(pmEmployeeTitleTempService.insert(employeeTitleTemp), HttpStatus.CREATED);
    }

    @Log("删除职称情况临时表")
    @ApiOperation("删除职称情况临时表")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeeTitleTempService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该职称情况临时表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改职称情况临时表")
    @ApiOperation("修改职称情况临时表")
    @PutMapping
    public ResponseEntity update(@Validated(PmEmployeeTitleTemp.Update.class) @RequestBody PmEmployeeTitleTemp employeeTitleTemp) {
        pmEmployeeTitleTempService.update(employeeTitleTemp);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个职称情况临时表")
    @ApiOperation("获取单个职称情况临时表")
    @GetMapping(value = "/{id}")
    public ResponseEntity getEmployeeTitleTemp(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeeTitleTempService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询职称情况临时表（分页）")
    @ApiOperation("查询职称情况临时表（分页）")
    @GetMapping
    public ResponseEntity getEmployeeTitleTempPage(PmEmployeeTitleTempQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeeTitleTempService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询职称情况临时表（不分页）")
    @ApiOperation("查询职称情况临时表（不分页）")
    @GetMapping(value = "/noPaging")
    public ResponseEntity getEmployeeTitleTempNoPaging(PmEmployeeTitleTempQueryCriteria criteria) {
        return new ResponseEntity<>(pmEmployeeTitleTempService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出职称情况临时表数据")
    @ApiOperation("导出职称情况临时表数据")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, PmEmployeeTitleTempQueryCriteria criteria) throws IOException {
        pmEmployeeTitleTempService.download(pmEmployeeTitleTempService.listAll(criteria), response);
    }

    @Log("校核职称情况临时表")
    @ApiOperation("校核职称情况临时表")
    @PutMapping(value = "/check")
    @PreAuthorize("@el.check('employeeTitleTemp:check','employeeTemp:check')")
    public ResponseEntity check(@Validated @RequestBody PmEmployeeTitleTemp employeeTitleTemp) {
        pmEmployeeTitleTempService.check(employeeTitleTemp);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
