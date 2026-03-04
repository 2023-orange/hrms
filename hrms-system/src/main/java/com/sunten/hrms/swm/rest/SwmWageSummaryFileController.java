package com.sunten.hrms.swm.rest;

import cn.hutool.json.JSONArray;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.domain.AdvancedQuery;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.swm.dao.SwmEmployeeDao;
import com.sunten.hrms.swm.dao.SwmWageSummaryFileDao;
import com.sunten.hrms.swm.domain.SwmEmployee;
import com.sunten.hrms.swm.dto.SwmWageSummaryFileDTO;
import com.sunten.hrms.swm.pdfView.PdfUtil;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmWageSummaryFile;
import com.sunten.hrms.swm.dto.SwmWageSummaryFileQueryCriteria;
import com.sunten.hrms.swm.service.SwmWageSummaryFileService;
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
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 工资汇总归档表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-12-25
 */
@RestController
@Api(tags = "工资汇总归档表")
@RequestMapping("/api/swm/wageSummaryFile")
public class SwmWageSummaryFileController {
    private static final String ENTITY_NAME = "wageSummaryFile";
    private final SwmWageSummaryFileService swmWageSummaryFileService;
    private final SwmWageSummaryFileDao swmWageSummaryFileDao;
    private final FndUserService fndUserService;
    private final SwmEmployeeDao swmEmployeeDao;

    public SwmWageSummaryFileController(SwmWageSummaryFileService swmWageSummaryFileService, SwmWageSummaryFileDao swmWageSummaryFileDao, FndUserService fndUserService,
                                        SwmEmployeeDao swmEmployeeDao) {
        this.swmWageSummaryFileService = swmWageSummaryFileService;
        this.swmWageSummaryFileDao = swmWageSummaryFileDao;
        this.fndUserService = fndUserService;
        this.swmEmployeeDao = swmEmployeeDao;
    }

    @Log("新增工资汇总归档表")
    @ApiOperation("新增工资汇总归档表")
    @PostMapping
    @PreAuthorize("@el.check('wageSummaryFile:add')")
    public ResponseEntity create(@Validated @RequestBody SwmWageSummaryFile wageSummaryFile) {
        if (wageSummaryFile.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(swmWageSummaryFileService.insert(wageSummaryFile), HttpStatus.CREATED);
    }

    @Log("删除工资汇总归档表")
    @ApiOperation("删除工资汇总归档表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('wageSummaryFile:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            swmWageSummaryFileService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该工资汇总归档表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改工资汇总归档表")
    @ApiOperation("修改工资汇总归档表")
    @PutMapping
    @PreAuthorize("@el.check('wageSummaryFile:edit')")
    public ResponseEntity update(@Validated(SwmWageSummaryFile.Update.class) @RequestBody SwmWageSummaryFile wageSummaryFile) {
        swmWageSummaryFileService.update(wageSummaryFile);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个工资汇总归档表")
    @ApiOperation("获取单个工资汇总归档表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('wageSummaryFile:list')")
    public ResponseEntity getWageSummaryFile(@PathVariable Long id) {
        return new ResponseEntity<>(swmWageSummaryFileService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询工资汇总归档表（分页）")
    @ApiOperation("查询工资汇总归档表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('wageSummaryFile:list')")
    public ResponseEntity getWageSummaryFilePage(SwmWageSummaryFileQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        setCriteria(criteria);
        return new ResponseEntity<>(swmWageSummaryFileService.listAll(criteria, pageable), HttpStatus.OK);
    }

    private void setCriteria(SwmWageSummaryFileQueryCriteria criteria) {

    }

    @ErrorLog("查询工资汇总归档表（不分页）")
    @ApiOperation("查询工资汇总归档表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('wageSummaryFile:list')")
    public ResponseEntity getWageSummaryFileNoPaging(SwmWageSummaryFileQueryCriteria criteria) {
        setCriteria(criteria);
        return new ResponseEntity<>(swmWageSummaryFileService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("单人工资汇总统计表（不分页）")
    @ApiOperation("单人工资汇总统计表（不分页）")
    @GetMapping(value = "/statistics")
    @PreAuthorize("@el.check('wageSummaryFile:list','employee:list')")
    public ResponseEntity getWageSummaryFileStatistics(String workCard) {
        List<SwmWageSummaryFileDTO> wageSummaryFileStatistics = swmWageSummaryFileService.listStatistics(workCard);
        return new ResponseEntity<>(wageSummaryFileStatistics, HttpStatus.OK);
    }

    @ErrorLog("导出工资汇总归档表数据")
    @ApiOperation("导出工资汇总归档表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('wageSummaryFile:list')")
    public void download(HttpServletResponse response, SwmWageSummaryFileQueryCriteria criteria) throws IOException {
        setCriteria(criteria);
        swmWageSummaryFileService.download(swmWageSummaryFileService.listAll(criteria), response);
    }

    @ErrorLog("工资汇总计税导出接口")
    @ApiOperation("工资汇总计税导出接口")
    @GetMapping(value = "/exportForTaxCalculation")
    @PreAuthorize("@el.check('wageSummaryFile:list')")
    public void exportForTaxCalculation(HttpServletResponse response, SwmWageSummaryFileQueryCriteria criteria) throws  IOException {
        // 根据criteria中的incomePeriod进行数据获取
        swmWageSummaryFileService.exportForTaxCalculation(swmWageSummaryFileService.listAll(criteria), response);
    }

    @ErrorLog("按成本中心汇总")
    @ApiOperation("按成本中心汇总")
    @GetMapping(value = "/exportForCostCenter")
    @PreAuthorize("@el.check('wageSummaryFile:list')")
    public void exportForCostCenter(HttpServletResponse response, SwmWageSummaryFileQueryCriteria criteria) throws  IOException  {
        swmWageSummaryFileService.exportForCostCenter(response, criteria);
    }

    @ErrorLog("按个人汇总导出")
    @ApiOperation("按个人汇总导出")
    @GetMapping(value = "/exportForPerson")
    @PreAuthorize("@el.check('wageSummaryFile:list')")
    public void exportForPerson(HttpServletResponse response, SwmWageSummaryFileQueryCriteria criteria) throws Exception {
        List<SwmWageSummaryFile> swmWageSummaryFiles = swmWageSummaryFileDao.listByPerson(criteria.getIncomePeriod());
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        String name = "";
        if (null == user.getEmployee()) {
            name = "用户无绑定员工";
        } else {
            name = swmEmployeeDao.getByEmpId(user.getEmployee().getId()).getName();
        }
        PdfUtil pdfUtil = new PdfUtil(name);
        pdfUtil.exportPdf(response, swmWageSummaryFiles,criteria.getIncomePeriod());
    }

    @ErrorLog("返回个人薪资明细")
    @ApiOperation("返回个人薪资明细")
    @GetMapping(value = "/grantSumSalaryReading")
    @PreAuthorize("@el.check('mySalary:list')")
    public ResponseEntity grantSumSalaryReading(String year){
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        if (null == user.getEmployee()) {
            throw new InfoCheckWarningMessException("当前用户尚未绑定用户， 请连续管理员进行绑定");
        }
        List<SwmWageSummaryFileDTO> swmWageSummaryFiles = swmWageSummaryFileService.getGrantDetail(user.getEmployee().getId(), year);
        return new ResponseEntity<>(swmWageSummaryFiles, HttpStatus.OK);
    }







}
