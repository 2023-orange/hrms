package com.sunten.hrms.swm.rest;

import cn.hutool.json.JSONArray;
import com.sunten.hrms.fnd.domain.AdvancedQuery;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmPersonalIncomeTax;
import com.sunten.hrms.swm.dto.SwmPersonalIncomeTaxQueryCriteria;
import com.sunten.hrms.swm.service.SwmPersonalIncomeTaxService;
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
 * 个人所得税表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@RestController
@Api(tags = "个人所得税表")
@RequestMapping("/api/swm/personalIncomeTax")
public class SwmPersonalIncomeTaxController {
    private static final String ENTITY_NAME = "personalIncomeTax";
    private final SwmPersonalIncomeTaxService swmPersonalIncomeTaxService;

    public SwmPersonalIncomeTaxController(SwmPersonalIncomeTaxService swmPersonalIncomeTaxService) {
        this.swmPersonalIncomeTaxService = swmPersonalIncomeTaxService;
    }

    @Log("新增个人所得税表")
    @ApiOperation("新增个人所得税表")
    @PostMapping
    @PreAuthorize("@el.check('personalIncomeTax:add')")
    public ResponseEntity create(@Validated @RequestBody SwmPersonalIncomeTax personalIncomeTax) {
        if (personalIncomeTax.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(swmPersonalIncomeTaxService.insert(personalIncomeTax), HttpStatus.CREATED);
    }

    @Log("删除个人所得税表")
    @ApiOperation("删除个人所得税表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('personalIncomeTax:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            swmPersonalIncomeTaxService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该个人所得税表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @ErrorLog("个人所得税所得期间集合获取")
    @ApiOperation("个人所得税所得期间集合获取")
    @GetMapping("/getSelfTaxPeriodList")
    @PreAuthorize("@el.check('personalIncomeTax:list')")
    public ResponseEntity getSelfTaxPeriodList() {
        return new ResponseEntity<>(swmPersonalIncomeTaxService.generatePeriodList(), HttpStatus.OK);
    }

    @Log("修改个人所得税表")
    @ApiOperation("修改个人所得税表")
    @PutMapping
    @PreAuthorize("@el.check('personalIncomeTax:edit')")
    public ResponseEntity update(@Validated(SwmPersonalIncomeTax.Update.class) @RequestBody SwmPersonalIncomeTax personalIncomeTax) {
        swmPersonalIncomeTaxService.update(personalIncomeTax);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个个人所得税表")
    @ApiOperation("获取单个个人所得税表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('personalIncomeTax:list')")
    public ResponseEntity getPersonalIncomeTax(@PathVariable Long id) {
        return new ResponseEntity<>(swmPersonalIncomeTaxService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询个人所得税表（分页）")
    @ApiOperation("查询个人所得税表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('personalIncomeTax:list')")
    public ResponseEntity getPersonalIncomeTaxPage(SwmPersonalIncomeTaxQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"work_card"}, direction = Sort.Direction.ASC) Pageable pageable) {
        setCriteria(criteria);
        return new ResponseEntity<>(swmPersonalIncomeTaxService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询个人所得税表（不分页）")
    @ApiOperation("查询个人所得税表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('personalIncomeTax:list')")
    public ResponseEntity getPersonalIncomeTaxNoPaging(SwmPersonalIncomeTaxQueryCriteria criteria) {
        setCriteria(criteria);
        return new ResponseEntity<>(swmPersonalIncomeTaxService.listAll(criteria), HttpStatus.OK);
    }

    private void setCriteria(SwmPersonalIncomeTaxQueryCriteria criteria) {
    }

    @ErrorLog("导出个人所得税表数据")
    @ApiOperation("导出个人所得税表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('personalIncomeTax:list')")
    public void download(HttpServletResponse response, SwmPersonalIncomeTaxQueryCriteria criteria) throws IOException {
        setCriteria(criteria);
        swmPersonalIncomeTaxService.download(swmPersonalIncomeTaxService.listAll(criteria), response);
    }

    @ErrorLog("获取个人所有所得税记录（普通用户使用）")
    @ApiOperation("获取个人所有所得税记录（普通用户使用）")
    @GetMapping(value = "/getPersonalTaxList")
    @PreAuthorize("@el.check('userPersonalTax:list')")
    public ResponseEntity getPersonalTaxList() {
        return new ResponseEntity<>(swmPersonalIncomeTaxService.getTaxListByUserId(), HttpStatus.OK);
    }
}
