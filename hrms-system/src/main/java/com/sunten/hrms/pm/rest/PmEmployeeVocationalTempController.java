package com.sunten.hrms.pm.rest;

import com.sunten.hrms.annotation.AnonymousAccess;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.pm.domain.PmEmployeeVocationalTemp;
import com.sunten.hrms.pm.dto.PmEmployeeVocationalTempQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeVocationalTempService;
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
 * 职业资格临时表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@RestController
@Api(tags = "职业资格临时表")
@RequestMapping("/api/pm/employeeVocationalTemp")
public class PmEmployeeVocationalTempController {
    private static final String ENTITY_NAME = "employeeVocationalTemp";
    private final PmEmployeeVocationalTempService pmEmployeeVocationalTempService;
    private final FndUserService fndUserService;

    public PmEmployeeVocationalTempController(PmEmployeeVocationalTempService pmEmployeeVocationalTempService, FndUserService fndUserService) {
        this.pmEmployeeVocationalTempService = pmEmployeeVocationalTempService;
        this.fndUserService = fndUserService;
    }

    @Log("新增职业资格临时表")
    @ApiOperation("新增职业资格临时表")
    @PostMapping
    @AnonymousAccess
    public ResponseEntity create(@Validated @RequestBody PmEmployeeVocationalTemp employeeVocationalTemp) {
        if (!fndUserService.isActiveUser(employeeVocationalTemp.getEmployee().getId())) {
            throw new BadRequestException("不是本人，无法新增");
        }
        return new ResponseEntity<>(pmEmployeeVocationalTempService.insert(employeeVocationalTemp), HttpStatus.CREATED);
    }

    @Log("删除职业资格临时表")
    @ApiOperation("删除职业资格临时表")
    @DeleteMapping(value = "/{id}")
    @AnonymousAccess
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeeVocationalTempService.delete(id);
        } catch (Throwable e) {
            System.out.println(e.getMessage());
            ThrowableUtil.throwForeignKeyException(e, "该职业资格临时表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改职业资格临时表")
    @ApiOperation("修改职业资格临时表")
    @PutMapping
    @AnonymousAccess
    public ResponseEntity update(@Validated(PmEmployeeVocationalTemp.Update.class) @RequestBody PmEmployeeVocationalTemp employeeVocationalTemp) {
        if (!fndUserService.isActiveUser(employeeVocationalTemp.getEmployee().getId())) {
            throw new BadRequestException("不是本人，无法修改");
        }
        pmEmployeeVocationalTempService.update(employeeVocationalTemp);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个职业资格临时表")
    @ApiOperation("获取单个职业资格临时表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeVocationalTemp:list')")
    public ResponseEntity getEmployeeVocationalTemp(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeeVocationalTempService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询职业资格临时表（分页）")
    @ApiOperation("查询职业资格临时表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeVocationalTemp:list')")
    public ResponseEntity getEmployeeVocationalTempPage(PmEmployeeVocationalTempQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeeVocationalTempService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询职业资格临时表（不分页）")
    @ApiOperation("查询职业资格临时表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeVocationalTemp:list')")
    public ResponseEntity getEmployeeVocationalTempNoPaging(PmEmployeeVocationalTempQueryCriteria criteria) {
        return new ResponseEntity<>(pmEmployeeVocationalTempService.listAll(criteria), HttpStatus.OK);
    }


    @ErrorLog("导出职业资格临时表数据")
    @ApiOperation("导出职业资格临时表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeVocationalTemp:list')")
    public void download(HttpServletResponse response, PmEmployeeVocationalTempQueryCriteria criteria) throws IOException {
        pmEmployeeVocationalTempService.download(pmEmployeeVocationalTempService.listAll(criteria), response);
    }

    @Log("校核家庭情况信息临时表")
    @ApiOperation("校核家庭情况信息临时表")
    @PutMapping(value = "/check")
    @PreAuthorize("@el.check('employeeVocationalTemp:check','employeeTemp:check')")
    public ResponseEntity check(@Validated @RequestBody PmEmployeeVocationalTemp employeeVocationalTemp) {
        pmEmployeeVocationalTempService.check(employeeVocationalTemp);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
