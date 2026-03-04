package com.sunten.hrms.pm.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.pm.domain.PmEmployeeEmergency;
import com.sunten.hrms.pm.dto.PmEmployeeEmergencyQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeEmergencyService;
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
 * 紧急电话表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@RestController
@Api(tags = "紧急电话表")
@RequestMapping("/api/pm/employeeEmergency")
public class PmEmployeeEmergencyController {
    private static final String ENTITY_NAME = "employeeEmergency";
    private final PmEmployeeEmergencyService pmEmployeeEmergencyService;

    public PmEmployeeEmergencyController(PmEmployeeEmergencyService pmEmployeeEmergencyService) {
        this.pmEmployeeEmergencyService = pmEmployeeEmergencyService;
    }

    @Log("新增紧急电话表")
    @ApiOperation("新增紧急电话表")
    @PostMapping
    @PreAuthorize("@el.check('employeeEmergency:add','employee:add')")
    public ResponseEntity create(@Validated @RequestBody PmEmployeeEmergency employeeEmergency) {
        if (employeeEmergency.getId() == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " must be already have an ID");
        }
        return new ResponseEntity<>(pmEmployeeEmergencyService.insert(employeeEmergency), HttpStatus.CREATED);
    }

    @Log("删除紧急电话表")
    @ApiOperation("删除紧急电话表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeEmergency:del','employee:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeeEmergencyService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该紧急电话表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改紧急电话表")
    @ApiOperation("修改紧急电话表")
    @PutMapping
    @PreAuthorize("@el.check('employeeEmergency:edit','employee:edit')")
    public ResponseEntity update(@Validated(PmEmployeeEmergency.Update.class) @RequestBody PmEmployeeEmergency employeeEmergency) {
        pmEmployeeEmergencyService.update(employeeEmergency);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个紧急电话表")
    @ApiOperation("获取单个紧急电话表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeEmergency:list','employee:list')")
    public ResponseEntity getEmployeeEmergency(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeeEmergencyService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询紧急电话表（分页）")
    @ApiOperation("查询紧急电话表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeEmergency:list','employee:list')")
    public ResponseEntity getEmployeeEmergencyPage(PmEmployeeEmergencyQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeeEmergencyService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询紧急电话表（不分页）")
    @ApiOperation("查询紧急电话表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeEmergency:list','employee:list')")
    public ResponseEntity getEmployeeEmergencyNoPaging(PmEmployeeEmergencyQueryCriteria criteria) {
        return new ResponseEntity<>(pmEmployeeEmergencyService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出紧急电话表数据")
    @ApiOperation("导出紧急电话表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeEmergency:list','employee:list')")
    public void download(HttpServletResponse response, PmEmployeeEmergencyQueryCriteria criteria) throws IOException {
        pmEmployeeEmergencyService.download(pmEmployeeEmergencyService.listAll(criteria), response);
    }

    @Log("批量编辑紧急电话表")
    @ApiOperation("批量编辑紧急电话表")
    @PutMapping(value = "/batchSave")
    @PreAuthorize("@el.check('employeeEmergency:edit','employeeEmergency:add','employee:add','employee:edit')")
    public ResponseEntity batchSave(@Validated @RequestBody List<PmEmployeeEmergency> employeeEmergencys) {
        if (employeeEmergencys == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot null");
        }
        return new ResponseEntity<>(pmEmployeeEmergencyService.batchInsert(employeeEmergencys, null), HttpStatus.CREATED);
    }
}
