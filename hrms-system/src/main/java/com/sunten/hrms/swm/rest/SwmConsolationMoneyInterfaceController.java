package com.sunten.hrms.swm.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmConsolationMoneyInterface;
import com.sunten.hrms.swm.dto.SwmConsolationMoneyInterfaceQueryCriteria;
import com.sunten.hrms.swm.service.SwmConsolationMoneyInterfaceService;
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
 * 慰问金接口表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2021-08-05
 */
@RestController
@Api(tags = "慰问金接口表")
@RequestMapping("/api/swm/consolationMoneyInterface")
public class SwmConsolationMoneyInterfaceController {
    private static final String ENTITY_NAME = "consolationMoneyInterface";
    private final SwmConsolationMoneyInterfaceService swmConsolationMoneyInterfaceService;

    public SwmConsolationMoneyInterfaceController(SwmConsolationMoneyInterfaceService swmConsolationMoneyInterfaceService) {
        this.swmConsolationMoneyInterfaceService = swmConsolationMoneyInterfaceService;
    }

    @Log("新增慰问金接口表")
    @ApiOperation("新增慰问金接口表")
    @PostMapping
    @PreAuthorize("@el.check('consolationMoneyInterface:add')")
    public ResponseEntity create(@Validated @RequestBody SwmConsolationMoneyInterface consolationMoneyInterface) {
        if (consolationMoneyInterface.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(swmConsolationMoneyInterfaceService.insert(consolationMoneyInterface), HttpStatus.CREATED);
    }

    @Log("删除慰问金接口表")
    @ApiOperation("删除慰问金接口表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('consolationMoneyInterface:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            swmConsolationMoneyInterfaceService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该慰问金接口表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改慰问金接口表")
    @ApiOperation("修改慰问金接口表")
    @PutMapping
    @PreAuthorize("@el.check('consolationMoneyInterface:edit')")
    public ResponseEntity update(@Validated(SwmConsolationMoneyInterface.Update.class) @RequestBody SwmConsolationMoneyInterface consolationMoneyInterface) {
        swmConsolationMoneyInterfaceService.update(consolationMoneyInterface);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个慰问金接口表")
    @ApiOperation("获取单个慰问金接口表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('consolationMoneyInterface:list')")
    public ResponseEntity getConsolationMoneyInterface(@PathVariable Long id) {
        return new ResponseEntity<>(swmConsolationMoneyInterfaceService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询慰问金接口表（分页）")
    @ApiOperation("查询慰问金接口表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('consolationMoney:list')")
    public ResponseEntity getConsolationMoneyInterfacePage(SwmConsolationMoneyInterfaceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(swmConsolationMoneyInterfaceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询慰问金接口表（不分页）")
    @ApiOperation("查询慰问金接口表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('consolationMoney:list')")
    public ResponseEntity getConsolationMoneyInterfaceNoPaging(SwmConsolationMoneyInterfaceQueryCriteria criteria) {
    return new ResponseEntity<>(swmConsolationMoneyInterfaceService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出慰问金接口表数据")
    @ApiOperation("导出慰问金接口表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('consolationMoney:list')")
    public void download(HttpServletResponse response, SwmConsolationMoneyInterfaceQueryCriteria criteria) throws IOException {
        swmConsolationMoneyInterfaceService.download(swmConsolationMoneyInterfaceService.listAll(criteria), response);
    }

    @ErrorLog("导入未上系统前慰问金")
    @ApiOperation("导入未上系统前慰问金")
    @PostMapping(value = "/insertOldestConsolationMoney")
    @PreAuthorize("@el.check('consolationCharge')")
    public ResponseEntity insertOldestConsolationMoney(@RequestBody List<SwmConsolationMoneyInterface> swmConsolationMoneyInterfaces) {
        return new ResponseEntity<>(swmConsolationMoneyInterfaceService.importExcel(swmConsolationMoneyInterfaces, "oldest"), HttpStatus.OK);
    }

    @ErrorLog("导入审批并发放完的慰问金")
    @ApiOperation("导入审批并发放完的慰问金")
    @PostMapping(value = "/insertReleasedConsolationMoney")
    @PreAuthorize("@el.check('consolationCharge')")
    public ResponseEntity insertReleasedConsolationMoney(@RequestBody List<SwmConsolationMoneyInterface> swmConsolationMoneyInterfaces) {
        return new ResponseEntity<>(swmConsolationMoneyInterfaceService.importExcel(swmConsolationMoneyInterfaces, "released"), HttpStatus.OK);
    }
}
