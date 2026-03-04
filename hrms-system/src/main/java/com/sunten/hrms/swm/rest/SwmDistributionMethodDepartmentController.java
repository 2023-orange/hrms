package com.sunten.hrms.swm.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmDistributionMethodDepartment;
import com.sunten.hrms.swm.dto.SwmDistributionMethodDepartmentQueryCriteria;
import com.sunten.hrms.swm.service.SwmDistributionMethodDepartmentService;
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
 * 分配方式部门科室关联表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@RestController
@Api(tags = "分配方式部门科室关联表")
@RequestMapping("/api/swm/distributionMethodDepartment")
public class SwmDistributionMethodDepartmentController {
    private static final String ENTITY_NAME = "distributionMethodDepartment";
    private final SwmDistributionMethodDepartmentService swmDistributionMethodDepartmentService;

    public SwmDistributionMethodDepartmentController(SwmDistributionMethodDepartmentService swmDistributionMethodDepartmentService) {
        this.swmDistributionMethodDepartmentService = swmDistributionMethodDepartmentService;
    }

    @Log("新增分配方式部门科室关联表")
    @ApiOperation("新增分配方式部门科室关联表")
    @PostMapping
    @PreAuthorize("@el.check('distributionMethodDepartment:add')")
    public ResponseEntity create(@Validated @RequestBody SwmDistributionMethodDepartment distributionMethodDepartment) {
        if (distributionMethodDepartment.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(swmDistributionMethodDepartmentService.insert(distributionMethodDepartment), HttpStatus.CREATED);
    }

    @Log("删除分配方式部门科室关联表")
    @ApiOperation("删除分配方式部门科室关联表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('distributionMethodDepartment:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            swmDistributionMethodDepartmentService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该分配方式部门科室关联表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改分配方式部门科室关联表")
    @ApiOperation("修改分配方式部门科室关联表")
    @PutMapping
    @PreAuthorize("@el.check('distributionMethodDepartment:edit')")
    public ResponseEntity update(@Validated(SwmDistributionMethodDepartment.Update.class) @RequestBody SwmDistributionMethodDepartment distributionMethodDepartment) {
        swmDistributionMethodDepartmentService.update(distributionMethodDepartment);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个分配方式部门科室关联表")
    @ApiOperation("获取单个分配方式部门科室关联表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('distributionMethodDepartment:list')")
    public ResponseEntity getDistributionMethodDepartment(@PathVariable Long id) {
        return new ResponseEntity<>(swmDistributionMethodDepartmentService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询分配方式部门科室关联表（分页）")
    @ApiOperation("查询分配方式部门科室关联表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('distributionMethodDepartment:list')")
    public ResponseEntity getDistributionMethodDepartmentPage(SwmDistributionMethodDepartmentQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(swmDistributionMethodDepartmentService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询分配方式部门科室关联表（不分页）")
    @ApiOperation("查询分配方式部门科室关联表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('distributionMethodDepartment:list')")
    public ResponseEntity getDistributionMethodDepartmentNoPaging(SwmDistributionMethodDepartmentQueryCriteria criteria) {
    return new ResponseEntity<>(swmDistributionMethodDepartmentService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出分配方式部门科室关联表数据")
    @ApiOperation("导出分配方式部门科室关联表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('distributionMethodDepartment:list')")
    public void download(HttpServletResponse response, SwmDistributionMethodDepartmentQueryCriteria criteria) throws IOException {
        swmDistributionMethodDepartmentService.download(swmDistributionMethodDepartmentService.listAll(criteria), response);
    }
}
