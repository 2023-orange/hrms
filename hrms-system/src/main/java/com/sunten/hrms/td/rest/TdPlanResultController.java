package com.sunten.hrms.td.rest;

import com.sunten.hrms.td.domain.TdPlanEmployee;
import com.sunten.hrms.td.dto.TdPlanEmployeeDTO;
import com.sunten.hrms.td.dto.TdPlanEmployeeQueryCriteria;
import com.sunten.hrms.td.service.TdPlanEmployeeService;
import com.sunten.hrms.td.vo.AgreementExcelVo;
import com.sunten.hrms.td.vo.DeptPlanExcelVo;
import com.sunten.hrms.td.vo.PmPlanExcelVo;
import com.sunten.hrms.td.vo.PmPlanHistoryVo;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.td.domain.TdPlanResult;
import com.sunten.hrms.td.dto.TdPlanResultQueryCriteria;
import com.sunten.hrms.td.service.TdPlanResultService;
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
 * 培训结果记录 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2021-06-16
 */
@RestController
@Api(tags = "培训结果记录")
@RequestMapping("/api/td/planResult")
public class TdPlanResultController {
    private static final String ENTITY_NAME = "planResult";
    private final TdPlanResultService tdPlanResultService;
    private final TdPlanEmployeeService tdPlanEmployeeService;

    public TdPlanResultController(TdPlanResultService tdPlanResultService, TdPlanEmployeeService tdPlanEmployeeService) {
        this.tdPlanResultService = tdPlanResultService;
        this.tdPlanEmployeeService = tdPlanEmployeeService;

    }

    @Log("新增培训结果记录")
    @ApiOperation("新增培训结果记录")
    @PostMapping
    @PreAuthorize("@el.check('planResult:add')")
    public ResponseEntity create(@Validated @RequestBody TdPlanResult planResult) {
        if (planResult.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(tdPlanResultService.insert(planResult), HttpStatus.CREATED);
    }

    @Log("删除培训结果记录")
    @ApiOperation("删除培训结果记录")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('planResult:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            tdPlanResultService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该培训结果记录存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改培训结果记录")
    @ApiOperation("修改培训结果记录")
    @PutMapping
    @PreAuthorize("@el.check('planResult:edit')")
    public ResponseEntity update(@Validated(TdPlanResult.Update.class) @RequestBody TdPlanResult planResult) {
        tdPlanResultService.update(planResult);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个培训结果记录")
    @ApiOperation("获取单个培训结果记录")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('planResult:list')")
    public ResponseEntity getPlanResult(@PathVariable Long id) {
        return new ResponseEntity<>(tdPlanResultService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询培训结果记录（分页）")
    @ApiOperation("查询培训结果记录（分页）")
    @GetMapping
    @PreAuthorize("@el.check('planResult:list')")
    public ResponseEntity getPlanResultPage(TdPlanResultQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(tdPlanResultService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询培训结果记录（不分页）")
    @ApiOperation("查询培训结果记录（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('planResult:list')")
    public ResponseEntity getPlanResultNoPaging(TdPlanResultQueryCriteria criteria) {
    return new ResponseEntity<>(tdPlanResultService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出培训结果记录数据")
    @ApiOperation("导出培训结果记录数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('planResult:list')")
    public void download(HttpServletResponse response, TdPlanResultQueryCriteria criteria) throws IOException {
        tdPlanResultService.download(tdPlanResultService.listAll(criteria), response);
    }

    @ErrorLog("导出总体或部门培训情况表")
    @ApiOperation("导出总体或部门培训情况表")
    @GetMapping(value = "/downloadDeptPlanExcel")
    @PreAuthorize("@el.check('planResult:list')")
    public void downloadDeptPlanExcel(HttpServletResponse response, DeptPlanExcelVo deptPlanExcelVo) throws IOException {
        tdPlanResultService.downloadDeptPlanExcel(tdPlanResultService.getDeptPlanExcelVoList(deptPlanExcelVo), response);
    }

    @ErrorLog("导出培训情况统计表")
    @ApiOperation("导出培训情况统计表")
    @GetMapping(value = "/downloadPmPlanExcel")
    @PreAuthorize("@el.check('planResult:list')")
    public void downloadPmPlanExcel(HttpServletResponse response, PmPlanExcelVo pmPlanExcelVo) throws IOException {
            tdPlanResultService.downloadPmPlanExcel(tdPlanResultService.listEmpByPlanExcelVo(pmPlanExcelVo), null,response);
    }

    @ErrorLog("导出员工培训统计表")
    @ApiOperation("导出员工培训统计表")
    @GetMapping(value = "/downloadPmPlanHistoryExcel")
    @PreAuthorize("@el.check('planResult:list')")
    public void downloadPmPlanHistoryExcel(HttpServletResponse response, PmPlanHistoryVo pmPlanHistoryVo) throws IOException {
        tdPlanResultService.downloadPmPlanHistoryExcel(tdPlanResultService.listPmPlanHistoryExcelVo(pmPlanHistoryVo), response);
    }

    @ErrorLog("导出人员培训协议书表")
    @ApiOperation("导出人员培训协议书表")
    @GetMapping(value = "/downloadAgreementExcel")
    @PreAuthorize("@el.check('planResult:list')")
    public void downloadAgreementExcel(HttpServletResponse response, AgreementExcelVo agreementExcelVo) throws IOException {
        tdPlanResultService.downloadAgreementExcel(tdPlanResultService.listAgreementForExcelVo(agreementExcelVo), response);
    }

    @ErrorLog("人员明细获取子集")
    @ApiOperation("人员明细获取子集")
    @GetMapping(value = "/listForChild/{id}")
    @PreAuthorize("@el.check('plan:list')")
    public ResponseEntity listForChild(@PathVariable Long id) {
        return new ResponseEntity<>(tdPlanResultService.listForChild(id), HttpStatus.OK);
    }
}
