package com.sunten.hrms.swm.rest;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.swm.domain.SwmEmployeeExcel;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmEmployeeInterface;
import com.sunten.hrms.swm.dto.SwmEmployeeInterfaceQueryCriteria;
import com.sunten.hrms.swm.service.SwmEmployeeInterfaceService;
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
import java.util.stream.Collectors;

/**
 * <p>
 * 薪酬员工信息接口表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2021-09-13
 */
@RestController
@Api(tags = "薪酬员工信息接口表")
@RequestMapping("/api/swm/employeeInterface")
public class SwmEmployeeInterfaceController {
    private static final String ENTITY_NAME = "employeeInterface";
    private final SwmEmployeeInterfaceService swmEmployeeInterfaceService;

    public SwmEmployeeInterfaceController(SwmEmployeeInterfaceService swmEmployeeInterfaceService) {
        this.swmEmployeeInterfaceService = swmEmployeeInterfaceService;
    }

    @Log("新增薪酬员工信息接口表")
    @ApiOperation("新增薪酬员工信息接口表")
    @PostMapping
    @PreAuthorize("@el.check('employeeInterface:add')")
    public ResponseEntity create(@Validated @RequestBody SwmEmployeeInterface employeeInterface) {
        if (employeeInterface.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(swmEmployeeInterfaceService.insert(employeeInterface), HttpStatus.CREATED);
    }

    @Log("删除薪酬员工信息接口表")
    @ApiOperation("删除薪酬员工信息接口表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeInterface:del')")
    public ResponseEntity delete(@PathVariable Double id) {
        try {
            swmEmployeeInterfaceService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该薪酬员工信息接口表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改薪酬员工信息接口表")
    @ApiOperation("修改薪酬员工信息接口表")
    @PutMapping
    @PreAuthorize("@el.check('employeeInterface:edit')")
    public ResponseEntity update(@Validated(SwmEmployeeInterface.Update.class) @RequestBody SwmEmployeeInterface employeeInterface) {
        swmEmployeeInterfaceService.update(employeeInterface);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个薪酬员工信息接口表")
    @ApiOperation("获取单个薪酬员工信息接口表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeInterface:list')")
    public ResponseEntity getEmployeeInterface(@PathVariable Double id) {
        return new ResponseEntity<>(swmEmployeeInterfaceService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询薪酬员工信息接口表（分页）")
    @ApiOperation("查询薪酬员工信息接口表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeInterface:list')")
    public ResponseEntity getEmployeeInterfacePage(SwmEmployeeInterfaceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(swmEmployeeInterfaceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询薪酬员工信息接口表（不分页）")
    @ApiOperation("查询薪酬员工信息接口表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeInterface:list')")
    public ResponseEntity getEmployeeInterfaceNoPaging(SwmEmployeeInterfaceQueryCriteria criteria) {
    return new ResponseEntity<>(swmEmployeeInterfaceService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出薪酬员工信息接口表数据")
    @ApiOperation("导出薪酬员工信息接口表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeInterface:list')")
    public void download(HttpServletResponse response, SwmEmployeeInterfaceQueryCriteria criteria) throws IOException {
        swmEmployeeInterfaceService.download(swmEmployeeInterfaceService.listAll(criteria), response);
    }

    @Log("薪酬员工信息导入")
    @ApiOperation("薪酬员工信息导入")
    @PutMapping(value = "/insertExcel")
    @PreAuthorize("@el.check('swmEmployee:add','swmEmployee:edit')")
    public ResponseEntity insertExcel(@RequestBody SwmEmployeeExcel swmEmployeeExcel) {
        return new ResponseEntity<>(swmEmployeeInterfaceService.importExcel(swmEmployeeExcel.getSwmEmployeeInterfaces(), swmEmployeeExcel.getReImportFlag()), HttpStatus.OK);
    }

    @Log("获取薪酬信息导入后的汇总")
    @ApiOperation("获取薪酬信息导入后的汇总")
    @PutMapping(value = "getSwmEmployeeSummaryByImportList")
    @PreAuthorize("@el.check('swmEmployee:add','swmEmployee:edit')")
    public ResponseEntity getSwmEmployeeSummaryByImportList(@RequestBody SwmEmployeeInterfaceQueryCriteria criteria) {
        if (null == criteria.getSwmEmployeeInterfaceList() || criteria.getSwmEmployeeInterfaceList().size() == 0) {
            throw new InfoCheckWarningMessException("薪酬人员信息导入数据，获取汇总结果失败");
        } else {
            return new ResponseEntity<>(swmEmployeeInterfaceService.getSwmEmployeeSummaryByImportList(
               criteria.getSwmEmployeeInterfaceList().stream().map(SwmEmployeeInterface::getWorkCard).collect(Collectors.toSet()),
               criteria.getGroupIds()
            ), HttpStatus.OK);
        }
    }
}
