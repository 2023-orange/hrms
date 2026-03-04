package com.sunten.hrms.kpi.rest;

import com.sunten.hrms.annotation.AnonymousAccess;
import com.sunten.hrms.fnd.dto.FndDeptQueryCriteria;
import com.sunten.hrms.fnd.service.FndDeptService;
import com.sunten.hrms.kpi.dao.KpiDepartmentTreeEmployeeDao;
import com.sunten.hrms.kpi.dto.KpiDepartmentTreeDTO;
import com.sunten.hrms.kpi.dto.KpiDepartmentTreeEmployeeQueryCriteria;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.kpi.domain.KpiDepartmentTree;
import com.sunten.hrms.kpi.dto.KpiDepartmentTreeQueryCriteria;
import com.sunten.hrms.kpi.service.KpiDepartmentTreeService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * KPI部门树表 前端控制器
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-27
 */
@RestController
@Api(tags = "KPI部门树表")
@RequestMapping("/api/kpi/departmentTree")
public class KpiDepartmentTreeController {
    private static final String ENTITY_NAME = "departmentTree";
    private final KpiDepartmentTreeService kpiDepartmentTreeService;
    private final FndDeptService fndDeptService;
    private final KpiDepartmentTreeEmployeeDao kpiDepartmentTreeEmployeeDao;

    public KpiDepartmentTreeController(KpiDepartmentTreeService kpiDepartmentTreeService, FndDeptService fndDeptService, KpiDepartmentTreeEmployeeDao kpiDepartmentTreeEmployeeDao) {
        this.kpiDepartmentTreeService = kpiDepartmentTreeService;
        this.fndDeptService = fndDeptService;
        this.kpiDepartmentTreeEmployeeDao = kpiDepartmentTreeEmployeeDao;
    }

    @Log("新增KPI部门树表")
    @ApiOperation("新增KPI部门树表")
    @PostMapping
    @PreAuthorize("@el.check('departmentTree:add')")
    public ResponseEntity create(@Validated @RequestBody KpiDepartmentTree departmentTree) {
        if (departmentTree.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(kpiDepartmentTreeService.insert(departmentTree), HttpStatus.CREATED);
    }

    @Log("删除KPI部门树表")
    @ApiOperation("删除KPI部门树表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('departmentTree:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            kpiDepartmentTreeService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该KPI部门树表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改KPI部门树表")
    @ApiOperation("修改KPI部门树表")
    @PutMapping
    @PreAuthorize("@el.check('departmentTree:edit')")
    public ResponseEntity update(@Validated(KpiDepartmentTree.Update.class) @RequestBody KpiDepartmentTree departmentTree) {
        kpiDepartmentTreeService.update(departmentTree);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个KPI部门树表")
    @ApiOperation("获取单个KPI部门树表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('departmentTree:list')")
    public ResponseEntity getDepartmentTree(@PathVariable Long id) {
        return new ResponseEntity<>(kpiDepartmentTreeService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询KPI部门树表（分页）")
    @ApiOperation("查询KPI部门树表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('departmentTree:list')")
    public ResponseEntity getDepartmentTreePage(KpiDepartmentTreeQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(kpiDepartmentTreeService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询KPI部门树表（不分页）")
    @ApiOperation("查询KPI部门树表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('departmentTree:list')")
    public ResponseEntity getDepartmentTreeNoPaging(KpiDepartmentTreeQueryCriteria criteria) {
    return new ResponseEntity<>(kpiDepartmentTreeService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出KPI部门树表数据")
    @ApiOperation("导出KPI部门树表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('departmentTree:list')")
    public void download(HttpServletResponse response, KpiDepartmentTreeQueryCriteria criteria) throws IOException {
        kpiDepartmentTreeService.download(kpiDepartmentTreeService.listAll(criteria), response);
    }

    @ErrorLog("查询KPI部门树")
    @ApiOperation("查询KPI部门树")
    @GetMapping(value = "/getKpiTree")
    @PreAuthorize("@el.check('departmentTree:list')")
    public ResponseEntity getKpiTree(KpiDepartmentTreeQueryCriteria criteria) {
        List<KpiDepartmentTreeDTO> kpiDepartmentTreeDTOS = kpiDepartmentTreeService.listAll(criteria);
        return new ResponseEntity<>(kpiDepartmentTreeService.buildTree(kpiDepartmentTreeDTOS, false), HttpStatus.OK);
    }

    @Log("保存KPI部门树数据")
    @ApiOperation("保存KPI部门树数据")
    @PostMapping("/saveTree")
    @PreAuthorize("@el.check('departmentTree:list')")
    public ResponseEntity saveTree(@Validated @RequestBody KpiDepartmentTree departmentTree) {
        kpiDepartmentTreeService.updateByKpiTree(departmentTree);
        return new ResponseEntity<>('1', HttpStatus.CREATED);
    }

    @ErrorLog("查询KPI部门树（下拉框）")
    @ApiOperation("查询KPI部门树（下拉框）")
    @PostMapping(value = "/getKpiTreeSelect")
    public ResponseEntity getKpiTreeSelect(@Validated @RequestBody KpiDepartmentTreeQueryCriteria criteria) {
        List<KpiDepartmentTreeDTO> kpiDepartmentTreeDTOS = kpiDepartmentTreeService.listAll(criteria);
        return new ResponseEntity<>(kpiDepartmentTreeDTOS, HttpStatus.OK);
    }

    @ErrorLog("查询KPI选中人员所属部门")
    @ApiOperation("查询KPI选中人员所属部门")
    @PostMapping(value = "/getKpiEmployee")
    public ResponseEntity getKpiEmployee(@Validated @RequestBody KpiDepartmentTreeEmployeeQueryCriteria criteria) {
        return new ResponseEntity<>(kpiDepartmentTreeEmployeeDao.getKpiEmployee(criteria), HttpStatus.OK);
    }

    @ErrorLog("查询所有部门（树结构）")
    @ApiOperation("查询所有部门（树结构）")
    @GetMapping(value = "/getKpiAllDepts")
    @AnonymousAccess
    public ResponseEntity getKpiAllDepts(KpiDepartmentTreeQueryCriteria criteria){
        return setCriteriaIds(criteria);
    }

    private ResponseEntity setCriteriaIds(KpiDepartmentTreeQueryCriteria criteria) {
        List<KpiDepartmentTreeDTO> deptDTOS = kpiDepartmentTreeService.listAll(criteria);
        if (null != criteria.getName()) {
            Map<String, Object> map = new HashMap<>();
            map.put("totalElements", deptDTOS.size());
            map.put("content", deptDTOS);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(kpiDepartmentTreeService.buildTree(deptDTOS, false), HttpStatus.OK);// new ResponseEntity<>(fndDeptService.buildTree(deptDTOS), HttpStatus.OK);
        }
    }
}
