package com.sunten.hrms.td.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.td.domain.TdJobAuthenticationInterface;
import com.sunten.hrms.td.dto.TdJobAuthenticationInterfaceQueryCriteria;
import com.sunten.hrms.td.service.TdJobAuthenticationInterfaceService;
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
 * 上岗认证接口表 前端控制器
 * </p>
 *
 * @author xukai
 * @since 2021-10-11
 */
@RestController
@Api(tags = "上岗认证接口表")
@RequestMapping("/api/td/jobAuthenticationInterface")
public class TdJobAuthenticationInterfaceController {
    private static final String ENTITY_NAME = "jobAuthenticationInterface";
    private final TdJobAuthenticationInterfaceService tdJobAuthenticationInterfaceService;

    public TdJobAuthenticationInterfaceController(TdJobAuthenticationInterfaceService tdJobAuthenticationInterfaceService) {
        this.tdJobAuthenticationInterfaceService = tdJobAuthenticationInterfaceService;
    }

    @Log("新增上岗认证接口表")
    @ApiOperation("新增上岗认证接口表")
    @PostMapping
    @PreAuthorize("@el.check('jobAuthentication:add')")
    public ResponseEntity create(@Validated @RequestBody TdJobAuthenticationInterface jobAuthenticationInterface) {
        if (jobAuthenticationInterface.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(tdJobAuthenticationInterfaceService.insert(jobAuthenticationInterface), HttpStatus.CREATED);
    }

    @Log("删除上岗认证接口表")
    @ApiOperation("删除上岗认证接口表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('jobAuthentication:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            tdJobAuthenticationInterfaceService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该上岗认证接口表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改上岗认证接口表")
    @ApiOperation("修改上岗认证接口表")
    @PutMapping
    @PreAuthorize("@el.check('jobAuthentication:edit')")
    public ResponseEntity update(@Validated(TdJobAuthenticationInterface.Update.class) @RequestBody TdJobAuthenticationInterface jobAuthenticationInterface) {
        tdJobAuthenticationInterfaceService.update(jobAuthenticationInterface);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个上岗认证接口表")
    @ApiOperation("获取单个上岗认证接口表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('jobAuthentication:list')")
    public ResponseEntity getJobAuthenticationInterface(@PathVariable Long id) {
        return new ResponseEntity<>(tdJobAuthenticationInterfaceService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询上岗认证接口表（分页）")
    @ApiOperation("查询上岗认证接口表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('jobAuthentication:list')")
    public ResponseEntity getJobAuthenticationInterfacePage(TdJobAuthenticationInterfaceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(tdJobAuthenticationInterfaceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询上岗认证接口表（不分页）")
    @ApiOperation("查询上岗认证接口表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('jobAuthentication:list')")
    public ResponseEntity getJobAuthenticationInterfaceNoPaging(TdJobAuthenticationInterfaceQueryCriteria criteria) {
    return new ResponseEntity<>(tdJobAuthenticationInterfaceService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出上岗认证接口表数据")
    @ApiOperation("导出上岗认证接口表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('jobAuthentication:list')")
    public void download(HttpServletResponse response, TdJobAuthenticationInterfaceQueryCriteria criteria) throws IOException {
        tdJobAuthenticationInterfaceService.download(tdJobAuthenticationInterfaceService.listAll(criteria), response);
    }

    @Log("上岗认证数据导入")
    @ApiOperation("上岗认证数据导入")
    @PutMapping(value="/importAuthenticationInterface")
    @PreAuthorize("@el.check('jobAuthentication:edit')")
    public ResponseEntity importAuthentication(@RequestBody List<TdJobAuthenticationInterface> jobAuthenticationInterfaces) {
        tdJobAuthenticationInterfaceService.importAuthenticationByExcel(jobAuthenticationInterfaces);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
