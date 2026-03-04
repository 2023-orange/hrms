package com.sunten.hrms.pm.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.pm.domain.PmSalesApprovalRelations;
import com.sunten.hrms.pm.dto.PmSalesApprovalRelationsQueryCriteria;
import com.sunten.hrms.pm.service.PmSalesApprovalRelationsService;
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
 * 销售审批节点关系表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2022-02-17
 */
@RestController
@Api(tags = "销售审批节点关系表")
@RequestMapping("/api/pm/salesApprovalRelations")
public class PmSalesApprovalRelationsController {
    private static final String ENTITY_NAME = "salesApprovalRelations";
    private final PmSalesApprovalRelationsService pmSalesApprovalRelationsService;

    public PmSalesApprovalRelationsController(PmSalesApprovalRelationsService pmSalesApprovalRelationsService) {
        this.pmSalesApprovalRelationsService = pmSalesApprovalRelationsService;
    }

    @Log("新增销售审批节点关系表")
    @ApiOperation("新增销售审批节点关系表")
    @PostMapping
    @PreAuthorize("@el.check('salesApprovalRelations:add')")
    public ResponseEntity create(@Validated @RequestBody PmSalesApprovalRelations salesApprovalRelations) {
        if (salesApprovalRelations.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(pmSalesApprovalRelationsService.insert(salesApprovalRelations), HttpStatus.CREATED);
    }

    @Log("删除销售审批节点关系表")
    @ApiOperation("删除销售审批节点关系表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('salesApprovalRelations:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            pmSalesApprovalRelationsService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该销售审批节点关系表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改销售审批节点关系表")
    @ApiOperation("修改销售审批节点关系表")
    @PutMapping
    @PreAuthorize("@el.check('salesApprovalRelations:edit')")
    public ResponseEntity update(@Validated(PmSalesApprovalRelations.Update.class) @RequestBody PmSalesApprovalRelations salesApprovalRelations) {
        pmSalesApprovalRelationsService.update(salesApprovalRelations);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个销售审批节点关系表")
    @ApiOperation("获取单个销售审批节点关系表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('salesApprovalRelations:list')")
    public ResponseEntity getSalesApprovalRelations(@PathVariable Long id) {
        return new ResponseEntity<>(pmSalesApprovalRelationsService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询销售审批节点关系表（分页）")
    @ApiOperation("查询销售审批节点关系表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('salesApprovalRelations:list')")
    public ResponseEntity getSalesApprovalRelationsPage(PmSalesApprovalRelationsQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(pmSalesApprovalRelationsService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询销售审批节点关系表（不分页）")
    @ApiOperation("查询销售审批节点关系表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('salesApprovalRelations:list')")
    public ResponseEntity getSalesApprovalRelationsNoPaging(PmSalesApprovalRelationsQueryCriteria criteria) {
    return new ResponseEntity<>(pmSalesApprovalRelationsService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出销售审批节点关系表数据")
    @ApiOperation("导出销售审批节点关系表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('salesApprovalRelations:list')")
    public void download(HttpServletResponse response, PmSalesApprovalRelationsQueryCriteria criteria) throws IOException {
        pmSalesApprovalRelationsService.download(pmSalesApprovalRelationsService.listAll(criteria), response);
    }
}
