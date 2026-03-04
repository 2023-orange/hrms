package com.sunten.hrms.re.rest;

import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.re.domain.ReEducation;
import com.sunten.hrms.re.dto.ReEducationQueryCriteria;
import com.sunten.hrms.re.service.ReEducationService;
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
 * 受教育经历表 前端控制器
 * </p>
 *
 * @author batan
 * @since 2020-08-05
 */
@RestController
@Api(tags = "受教育经历表")
@RequestMapping("/api/re/education")
public class ReEducationController {
    private static final String ENTITY_NAME = "education";
    private final ReEducationService reEducationService;

    public ReEducationController(ReEducationService reEducationService) {
        this.reEducationService = reEducationService;
    }

    @Log("新增受教育经历表")
    @ApiOperation("新增受教育经历表")
    @PostMapping
    @PreAuthorize("@el.check('education:add')")
    public ResponseEntity create(@Validated @RequestBody ReEducation education) {
        if (education.getId() == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " must be already have an ID");
        }
        return new ResponseEntity<>(reEducationService.insert(education), HttpStatus.CREATED);
    }

    @Log("删除受教育经历表")
    @ApiOperation("删除受教育经历表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('education:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            reEducationService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该受教育经历表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改受教育经历表")
    @ApiOperation("修改受教育经历表")
    @PutMapping
    @PreAuthorize("@el.check('education:edit')")
    public ResponseEntity update(@Validated(ReEducation.Update.class) @RequestBody ReEducation education) {
        reEducationService.update(education);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个受教育经历表")
    @ApiOperation("获取单个受教育经历表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('education:list')")
    public ResponseEntity getEducation(@PathVariable Long id) {
        return new ResponseEntity<>(reEducationService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询受教育经历表（分页）")
    @ApiOperation("查询受教育经历表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('education:list','recruitment:list')")
    public ResponseEntity getEducationPage(ReEducationQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(reEducationService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询受教育经历表（不分页）")
    @ApiOperation("查询受教育经历表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('education:list','recruitment:list')")
    public ResponseEntity getEducationNoPaging(ReEducationQueryCriteria criteria) {
        return new ResponseEntity<>(reEducationService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出受教育经历表数据")
    @ApiOperation("导出受教育经历表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('education:list')")
    public void download(HttpServletResponse response, ReEducationQueryCriteria criteria) throws IOException {
        reEducationService.download(reEducationService.listAll(criteria), response);
    }

    @Log("批量编辑受教育经历表")
    @ApiOperation("批量编辑受教育经历表")
    @PutMapping(value = "/batchSave")
    @PreAuthorize("@el.check('education:add','education:edit','recruitment:edit','recruitment:add')")
    public ResponseEntity batchSave(@Validated @RequestBody List<ReEducation> educations) {
        if (educations == null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot be null");
        }
        return new ResponseEntity<>(reEducationService.batchInsert(educations, null), HttpStatus.CREATED);
    }
}
