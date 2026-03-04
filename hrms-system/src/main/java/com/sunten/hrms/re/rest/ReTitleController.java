package com.sunten.hrms.re.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.re.domain.ReTitle;
import com.sunten.hrms.re.dto.ReTitleQueryCriteria;
import com.sunten.hrms.re.service.ReTitleService;
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
 * 职称情况表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@RestController
@Api(tags = "职称情况表")
@RequestMapping("/api/re/title")
public class ReTitleController {
    private static final String ENTITY_NAME = "title";
    private final ReTitleService reTitleService;

    public ReTitleController(ReTitleService reTitleService) {
        this.reTitleService = reTitleService;
    }

    @Log("新增职称情况表")
    @ApiOperation("新增职称情况表")
    @PostMapping
    @PreAuthorize("@el.check('title:add')")
    public ResponseEntity create(@Validated @RequestBody ReTitle title) {
        if (title.getId() == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " must be already have an ID");
        }
        return new ResponseEntity<>(reTitleService.insert(title), HttpStatus.CREATED);
    }

    @Log("删除职称情况表")
    @ApiOperation("删除职称情况表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('title:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            reTitleService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该职称情况表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改职称情况表")
    @ApiOperation("修改职称情况表")
    @PutMapping
    @PreAuthorize("@el.check('title:edit')")
    public ResponseEntity update(@Validated(ReTitle.Update.class) @RequestBody ReTitle title) {
        reTitleService.update(title);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个职称情况表")
    @ApiOperation("获取单个职称情况表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('title:list')")
    public ResponseEntity getTitle(@PathVariable Long id) {
        return new ResponseEntity<>(reTitleService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询职称情况表（分页）")
    @ApiOperation("查询职称情况表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('title:list','recruitment:list')")
    public ResponseEntity getTitlePage(ReTitleQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(reTitleService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询职称情况表（不分页）")
    @ApiOperation("查询职称情况表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('title:list','recruitment:list')")
    public ResponseEntity getTitleNoPaging(ReTitleQueryCriteria criteria) {
        return new ResponseEntity<>(reTitleService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出职称情况表数据")
    @ApiOperation("导出职称情况表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('title:list')")
    public void download(HttpServletResponse response, ReTitleQueryCriteria criteria) throws IOException {
        reTitleService.download(reTitleService.listAll(criteria), response);
    }

    @Log("批量编辑职称情况表")
    @ApiOperation("批量编辑职称情况表")
    @PutMapping(value = "/batchSave")
    @PreAuthorize("@el.check('title:add','title:edit','recruitment:edit','recruitment:add')")
    public ResponseEntity batchSave(@Validated @RequestBody List<ReTitle> reTitles) {
        if (reTitles == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot be null");
        }
        return new ResponseEntity<>(reTitleService.batchInsert(reTitles, null), HttpStatus.CREATED);
    }
}
