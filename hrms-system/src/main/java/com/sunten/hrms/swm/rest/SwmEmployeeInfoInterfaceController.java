package com.sunten.hrms.swm.rest;

import com.sunten.hrms.swm.domain.SwmEmployeeInfoExcel;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmEmployeeInfoInterface;
import com.sunten.hrms.swm.dto.SwmEmployeeInfoInterfaceQueryCriteria;
import com.sunten.hrms.swm.service.SwmEmployeeInfoInterfaceService;
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
 * 薪酬员工基本信息接口表 前端控制器
 * </p>
 *
 * @author zhoujy
 * @since 2023-03-23
 */
@RestController
@Api(tags = "薪酬员工基本信息接口表")
@RequestMapping("/api/swm/employeeInfoInterface")
public class SwmEmployeeInfoInterfaceController {
    private static final String ENTITY_NAME = "employeeInfoInterface";
    private final SwmEmployeeInfoInterfaceService swmEmployeeInfoInterfaceService;

    public SwmEmployeeInfoInterfaceController(SwmEmployeeInfoInterfaceService swmEmployeeInfoInterfaceService) {
        this.swmEmployeeInfoInterfaceService = swmEmployeeInfoInterfaceService;
    }

    @Log("新增薪酬员工基本信息接口表")
    @ApiOperation("新增薪酬员工基本信息接口表")
    @PostMapping
    @PreAuthorize("@el.check('employeeInfoInterface:add')")
    public ResponseEntity create(@Validated @RequestBody SwmEmployeeInfoInterface employeeInfoInterface) {
        if (employeeInfoInterface.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(swmEmployeeInfoInterfaceService.insert(employeeInfoInterface), HttpStatus.CREATED);
    }

    @Log("删除薪酬员工基本信息接口表")
    @ApiOperation("删除薪酬员工基本信息接口表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeInfoInterface:del')")
    public ResponseEntity delete(@PathVariable Double id) {
        try {
            swmEmployeeInfoInterfaceService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该薪酬员工基本信息接口表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改薪酬员工基本信息接口表")
    @ApiOperation("修改薪酬员工基本信息接口表")
    @PutMapping
    @PreAuthorize("@el.check('employeeInfoInterface:edit')")
    public ResponseEntity update(@Validated(SwmEmployeeInfoInterface.Update.class) @RequestBody SwmEmployeeInfoInterface employeeInfoInterface) {
        swmEmployeeInfoInterfaceService.update(employeeInfoInterface);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个薪酬员工基本信息接口表")
    @ApiOperation("获取单个薪酬员工基本信息接口表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('employeeInfoInterface:list')")
    public ResponseEntity getEmployeeInfoInterface(@PathVariable Double id) {
        return new ResponseEntity<>(swmEmployeeInfoInterfaceService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询薪酬员工基本信息接口表（分页）")
    @ApiOperation("查询薪酬员工基本信息接口表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('employeeInfoInterface:list')")
    public ResponseEntity getEmployeeInfoInterfacePage(SwmEmployeeInfoInterfaceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(swmEmployeeInfoInterfaceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询薪酬员工基本信息接口表（不分页）")
    @ApiOperation("查询薪酬员工基本信息接口表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('employeeInfoInterface:list')")
    public ResponseEntity getEmployeeInfoInterfaceNoPaging(SwmEmployeeInfoInterfaceQueryCriteria criteria) {
    return new ResponseEntity<>(swmEmployeeInfoInterfaceService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出薪酬员工基本信息接口表数据")
    @ApiOperation("导出薪酬员工基本信息接口表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('employeeInfoInterface:list')")
    public void download(HttpServletResponse response, SwmEmployeeInfoInterfaceQueryCriteria criteria) throws IOException {
        swmEmployeeInfoInterfaceService.download(swmEmployeeInfoInterfaceService.listAll(criteria), response);
    }

    @Log("薪酬员工信息导入")
    @ApiOperation("薪酬员工信息导入")
    @PutMapping(value = "/insertExcel")
    @PreAuthorize("@el.check('swmEmployee:add','swmEmployee:edit')")
    public ResponseEntity insertExcel(@RequestBody SwmEmployeeInfoExcel swmEmployeeInfoExcel) {
        return new ResponseEntity<>(swmEmployeeInfoInterfaceService.importExcel(swmEmployeeInfoExcel.getSwmEmployeeInfoInterfaces()), HttpStatus.OK);
    }
}
