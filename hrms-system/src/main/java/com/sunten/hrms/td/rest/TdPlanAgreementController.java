package com.sunten.hrms.td.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.td.domain.TdPlanAgreement;
import com.sunten.hrms.td.dto.TdPlanAgreementQueryCriteria;
import com.sunten.hrms.td.service.TdPlanAgreementService;
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
 * 培训协议书记录表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2021-06-18
 */
@RestController
@Api(tags = "培训协议书记录表")
@RequestMapping("/api/td/planAgreement")
public class TdPlanAgreementController {
    private static final String ENTITY_NAME = "planAgreement";
    private final TdPlanAgreementService tdPlanAgreementService;

    public TdPlanAgreementController(TdPlanAgreementService tdPlanAgreementService) {
        this.tdPlanAgreementService = tdPlanAgreementService;
    }

    @Log("新增培训协议书记录表")
    @ApiOperation("新增培训协议书记录表")
    @PostMapping
    @PreAuthorize("@el.check('planAgreement:add')")
    public ResponseEntity create(@Validated @RequestBody TdPlanAgreement planAgreement) {
        if (planAgreement.getId()  != -1) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(tdPlanAgreementService.insert(planAgreement), HttpStatus.CREATED);
    }

    @Log("删除培训协议书记录表")
    @ApiOperation("删除培训协议书记录表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('planAgreement:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            tdPlanAgreementService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该培训协议书记录表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改培训协议书记录表")
    @ApiOperation("修改培训协议书记录表")
    @PutMapping
    @PreAuthorize("@el.check('planAgreement:edit')")
    public ResponseEntity update(@Validated(TdPlanAgreement.Update.class) @RequestBody TdPlanAgreement planAgreement) {
        tdPlanAgreementService.update(planAgreement);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个培训协议书记录表")
    @ApiOperation("获取单个培训协议书记录表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('planAgreement:list')")
    public ResponseEntity getPlanAgreement(@PathVariable Long id) {
        return new ResponseEntity<>(tdPlanAgreementService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询培训协议书记录表（分页）")
    @ApiOperation("查询培训协议书记录表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('planAgreement:list')")
    public ResponseEntity getPlanAgreementPage(TdPlanAgreementQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(tdPlanAgreementService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询培训协议书记录表（不分页）")
    @ApiOperation("查询培训协议书记录表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('planAgreement:list')")
    public ResponseEntity getPlanAgreementNoPaging(TdPlanAgreementQueryCriteria criteria) {
    return new ResponseEntity<>(tdPlanAgreementService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出培训协议书记录表数据")
    @ApiOperation("导出培训协议书记录表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('planAgreement:list')")
    public void download(HttpServletResponse response, TdPlanAgreementQueryCriteria criteria) throws IOException {
        tdPlanAgreementService.download(tdPlanAgreementService.listAll(criteria), response);
    }

    @ErrorLog("导出培训协议书记录表数据")
    @ApiOperation("导出培训协议书记录表数据")
    @GetMapping(value = "/getAgreementByEmployeeIdAndPlanId")
    @PreAuthorize("@el.check('planAgreement:edit')")
    public ResponseEntity getAgreementByEmployeeIdAndPlanId(TdPlanAgreement tdPlanAgreement) {
        return new ResponseEntity<>(tdPlanAgreementService.getByPlanIdAndEmployeeId(tdPlanAgreement.getEmployeeId(), tdPlanAgreement.getPlanId(), tdPlanAgreement.getCheckMethod()), HttpStatus.OK);
    }
}
