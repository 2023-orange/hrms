package com.sunten.hrms.kpi.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.kpi.domain.KpiAssessmentIndicatorsBbs;
import com.sunten.hrms.kpi.dto.KpiAssessmentIndicatorsBbsQueryCriteria;
import com.sunten.hrms.kpi.service.KpiAssessmentIndicatorsBbsService;
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
 *  前端控制器
 * </p>
 *
 * @author zhoujy
 * @since 2023-12-26
 */
@RestController
@Api(tags = "KPI考核指标BBS表")
@RequestMapping("/api/kpi/assessmentIndicatorsBbs")
public class KpiAssessmentIndicatorsBbsController {
    private static final String ENTITY_NAME = "assessmentIndicatorsBbs";
    private final KpiAssessmentIndicatorsBbsService kpiAssessmentIndicatorsBbsService;

    public KpiAssessmentIndicatorsBbsController(KpiAssessmentIndicatorsBbsService kpiAssessmentIndicatorsBbsService) {
        this.kpiAssessmentIndicatorsBbsService = kpiAssessmentIndicatorsBbsService;
    }

    @Log("新增KPI考核指标BBS表")
    @ApiOperation("新增KPI考核指标BBS表")
    @PostMapping
    public ResponseEntity create(@Validated @RequestBody KpiAssessmentIndicatorsBbs assessmentIndicatorsBbs) {
        return new ResponseEntity<>(kpiAssessmentIndicatorsBbsService.insert(assessmentIndicatorsBbs), HttpStatus.CREATED);
    }

    @Log("删除KPI考核指标BBS表")
    @ApiOperation("删除KPI考核指标BBS表")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete() {
        try {
            kpiAssessmentIndicatorsBbsService.delete();
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该KPI考核指标BBS表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改KPI考核指标BBS表")
    @ApiOperation("修改KPI考核指标BBS表")
    @PutMapping
    public ResponseEntity update(@Validated(KpiAssessmentIndicatorsBbs.Update.class) @RequestBody KpiAssessmentIndicatorsBbs assessmentIndicatorsBbs) {
        kpiAssessmentIndicatorsBbsService.update(assessmentIndicatorsBbs);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个KPI考核指标BBS表")
    @ApiOperation("获取单个KPI考核指标BBS表")
    @GetMapping(value = "/{id}")
    public ResponseEntity getAssessmentIndicatorsBbs() {
        return new ResponseEntity<>(kpiAssessmentIndicatorsBbsService.getByKey(), HttpStatus.OK);
    }

    @ErrorLog("查询（分页）KPI考核指标BBS表")
    @ApiOperation("查询（分页）KPI考核指标BBS表")
    @GetMapping
    public ResponseEntity getAssessmentIndicatorsBbsPage(KpiAssessmentIndicatorsBbsQueryCriteria criteria, @PageableDefault(value = 9999, sort = {}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(kpiAssessmentIndicatorsBbsService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询（不分页）KPI考核指标BBS表")
    @ApiOperation("查询（不分页）KPI考核指标BBS表")
    @GetMapping(value = "/noPaging")
    public ResponseEntity getAssessmentIndicatorsBbsNoPaging(KpiAssessmentIndicatorsBbsQueryCriteria criteria) {
    return new ResponseEntity<>(kpiAssessmentIndicatorsBbsService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出KPI考核指标BBS表数据")
    @ApiOperation("导出KPI考核指标BBS表数据")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, KpiAssessmentIndicatorsBbsQueryCriteria criteria) throws IOException {
        kpiAssessmentIndicatorsBbsService.download(kpiAssessmentIndicatorsBbsService.listAll(criteria), response);
    }
}
