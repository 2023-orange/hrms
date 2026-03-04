package com.sunten.hrms.kpi.rest;

import com.sunten.hrms.kpi.dao.KpiAnnualDao;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.kpi.domain.KpiAnnual;
import com.sunten.hrms.kpi.dto.KpiAnnualQueryCriteria;
import com.sunten.hrms.kpi.service.KpiAnnualService;
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
 * KPI考核年度概况 前端控制器
 * </p>
 *
 * @author zhoujy
 * @since 2023-11-27
 */
@RestController
@Api(tags = "KPI考核年度概况")
@RequestMapping("/api/kpi/annual")
public class KpiAnnualController {
    private static final String ENTITY_NAME = "annual";
    private final KpiAnnualService kpiAnnualService;
    private final KpiAnnualDao kpiAnnualDao;

    public KpiAnnualController(KpiAnnualService kpiAnnualService, KpiAnnualDao kpiAnnualDao) {
        this.kpiAnnualService = kpiAnnualService;
        this.kpiAnnualDao = kpiAnnualDao;
    }

    @Log("新增KPI考核年度概况")
    @ApiOperation("新增KPI考核年度概况")
    @PostMapping
    @PreAuthorize("@el.check('annual:add')")
    public ResponseEntity create(@Validated @RequestBody KpiAnnual annual) {
        if (annual.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        if (kpiAnnualDao.getEnabledAnnual() > 0 )
        {
            return new ResponseEntity<>("存在未为终稿定稿的年份，不能生成年度考核数据", HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(kpiAnnualService.insert(annual), HttpStatus.CREATED);
        }
    }

    @Log("删除KPI考核年度概况")
    @ApiOperation("删除KPI考核年度概况")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('annual:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            kpiAnnualService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该KPI考核年度概况存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改KPI考核年度概况")
    @ApiOperation("修改KPI考核年度概况")
    @PutMapping
    @PreAuthorize("@el.check('annual:edit')")
    public ResponseEntity update(@Validated(KpiAnnual.Update.class) @RequestBody KpiAnnual annual) {
        kpiAnnualService.update(annual);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个KPI考核年度概况")
    @ApiOperation("获取单个KPI考核年度概况")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('annual:list')")
    public ResponseEntity getAnnual(@PathVariable Long id) {
        return new ResponseEntity<>(kpiAnnualService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询KPI考核年度概况（分页）")
    @ApiOperation("查询KPI考核年度概况（分页）")
    @GetMapping
    @PreAuthorize("@el.check('annual:list')")
    public ResponseEntity getAnnualPage(KpiAnnualQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(kpiAnnualService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询KPI考核年度概况（不分页）")
    @ApiOperation("查询KPI考核年度概况（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('annual:list','assessmentIndicators:list')")
    public ResponseEntity getAnnualNoPaging(KpiAnnualQueryCriteria criteria) {
    return new ResponseEntity<>(kpiAnnualService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出KPI考核年度概况数据")
    @ApiOperation("导出KPI考核年度概况数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('annual:list')")
    public void download(HttpServletResponse response, KpiAnnualQueryCriteria criteria) throws IOException {
        kpiAnnualService.download(kpiAnnualService.listAll(criteria), response);
    }

    @ErrorLog("获取年度概况最新年份")
    @ApiOperation("获取年度概况最新年份")
    @GetMapping(value = "/getNowDate")
    @PreAuthorize("@el.check('annual:list','assessmentIndicators:list')")
    public ResponseEntity getDepartmentTree() {
        return new ResponseEntity<>(kpiAnnualService.getYearList(), HttpStatus.OK);
    }

    // 用于判断能否修改KPI考核指标编制
    @ErrorLog("根据年份获取是否存在不是终稿定稿和不是数据初始化的情况")
    @ApiOperation("根据年份获取是否存在不是终稿定稿和不是数据初始化的情况")
    @GetMapping(value = "/getEnabledAnnualByYear")
    public ResponseEntity getEnabledAnnualByYear(KpiAnnualQueryCriteria criteria) {
        return new ResponseEntity<>(kpiAnnualDao.getEnabledAnnualByYear(criteria), HttpStatus.OK);
    }

    // 用于判断能否修改部门树
    @ErrorLog("根据年份获取是否存在不是终稿定稿的情况")
    @ApiOperation("根据年份获取是否存在不是终稿定稿的情况")
    @GetMapping(value = "/getDataBeginByYear")
    public ResponseEntity getDataBeginByYear(KpiAnnualQueryCriteria criteria) {
        return new ResponseEntity<>(kpiAnnualDao.getDataBeginByYear(criteria), HttpStatus.OK);
    }
}
