package com.sunten.hrms.fnd.rest;

import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.fnd.aop.ErrorLog;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.fnd.domain.FndJob;
import com.sunten.hrms.fnd.dto.FndJobQueryCriteria;
import com.sunten.hrms.fnd.service.FndJobService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author batan
 * @since 2019-12-19
 */
@RestController
@Api(tags = "系统：岗位管理")
@RequestMapping("/api/fnd/job")
public class FndJobController {
    private static final String ENTITY_NAME = "job";
    private final FndJobService fndJobService;
    private final FndDataScope fndDataScope;

    public FndJobController(FndJobService fndJobService, FndDataScope fndDataScope) {
        this.fndJobService = fndJobService;
        this.fndDataScope = fndDataScope;
    }
    @Log("获取认证岗位List")
    @ApiOperation("获取认证岗位List")
    @GetMapping("/loadAllCertificationJobList")
    public ResponseEntity loadAllCertificationJobList() {
        List<HashMap<String, Object>> resList = new ArrayList<>(1000);
        resList = fndJobService.loadAllCertificationJobList();
        return new ResponseEntity<>(resList, HttpStatus.OK);
    }

    @Log("新增岗位")
    @ApiOperation("新增岗位")
    @PostMapping
    @PreAuthorize("@el.check('job:add')")
    public ResponseEntity create(@Validated @RequestBody FndJob job) {
        if (job.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(fndJobService.insert(job), HttpStatus.CREATED);
    }

    @Log("删除岗位")
    @ApiOperation("删除岗位")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('job:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            fndJobService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该岗位存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改岗位")
    @ApiOperation("修改岗位")
    @PutMapping
    @PreAuthorize("@el.check('job:edit')")
    public ResponseEntity update(@Validated(FndJob.Update.class) @RequestBody FndJob job) {
        fndJobService.update(job);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个岗位")
    @ApiOperation("获取单个岗位")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('job:list')")
    public ResponseEntity getJob(@PathVariable Long id){
        return new ResponseEntity<>(fndJobService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询岗位（分页）")
    @ApiOperation("查询岗位（分页）")
    @GetMapping
    @PreAuthorize("@el.check('job:list','employee:list')")
    public ResponseEntity getJobPage(FndJobQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable){
        // 数据权限
        criteria.setDeptIds(fndDataScope.getDeptIds());
        return new ResponseEntity<>(fndJobService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询岗位（不分页）,不涉及任何权限")
    @ApiOperation("查询岗位（不分页）,不涉及任何权限")
    @GetMapping(value = "/jobsByPage")
    @PreAuthorize("@el.check('job:list','employee:list')")
    public ResponseEntity getJobByPage(FndJobQueryCriteria criteria){
        // 数据权限
        return new ResponseEntity<>(fndJobService.listByAdminJob(criteria), HttpStatus.OK);
    }

    @ErrorLog("根据部门查询岗位(不分页)")
    @ApiOperation("根据部门查询岗位（不分页）")
    @GetMapping(value = "/getJobByDept")
    @PreAuthorize("@el.check('job:list','employee:list')")
    public ResponseEntity getJobByDept(FndJobQueryCriteria criteria){
        // 数据权限
        return new ResponseEntity<>(fndJobService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出岗位数据")
    @ApiOperation("导出岗位数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('job:list')")
    public void download(HttpServletResponse response, FndJobQueryCriteria criteria) throws IOException {
        fndJobService.download(fndJobService.listAll(criteria), response);
    }

    @Log("批量修改岗位(排序)")
    @ApiOperation("批量修改岗位(排序)")
    @PutMapping(value="/batch")
    @PreAuthorize("@el.check('job:edit')")
    public ResponseEntity updateBatchSortJob(@RequestBody List<FndJob> jobs) {
        fndJobService.updateBatchSortJob(jobs);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("查询某部门及公司领导下的所有岗位")
    @ApiOperation("查询某部门及公司领导下的所有岗位")
    @GetMapping(value = "/getJobByDeptAndLeader/{deptId}")
    @PreAuthorize("@el.check('job:list','employee:list')")
    public ResponseEntity getJobByDeptAndLeader(@PathVariable Long deptId){
        // 数据权限
        return new ResponseEntity<>(fndJobService.listAllByDeptAndLeader(deptId), HttpStatus.OK);
    }
}
