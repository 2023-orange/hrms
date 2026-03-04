package com.sunten.hrms.swm.rest;

import cn.hutool.json.JSONArray;
import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.utils.DateUtil;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmEmployeeMonthlyBak;
import com.sunten.hrms.swm.dto.SwmEmployeeMonthlyBakQueryCriteria;
import com.sunten.hrms.swm.service.SwmEmployeeMonthlyBakService;
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
 * 薪酬员工信息每月备份表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2021-09-15
 */
@RestController
@Api(tags = "薪酬员工信息每月备份表")
@RequestMapping("/api/swm/employeeMonthlyBak")
public class SwmEmployeeMonthlyBakController {
    private static final String ENTITY_NAME = "employeeMonthlyBak";
    private final SwmEmployeeMonthlyBakService swmEmployeeMonthlyBakService;

    public SwmEmployeeMonthlyBakController(SwmEmployeeMonthlyBakService swmEmployeeMonthlyBakService) {
        this.swmEmployeeMonthlyBakService = swmEmployeeMonthlyBakService;
    }

    @Log("新增薪酬员工信息每月备份表")
    @ApiOperation("新增薪酬员工信息每月备份表")
    @PostMapping
    @PreAuthorize("@el.check('employeeMonthlyBak:add')")
    public ResponseEntity create(@Validated @RequestBody SwmEmployeeMonthlyBak employeeMonthlyBak) {
        if (employeeMonthlyBak.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(swmEmployeeMonthlyBakService.insert(employeeMonthlyBak), HttpStatus.CREATED);
    }

    @Log("删除薪酬员工信息每月备份表")
    @ApiOperation("删除薪酬员工信息每月备份表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeMonthlyBak:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            swmEmployeeMonthlyBakService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该薪酬员工信息每月备份表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改薪酬员工信息每月备份表")
    @ApiOperation("修改薪酬员工信息每月备份表")
    @PutMapping
    @PreAuthorize("@el.check('employeeMonthlyBak:edit')")
    public ResponseEntity update(@Validated(SwmEmployeeMonthlyBak.Update.class) @RequestBody SwmEmployeeMonthlyBak employeeMonthlyBak) {
        swmEmployeeMonthlyBakService.update(employeeMonthlyBak);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个薪酬员工信息每月备份表")
    @ApiOperation("获取单个薪酬员工信息每月备份表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeMonthlyBak:list')")
    public ResponseEntity getEmployeeMonthlyBak(@PathVariable Long id) {
        return new ResponseEntity<>(swmEmployeeMonthlyBakService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询薪酬员工信息每月备份表（分页）")
    @ApiOperation("查询薪酬员工信息每月备份表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeMonthlyBak:list')")
    public ResponseEntity getEmployeeMonthlyBakPage(SwmEmployeeMonthlyBakQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        setMonthlyBakQueryCriteria(criteria);
        return new ResponseEntity<>(swmEmployeeMonthlyBakService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询薪酬员工信息每月备份表（不分页）")
    @ApiOperation("查询薪酬员工信息每月备份表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeMonthlyBak:list')")
    public ResponseEntity getEmployeeMonthlyBakNoPaging(SwmEmployeeMonthlyBakQueryCriteria criteria) {
    return new ResponseEntity<>(swmEmployeeMonthlyBakService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出薪酬员工信息每月备份表数据")
    @ApiOperation("导出薪酬员工信息每月备份表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeMonthlyBak:list')")
    public void download(HttpServletResponse response, SwmEmployeeMonthlyBakQueryCriteria criteria) throws IOException {
        setMonthlyBakQueryCriteria(criteria);
        swmEmployeeMonthlyBakService.download(swmEmployeeMonthlyBakService.listAll(criteria), response);
    }

    private void setMonthlyBakQueryCriteria(SwmEmployeeMonthlyBakQueryCriteria criteria) {
        if (null != criteria.getDeptCode()) {
            if (criteria.getDeptCode().contains(".")) {
                String[] strs = criteria.getDeptCode().split("\\.");
                criteria.setDepartment(strs[0]);
                criteria.setAdministrativeOffice(strs[1]);
            } else {
                criteria.setDepartment(criteria.getDeptCode());
            }
        }
        if (null != criteria.getBakDate() && !criteria.getBakDate().trim().equals("")) {
            criteria.setBakDate(criteria.getBakDate().replaceAll("\\.", "-"));
            criteria.setBakDateLocal(DateUtil.strToLocalDate(criteria.getBakDate().length() == 6 ? (criteria.getBakDate() + "0-01") : (criteria.getBakDate() + "-01")));
        }
    }
}
