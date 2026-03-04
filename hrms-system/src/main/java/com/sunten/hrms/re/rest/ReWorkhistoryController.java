package com.sunten.hrms.re.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.re.domain.ReWorkhistory;
import com.sunten.hrms.re.dto.ReWorkhistoryQueryCriteria;
import com.sunten.hrms.re.service.ReWorkhistoryService;
import com.sunten.hrms.utils.ThrowableUtil;
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
 * 工作经历表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@RestController
@Api(tags = "工作经历表")
@RequestMapping("/api/re/workhistory")
public class ReWorkhistoryController {
    private static final String ENTITY_NAME = "workhistory";
    private final ReWorkhistoryService reWorkhistoryService;

    public ReWorkhistoryController(ReWorkhistoryService reWorkhistoryService) {
        this.reWorkhistoryService = reWorkhistoryService;
    }

    @Log("新增工作经历表")
    @ApiOperation("新增工作经历表")
    @PostMapping
    @PreAuthorize("@el.check('workhistory:add')")
    public ResponseEntity create(@Validated @RequestBody ReWorkhistory workhistory) {
        if (workhistory.getId() == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " must be already have an ID");
        }
        return new ResponseEntity<>(reWorkhistoryService.insert(workhistory), HttpStatus.CREATED);
    }

    @Log("删除工作经历表")
    @ApiOperation("删除工作经历表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('workhistory:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            reWorkhistoryService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该工作经历表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改工作经历表")
    @ApiOperation("修改工作经历表")
    @PutMapping
    @PreAuthorize("@el.check('workhistory:edit')")
    public ResponseEntity update(@Validated(ReWorkhistory.Update.class) @RequestBody ReWorkhistory workhistory) {
        reWorkhistoryService.update(workhistory);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个工作经历表")
    @ApiOperation("获取单个工作经历表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('workhistory:list')")
    public ResponseEntity getWorkhistory(@PathVariable Long id) {
        return new ResponseEntity<>(reWorkhistoryService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询工作经历表（分页）")
    @ApiOperation("查询工作经历表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('workhistory:list','recruitment:list')")
    public ResponseEntity getWorkhistoryPage(ReWorkhistoryQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(reWorkhistoryService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询工作经历表（不分页）")
    @ApiOperation("查询工作经历表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('workhistory:list','recruitment:list')")
    public ResponseEntity getWorkhistoryNoPaging(ReWorkhistoryQueryCriteria criteria) {
        return new ResponseEntity<>(reWorkhistoryService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出工作经历表数据")
    @ApiOperation("导出工作经历表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('workhistory:list')")
    public void download(HttpServletResponse response, ReWorkhistoryQueryCriteria criteria) throws IOException {
        reWorkhistoryService.download(reWorkhistoryService.listAll(criteria), response);
    }

    @Log("批量编辑工作经历表")
    @ApiOperation("批量编辑工作经历表")
    @PutMapping(value = "/batchSave")
    @PreAuthorize("@el.check('workhistory:add','workhistory:edit','recruitment:edit','recruitment:add')")
    public ResponseEntity batchSave(@Validated @RequestBody List<ReWorkhistory> workhistorys) {
        if (workhistorys == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot be null");
        }
        return new ResponseEntity<>(reWorkhistoryService.batchInsert(workhistorys, null), HttpStatus.CREATED);
    }
}
