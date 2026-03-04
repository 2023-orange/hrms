package com.sunten.hrms.kpi.rest;

import com.sunten.hrms.kpi.dao.KpiAssessmentDimensionDao;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.kpi.domain.KpiAssessmentDimension;
import com.sunten.hrms.kpi.dto.KpiAssessmentDimensionQueryCriteria;
import com.sunten.hrms.kpi.service.KpiAssessmentDimensionService;
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
 * KPI考核维度表 前端控制器
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-27
 */
@RestController
@Api(tags = "KPI考核维度表")
@RequestMapping("/api/kpi/assessmentDimension")
public class KpiAssessmentDimensionController {
    private static final String ENTITY_NAME = "assessmentDimension";
    private final KpiAssessmentDimensionService kpiAssessmentDimensionService;
    private final KpiAssessmentDimensionDao kpiAssessmentDimensionDao;

    public KpiAssessmentDimensionController(KpiAssessmentDimensionService kpiAssessmentDimensionService, KpiAssessmentDimensionDao kpiAssessmentDimensionDao) {
        this.kpiAssessmentDimensionService = kpiAssessmentDimensionService;
        this.kpiAssessmentDimensionDao = kpiAssessmentDimensionDao;
    }

    @Log("新增KPI考核维度表")
    @ApiOperation("新增KPI考核维度表")
    @PostMapping
    @PreAuthorize("@el.check('assessmentDimension:add')")
    public ResponseEntity create(@Validated @RequestBody KpiAssessmentDimension assessmentDimension) {
        if (assessmentDimension.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(kpiAssessmentDimensionService.insert(assessmentDimension), HttpStatus.CREATED);
    }

    @Log("删除KPI考核维度表")
    @ApiOperation("删除KPI考核维度表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('assessmentDimension:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            kpiAssessmentDimensionService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该KPI考核维度表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改KPI考核维度表")
    @ApiOperation("修改KPI考核维度表")
    @PutMapping
    @PreAuthorize("@el.check('assessmentDimension:edit')")
    public ResponseEntity update(@Validated(KpiAssessmentDimension.Update.class) @RequestBody KpiAssessmentDimension assessmentDimension) {
        kpiAssessmentDimensionService.update(assessmentDimension);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个KPI考核维度表")
    @ApiOperation("获取单个KPI考核维度表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('assessmentDimension:list')")
    public ResponseEntity getAssessmentDimension(@PathVariable Long id) {
        return new ResponseEntity<>(kpiAssessmentDimensionService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询KPI考核维度表（分页）")
    @ApiOperation("查询KPI考核维度表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('assessmentDimension:list')")
    public ResponseEntity getAssessmentDimensionPage(KpiAssessmentDimensionQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(kpiAssessmentDimensionService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询KPI考核维度表（不分页）")
    @ApiOperation("查询KPI考核维度表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('assessmentDimension:list')")
    public ResponseEntity getAssessmentDimensionNoPaging(KpiAssessmentDimensionQueryCriteria criteria) {
    return new ResponseEntity<>(kpiAssessmentDimensionService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出KPI考核维度表数据")
    @ApiOperation("导出KPI考核维度表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('assessmentDimension:list')")
    public void download(HttpServletResponse response, KpiAssessmentDimensionQueryCriteria criteria) throws IOException {
        kpiAssessmentDimensionService.download(kpiAssessmentDimensionService.listAll(criteria), response);
    }

    @ErrorLog("查询KPI考核维度表（生效）")
    @ApiOperation("查询KPI考核维度表（生效）")
    @GetMapping(value = "/getAssessmentDimension")
    @PreAuthorize("@el.check('assessmentDimension:list')")
    public ResponseEntity listAllByEnableFlag() {
        return new ResponseEntity<>(kpiAssessmentDimensionService.listAllByEnableFlag(), HttpStatus.OK);
    }
}
