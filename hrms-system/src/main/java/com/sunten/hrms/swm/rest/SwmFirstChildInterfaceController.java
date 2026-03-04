package com.sunten.hrms.swm.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmFirstChildInterface;
import com.sunten.hrms.swm.dto.SwmFirstChildInterfaceQueryCriteria;
import com.sunten.hrms.swm.service.SwmFirstChildInterfaceService;
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
 * 第一胎子女信息登记表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2021-08-10
 */
@RestController
@Api(tags = "第一胎子女信息登记表")
@RequestMapping("/api/swm/firstChildInterface")
public class SwmFirstChildInterfaceController {
    private static final String ENTITY_NAME = "firstChildInterface";
    private final SwmFirstChildInterfaceService swmFirstChildInterfaceService;

    public SwmFirstChildInterfaceController(SwmFirstChildInterfaceService swmFirstChildInterfaceService) {
        this.swmFirstChildInterfaceService = swmFirstChildInterfaceService;
    }

    @Log("新增第一胎子女信息登记表")
    @ApiOperation("新增第一胎子女信息登记表")
    @PostMapping
    @PreAuthorize("@el.check('firstChildInterface:add')")
    public ResponseEntity create(@Validated @RequestBody SwmFirstChildInterface firstChildInterface) {
        if (firstChildInterface.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(swmFirstChildInterfaceService.insert(firstChildInterface), HttpStatus.CREATED);
    }

    @Log("删除第一胎子女信息登记表")
    @ApiOperation("删除第一胎子女信息登记表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('firstChildInterface:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            swmFirstChildInterfaceService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该第一胎子女信息登记表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改第一胎子女信息登记表")
    @ApiOperation("修改第一胎子女信息登记表")
    @PutMapping
    @PreAuthorize("@el.check('firstChildInterface:edit')")
    public ResponseEntity update(@Validated(SwmFirstChildInterface.Update.class) @RequestBody SwmFirstChildInterface firstChildInterface) {
        swmFirstChildInterfaceService.update(firstChildInterface);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个第一胎子女信息登记表")
    @ApiOperation("获取单个第一胎子女信息登记表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('firstChildInterface:list')")
    public ResponseEntity getFirstChildInterface(@PathVariable Long id) {
        return new ResponseEntity<>(swmFirstChildInterfaceService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询第一胎子女信息登记表（分页）")
    @ApiOperation("查询第一胎子女信息登记表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('firstChildInterface:list')")
    public ResponseEntity getFirstChildInterfacePage(SwmFirstChildInterfaceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(swmFirstChildInterfaceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询第一胎子女信息登记表（不分页）")
    @ApiOperation("查询第一胎子女信息登记表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('firstChildInterface:list')")
    public ResponseEntity getFirstChildInterfaceNoPaging(SwmFirstChildInterfaceQueryCriteria criteria) {
    return new ResponseEntity<>(swmFirstChildInterfaceService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出第一胎子女信息登记表数据")
    @ApiOperation("导出第一胎子女信息登记表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('firstChildInterface:list')")
    public void download(HttpServletResponse response, SwmFirstChildInterfaceQueryCriteria criteria) throws IOException {
        swmFirstChildInterfaceService.download(swmFirstChildInterfaceService.listAll(criteria), response);
    }

    @ErrorLog("导入第一胎子女信息")
    @ApiOperation("导入第一胎子女信息")
    @PostMapping(value = "/importFirstChildExcel")
    @PreAuthorize("@el.check('consolationCharge')")
    public ResponseEntity importFirstChildExcel(@RequestBody List<SwmFirstChildInterface> swmFirstChildInterfaces) {
        return new ResponseEntity<>(swmFirstChildInterfaceService.importFirstChildExcel(swmFirstChildInterfaces), HttpStatus.OK);
    }
}
