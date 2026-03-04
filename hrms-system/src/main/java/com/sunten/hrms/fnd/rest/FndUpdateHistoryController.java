package com.sunten.hrms.fnd.rest;

import com.sunten.hrms.fnd.aop.ErrorLog;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.fnd.domain.FndUpdateHistory;
import com.sunten.hrms.fnd.dto.FndUpdateHistoryQueryCriteria;
import com.sunten.hrms.fnd.service.FndUpdateHistoryService;
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
 * 历史修改表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-07-24
 */
@RestController
@Api(tags = "历史修改表")
@RequestMapping("/api/fnd/updateHistory")
public class FndUpdateHistoryController {
    private static final String ENTITY_NAME = "updateHistory";
    private final FndUpdateHistoryService fndUpdateHistoryService;

    public FndUpdateHistoryController(FndUpdateHistoryService fndUpdateHistoryService) {
        this.fndUpdateHistoryService = fndUpdateHistoryService;
    }

    @Log("新增历史修改表")
    @ApiOperation("新增历史修改表")
    @PostMapping
    @PreAuthorize("@el.check('updateHistory:add')")
    public ResponseEntity create(@Validated @RequestBody FndUpdateHistory updateHistory) {
        if (updateHistory.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(fndUpdateHistoryService.insert(updateHistory), HttpStatus.CREATED);
    }

    @Log("删除历史修改表")
    @ApiOperation("删除历史修改表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('updateHistory:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            fndUpdateHistoryService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该历史修改表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改历史修改表")
    @ApiOperation("修改历史修改表")
    @PutMapping
    @PreAuthorize("@el.check('updateHistory:edit')")
    public ResponseEntity update(@Validated(FndUpdateHistory.Update.class) @RequestBody FndUpdateHistory updateHistory) {
        fndUpdateHistoryService.update(updateHistory);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个历史修改表")
    @ApiOperation("获取单个历史修改表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('updateHistory:list')")
    public ResponseEntity getUpdateHistory(@PathVariable Long id){
        return new ResponseEntity<>(fndUpdateHistoryService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询历史修改表（分页）")
    @ApiOperation("查询历史修改表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('updateHistory:list')")
    public ResponseEntity getUpdateHistoryPage(FndUpdateHistoryQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable){
        return new ResponseEntity<>(fndUpdateHistoryService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("导出历史修改表数据")
    @ApiOperation("导出历史修改表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('updateHistory:list')")
    public void download(HttpServletResponse response, FndUpdateHistoryQueryCriteria criteria) throws IOException {
        fndUpdateHistoryService.download(fndUpdateHistoryService.listAll(criteria), response);
    }
}
