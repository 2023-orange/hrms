package com.sunten.hrms.swm.rest;

import cn.hutool.json.JSONArray;
import com.sunten.hrms.fnd.domain.AdvancedQuery;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmWageDistribution;
import com.sunten.hrms.swm.dto.SwmWageDistributionQueryCriteria;
import com.sunten.hrms.swm.service.SwmWageDistributionService;
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
 * 工资分配（工资系数）表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@RestController
@Api(tags = "工资分配（工资系数）表")
@RequestMapping("/api/swm/wageDistribution")
public class SwmWageDistributionController {
    private static final String ENTITY_NAME = "wageDistribution";
    private final SwmWageDistributionService swmWageDistributionService;


    public SwmWageDistributionController(SwmWageDistributionService swmWageDistributionService) {
        this.swmWageDistributionService = swmWageDistributionService;
    }

    @Log("新增工资分配（工资系数）表")
    @ApiOperation("新增工资分配（工资系数）表")
    @PostMapping
    @PreAuthorize("@el.check('wageDistribution:add')")
    public ResponseEntity create(@Validated @RequestBody SwmWageDistribution wageDistribution) {
        if (wageDistribution.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(swmWageDistributionService.insert(wageDistribution), HttpStatus.CREATED);
    }

    @Log("删除工资分配（工资系数）表")
    @ApiOperation("删除工资分配（工资系数）表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('wageDistribution:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            swmWageDistributionService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该工资分配（工资系数）表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改工资分配（工资系数）表")
    @ApiOperation("修改工资分配（工资系数）表")
    @PutMapping
    @PreAuthorize("@el.check('wageDistribution:edit')")
    public ResponseEntity update(@Validated(SwmWageDistribution.Update.class) @RequestBody SwmWageDistribution wageDistribution) {
        swmWageDistributionService.update(wageDistribution);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个工资分配（工资系数）表")
    @ApiOperation("获取单个工资分配（工资系数）表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('wageDistribution:list')")
    public ResponseEntity getWageDistribution(@PathVariable Long id) {
        return new ResponseEntity<>(swmWageDistributionService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询工资分配（工资系数）表（分页）")
    @ApiOperation("查询工资分配（工资系数）表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('wageDistribution:list')")
    public ResponseEntity getWageDistributionPage(SwmWageDistributionQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        setCriteria(criteria);
        return new ResponseEntity<>(swmWageDistributionService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询工资分配（工资系数）表（不分页）")
    @ApiOperation("查询工资分配（工资系数）表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('wageDistribution:list')")
    public ResponseEntity getWageDistributionNoPaging(SwmWageDistributionQueryCriteria criteria) {
        setCriteria(criteria);
        return new ResponseEntity<>(swmWageDistributionService.listAll(criteria), HttpStatus.OK);
    }

    public void setCriteria(SwmWageDistributionQueryCriteria criteria) {

    }

    @ErrorLog("导出工资分配（工资系数）表数据")
    @ApiOperation("导出工资分配（工资系数）表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('wageDistribution:list')")
    public void download(HttpServletResponse response, SwmWageDistributionQueryCriteria criteria) throws IOException {
        setCriteria(criteria);
        swmWageDistributionService.download(swmWageDistributionService.listAll(criteria), response);
    }

    @Log("生成工资分配")
    @ApiOperation("生成工资分配")
    @PostMapping(value = "/generateWageDistribution")
    @PreAuthorize("@el.check('wageDistribution:add')")
    public ResponseEntity generateWageDistribution(@RequestBody String period) {
        // 返回的数据不分页
        return new ResponseEntity<>(swmWageDistributionService.generateWageDistributionByMsp(period), HttpStatus.CREATED);
    }

    @Log("按所得期间删除工资分配")
    @ApiOperation("按所得期间删除工资分配")
    @DeleteMapping(value = "/removeByPeriod")
    @PreAuthorize("@el.check('wageDistribution:del')")
    public ResponseEntity removeByPeriod( @RequestBody String period) {
        swmWageDistributionService.removeByPeriod(period);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("批量修改分配系数")
    @ApiOperation("批量修改分配系数")
    @PutMapping("/batchUpdateWageDistribution")
    @PreAuthorize("@el.check('wageDistribution:edit')")
    public ResponseEntity batchUpdateWageDistribution(@RequestBody List<SwmWageDistribution> swmWageDistributions) {
        swmWageDistributionService.batchUpdateWageDistribution(swmWageDistributions);
        return new ResponseEntity(HttpStatus.OK);
    }

}
