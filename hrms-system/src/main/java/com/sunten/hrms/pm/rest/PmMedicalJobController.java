package com.sunten.hrms.pm.rest;

import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.utils.PageUtil;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.pm.domain.PmMedicalJob;
import com.sunten.hrms.pm.dto.PmMedicalJobQueryCriteria;
import com.sunten.hrms.pm.service.PmMedicalJobService;
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
 * 岗位体检表 前端控制器
 * </p>
 *
 * @author xukai
 * @since 2021-04-19
 */
@RestController
@Api(tags = "岗位体检表")
@RequestMapping("/api/pm/medicalJob")
public class PmMedicalJobController {
    private static final String ENTITY_NAME = "medicalJob";
    private final PmMedicalJobService pmMedicalJobService;
    private final FndDataScope fndDataScope;

    public PmMedicalJobController(PmMedicalJobService pmMedicalJobService, FndDataScope fndDataScope) {
        this.pmMedicalJobService = pmMedicalJobService;
        this.fndDataScope = fndDataScope;
    }

    @Log("新增岗位体检表")
    @ApiOperation("新增岗位体检表")
    @PostMapping
    @PreAuthorize("@el.check('medicalJob:add')")
    public ResponseEntity create(@Validated @RequestBody PmMedicalJob medicalJob) {
        if (medicalJob.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(pmMedicalJobService.insert(medicalJob), HttpStatus.CREATED);
    }

    @Log("删除岗位体检表")
    @ApiOperation("删除岗位体检表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('medicalJob:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmMedicalJobService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该岗位体检表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改岗位体检表")
    @ApiOperation("修改岗位体检表")
    @PutMapping
    @PreAuthorize("@el.check('medicalJob:edit')")
    public ResponseEntity update(@Validated(PmMedicalJob.Update.class) @RequestBody PmMedicalJob medicalJob) {
        pmMedicalJobService.update(medicalJob);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个岗位体检表")
    @ApiOperation("获取单个岗位体检表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('medicalJob:list')")
    public ResponseEntity getMedicalJob(@PathVariable Long id) {
        return new ResponseEntity<>(pmMedicalJobService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("获取岗位体检具体体检项目信息")
    @ApiOperation("获取岗位体检具体体检项目信息")
    @GetMapping(value = "getJobMedicalDetail/{id}")
    @PreAuthorize("@el.check('medicalJob:list')")
    public ResponseEntity getJobMedicalProject(@PathVariable Long id) {
        return new ResponseEntity<>(pmMedicalJobService.getProjectListByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询岗位体检表（分页）")
    @ApiOperation("查询岗位体检表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('medicalJob:list')")
    public ResponseEntity getMedicalJobPage(PmMedicalJobQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        FndDataScopeVo dataScopeVo = new FndDataScopeVo(criteria.getDeptId(), null, false, true, criteria.getDeptIds(), null);
        if (fndDataScope.isNoDataPermission(dataScopeVo)) {
            return new ResponseEntity<>(PageUtil.toPage(null, 0), HttpStatus.OK);
        }
        criteria.setDeptIds(dataScopeVo.getDeptIds());
        return new ResponseEntity<>(pmMedicalJobService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询岗位体检表（不分页）")
    @ApiOperation("查询岗位体检表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('medicalJob:list')")
    public ResponseEntity getMedicalJobNoPaging(PmMedicalJobQueryCriteria criteria) {
    return new ResponseEntity<>(pmMedicalJobService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出岗位体检表数据")
    @ApiOperation("导出岗位体检表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('medicalJob:list')")
    public void download(HttpServletResponse response, PmMedicalJobQueryCriteria criteria) throws IOException {
        pmMedicalJobService.download(pmMedicalJobService.listAll(criteria), response);
    }

    @ErrorLog("获取岗位体检具体体检项目信息")
    @ApiOperation("获取岗位体检具体体检项目信息")
    @GetMapping(value = "/getByJobId/{jobId}")
//    @PreAuthorize("@el.check('medicalJob:list','medical:list')")
    public ResponseEntity getJobMedicalProjectByJobid(@PathVariable Long jobId) {
        return new ResponseEntity<>(pmMedicalJobService.getByJobId(jobId), HttpStatus.OK);
    }
}
