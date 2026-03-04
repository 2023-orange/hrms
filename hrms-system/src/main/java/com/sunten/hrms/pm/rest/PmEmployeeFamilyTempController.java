package com.sunten.hrms.pm.rest;

import com.sunten.hrms.annotation.AnonymousAccess;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.domain.PmEmployeeFamilyTemp;
import com.sunten.hrms.pm.dto.PmEmployeeFamilyTempQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeFamilyTempService;
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
 * 前端控制器
 * </p>
 *
 * @author xukai
 * @since 2020-08-25
 */
@RestController
@Api(tags = "家庭情况临时表")
@RequestMapping("/api/pm/employeeFamilyTemp")
public class PmEmployeeFamilyTempController {
    private static final String ENTITY_NAME = "employeeFamilyTemp";
    private final PmEmployeeFamilyTempService pmEmployeeFamilyTempService;
    private final FndUserService fndUserService;

    public PmEmployeeFamilyTempController(PmEmployeeFamilyTempService pmEmployeeFamilyTempService, FndUserService fndUserService) {
        this.pmEmployeeFamilyTempService = pmEmployeeFamilyTempService;
        this.fndUserService = fndUserService;
    }

    @Log("新增EmployeeFamilyTemp")
    @ApiOperation("新增EmployeeFamilyTemp")
    @PostMapping
    @AnonymousAccess
    public ResponseEntity create(@Validated @RequestBody PmEmployeeFamilyTemp employeeFamilyTemp) {
        if (!fndUserService.isActiveUser(employeeFamilyTemp.getEmployee().getId())) {
            throw new BadRequestException("不是本人，无法新增");
        }
        return new ResponseEntity<>(pmEmployeeFamilyTempService.insert(employeeFamilyTemp), HttpStatus.CREATED);
    }

    @Log("删除EmployeeFamilyTemp")
    @ApiOperation("删除EmployeeFamilyTemp")
    @DeleteMapping(value = "/{id}")
    @AnonymousAccess
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeeFamilyTempService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该EmployeeFamilyTemp存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改EmployeeFamilyTemp")
    @ApiOperation("修改EmployeeFamilyTemp")
    @PutMapping
    @AnonymousAccess
    public ResponseEntity update(@Validated(PmEmployeeFamilyTemp.Update.class) @RequestBody PmEmployeeFamilyTemp employeeFamilyTemp) {
        if (!fndUserService.isActiveUser(employeeFamilyTemp.getEmployee().getId())) {
            throw new BadRequestException("不是本人，保存修改");
        }
        pmEmployeeFamilyTempService.update(employeeFamilyTemp);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个EmployeeFamilyTemp")
    @ApiOperation("获取单个EmployeeFamilyTemp")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeFamilyTemp:list')")
    public ResponseEntity getEmployeeFamilyTemp(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeeFamilyTempService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询EmployeeFamilyTemp（分页）")
    @ApiOperation("查询EmployeeFamilyTemp（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeFamilyTemp:list')")
    public ResponseEntity getEmployeeFamilyTempPage(PmEmployeeFamilyTempQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeeFamilyTempService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询EmployeeFamilyTemp（不分页）")
    @ApiOperation("查询EmployeeFamilyTemp（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeFamilyTemp:list')")
    public ResponseEntity getEmployeeFamilyTempNoPaging(PmEmployeeFamilyTempQueryCriteria criteria) {
        return new ResponseEntity<>(pmEmployeeFamilyTempService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出EmployeeFamilyTemp数据")
    @ApiOperation("导出EmployeeFamilyTemp数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeFamilyTemp:list')")
    public void download(HttpServletResponse response, PmEmployeeFamilyTempQueryCriteria criteria) throws IOException {
        pmEmployeeFamilyTempService.download(pmEmployeeFamilyTempService.listAll(criteria), response);
    }

    @Log("校核家庭情况信息临时表")
    @ApiOperation("校核家庭情况信息临时表")
    @PutMapping(value = "/check")
    @PreAuthorize("@el.check('employeeEducationTemp:check','employeeTemp:check')")
    public ResponseEntity check(@Validated @RequestBody PmEmployeeFamilyTemp pmEmployeeFamilyTemp) {
        pmEmployeeFamilyTempService.check(pmEmployeeFamilyTemp);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
