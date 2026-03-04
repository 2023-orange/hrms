package com.sunten.hrms.td.rest;

import com.sunten.hrms.config.FndDataScope;
import com.sunten.hrms.config.FndDataScopeVo;
import com.sunten.hrms.td.dto.TeachingReportQueryCriteria;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.td.domain.TdTeacher;
import com.sunten.hrms.td.dto.TdTeacherQueryCriteria;
import com.sunten.hrms.td.service.TdTeacherService;
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

/**
 * <p>
 * 培训讲师列表 前端控制器
 * </p>
 *
 * @author xukai
 * @since 2021-06-16
 */
@RestController
@Api(tags = "培训讲师列表")
@RequestMapping("/api/td/teacher")
public class TdTeacherController {
    private static final String ENTITY_NAME = "teacher";
    private final TdTeacherService tdTeacherService;
    private final FndDataScope fndDataScope;

    public TdTeacherController(TdTeacherService tdTeacherService, FndDataScope fndDataScope) {
        this.tdTeacherService = tdTeacherService;
        this.fndDataScope = fndDataScope;
    }

    @Log("新增培训讲师列表")
    @ApiOperation("新增培训讲师列表")
    @PostMapping
    @PreAuthorize("@el.check('teacher:add')")
    public ResponseEntity create(@Validated @RequestBody TdTeacher teacher) {
        if (teacher.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(tdTeacherService.insert(teacher), HttpStatus.CREATED);
    }

    @Log("删除培训讲师列表")
    @ApiOperation("删除培训讲师列表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('teacher:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            tdTeacherService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该培训讲师列表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改培训讲师列表")
    @ApiOperation("修改培训讲师列表")
    @PutMapping
    @PreAuthorize("@el.check('teacher:edit')")
    public ResponseEntity update(@Validated(TdTeacher.Update.class) @RequestBody TdTeacher teacher) {
        tdTeacherService.update(teacher);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个培训讲师列表")
    @ApiOperation("获取单个培训讲师列表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('teacher:list')")
    public ResponseEntity getTeacher(@PathVariable Long id) {
        return new ResponseEntity<>(tdTeacherService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询培训讲师列表（分页）")
    @ApiOperation("查询培训讲师列表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('teacher:list')")
    public ResponseEntity getTeacherPage(TdTeacherQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(tdTeacherService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询培训讲师列表（不分页）")
    @ApiOperation("查询培训讲师列表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('teacher:list')")
    public ResponseEntity getTeacherNoPaging(TdTeacherQueryCriteria criteria) {

    return new ResponseEntity<>(tdTeacherService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出培训讲师列表数据")
    @ApiOperation("导出培训讲师列表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('teacher:list')")
    public void download(HttpServletResponse response, TdTeacherQueryCriteria criteria) throws IOException {
        tdTeacherService.download(tdTeacherService.listAll(criteria), response);
    }

    @ErrorLog("导出培训讲师授课记录")
    @ApiOperation("导出培训讲师授课记录")
    @GetMapping(value = "/downloadReport")
    @PreAuthorize("@el.check('teacher:list','teacher:add')")
    public void downloadReport(HttpServletResponse response, TeachingReportQueryCriteria criteria) throws IOException {
        tdTeacherService.downloadReport(tdTeacherService.listTeachingByCriteria(criteria), response);
    }

    @ErrorLog("获取讲师的简单列表")
    @ApiOperation("获取讲师的简单列表")
    @GetMapping(value = "/getTeacherNoAuth")
    public ResponseEntity getTeacherNoAuth(TdTeacherQueryCriteria tdTeacherQueryCriteria) {
        return new ResponseEntity<>(tdTeacherService.listTeacherVo(tdTeacherQueryCriteria), HttpStatus.OK);
    }
}
