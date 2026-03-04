package com.sunten.hrms.kpi.rest;

import com.sunten.hrms.kpi.dao.KpiAssessmentDimensionDeptDao;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.kpi.domain.KpiAssessmentDimensionDept;
import com.sunten.hrms.kpi.dto.KpiAssessmentDimensionDeptQueryCriteria;
import com.sunten.hrms.kpi.service.KpiAssessmentDimensionDeptService;
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
 * KPI考核维度与部门关系表 前端控制器
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-28
 */
@RestController
@Api(tags = "KPI考核维度与部门关系表")
@RequestMapping("/api/kpi/assessmentDimensionDept")
public class KpiAssessmentDimensionDeptController {
    private static final String ENTITY_NAME = "assessmentDimensionDept";
    private final KpiAssessmentDimensionDeptService kpiAssessmentDimensionDeptService;
    private final KpiAssessmentDimensionDeptDao kpiAssessmentDimensionDeptDao;

    public KpiAssessmentDimensionDeptController(KpiAssessmentDimensionDeptService kpiAssessmentDimensionDeptService, KpiAssessmentDimensionDeptDao kpiAssessmentDimensionDeptDao) {
        this.kpiAssessmentDimensionDeptService = kpiAssessmentDimensionDeptService;
        this.kpiAssessmentDimensionDeptDao = kpiAssessmentDimensionDeptDao;
    }

    @Log("新增KPI考核维度与部门关系表")
    @ApiOperation("新增KPI考核维度与部门关系表")
    @PostMapping
    @PreAuthorize("@el.check('assessmentDimensionDept:add')")
    public ResponseEntity create(@Validated @RequestBody KpiAssessmentDimensionDept assessmentDimensionDept) {
        if (assessmentDimensionDept.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(kpiAssessmentDimensionDeptService.insert(assessmentDimensionDept), HttpStatus.CREATED);
    }

    @Log("删除KPI考核维度与部门关系表")
    @ApiOperation("删除KPI考核维度与部门关系表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('assessmentDimensionDept:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            kpiAssessmentDimensionDeptService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该KPI考核维度与部门关系表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改KPI考核维度与部门关系表")
    @ApiOperation("修改KPI考核维度与部门关系表")
    @PutMapping
    @PreAuthorize("@el.check('assessmentDimensionDept:edit')")
    public ResponseEntity update(@Validated(KpiAssessmentDimensionDept.Update.class) @RequestBody KpiAssessmentDimensionDept assessmentDimensionDept) {
        kpiAssessmentDimensionDeptService.update(assessmentDimensionDept);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个KPI考核维度与部门关系表")
    @ApiOperation("获取单个KPI考核维度与部门关系表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('assessmentDimensionDept:list')")
    public ResponseEntity getAssessmentDimensionDept(@PathVariable Long id) {
        return new ResponseEntity<>(kpiAssessmentDimensionDeptService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询KPI考核维度与部门关系表（分页）")
    @ApiOperation("查询KPI考核维度与部门关系表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('assessmentDimensionDept:list')")
    public ResponseEntity getAssessmentDimensionDeptPage(KpiAssessmentDimensionDeptQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(kpiAssessmentDimensionDeptService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询KPI考核维度与部门关系表（不分页）")
    @ApiOperation("查询KPI考核维度与部门关系表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('assessmentDimensionDept:list')")
    public ResponseEntity getAssessmentDimensionDeptNoPaging(KpiAssessmentDimensionDeptQueryCriteria criteria) {
    return new ResponseEntity<>(kpiAssessmentDimensionDeptService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出KPI考核维度与部门关系表数据")
    @ApiOperation("导出KPI考核维度与部门关系表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('assessmentDimensionDept:list')")
    public void download(HttpServletResponse response, KpiAssessmentDimensionDeptQueryCriteria criteria) throws IOException {
        kpiAssessmentDimensionDeptService.download(kpiAssessmentDimensionDeptService.listAll(criteria), response);
    }

    @ErrorLog("获取KPI部门维度多选关系")
    @ApiOperation("获取KPI部门维度多选关系")
    @GetMapping(value = "/listMultipleChoice/{id}")
    public ResponseEntity listMultipleChoice(@PathVariable Long id) {
        return new ResponseEntity<>(kpiAssessmentDimensionDeptService.listMultipleChoice(id), HttpStatus.OK);
    }
}
