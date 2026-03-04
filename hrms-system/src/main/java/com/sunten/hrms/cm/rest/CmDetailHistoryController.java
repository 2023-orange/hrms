package com.sunten.hrms.cm.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.cm.domain.CmDetailHistory;
import com.sunten.hrms.cm.dto.CmDetailHistoryQueryCriteria;
import com.sunten.hrms.cm.service.CmDetailHistoryService;
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
 *  前端控制器
 * </p>
 *
 * @author zhoujy
 * @since 2023-02-23
 */
@RestController
@Api(tags = "工衣明细历史表")
@RequestMapping("/api/cm/detailHistory")
public class CmDetailHistoryController {
    private static final String ENTITY_NAME = "detailHistory";
    private final CmDetailHistoryService cmDetailHistoryService;

    public CmDetailHistoryController(CmDetailHistoryService cmDetailHistoryService) {
        this.cmDetailHistoryService = cmDetailHistoryService;
    }

    @Log("新增工衣明细历史表")
    @ApiOperation("新增工衣明细历史表")
    @PostMapping
    @PreAuthorize("@el.check('cmDetail:add')")
    public ResponseEntity create(@Validated @RequestBody CmDetailHistory detailHistory) {
        detailHistory.setId(null);
        if (detailHistory.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(cmDetailHistoryService.insert(detailHistory), HttpStatus.CREATED);
    }

    @Log("删除工衣明细历史表")
    @ApiOperation("删除工衣明细历史表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('cmDetail:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            cmDetailHistoryService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改工衣明细历史表")
    @ApiOperation("修改工衣明细历史表")
    @PutMapping
    @PreAuthorize("@el.check('cmDetail:edit')")
    public ResponseEntity update(@Validated(CmDetailHistory.Update.class) @RequestBody CmDetailHistory detailHistory) {
        cmDetailHistoryService.update(detailHistory);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个工衣明细历史表")
    @ApiOperation("获取单个工衣明细历史表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('cmDetail:list')")
    public ResponseEntity getDetailHistory(@PathVariable Long id) {
        return new ResponseEntity<>(cmDetailHistoryService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("获取同一人的工衣明细历史表")
    @ApiOperation("获取同一人的工衣明细历史表")
    @GetMapping(value = "/detailId/{detailId}")
    @PreAuthorize("@el.check('cmDetail:list')")
    public ResponseEntity getDetailHistoryByDetailID(@PathVariable Long detailId) {
        return new ResponseEntity<>(cmDetailHistoryService.getByDetailId(detailId), HttpStatus.OK);
    }

    @ErrorLog("查询（分页）工衣明细历史表")
    @ApiOperation("查询（分页）工衣明细历史表")
    @GetMapping
    @PreAuthorize("@el.check('cmDetail:list')")
    public ResponseEntity getDetailHistoryPage(CmDetailHistoryQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(cmDetailHistoryService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询（不分页）工衣明细历史表")
    @ApiOperation("查询（不分页）工衣明细历史表")
    @PostMapping(value = "/noPaging")
    @PreAuthorize("@el.check('cmDetail:list')")
    public ResponseEntity getDetailHistoryNoPaging(CmDetailHistoryQueryCriteria criteria) {
    return new ResponseEntity<>(cmDetailHistoryService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出工衣明细历史表数据")
    @ApiOperation("导出工衣明细历史表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('cmDetail:list')")
    public void download(HttpServletResponse response, CmDetailHistoryQueryCriteria criteria) throws IOException {
        cmDetailHistoryService.download(cmDetailHistoryService.listAll(criteria), response);
    }
}
