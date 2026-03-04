package com.sunten.hrms.pm.rest;

import com.sunten.hrms.annotation.AnonymousAccess;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.domain.PmEmployeeEducationTemp;
import com.sunten.hrms.pm.dto.PmEmployeeEducationTempQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeEducationTempService;
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
 * 教育信息临时表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@RestController
@Api(tags = "教育信息临时表")
@RequestMapping("/api/pm/employeeEducationTemp")
public class PmEmployeeEducationTempController {
    private static final String ENTITY_NAME = "employeeEducationTemp";
    private final PmEmployeeEducationTempService pmEmployeeEducationTempService;
    private final FndUserService fndUserService;

    public PmEmployeeEducationTempController(PmEmployeeEducationTempService pmEmployeeEducationTempService, FndUserService fndUserService) {
        this.pmEmployeeEducationTempService = pmEmployeeEducationTempService;
        this.fndUserService = fndUserService;
    }

    @Log("新增教育信息临时表")
    @ApiOperation("新增教育信息临时表")
    @PostMapping
    @AnonymousAccess
    public ResponseEntity create(@Validated @RequestBody PmEmployeeEducationTemp employeeEducationTemp) {
        if (!fndUserService.isActiveUser(employeeEducationTemp.getEmployee().getId())) {
            throw new BadRequestException("不是本人，无法新增");
        }
        return new ResponseEntity<>(pmEmployeeEducationTempService.insert(employeeEducationTemp), HttpStatus.CREATED);
    }

    @Log("删除教育信息临时表")
    @ApiOperation("删除教育信息临时表")
    @DeleteMapping(value = "/{id}")
    @AnonymousAccess
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeeEducationTempService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该教育信息临时表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改教育信息临时表")
    @ApiOperation("修改教育信息临时表")
    @PutMapping
    @AnonymousAccess
    public ResponseEntity update(@Validated(PmEmployeeEducationTemp.Update.class) @RequestBody PmEmployeeEducationTemp employeeEducationTemp) {
        if (!fndUserService.isActiveUser(employeeEducationTemp.getEmployee().getId())) {
            throw new BadRequestException("不是本人，无法修改此信息");
        }
        pmEmployeeEducationTempService.update(employeeEducationTemp);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个教育信息临时表")
    @ApiOperation("获取单个教育信息临时表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeEducationTemp:list')")
    public ResponseEntity getEmployeeEducationTemp(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeeEducationTempService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询教育信息临时表（分页）")
    @ApiOperation("查询教育信息临时表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeEducationTemp:list','employeeTemp:list')")
    public ResponseEntity getEmployeeEducationTempPage(PmEmployeeEducationTempQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeeEducationTempService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询教育信息临时表（不分页）")
    @ApiOperation("查询教育信息临时表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeEducationTemp:list','employeeTemp:list')")
    public ResponseEntity getEmployeeEducationTempNoPaging(PmEmployeeEducationTempQueryCriteria criteria) {
        return new ResponseEntity<>(pmEmployeeEducationTempService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出教育信息临时表数据")
    @ApiOperation("导出教育信息临时表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeEducationTemp:list','employeeTemp:list')")
    public void download(HttpServletResponse response, PmEmployeeEducationTempQueryCriteria criteria) throws IOException {
        pmEmployeeEducationTempService.download(pmEmployeeEducationTempService.listAll(criteria), response);
    }

    @Log("校核教育信息临时表")
    @ApiOperation("校核教育信息临时表")
    @PutMapping(value = "/check")
    @PreAuthorize("@el.check('employeeEducationTemp:check','employeeTemp:check')")
    public ResponseEntity check(@Validated @RequestBody PmEmployeeEducationTemp employeeEducationTemp) {
        pmEmployeeEducationTempService.check(employeeEducationTemp);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
