package com.sunten.hrms.pm.rest;

import com.sunten.hrms.fnd.service.FndUserService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.pm.domain.PmEmployeePoliticalTemp;
import com.sunten.hrms.pm.dto.PmEmployeePoliticalTempQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeePoliticalTempService;
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
 * 员工政治面貌临时表 前端控制器
 * </p>
 *
 * @author xukai
 * @since 2021-11-24
 */
@RestController
@Api(tags = "员工政治面貌临时表")
@RequestMapping("/api/pm/employeePoliticalTemp")
public class PmEmployeePoliticalTempController {
    private static final String ENTITY_NAME = "employeePoliticalTemp";
    private final PmEmployeePoliticalTempService pmEmployeePoliticalTempService;
    private final FndUserService fndUserService;

    public PmEmployeePoliticalTempController(PmEmployeePoliticalTempService pmEmployeePoliticalTempService, FndUserService fndUserService) {
        this.pmEmployeePoliticalTempService = pmEmployeePoliticalTempService;
        this.fndUserService = fndUserService;
    }

    @Log("新增员工政治面貌临时表")
    @ApiOperation("新增员工政治面貌临时表")
    @PostMapping
    public ResponseEntity create(@Validated @RequestBody PmEmployeePoliticalTemp employeePoliticalTemp) {
        if (!fndUserService.isActiveUser(employeePoliticalTemp.getEmployee().getId())) {
            throw new BadRequestException("不是本人，无法新增");
        }
        return new ResponseEntity<>(pmEmployeePoliticalTempService.insert(employeePoliticalTemp), HttpStatus.CREATED);
    }

    @Log("删除员工政治面貌临时表")
    @ApiOperation("删除员工政治面貌临时表")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeePoliticalTempService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该员工政治面貌临时表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改员工政治面貌临时表")
    @ApiOperation("修改员工政治面貌临时表")
    @PutMapping
    public ResponseEntity update(@Validated(PmEmployeePoliticalTemp.Update.class) @RequestBody PmEmployeePoliticalTemp employeePoliticalTemp) {
        if (!fndUserService.isActiveUser(employeePoliticalTemp.getEmployee().getId())) {
            throw new BadRequestException("不是本人，无法修改");
        }
        pmEmployeePoliticalTempService.update(employeePoliticalTemp);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个员工政治面貌临时表")
    @ApiOperation("获取单个员工政治面貌临时表")
    @GetMapping(value = "/{id}")
    public ResponseEntity getEmployeePoliticalTemp(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeePoliticalTempService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询员工政治面貌临时表（分页）")
    @ApiOperation("查询员工政治面貌临时表（分页）")
    @GetMapping
    public ResponseEntity getEmployeePoliticalTempPage(PmEmployeePoliticalTempQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeePoliticalTempService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询员工政治面貌临时表（不分页）")
    @ApiOperation("查询员工政治面貌临时表（不分页）")
    @GetMapping(value = "/noPaging")
    public ResponseEntity getEmployeePoliticalTempNoPaging(PmEmployeePoliticalTempQueryCriteria criteria) {
    return new ResponseEntity<>(pmEmployeePoliticalTempService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出员工政治面貌临时表数据")
    @ApiOperation("导出员工政治面貌临时表数据")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, PmEmployeePoliticalTempQueryCriteria criteria) throws IOException {
        pmEmployeePoliticalTempService.download(pmEmployeePoliticalTempService.listAll(criteria), response);
    }

    @Log("校核员工政治面貌临时表")
    @ApiOperation("校核员工政治面貌临时表")
    @PutMapping(value = "/check")
    @PreAuthorize("@el.check('employeePoliticalTemp:check','employeeTemp:check')")
    public ResponseEntity check(@Validated @RequestBody PmEmployeePoliticalTemp employeePoliticalTemp) {
        pmEmployeePoliticalTempService.check(employeePoliticalTemp);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
