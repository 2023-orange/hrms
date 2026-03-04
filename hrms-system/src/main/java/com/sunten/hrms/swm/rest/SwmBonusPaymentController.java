package com.sunten.hrms.swm.rest;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.fnd.dto.FndUserDTO;
import com.sunten.hrms.fnd.service.FndUserService;
import com.sunten.hrms.swm.dao.SwmBonusDao;
import com.sunten.hrms.swm.dao.SwmBonusPaymentDao;
import com.sunten.hrms.swm.dao.SwmEmployeeDao;
import com.sunten.hrms.swm.domain.SwmBonus;
import com.sunten.hrms.swm.domain.SwmWageSummaryFile;
import com.sunten.hrms.swm.dto.SwmBonusDTO;
import com.sunten.hrms.swm.dto.SwmWageSummaryFileQueryCriteria;
import com.sunten.hrms.swm.pdfView.PdfUtil;
import com.sunten.hrms.swm.service.SwmBonusService;
import com.sunten.hrms.utils.SecurityUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmBonusPayment;
import com.sunten.hrms.swm.dto.SwmBonusPaymentQueryCriteria;
import com.sunten.hrms.swm.service.SwmBonusPaymentService;
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
 * 奖金发放表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@RestController
@Api(tags = "奖金发放表")
@RequestMapping("/api/swm/bonusPayment")
public class SwmBonusPaymentController {
    private static final String ENTITY_NAME = "bonusPayment";
    private final SwmBonusPaymentService swmBonusPaymentService;
    private final SwmBonusDao swmBonusDao;
    private final SwmBonusPaymentDao swmBonusPaymentDao;
    private final SwmEmployeeDao swmEmployeeDao;
    private final FndUserService fndUserService;

    public SwmBonusPaymentController(SwmBonusPaymentService swmBonusPaymentService, SwmBonusDao swmBonusDao,
                                     SwmBonusPaymentDao swmBonusPaymentDao, SwmEmployeeDao swmEmployeeDao,
                                     FndUserService fndUserService) {
        this.swmBonusPaymentService = swmBonusPaymentService;
        this.swmBonusDao = swmBonusDao;
        this.swmBonusPaymentDao = swmBonusPaymentDao;
        this.swmEmployeeDao = swmEmployeeDao;
        this.fndUserService = fndUserService;
    }

//    @Log("新增奖金发放表")
//    @ApiOperation("新增奖金发放表")
//    @PostMapping
//    @PreAuthorize("@el.check('bonusPayment:add')")
//    public ResponseEntity create(@Validated @RequestBody SwmBonusPayment bonusPayment) {
//        if (bonusPayment.getId()  != null) {
//            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
//        }
//        return new ResponseEntity<>(swmBonusPaymentService.insert(bonusPayment), HttpStatus.CREATED);
//    }

    @Log("奖金发放接口")
    @ApiOperation("奖金发放接口")
    @PostMapping(value="/generateBonusPayment")
    @PreAuthorize("@el.check('bonusPayment:add')")
    public ResponseEntity create(@Validated @RequestBody SwmBonus swmBonus) {
        return new ResponseEntity<>(swmBonusPaymentService.generateBonusPayment(swmBonus), HttpStatus.CREATED);
    }

    @Log("删除奖金发放表")
    @ApiOperation("删除奖金发放表")
    @DeleteMapping(value = "/{bonusId}")
    @PreAuthorize("@el.check('bonusPayment:del')")
    public ResponseEntity delete(@PathVariable Long bonusId) {
        swmBonusPaymentService.delete(bonusId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改奖金发放表")
    @ApiOperation("修改奖金发放表")
    @PutMapping
    @PreAuthorize("@el.check('bonusPayment:edit')")
    public ResponseEntity update(@Validated(SwmBonusPayment.Update.class) @RequestBody SwmBonusPayment bonusPayment) {
        swmBonusPaymentService.update(bonusPayment);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个奖金发放表")
    @ApiOperation("获取单个奖金发放表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('bonusPayment:list')")
    public ResponseEntity getBonusPayment(@PathVariable Long id) {
        return new ResponseEntity<>(swmBonusPaymentService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询奖金发放表（分页）")
    @ApiOperation("查询奖金发放表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('bonusPayment:list')")
    public ResponseEntity getBonusPaymentPage(SwmBonusPaymentQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(swmBonusPaymentService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询个人的奖金发放")
    @ApiOperation("查询个人的奖金发放")
    @GetMapping(value = "/selfBonusList")
    @PreAuthorize("@el.check('mySalary:list')")
    public ResponseEntity selfBonusList(@PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        if (null == user.getEmployee()) {
            throw new InfoCheckWarningMessException("当前用户没有绑定员工信息");
        }
        SwmBonusPaymentQueryCriteria swmBonusPaymentQueryCriteria = new SwmBonusPaymentQueryCriteria();
        swmBonusPaymentQueryCriteria.setEmployeeId(user.getEmployee().getId());
        return new ResponseEntity<>(swmBonusPaymentService.selfListAll(swmBonusPaymentQueryCriteria, pageable), HttpStatus.OK);
    }



    @ErrorLog("查询奖金发放表（不分页）")
    @ApiOperation("查询奖金发放表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('bonusPayment:list')")
    public ResponseEntity getBonusPaymentNoPaging(SwmBonusPaymentQueryCriteria criteria) {
    return new ResponseEntity<>(swmBonusPaymentService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出奖金发放表数据")
    @ApiOperation("导出奖金发放表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('bonusPayment:list')")
    public void download(HttpServletResponse response, SwmBonusPaymentQueryCriteria criteria) throws IOException {
        swmBonusPaymentService.download(swmBonusPaymentService.listAll(criteria), response);
    }

    @ErrorLog("导出奖金发放PDF")
    @ApiOperation("导出奖金发放PDF")
    @GetMapping(value = "/exportWithPdf")
    @PreAuthorize("@el.check('bonusPayment:list')")
    public void exportForPerson(HttpServletResponse response, SwmBonus swmBonus) throws Exception {
        SwmBonus swmBonusReal = swmBonusDao.getByKey(swmBonus.getId());
        List<SwmBonusPayment> swmBonusPayments = swmBonusPaymentDao.exportBonusPaymentByBonusId(swmBonusReal.getId());
        FndUserDTO user = fndUserService.getByName(SecurityUtils.getUsername());
        String name = "";
        if (null == user.getEmployee()) {
            name = "用户无绑定员工";
        } else {
            name = swmEmployeeDao.getByEmpId(user.getEmployee().getId()).getName();
        }
        PdfUtil pdfUtil = new PdfUtil(name);
        pdfUtil.exportBonusPaymentPdf(response,swmBonusPayments, swmBonusReal);
    }

    @Log("开放奖金发放查询")
    @ApiOperation("开放奖金发放查询")
    @PutMapping(value = "/grantAll/{bonusId}")
    @PreAuthorize("@el.check('bonusPayment:edit')")
    public void grantAll(@PathVariable Long bonusId) {
        swmBonusPaymentService.grantAllBonus(bonusId, true);
    }

    @Log("取消开放奖金发放查询")
    @ApiOperation("取消开放奖金发放查询")
    @PutMapping(value = "/cancelGrantAll/{bonusId}")
    @PreAuthorize("@el.check('bonusPayment:edit')")
    public void cancelGrantAll(@PathVariable Long bonusId) {
        swmBonusPaymentService.grantAllBonus(bonusId, false);
    }

}
