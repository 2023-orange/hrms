package com.sunten.hrms.re.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.re.domain.ReAward;
import com.sunten.hrms.re.dto.ReAwardQueryCriteria;
import com.sunten.hrms.re.service.ReAwardService;
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
 * 奖罚情况表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@RestController
@Api(tags = "奖罚情况表")
@RequestMapping("/api/re/award")
public class ReAwardController {
    private static final String ENTITY_NAME = "award";
    private final ReAwardService reAwardService;

    public ReAwardController(ReAwardService reAwardService) {
        this.reAwardService = reAwardService;
    }

    @Log("新增奖罚情况表")
    @ApiOperation("新增奖罚情况表")
    @PostMapping
    @PreAuthorize("@el.check('award:add')")
    public ResponseEntity create(@Validated @RequestBody ReAward award) {
        if (award.getId() == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " must be already have an ID");
        }
        return new ResponseEntity<>(reAwardService.insert(award), HttpStatus.CREATED);
    }

    @Log("删除奖罚情况表")
    @ApiOperation("删除奖罚情况表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('award:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            reAwardService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该奖罚情况表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改奖罚情况表")
    @ApiOperation("修改奖罚情况表")
    @PutMapping
    @PreAuthorize("@el.check('award:edit')")
    public ResponseEntity update(@Validated(ReAward.Update.class) @RequestBody ReAward award) {
        reAwardService.update(award);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个奖罚情况表")
    @ApiOperation("获取单个奖罚情况表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('award:list')")
    public ResponseEntity getAward(@PathVariable Long id) {
        return new ResponseEntity<>(reAwardService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询奖罚情况表（分页）")
    @ApiOperation("查询奖罚情况表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('award:list','recruitment:list')")
    public ResponseEntity getAwardPage(ReAwardQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(reAwardService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询奖罚情况表（不分页）")
    @ApiOperation("查询奖罚情况表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('award:list','recruitment:list')")
    public ResponseEntity getAwardNoPaging(ReAwardQueryCriteria criteria) {
        return new ResponseEntity<>(reAwardService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出奖罚情况表数据")
    @ApiOperation("导出奖罚情况表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('award:list')")
    public void download(HttpServletResponse response, ReAwardQueryCriteria criteria) throws IOException {
        reAwardService.download(reAwardService.listAll(criteria), response);
    }

    @Log("批量编辑奖罚情况表")
    @ApiOperation("批量编辑奖罚情况表")
    @PutMapping(value = "/batchSave")
    @PreAuthorize("@el.check('award:edit','award:add','recruitment:edit','recruitment:add')")
    public ResponseEntity batchSave(@Validated @RequestBody List<ReAward> awards) {
        if (awards == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot be null");
        }
        return new ResponseEntity<>(reAwardService.batchInsert(awards, null), HttpStatus.CREATED);
    }
}
