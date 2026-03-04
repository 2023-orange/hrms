package com.sunten.hrms.fnd.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.fnd.domain.FndServerInformation;
import com.sunten.hrms.fnd.dto.FndServerInformationQueryCriteria;
import com.sunten.hrms.fnd.service.FndServerInformationService;
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
 * 服务器信息表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2024-06-06
 */
@RestController
@Api(tags = "服务器信息表")
@RequestMapping("/api/fnd/serverInformation")
public class FndServerInformationController {
    private static final String ENTITY_NAME = "serverInformation";
    private final FndServerInformationService fndServerInformationService;

    public FndServerInformationController(FndServerInformationService fndServerInformationService) {
        this.fndServerInformationService = fndServerInformationService;
    }

    @Log("新增服务器信息表")
    @ApiOperation("新增服务器信息表")
    @PostMapping
    @PreAuthorize("@el.check('serverInformation:add')")
    public ResponseEntity create(@Validated @RequestBody FndServerInformation serverInformation) {
        if (serverInformation.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(fndServerInformationService.insert(serverInformation), HttpStatus.CREATED);
    }

    @Log("删除服务器信息表")
    @ApiOperation("删除服务器信息表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('serverInformation:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            fndServerInformationService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该服务器信息表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改服务器信息表")
    @ApiOperation("修改服务器信息表")
    @PutMapping
    @PreAuthorize("@el.check('serverInformation:edit')")
    public ResponseEntity update(@Validated(FndServerInformation.Update.class) @RequestBody FndServerInformation serverInformation) {
        fndServerInformationService.update(serverInformation);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个服务器信息表")
    @ApiOperation("获取单个服务器信息表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('serverInformation:list')")
    public ResponseEntity getServerInformation(@PathVariable Long id) {
        return new ResponseEntity<>(fndServerInformationService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询服务器信息表（分页）")
    @ApiOperation("查询服务器信息表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('serverInformation:list')")
    public ResponseEntity getServerInformationPage(FndServerInformationQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(fndServerInformationService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询服务器信息表（不分页）")
    @ApiOperation("查询服务器信息表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('serverInformation:list')")
    public ResponseEntity getServerInformationNoPaging(FndServerInformationQueryCriteria criteria) {
    return new ResponseEntity<>(fndServerInformationService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出服务器信息表数据")
    @ApiOperation("导出服务器信息表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('serverInformation:list')")
    public void download(HttpServletResponse response, FndServerInformationQueryCriteria criteria) throws IOException {
        fndServerInformationService.download(fndServerInformationService.listAll(criteria), response);
    }
}
