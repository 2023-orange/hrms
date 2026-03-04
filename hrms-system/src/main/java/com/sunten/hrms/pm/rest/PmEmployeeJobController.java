package com.sunten.hrms.pm.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.pm.domain.PmEmployeeJob;
import com.sunten.hrms.pm.dto.PmEmployeeJobQueryCriteria;
import com.sunten.hrms.pm.service.PmEmployeeJobService;
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
 * 部门科室岗位关系表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-04
 */
@RestController
@Api(tags = "部门科室岗位关系表")
@RequestMapping("/api/pm/employeeJob")
public class PmEmployeeJobController {
    private static final String ENTITY_NAME = "employeeJob";
    private final PmEmployeeJobService pmEmployeeJobService;

    public PmEmployeeJobController(PmEmployeeJobService pmEmployeeJobService) {
        this.pmEmployeeJobService = pmEmployeeJobService;
    }

    @Log("新增部门科室岗位关系表")
    @ApiOperation("新增部门科室岗位关系表")
    @PostMapping
    @PreAuthorize("@el.check('employeeJob:add','employee:add')")
    public ResponseEntity create(@Validated @RequestBody PmEmployeeJob employeeJob) {
        if (employeeJob.getId() == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " must be already have an ID");
        }
        return new ResponseEntity<>(pmEmployeeJobService.insert(employeeJob), HttpStatus.CREATED);
    }

    @Log("删除部门科室岗位关系表")
    @ApiOperation("删除部门科室岗位关系表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeJob:del','employee:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmEmployeeJobService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该部门科室岗位关系表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改部门科室岗位关系表")
    @ApiOperation("修改部门科室岗位关系表")
    @PutMapping
    @PreAuthorize("@el.check('employeeJob:edit','employee:edit')")
    public ResponseEntity update(@Validated(PmEmployeeJob.Update.class) @RequestBody PmEmployeeJob employeeJob) {
        pmEmployeeJobService.update(employeeJob);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个部门科室岗位关系表")
    @ApiOperation("获取单个部门科室岗位关系表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeJob:list','employee:list')")
    public ResponseEntity getEmployeeJob(@PathVariable Long id) {
        return new ResponseEntity<>(pmEmployeeJobService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询部门科室岗位关系表（分页）")
    @ApiOperation("查询部门科室岗位关系表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeJob:list','employee:list')")
    public ResponseEntity getEmployeeJobPage(PmEmployeeJobQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmEmployeeJobService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询部门科室岗位关系表（不分页）")
    @ApiOperation("查询部门科室岗位关系表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeJob:list','employee:list')")
    public ResponseEntity getEmployeeJobNoPaging(PmEmployeeJobQueryCriteria criteria) {
        return new ResponseEntity<>(pmEmployeeJobService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出部门科室岗位关系表数据")
    @ApiOperation("导出部门科室岗位关系表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeJob:list','employee:list')")
    public void download(HttpServletResponse response, PmEmployeeJobQueryCriteria criteria) throws IOException {
        pmEmployeeJobService.download(pmEmployeeJobService.listAll(criteria), response);
    }

    @Log("设置主岗位")
    @ApiOperation("设置主岗位")
    @PutMapping(value = "/setMainJob/{id}")
    @PreAuthorize("@el.check('employeeJob:add','employeeJob:edit','employee:add','employee:edit')")
    public ResponseEntity setMainJob(@PathVariable Long id) {
        if (id == null) {
            throw new BadRequestException("获取不到预设主岗位的ID");
        }
        pmEmployeeJobService.setMianJob(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取某岗位任职人员/曾任职人员")
    @ApiOperation("获取某岗位任职人员/曾任职人员")
    @GetMapping(value = "/employee/{jobId}")
    @PreAuthorize("@el.check('job:list')")
    public ResponseEntity getJobEmployee(@PathVariable Long jobId, Boolean enabledFlag) {
        return new ResponseEntity<>(pmEmployeeJobService.listJobEmployee(jobId, enabledFlag), HttpStatus.OK);
    }

    @ErrorLog("导出某岗位的人员信息")
    @ApiOperation("导出某岗位的人员信息")
    @GetMapping(value = "/employee/download/{jobId}")
    @PreAuthorize("@el.check('job:list')")
    public void downloadByJob(HttpServletResponse response, @PathVariable Long jobId, Boolean enabledFlag) throws IOException {
        pmEmployeeJobService.downloadByJob(pmEmployeeJobService.listJobEmployee(jobId, enabledFlag), response);
    }

//    @ErrorLog("通过岗位名称获取某岗位任职人员")
//    @ApiOperation("通过岗位名称获取某岗位任职人员")
//    @GetMapping(value = "/employee/jobName/{jobName}")
//    public ResponseEntity getJobEmployeeByName(@PathVariable String jobName) {
//        return new ResponseEntity<>(pmEmployeeJobService.listJobEmployeeByName(jobName), HttpStatus.OK);
//    }
}
