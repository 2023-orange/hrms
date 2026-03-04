package com.sunten.hrms.swm.rest;

import com.sunten.hrms.swm.domain.SwmPersonalIncomeTaxExcel;
import com.sunten.hrms.swm.domain.SwmPostSkillSalaryInterface;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmPersonalIncomeTaxInterface;
import com.sunten.hrms.swm.dto.SwmPersonalIncomeTaxInterfaceQueryCriteria;
import com.sunten.hrms.swm.service.SwmPersonalIncomeTaxInterfaceService;
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
 * 个人所得税接口表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2021-01-14
 */
@RestController
@Api(tags = "个人所得税接口表")
@RequestMapping("/api/swm/personalIncomeTaxInterface")
public class SwmPersonalIncomeTaxInterfaceController {
    private static final String ENTITY_NAME = "personalIncomeTaxInterface";
    private final SwmPersonalIncomeTaxInterfaceService swmPersonalIncomeTaxInterfaceService;

    public SwmPersonalIncomeTaxInterfaceController(SwmPersonalIncomeTaxInterfaceService swmPersonalIncomeTaxInterfaceService) {
        this.swmPersonalIncomeTaxInterfaceService = swmPersonalIncomeTaxInterfaceService;
    }

    @Log("新增个人所得税接口表")
    @ApiOperation("新增个人所得税接口表")
    @PostMapping
    @PreAuthorize("@el.check('personalIncomeTax:add')")
    public ResponseEntity create(@Validated @RequestBody SwmPersonalIncomeTaxInterface personalIncomeTaxInterface) {
        if (personalIncomeTaxInterface.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(swmPersonalIncomeTaxInterfaceService.insert(personalIncomeTaxInterface), HttpStatus.CREATED);
    }

    @Log("删除个人所得税接口表")
    @ApiOperation("删除个人所得税接口表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('personalIncomeTax:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            swmPersonalIncomeTaxInterfaceService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该个人所得税接口表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改个人所得税接口表")
    @ApiOperation("修改个人所得税接口表")
    @PutMapping
    @PreAuthorize("@el.check('personalIncomeTax:edit')")
    public ResponseEntity update(@Validated(SwmPersonalIncomeTaxInterface.Update.class) @RequestBody SwmPersonalIncomeTaxInterface personalIncomeTaxInterface) {
        swmPersonalIncomeTaxInterfaceService.update(personalIncomeTaxInterface);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个个人所得税接口表")
    @ApiOperation("获取单个个人所得税接口表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('personalIncomeTax:list')")
    public ResponseEntity getPersonalIncomeTaxInterface(@PathVariable Long id) {
        return new ResponseEntity<>(swmPersonalIncomeTaxInterfaceService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询个人所得税接口表（分页）")
    @ApiOperation("查询个人所得税接口表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('personalIncomeTax:list')")
    public ResponseEntity getPersonalIncomeTaxInterfacePage(SwmPersonalIncomeTaxInterfaceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(swmPersonalIncomeTaxInterfaceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询个人所得税接口表（不分页）")
    @ApiOperation("查询个人所得税接口表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('personalIncomeTax:list')")
    public ResponseEntity getPersonalIncomeTaxInterfaceNoPaging(SwmPersonalIncomeTaxInterfaceQueryCriteria criteria) {
    return new ResponseEntity<>(swmPersonalIncomeTaxInterfaceService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出个人所得税接口表数据")
    @ApiOperation("导出个人所得税接口表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('personalIncomeTax:list')")
    public void download(HttpServletResponse response, SwmPersonalIncomeTaxInterfaceQueryCriteria criteria) throws IOException {
        swmPersonalIncomeTaxInterfaceService.download(swmPersonalIncomeTaxInterfaceService.listAll(criteria), response);
    }

    @Log("个税导入(自动累计)")
    @ApiOperation("个税导入(自动累计)")
    @PutMapping(value = "/insertExcel")
    @PreAuthorize("@el.check('personalIncomeTax:add','personalIncomeTax:edit')")
    public ResponseEntity insertExcel(@RequestBody SwmPersonalIncomeTaxExcel swmPersonalIncomeTaxExcel) {
        return new ResponseEntity<>(swmPersonalIncomeTaxInterfaceService.insertExcel(swmPersonalIncomeTaxExcel.getSwmPersonalIncomeTaxInterfaces(), true, swmPersonalIncomeTaxExcel.getReImportFlag()), HttpStatus.OK);
    }


    @Log("个税导入(非自动累计)")
    @ApiOperation("个税导入(非自动累计)")
    @PutMapping(value = "/insertExcelWithNotAmount")
    @PreAuthorize("@el.check('personalIncomeTax:add','personalIncomeTax:edit')")
    public ResponseEntity insertExcelWithNotAmount(@RequestBody SwmPersonalIncomeTaxExcel swmPersonalIncomeTaxExcel) {
        return new ResponseEntity<>(swmPersonalIncomeTaxInterfaceService.insertExcel(swmPersonalIncomeTaxExcel.getSwmPersonalIncomeTaxInterfaces(), false, swmPersonalIncomeTaxExcel.getReImportFlag()), HttpStatus.OK);
    }


    @Log("个税检测(自动累计导入)")
    @ApiOperation("个税检测（自动累计导入）")
    @PutMapping(value = "/checkTax")
    @PreAuthorize("@el.check('personalIncomeTax:add','personalIncomeTax:edit')")
    public ResponseEntity checkTax(@RequestBody List<SwmPersonalIncomeTaxInterface> swmPersonalIncomeTaxInterfaces) {
        return new ResponseEntity<>(swmPersonalIncomeTaxInterfaceService.checkTax(swmPersonalIncomeTaxInterfaces), HttpStatus.OK);
    }

    @Log("个税检测（非自动累计导入）")
    @ApiOperation("个人检测（非自动累计导入）")
    @PutMapping(value = "/checkTaxWithNotAmount")
    @PreAuthorize("@el.check('personalIncomeTax:add','personalIncomeTax:edit')")
    public ResponseEntity checkTaxNotAmount(@RequestBody List<SwmPersonalIncomeTaxInterface> swmPersonalIncomeTaxInterfaces) {
        return new ResponseEntity<>(swmPersonalIncomeTaxInterfaceService.checkTaxWithNotAmount(swmPersonalIncomeTaxInterfaces), HttpStatus.OK);
    }
}
