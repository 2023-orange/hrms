package com.sunten.hrms.swm.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.swm.domain.SwmEntryBatchSettingHistory;
import com.sunten.hrms.swm.dto.SwmEntryBatchSettingHistoryQueryCriteria;
import com.sunten.hrms.swm.service.SwmEntryBatchSettingHistoryService;
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
 * 人员薪资条目批量设置历史表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-11-24
 */
@RestController
@Api(tags = "人员薪资条目批量设置历史表")
@RequestMapping("/api/swm/entryBatchSettingHistory")
public class SwmEntryBatchSettingHistoryController {
    private static final String ENTITY_NAME = "entryBatchSettingHistory";
    private final SwmEntryBatchSettingHistoryService swmEntryBatchSettingHistoryService;

    public SwmEntryBatchSettingHistoryController(SwmEntryBatchSettingHistoryService swmEntryBatchSettingHistoryService) {
        this.swmEntryBatchSettingHistoryService = swmEntryBatchSettingHistoryService;
    }

    @Log("新增人员薪资条目批量设置历史表")
    @ApiOperation("新增人员薪资条目批量设置历史表")
    @PostMapping
    @PreAuthorize("@el.check('entryBatchSettingHistory:add')")
    public ResponseEntity create(@Validated @RequestBody SwmEntryBatchSettingHistory entryBatchSettingHistory) {
        if (entryBatchSettingHistory.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(swmEntryBatchSettingHistoryService.insert(entryBatchSettingHistory), HttpStatus.CREATED);
    }

    @Log("删除人员薪资条目批量设置历史表")
    @ApiOperation("删除人员薪资条目批量设置历史表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('entryBatchSettingHistory:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            swmEntryBatchSettingHistoryService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该人员薪资条目批量设置历史表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改人员薪资条目批量设置历史表")
    @ApiOperation("修改人员薪资条目批量设置历史表")
    @PutMapping
    @PreAuthorize("@el.check('entryBatchSettingHistory:edit')")
    public ResponseEntity update(@Validated(SwmEntryBatchSettingHistory.Update.class) @RequestBody SwmEntryBatchSettingHistory entryBatchSettingHistory) {
        swmEntryBatchSettingHistoryService.update(entryBatchSettingHistory);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个人员薪资条目批量设置历史表")
    @ApiOperation("获取单个人员薪资条目批量设置历史表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('entryBatchSettingHistory:list')")
    public ResponseEntity getEntryBatchSettingHistory(@PathVariable Long id) {
        return new ResponseEntity<>(swmEntryBatchSettingHistoryService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询人员薪资条目批量设置历史表（分页）")
    @ApiOperation("查询人员薪资条目批量设置历史表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('entryBatchSettingHistory:list')")
    public ResponseEntity getEntryBatchSettingHistoryPage(SwmEntryBatchSettingHistoryQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(swmEntryBatchSettingHistoryService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询人员薪资条目批量设置历史表（不分页）")
    @ApiOperation("查询人员薪资条目批量设置历史表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('entryBatchSettingHistory:list')")
    public ResponseEntity getEntryBatchSettingHistoryNoPaging(SwmEntryBatchSettingHistoryQueryCriteria criteria) {
    return new ResponseEntity<>(swmEntryBatchSettingHistoryService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出人员薪资条目批量设置历史表数据")
    @ApiOperation("导出人员薪资条目批量设置历史表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('entryBatchSettingHistory:list')")
    public void download(HttpServletResponse response, SwmEntryBatchSettingHistoryQueryCriteria criteria) throws IOException {
        swmEntryBatchSettingHistoryService.download(swmEntryBatchSettingHistoryService.listAll(criteria), response);
    }
}
