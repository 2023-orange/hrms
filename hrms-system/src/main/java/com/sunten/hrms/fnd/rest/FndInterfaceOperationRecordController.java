package com.sunten.hrms.fnd.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.fnd.domain.FndInterfaceOperationRecord;
import com.sunten.hrms.fnd.dto.FndInterfaceOperationRecordQueryCriteria;
import com.sunten.hrms.fnd.service.FndInterfaceOperationRecordService;
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
 * 接口操作记录表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-10-26
 */
@RestController
@Api(tags = "接口操作记录表")
@RequestMapping("/api/fnd/interfaceOperationRecord")
public class FndInterfaceOperationRecordController {
    private static final String ENTITY_NAME = "interfaceOperationRecord";
    private final FndInterfaceOperationRecordService fndInterfaceOperationRecordService;

    public FndInterfaceOperationRecordController(FndInterfaceOperationRecordService fndInterfaceOperationRecordService) {
        this.fndInterfaceOperationRecordService = fndInterfaceOperationRecordService;
    }

    @Log("新增接口操作记录表")
    @ApiOperation("新增接口操作记录表")
    @PostMapping
    @PreAuthorize("@el.check('interfaceOperationRecord:add')")
    public ResponseEntity create(@Validated @RequestBody FndInterfaceOperationRecord interfaceOperationRecord) {
        if (interfaceOperationRecord.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(fndInterfaceOperationRecordService.insert(interfaceOperationRecord), HttpStatus.CREATED);
    }

    @Log("删除接口操作记录表")
    @ApiOperation("删除接口操作记录表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('interfaceOperationRecord:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            fndInterfaceOperationRecordService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该接口操作记录表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改接口操作记录表")
    @ApiOperation("修改接口操作记录表")
    @PutMapping
    @PreAuthorize("@el.check('interfaceOperationRecord:edit')")
    public ResponseEntity update(@Validated(FndInterfaceOperationRecord.Update.class) @RequestBody FndInterfaceOperationRecord interfaceOperationRecord) {
        fndInterfaceOperationRecordService.update(interfaceOperationRecord);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个接口操作记录表")
    @ApiOperation("获取单个接口操作记录表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('interfaceOperationRecord:list')")
    public ResponseEntity getInterfaceOperationRecord(@PathVariable Long id) {
        return new ResponseEntity<>(fndInterfaceOperationRecordService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询接口操作记录表（分页）")
    @ApiOperation("查询接口操作记录表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('interfaceOperationRecord:list')")
    public ResponseEntity getInterfaceOperationRecordPage(FndInterfaceOperationRecordQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(fndInterfaceOperationRecordService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询接口操作记录表（不分页）")
    @ApiOperation("查询接口操作记录表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('interfaceOperationRecord:list')")
    public ResponseEntity getInterfaceOperationRecordNoPaging(FndInterfaceOperationRecordQueryCriteria criteria) {
    return new ResponseEntity<>(fndInterfaceOperationRecordService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出接口操作记录表数据")
    @ApiOperation("导出接口操作记录表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('interfaceOperationRecord:list')")
    public void download(HttpServletResponse response, FndInterfaceOperationRecordQueryCriteria criteria) throws IOException {
        fndInterfaceOperationRecordService.download(fndInterfaceOperationRecordService.listAll(criteria), response);
    }
}
