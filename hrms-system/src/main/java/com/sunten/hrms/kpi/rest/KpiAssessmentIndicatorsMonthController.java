package com.sunten.hrms.kpi.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.kpi.domain.KpiAssessmentIndicatorsMonth;
import com.sunten.hrms.kpi.dto.KpiAssessmentIndicatorsMonthQueryCriteria;
import com.sunten.hrms.kpi.service.KpiAssessmentIndicatorsMonthService;
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
 * KPI考核指标子表 前端控制器
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-28
 */
@RestController
@Api(tags = "KPI考核指标子表")
@RequestMapping("/api/kpi/assessmentIndicatorsMonth")
public class KpiAssessmentIndicatorsMonthController {
    private static final String ENTITY_NAME = "assessmentIndicatorsMonth";
    private final KpiAssessmentIndicatorsMonthService kpiAssessmentIndicatorsMonthService;

    public KpiAssessmentIndicatorsMonthController(KpiAssessmentIndicatorsMonthService kpiAssessmentIndicatorsMonthService) {
        this.kpiAssessmentIndicatorsMonthService = kpiAssessmentIndicatorsMonthService;
    }

    @Log("新增KPI考核指标子表")
    @ApiOperation("新增KPI考核指标子表")
    @PostMapping
    @PreAuthorize("@el.check('assessmentIndicatorsMonth:add')")
    public ResponseEntity create(@Validated @RequestBody KpiAssessmentIndicatorsMonth assessmentIndicatorsMonth) {
        if (assessmentIndicatorsMonth.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(kpiAssessmentIndicatorsMonthService.insert(assessmentIndicatorsMonth), HttpStatus.CREATED);
    }

    @Log("删除KPI考核指标子表")
    @ApiOperation("删除KPI考核指标子表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('assessmentIndicatorsMonth:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            kpiAssessmentIndicatorsMonthService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该KPI考核指标子表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改KPI考核指标子表")
    @ApiOperation("修改KPI考核指标子表")
    @PutMapping
    @PreAuthorize("@el.check('assessmentIndicatorsMonth:edit')")
    public ResponseEntity update(@Validated(KpiAssessmentIndicatorsMonth.Update.class) @RequestBody KpiAssessmentIndicatorsMonth assessmentIndicatorsMonth) {
        kpiAssessmentIndicatorsMonthService.update(assessmentIndicatorsMonth);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个KPI考核指标子表")
    @ApiOperation("获取单个KPI考核指标子表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('assessmentIndicatorsMonth:list')")
    public ResponseEntity getAssessmentIndicatorsMonth(@PathVariable Long id) {
        return new ResponseEntity<>(kpiAssessmentIndicatorsMonthService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询KPI考核指标子表（分页）")
    @ApiOperation("查询KPI考核指标子表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('assessmentIndicatorsMonth:list')")
    public ResponseEntity getAssessmentIndicatorsMonthPage(KpiAssessmentIndicatorsMonthQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(kpiAssessmentIndicatorsMonthService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询KPI考核指标子表（不分页）")
    @ApiOperation("查询KPI考核指标子表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('assessmentIndicatorsMonth:list')")
    public ResponseEntity getAssessmentIndicatorsMonthNoPaging(KpiAssessmentIndicatorsMonthQueryCriteria criteria) {
    return new ResponseEntity<>(kpiAssessmentIndicatorsMonthService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出KPI考核指标子表数据")
    @ApiOperation("导出KPI考核指标子表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('assessmentIndicatorsMonth:list')")
    public void download(HttpServletResponse response, KpiAssessmentIndicatorsMonthQueryCriteria criteria) throws IOException {
        kpiAssessmentIndicatorsMonthService.download(kpiAssessmentIndicatorsMonthService.listAll(criteria), response);
    }
}
