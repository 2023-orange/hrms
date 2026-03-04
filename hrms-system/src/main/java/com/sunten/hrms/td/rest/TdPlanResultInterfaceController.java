package com.sunten.hrms.td.rest;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.sunten.hrms.fnd.aop.Log;
import com.sunten.hrms.fnd.aop.ErrorLog;
import com.sunten.hrms.exception.BadRequestException;
import com.sunten.hrms.utils.ThrowableUtil;
import com.sunten.hrms.td.domain.TdPlanResultInterface;
import com.sunten.hrms.td.dto.TdPlanResultInterfaceQueryCriteria;
import com.sunten.hrms.td.service.TdPlanResultInterfaceService;
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
 * 培训结果接口表 前端控制器
 * </p>
 *
 * @author liangjw
 * @since 2021-06-17
 */
@RestController
@Api(tags = "培训结果接口表")
@RequestMapping("/api/td/planResultInterface")
public class TdPlanResultInterfaceController {
    private static final String ENTITY_NAME = "planResultInterface";
    private final TdPlanResultInterfaceService tdPlanResultInterfaceService;

    public TdPlanResultInterfaceController(TdPlanResultInterfaceService tdPlanResultInterfaceService) {
        this.tdPlanResultInterfaceService = tdPlanResultInterfaceService;
    }

    @Log("新增培训结果接口表")
    @ApiOperation("新增培训结果接口表")
    @PostMapping
    @PreAuthorize("@el.check('planResult:add')")
    public ResponseEntity create(@Validated @RequestBody TdPlanResultInterface planResultInterface) {
        if (planResultInterface.getId()  != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        return new ResponseEntity<>(tdPlanResultInterfaceService.insert(planResultInterface), HttpStatus.CREATED);
    }

    @Log("删除培训结果接口表")
    @ApiOperation("删除培训结果接口表")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('planResult:del')")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            tdPlanResultInterfaceService.delete(id);
        } catch (Throwable e) {
            ThrowableUtil.throwForeignKeyException(e, "该培训结果接口表存在 关联，请取消关联后再试");
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("修改培训结果接口表")
    @ApiOperation("修改培训结果接口表")
    @PutMapping
    @PreAuthorize("@el.check('planResult:edit')")
    public ResponseEntity update(@Validated(TdPlanResultInterface.Update.class) @RequestBody TdPlanResultInterface planResultInterface) {
        tdPlanResultInterfaceService.update(planResultInterface);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ErrorLog("获取单个培训结果接口表")
    @ApiOperation("获取单个培训结果接口表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('planResult:list')")
    public ResponseEntity getPlanResultInterface(@PathVariable Long id) {
        return new ResponseEntity<>(tdPlanResultInterfaceService.getByKey(id), HttpStatus.OK);
    }

    @ErrorLog("查询培训结果接口表（分页）")
    @ApiOperation("查询培训结果接口表（分页）")
    @GetMapping
    @PreAuthorize("@el.check('planResult:list')")
    public ResponseEntity getPlanResultInterfacePage(TdPlanResultInterfaceQueryCriteria criteria, @PageableDefault(value = 9999, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(tdPlanResultInterfaceService.listAll(criteria, pageable), HttpStatus.OK);
    }

    @ErrorLog("查询培训结果接口表（不分页）")
    @ApiOperation("查询培训结果接口表（不分页）")
    @GetMapping(value = "/noPaging")
    @PreAuthorize("@el.check('planResult:list')")
    public ResponseEntity getPlanResultInterfaceNoPaging(TdPlanResultInterfaceQueryCriteria criteria) {
    return new ResponseEntity<>(tdPlanResultInterfaceService.listAll(criteria), HttpStatus.OK);
    }

    @ErrorLog("导出培训结果接口表数据")
    @ApiOperation("导出培训结果接口表数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('planResult:list')")
    public void download(HttpServletResponse response, TdPlanResultInterfaceQueryCriteria criteria) throws IOException {
        tdPlanResultInterfaceService.download(tdPlanResultInterfaceService.listAll(criteria), response);
    }

    @ErrorLog("导入培训结果")
    @ApiOperation("导入年度计划数据")
    @PutMapping(value = "/insertExcel")
    @PreAuthorize("@el.check('planResult:add')")
    public ResponseEntity insertExcel(@RequestBody List<TdPlanResultInterface> tdPlanResultInterfaces) {
        return new ResponseEntity<>(tdPlanResultInterfaceService.insertExcel(tdPlanResultInterfaces), HttpStatus.OK);
    }
}
