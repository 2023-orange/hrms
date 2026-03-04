package com.sunten.hrms.ac.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.ac.domain.AcSetUp;
import com.sunten.hrms.ac.dto.AcSetUpQueryCriteria;
import com.sunten.hrms.ac.service.AcSetUpService;
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
 * 考勤异常允许设置表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2020-10-23
 */
@RestController
@Api(tags = "考勤异常允许设置表")
@RequestMapping("/api/ac/setUp")
public class AcSetUpController {
    private static final String ENTITY_NAME = "setUp";
    private final AcSetUpService acSetUpService;

    public AcSetUpController(AcSetUpService acSetUpService) {
        this.acSetUpService = acSetUpService;
    }

    @Log("新增考勤异常允许设置表")
    @ApiOperation("新增考勤异常允许设置表")
    @PostMapping
    @PreAuthorize("@el.check('setUp:add')")
    public ResponseEntity create(@Validated @RequestBody AcSetUp setUp) {
        if (setUp.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(acSetUpService.insert(setUp), HttpStatus.CREATED);
    }

    @Log("删除考勤异常允许设置表")
    @ApiOperation("删除考勤异常允许设置表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('setUp:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            acSetUpService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该考勤异常允许设置表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改考勤异常允许设置表")
    @ApiOperation("修改考勤异常允许设置表")
    @PutMapping
    @PreAuthorize("@el.check('setUp:edit')")
    public ResponseEntity update(@Validated(AcSetUp.Update.class) @RequestBody AcSetUp setUp) {
        acSetUpService.update(setUp);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个考勤异常允许设置表")
    @ApiOperation("获取单个考勤异常允许设置表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('setUp:list')")
    public ResponseEntity getSetUp(@PathVariable Long id) {
        return new ResponseEntity<>(acSetUpService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("获取最新一个考勤异常允许设置表")
    @ApiOperation("获取最新一个考勤异常允许设置表")
    @GetMapping(value = "/getNew")
    @PreAuthorize("@el.check('setUp:list')")
    public ResponseEntity getNew() {
        return  new ResponseEntity<>(acSetUpService.getByTop(), HttpStatus.OK);
    }



    @ErrorLog("查询考勤异常允许设置表（分页）")
    @ApiOperation("查询考勤异常允许设置表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('setUp:list')")
    public ResponseEntity getSetUpPage(AcSetUpQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(acSetUpService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询考勤异常允许设置表（不分页）")
    @ApiOperation("查询考勤异常允许设置表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('setUp:list')")
    public ResponseEntity getSetUpNoPaging(AcSetUpQueryCriteria criteria) {
    return new ResponseEntity<>(acSetUpService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出考勤异常允许设置表数据")
    @ApiOperation("导出考勤异常允许设置表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('setUp:list')")
    public void download(HttpServletResponse response, AcSetUpQueryCriteria criteria) throws IOException {
        acSetUpService.download(acSetUpService.listAll(criteria), response);
    }
}
