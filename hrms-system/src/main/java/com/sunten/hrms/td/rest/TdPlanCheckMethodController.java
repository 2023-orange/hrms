package com.sunten.hrms.td.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.td.domain.TdPlanCheckMethod;
import com.sunten.hrms.td.dto.TdPlanCheckMethodQueryCriteria;
import com.sunten.hrms.td.service.TdPlanCheckMethodService;
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
 * 培训效果评价方式表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2022-03-08
 */
@RestController
@Api(tags = "培训效果评价方式表")
@RequestMapping("/api/td/planCheckMethod")
public class TdPlanCheckMethodController {
    private static final String ENTITY_NAME = "planCheckMethod";
    private final TdPlanCheckMethodService tdPlanCheckMethodService;

    public TdPlanCheckMethodController(TdPlanCheckMethodService tdPlanCheckMethodService) {
        this.tdPlanCheckMethodService = tdPlanCheckMethodService;
    }

    @Log("新增培训效果评价方式表")
    @ApiOperation("新增培训效果评价方式表")
    @PostMapping
    @PreAuthorize("@el.check('planCheckMethod:add')")
    public ResponseEntity create(@Validated @RequestBody TdPlanCheckMethod planCheckMethod) {
        if (planCheckMethod.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(tdPlanCheckMethodService.insert(planCheckMethod), HttpStatus.CREATED);
    }

    @Log("删除培训效果评价方式表")
    @ApiOperation("删除培训效果评价方式表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('planCheckMethod:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            tdPlanCheckMethodService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该培训效果评价方式表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改培训效果评价方式表")
    @ApiOperation("修改培训效果评价方式表")
    @PutMapping
    @PreAuthorize("@el.check('planCheckMethod:edit')")
    public ResponseEntity update(@Validated(TdPlanCheckMethod.Update.class) @RequestBody TdPlanCheckMethod planCheckMethod) {
        tdPlanCheckMethodService.update(planCheckMethod);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("根据method及planImplementId更新评价结果")
    @ApiOperation("根据method及planImplementId更新评价结果")
    @PutMapping(value = "/updateEvaluationResults")
    @PreAuthorize("@el.check('planCheckMethodEvaluationResults:edit')")
    public ResponseEntity updateEvaluationResults(@RequestBody TdPlanCheckMethod planCheckMethod) {
        tdPlanCheckMethodService.updateEvaluationResultsByMethodAndPlanImplementId(planCheckMethod);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ErrorLog("获取单个培训效果评价方式表")
    @ApiOperation("获取单个培训效果评价方式表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('planCheckMethod:list')")
    public ResponseEntity getPlanCheckMethod(@PathVariable Long id) {
        return new ResponseEntity<>(tdPlanCheckMethodService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询培训效果评价方式表（分页）")
    @ApiOperation("查询培训效果评价方式表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('planCheckMethod:list')")
    public ResponseEntity getPlanCheckMethodPage(TdPlanCheckMethodQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(tdPlanCheckMethodService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询培训效果评价方式表（不分页）")
    @ApiOperation("查询培训效果评价方式表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('planCheckMethod:list')")
    public ResponseEntity getPlanCheckMethodNoPaging(TdPlanCheckMethodQueryCriteria criteria) {
    return new ResponseEntity<>(tdPlanCheckMethodService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出培训效果评价方式表数据")
    @ApiOperation("导出培训效果评价方式表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('planCheckMethod:list')")
    public void download(HttpServletResponse response, TdPlanCheckMethodQueryCriteria criteria) throws IOException {
        tdPlanCheckMethodService.download(tdPlanCheckMethodService.listAll(criteria), response);
    }
}
