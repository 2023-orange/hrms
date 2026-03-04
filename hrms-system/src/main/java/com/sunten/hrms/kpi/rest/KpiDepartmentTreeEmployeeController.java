package com.sunten.hrms.kpi.rest;

import com.sunten.hrms.kpi.dao.KpiDepartmentTreeEmployeeDao;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.kpi.domain.KpiDepartmentTreeEmployee;
import com.sunten.hrms.kpi.dto.KpiDepartmentTreeEmployeeQueryCriteria;
import com.sunten.hrms.kpi.service.KpiDepartmentTreeEmployeeService;
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
 * KPI资料填写人中间表 前端控制器
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-27
 */
@RestController
@Api(tags = "KPI资料填写人中间表")
@RequestMapping("/api/kpi/departmentTreeEmployee")
public class KpiDepartmentTreeEmployeeController {
    private static final String ENTITY_NAME = "departmentTreeEmployee";
    private final KpiDepartmentTreeEmployeeService kpiDepartmentTreeEmployeeService;
    private final KpiDepartmentTreeEmployeeDao kpiDepartmentTreeEmployeeDao;

    public KpiDepartmentTreeEmployeeController(KpiDepartmentTreeEmployeeService kpiDepartmentTreeEmployeeService, KpiDepartmentTreeEmployeeDao kpiDepartmentTreeEmployeeDao) {
        this.kpiDepartmentTreeEmployeeService = kpiDepartmentTreeEmployeeService;
        this.kpiDepartmentTreeEmployeeDao = kpiDepartmentTreeEmployeeDao;
    }

    @Log("新增KPI资料填写人中间表")
    @ApiOperation("新增KPI资料填写人中间表")
    @PostMapping
    @PreAuthorize("@el.check('departmentTreeEmployee:add')")
    public ResponseEntity create(@Validated @RequestBody KpiDepartmentTreeEmployee departmentTreeEmployee) {
        if (departmentTreeEmployee.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(kpiDepartmentTreeEmployeeService.insert(departmentTreeEmployee), HttpStatus.CREATED);
    }

    @Log("删除KPI资料填写人中间表")
    @ApiOperation("删除KPI资料填写人中间表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('departmentTreeEmployee:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            kpiDepartmentTreeEmployeeService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该KPI资料填写人中间表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改KPI资料填写人中间表")
    @ApiOperation("修改KPI资料填写人中间表")
    @PutMapping
    @PreAuthorize("@el.check('departmentTreeEmployee:edit')")
    public ResponseEntity update(@Validated(KpiDepartmentTreeEmployee.Update.class) @RequestBody KpiDepartmentTreeEmployee departmentTreeEmployee) {
        kpiDepartmentTreeEmployeeService.update(departmentTreeEmployee);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个KPI资料填写人中间表")
    @ApiOperation("获取单个KPI资料填写人中间表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('departmentTreeEmployee:list')")
    public ResponseEntity getDepartmentTreeEmployee(@PathVariable Long id) {
        return new ResponseEntity<>(kpiDepartmentTreeEmployeeService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询KPI资料填写人中间表（分页）")
    @ApiOperation("查询KPI资料填写人中间表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('departmentTreeEmployee:list')")
    public ResponseEntity getDepartmentTreeEmployeePage(KpiDepartmentTreeEmployeeQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(kpiDepartmentTreeEmployeeService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询KPI资料填写人中间表（不分页）")
    @ApiOperation("查询KPI资料填写人中间表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('departmentTreeEmployee:list')")
    public ResponseEntity getDepartmentTreeEmployeeNoPaging(KpiDepartmentTreeEmployeeQueryCriteria criteria) {
    return new ResponseEntity<>(kpiDepartmentTreeEmployeeService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出KPI资料填写人中间表数据")
    @ApiOperation("导出KPI资料填写人中间表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('departmentTreeEmployee:list')")
    public void download(HttpServletResponse response, KpiDepartmentTreeEmployeeQueryCriteria criteria) throws IOException {
        kpiDepartmentTreeEmployeeService.download(kpiDepartmentTreeEmployeeService.listAll(criteria), response);
    }

    @ErrorLog("获取KPI部门资料填写人多选关系")
    @ApiOperation("获取KPI部门资料填写人多选关系")
    @GetMapping(value = "/listDepartmentTreeEmployee/{id}")
    @PreAuthorize("@el.check('departmentTree:list')")
    public ResponseEntity listDepartmentTreeEmployee(@PathVariable Long id) {
        return new ResponseEntity<>(kpiDepartmentTreeEmployeeDao.listDepartmentTreeEmployee(id), HttpStatus.OK);
    }

    @Log("删除KPI资料填写人中间表")
    @ApiOperation("删除KPI资料填写人中间表")
    @DeleteMapping(value = "/deleteTreeEmployee/{id}")
    @PreAuthorize("@el.check('departmentTreeEmployee:del')")
    public ResponseEntity deleteTreeEmployee(@PathVariable Long id) {
        kpiDepartmentTreeEmployeeDao.deleteTreeEmployee(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
