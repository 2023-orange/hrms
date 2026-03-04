package com.sunten.hrms.swm.rest;

import com.sunten.hrms.exception.InfoCheckWarningMessException;
import com.sunten.hrms.swm.domain.SwmBonusExcel;
import com.sunten.hrms.swm.domain.SwmBonusPayment;
import com.sunten.hrms.swm.dto.SwmBonusPaymentQueryCriteria;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmBonusPaymentInterface;
import com.sunten.hrms.swm.dto.SwmBonusPaymentInterfaceQueryCriteria;
import com.sunten.hrms.swm.service.SwmBonusPaymentInterfaceService;
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
 * 奖金发放接口表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@RestController
@Api(tags = "奖金发放接口表")
@RequestMapping("/api/swm/bonusPaymentInterface")
public class SwmBonusPaymentInterfaceController {
    private static final String ENTITY_NAME = "bonusPaymentInterface";
    private final SwmBonusPaymentInterfaceService swmBonusPaymentInterfaceService;

    public SwmBonusPaymentInterfaceController(SwmBonusPaymentInterfaceService swmBonusPaymentInterfaceService) {
        this.swmBonusPaymentInterfaceService = swmBonusPaymentInterfaceService;
    }

    @Log("新增奖金发放接口表")
    @ApiOperation("新增奖金发放接口表")
    @PostMapping
    @PreAuthorize("@el.check('bonusPaymentInterface:add')")
    public ResponseEntity create(@Validated @RequestBody SwmBonusPaymentInterface bonusPaymentInterface) {
        if (bonusPaymentInterface.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(swmBonusPaymentInterfaceService.insert(bonusPaymentInterface), HttpStatus.CREATED);
    }

    @Log("删除奖金发放接口表")
    @ApiOperation("删除奖金发放接口表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('bonusPaymentInterface:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            swmBonusPaymentInterfaceService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该奖金发放接口表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改奖金发放接口表")
    @ApiOperation("修改奖金发放接口表")
    @PutMapping
    @PreAuthorize("@el.check('bonusPaymentInterface:edit')")
    public ResponseEntity update(@Validated(SwmBonusPaymentInterface.Update.class) @RequestBody SwmBonusPaymentInterface bonusPaymentInterface) {
        swmBonusPaymentInterfaceService.update(bonusPaymentInterface);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个奖金发放接口表")
    @ApiOperation("获取单个奖金发放接口表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('bonusPaymentInterface:list')")
    public ResponseEntity getBonusPaymentInterface(@PathVariable Long id) {
        return new ResponseEntity<>(swmBonusPaymentInterfaceService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询奖金发放接口表（分页）")
    @ApiOperation("查询奖金发放接口表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('bonusPaymentInterface:list')")
    public ResponseEntity getBonusPaymentInterfacePage(SwmBonusPaymentInterfaceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(swmBonusPaymentInterfaceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询奖金发放接口表（不分页）")
    @ApiOperation("查询奖金发放接口表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('bonusPaymentInterface:list')")
    public ResponseEntity getBonusPaymentInterfaceNoPaging(SwmBonusPaymentInterfaceQueryCriteria criteria) {
    return new ResponseEntity<>(swmBonusPaymentInterfaceService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出奖金发放接口表数据")
    @ApiOperation("导出奖金发放接口表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('bonusPaymentInterface:list')")
    public void download(HttpServletResponse response, SwmBonusPaymentInterfaceQueryCriteria criteria) throws IOException {
        swmBonusPaymentInterfaceService.download(swmBonusPaymentInterfaceService.listAll(criteria), response);
    }

    @Log("导入奖金发放")
    @ApiOperation("导入奖金发放")
    @PostMapping(value = "/importExcel")
    @PreAuthorize("@el.check('bonusPayment:edit', 'bonusPayment:add')")
    public ResponseEntity importExcel(@RequestBody SwmBonusExcel swmBonusExcel) {
        return new ResponseEntity<>(swmBonusPaymentInterfaceService.insertExcel(swmBonusExcel.getSwmBonusPaymentInterfaces(), swmBonusExcel.getReImportFlag()), HttpStatus.OK);
    }

    @Log("奖金发放导入数据汇总信息")
    @ApiOperation("奖金发放导入数据汇总信息")
    @PutMapping(value = "/getBonusPaymentSummaryByImportList")
    @PreAuthorize("@el.check('bonusPayment:edit', 'bonusPayment:add')")
    public ResponseEntity getBonusPaymentSummaryByImportList(@RequestBody SwmBonusPaymentInterfaceQueryCriteria criteria) {
        if (null == criteria.getSwmBonusPaymentInterfaceList() || criteria.getSwmBonusPaymentInterfaceList().size() == 0) {
            throw new InfoCheckWarningMessException("奖金发放导入数据，获取汇总结果失败");
        } else {
            return new ResponseEntity<>(swmBonusPaymentInterfaceService.getBonusPaymentSummaryByImportList(
                    criteria.getSwmBonusPaymentInterfaceList().stream().map(SwmBonusPaymentInterface::getWorkCard).collect(Collectors.toSet()),
                    criteria.getGroupIds()
            ), HttpStatus.OK);
        }
    }
}
