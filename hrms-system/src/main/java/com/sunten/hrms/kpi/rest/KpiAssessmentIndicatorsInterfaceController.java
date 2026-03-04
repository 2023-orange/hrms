package com.sunten.hrms.kpi.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.kpi.domain.KpiAssessmentIndicatorsInterface;
import com.sunten.hrms.kpi.dto.KpiAssessmentIndicatorsInterfaceQueryCriteria;
import com.sunten.hrms.kpi.service.KpiAssessmentIndicatorsInterfaceService;
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
 * @since 2023-12-20
 */
@RestController
@Api(tags = "KPI考核指标导入接口表")
@RequestMapping("/api/kpi/assessmentIndicatorsInterface")
public class KpiAssessmentIndicatorsInterfaceController {
    private static final String ENTITY_NAME = "assessmentIndicatorsInterface";
    private final KpiAssessmentIndicatorsInterfaceService kpiAssessmentIndicatorsInterfaceService;

    public KpiAssessmentIndicatorsInterfaceController(KpiAssessmentIndicatorsInterfaceService kpiAssessmentIndicatorsInterfaceService) {
        this.kpiAssessmentIndicatorsInterfaceService = kpiAssessmentIndicatorsInterfaceService;
    }

    @Log("新增KPI考核指标导入接口表")
    @ApiOperation("新增KPI考核指标导入接口表")
    @PostMapping
    @PreAuthorize("@el.check('assessmentIndicatorsInterface:add')")
    public ResponseEntity create(@Validated @RequestBody KpiAssessmentIndicatorsInterface assessmentIndicatorsInterface) {
        return new ResponseEntity<>(kpiAssessmentIndicatorsInterfaceService.insert(assessmentIndicatorsInterface), HttpStatus.CREATED);
    }

    @Log("删除KPI考核指标导入接口表")
    @ApiOperation("删除KPI考核指标导入接口表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('assessmentIndicatorsInterface:del')")
    public ResponseEntity delete() {
        try {
            kpiAssessmentIndicatorsInterfaceService.delete();
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改KPI考核指标导入接口表")
    @ApiOperation("修改KPI考核指标导入接口表")
    @PutMapping
    @PreAuthorize("@el.check('assessmentIndicatorsInterface:edit')")
    public ResponseEntity update(@Validated(KpiAssessmentIndicatorsInterface.Update.class) @RequestBody KpiAssessmentIndicatorsInterface assessmentIndicatorsInterface) {
        kpiAssessmentIndicatorsInterfaceService.update(assessmentIndicatorsInterface);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个KPI考核指标导入接口表")
    @ApiOperation("获取单个KPI考核指标导入接口表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('assessmentIndicatorsInterface:list')")
    public ResponseEntity getAssessmentIndicatorsInterface() {
        return new ResponseEntity<>(kpiAssessmentIndicatorsInterfaceService.getByKey(), HttpStatus.OK);
    }

    @ErrorLog("查询（分页）KPI考核指标导入接口表")
    @ApiOperation("查询（分页）KPI考核指标导入接口表")
    @GetMapping
    @PreAuthorize("@el.check('assessmentIndicatorsInterface:list')")
    public ResponseEntity getAssessmentIndicatorsInterfacePage(KpiAssessmentIndicatorsInterfaceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(kpiAssessmentIndicatorsInterfaceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询（不分页）KPI考核指标导入接口表")
    @ApiOperation("查询（不分页）KPI考核指标导入接口表")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('assessmentIndicatorsInterface:list')")
    public ResponseEntity getAssessmentIndicatorsInterfaceNoPaging(KpiAssessmentIndicatorsInterfaceQueryCriteria criteria) {
    return new ResponseEntity<>(kpiAssessmentIndicatorsInterfaceService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出数据KPI考核指标导入接口表")
    @ApiOperation("导出数据KPI考核指标导入接口表")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('assessmentIndicatorsInterface:list')")
    public void download(HttpServletResponse response, KpiAssessmentIndicatorsInterfaceQueryCriteria criteria) throws IOException {
        kpiAssessmentIndicatorsInterfaceService.download(kpiAssessmentIndicatorsInterfaceService.listAll(criteria), response);
    }
}
