package com.sunten.hrms.re.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.re.domain.ReVocational;
import com.sunten.hrms.re.dto.ReVocationalQueryCriteria;
import com.sunten.hrms.re.service.ReVocationalService;
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
 * 招聘职业资格表 前端控制器
 * </p>
 *
 * @author xukai
 * @since 2020-08-28
 */
@RestController
@Api(tags = "招聘职业资格表")
@RequestMapping("/api/re/vocational")
public class ReVocationalController {
    private static final String ENTITY_NAME = "vocational";
    private final ReVocationalService reVocationalService;

    public ReVocationalController(ReVocationalService reVocationalService) {
        this.reVocationalService = reVocationalService;
    }

    @Log("新增招聘职业资格表")
    @ApiOperation("新增招聘职业资格表")
    @PostMapping
    @PreAuthorize("@el.check('vocational:add')")
    public ResponseEntity create(@Validated @RequestBody ReVocational vocational) {
        if (vocational.getId() == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " must be already have an ID");
        }
        return new ResponseEntity<>(reVocationalService.insert(vocational), HttpStatus.CREATED);
    }

    @Log("删除招聘职业资格表")
    @ApiOperation("删除招聘职业资格表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('vocational:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            reVocationalService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该招聘职业资格表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改招聘职业资格表")
    @ApiOperation("修改招聘职业资格表")
    @PutMapping
    @PreAuthorize("@el.check('vocational:edit')")
    public ResponseEntity update(@Validated(ReVocational.Update.class) @RequestBody ReVocational vocational) {
        reVocationalService.update(vocational);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个招聘职业资格表")
    @ApiOperation("获取单个招聘职业资格表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('vocational:list')")
    public ResponseEntity getVocational(@PathVariable Long id) {
        return new ResponseEntity<>(reVocationalService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询招聘职业资格表（分页）")
    @ApiOperation("查询招聘职业资格表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('vocational:list','recruitment:list')")
    public ResponseEntity getVocationalPage(ReVocationalQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(reVocationalService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询招聘职业资格表（不分页）")
    @ApiOperation("查询招聘职业资格表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('vocational:list','recruitment:list')")
    public ResponseEntity getVocationalNoPaging(ReVocationalQueryCriteria criteria) {
        return new ResponseEntity<>(reVocationalService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出招聘职业资格表数据")
    @ApiOperation("导出招聘职业资格表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('vocational:list')")
    public void download(HttpServletResponse response, ReVocationalQueryCriteria criteria) throws IOException {
        reVocationalService.download(reVocationalService.listAll(criteria), response);
    }

    @Log("批量编辑招聘职业资格表")
    @ApiOperation("批量编辑招聘职业资格表")
    @PutMapping(value = "/batchSave")
    @PreAuthorize("@el.check('vocational:add','vocational:edit','recruitment:edit','recruitment:add')")
    public ResponseEntity batchSave(@Validated @RequestBody List<ReVocational> vocationals) {
        if (vocationals == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot be null");
        }
        return new ResponseEntity<>(reVocationalService.batchInsert(vocationals, null), HttpStatus.CREATED);
    }
}
